/*
 * Copyright 2011-2012 PrimeFaces Extensions.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */

package org.primefaces.extensions.component.ajaxerrorhandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;
import javax.faces.event.SystemEvent;

import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * AjaxExceptionHandler
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class AjaxExceptionHandler extends ExceptionHandlerWrapper {

	private static final Logger LOGGER = Logger.getLogger(AjaxExceptionHandler.class.getCanonicalName());

	private static final String LOG_BEFORE_KEY = "jsf.context.exception.handler.log_before";
	private static final String LOG_AFTER_KEY = "jsf.context.exception.handler.log_after";
	private static final String LOG_KEY = "jsf.context.exception.handler.log";

	private LinkedList<ExceptionQueuedEvent> unhandledExceptions;
	private LinkedList<ExceptionQueuedEvent> handledExceptions;
	private ExceptionQueuedEvent handled;
	private ExceptionHandler wrapped = null;

	/**
	 * Construct a new {@link AjaxExceptionHandler} around the given wrapped {@link ExceptionHandler}.
	 * 
	 * @param exceptionHandler The wrapped {@link ExceptionHandler}.
	 */
	public AjaxExceptionHandler(final ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public ExceptionQueuedEvent getHandledExceptionQueuedEvent() {
		return handled;
	}

	@Override
	public void handle() throws FacesException {
		if (FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest() == false) {
			return;
		}

		for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext(); ) {
			ExceptionQueuedEvent event = i.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			try {
				Throwable t = context.getException();
				if (isRethrown(t)) {
					handled = event;

					handlePartialResponseError(context.getContext(), t);

					/*
					Throwable unwrapped = getRootCause(t);

					if (unwrapped != null) {
						handlePartialResponseError(context.getContext(), unwrapped);
					} else {
						if (t instanceof FacesException) {
							handlePartialResponseError(context.getContext(), t);
						} else {
							handlePartialResponseError(context.getContext(), new FacesException(t.getMessage(), t));
						}
					}
					 */
				} else {
					log(context);
				}

			} finally {
				if (handledExceptions == null) {
					handledExceptions = new LinkedList<ExceptionQueuedEvent>();
				}

				handledExceptions.add(event);
				i.remove();
			}
		}
	}

	@Override
	public void processEvent(final SystemEvent event) throws AbortProcessingException {
		if (event != null) {
			if (unhandledExceptions == null) {
				unhandledExceptions = new LinkedList<ExceptionQueuedEvent>();
			}

			unhandledExceptions.add((ExceptionQueuedEvent) event);
		}
	}

	@Override
	public Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents() {
		return ((unhandledExceptions != null) ? unhandledExceptions : Collections.<ExceptionQueuedEvent>emptyList());
	}

	@Override
	public Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents() {
		return ((handledExceptions != null) ? handledExceptions : Collections.<ExceptionQueuedEvent>emptyList());
	}

	private void handlePartialResponseError(final FacesContext context, final Throwable t) {
		if (context.getResponseComplete()) {
			return; // don't write anything if the response is complete
		}

		try {
			Throwable rootCause = ExceptionUtils.getRootCause(t);

			String errorName = (rootCause == null) ? t.getClass().getCanonicalName() : rootCause.getClass().getCanonicalName();

			ExternalContext extContext = context.getExternalContext();
			extContext.setResponseContentType("text/xml");
			extContext.addResponseHeader("Cache-Control", "no-cache");
			PartialResponseWriter writer = context.getPartialViewContext().getPartialResponseWriter();

			writer.startDocument();

			writer.startElement("error", null);

			// Node <error-name>
			writer.startElement("error-name", null);
			writer.write(errorName);
			writer.endElement("error-name");

			// Node <error-message>
			writer.startElement("error-message", null);
			writer.startCDATA();
			writer.write(ExceptionUtils.getRootCauseMessage(t));
			writer.endCDATA();
			writer.endElement("error-message");

			// Node <error-stacktrace>
			writer.startElement("error-stacktrace", null);
			writer.startCDATA();
			String stackTrace = ExceptionUtils.getStackTrace(rootCause == null? t : rootCause);
			writer.write(stackTrace);
			writer.endCDATA();
			writer.endElement("error-stacktrace");

			// Node <error-stacktrace>
			writer.startElement("error-hostname", null);
			writer.write(getHostname());
			writer.endElement("error-hostname");

			UIViewRoot root = context.getViewRoot();
			AjaxErrorHandlerVisitCallback visitCallback = new AjaxErrorHandlerVisitCallback(errorName);
			root.visitTree(VisitContext.createVisitContext(context), visitCallback);

			UIComponent titleFacet = visitCallback.findCurrentTitleFacet();
			if (titleFacet != null) {
				writer.startElement("updateTitle", null);
				writer.startCDATA();

				try {
					context.setResponseWriter(writer);
					titleFacet.encodeAll(context);
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Rendering titleUpdate in AjaxExceptionHandler throws exception!", e);
					writer.write("<exception />");
				}

				writer.endCDATA();
				writer.endElement("updateTitle");
			}

			UIComponent bodyFacet = visitCallback.findCurrentBodyFacet();
			if (bodyFacet != null) {
				writer.startElement("updateBody", null);
				writer.startCDATA();

				try {
					context.setResponseWriter(writer);
					bodyFacet.encodeAll(context);
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Rendering bodyUpdate in AjaxExceptionHandler throws exception!", e);
					writer.write("<exception />");
				}

				writer.endCDATA();
				writer.endElement("updateBody");
			}

			List<UIComponent> customContent = visitCallback.findCurrentChildren();
			if (customContent != null && customContent.size() > 0) {
				writer.startElement("updateCustomContent", null);
				writer.startCDATA();

				try {
					context.setResponseWriter(writer);
					for (UIComponent component : customContent) {
						component.encodeAll(context);
					}
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Rendering updateCustomContent in AjaxExceptionHandler throws exception!", e);
					writer.write("<exception />");
				}

				writer.endCDATA();
				writer.endElement("updateCustomContent");
			}

			/*
			// Update state - is ignored, because ajaxerrorhandler.js is not ready for this update ...
			if (!context.getViewRoot().isTransient()) {
				// Get the view state and write it to the response..
				writer.startElement("updateViewState", null);
				writer.startCDATA();
				try {
					String state = context.getApplication().getStateManager().getViewState(context);
					writer.write(state);
				} catch (Exception e) {
					LOGGER.log(Level.WARNING, "Rendering updateViewState in AjaxExceptionHandler throws exception!", e);
					writer.write("<exception />");
				}

				writer.endCDATA();
				writer.endElement("updateViewState");
			}
			 */

			writer.endElement("error");

			writer.endDocument();
			context.responseComplete();
		} catch (IOException e) {
			if (LOGGER.isLoggable(Level.SEVERE)) {
				LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private boolean isRethrown(final Throwable t) {
		return (!(t instanceof AbortProcessingException));
	}

	private void log(final ExceptionQueuedEventContext exceptionContext) {
		UIComponent c = exceptionContext.getComponent();
		boolean beforePhase = exceptionContext.inBeforePhase();
		boolean afterPhase = exceptionContext.inAfterPhase();
		PhaseId phaseId = exceptionContext.getPhaseId();
		Throwable t = exceptionContext.getException();
		String key = getLoggingKey(beforePhase, afterPhase);

		if (LOGGER.isLoggable(Level.SEVERE)) {
			LOGGER.log(Level.SEVERE,
					key,
					new Object[] { t.getClass().getName(),
					phaseId.toString(),
					((c != null) ? c.getClientId(exceptionContext.getContext()) : ""),
					t.getMessage()});
			LOGGER.log(Level.SEVERE, t.getMessage(), t);
		}
	}

	private String getLoggingKey(final boolean beforePhase, final boolean afterPhase) {
		if (beforePhase) {
			return LOG_BEFORE_KEY;
		} else if (afterPhase) {
			return LOG_AFTER_KEY;
		} else {
			return LOG_KEY;
		}
	}

	protected String getHostname() throws UnknownHostException {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "???unknown???";
		}
	}
}
