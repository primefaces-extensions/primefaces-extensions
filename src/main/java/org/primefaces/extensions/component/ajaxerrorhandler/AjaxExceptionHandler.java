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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitContext;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.view.ViewDeclarationLanguage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * {@link ExceptionHandlerWrapper} which writes a custom XML response for the {@link AjaxErrorHandler} component.
 *
 * @author Pavol Slany / last modified by $Author$
 * @version $Revision$
 * @since 0.5
 */
public class AjaxExceptionHandler extends ExceptionHandlerWrapper {

	private static final Logger LOGGER = Logger.getLogger(AjaxExceptionHandler.class.getCanonicalName());

	private ExceptionHandler wrapped = null;

	/**
	 * Construct a new {@link AjaxExceptionHandler} around the given wrapped {@link ExceptionHandler}.
	 *
	 * @param wrapped The wrapped {@link ExceptionHandler}.
	 */
	public AjaxExceptionHandler(final ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() throws FacesException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getPartialViewContext()!=null && context.getPartialViewContext().isAjaxRequest()) {
			Iterable<ExceptionQueuedEvent> exceptionQueuedEvents = getUnhandledExceptionQueuedEvents();
			if (exceptionQueuedEvents!=null && exceptionQueuedEvents.iterator()!=null) {
				Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

				if (unhandledExceptionQueuedEvents.hasNext()) {
					Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();
					unhandledExceptionQueuedEvents.remove();

					handlePartialResponseError(context, exception);
				}

				while (unhandledExceptionQueuedEvents.hasNext()) {
					// Any remaining unhandled exceptions are not interesting. First fix the first.
					unhandledExceptionQueuedEvents.next();
					unhandledExceptionQueuedEvents.remove();
				}
			}

		}

		wrapped.handle();
	}

	private void handlePartialResponseError(final FacesContext context, final Throwable t) {
		if (context.getResponseComplete()) {
			return; // don't write anything if the response is complete
		}

		if (!context.getExternalContext().isResponseCommitted()) {
			context.getExternalContext().responseReset();
		}

		try {
			Throwable rootCause = ExceptionUtils.getRootCause(t);

			// Workaround for ViewExpiredException if UIViewRoot was not restored ...
			if (context.getViewRoot() == null) {
				try {
					String uri = ((HttpServletRequest) context.getExternalContext().getRequest()).getRequestURI();
					UIViewRoot viewRoot = context.getApplication().getViewHandler().createView(context, uri);
					context.setViewRoot(viewRoot);

					// Workaround for Mojarra : if  UIViewRoot == null (VIEW is lost in session), throwed is  IllegalArgumentException instead of 'ViewExpiredException'
					if (rootCause==null && t instanceof IllegalArgumentException) {
						rootCause = new javax.faces.application.ViewExpiredException(uri);
					}

					// buildView - create component tree in view ...
					// todo: add CONTEXT-PARAM for set this feature BUILD VIEW
					String viewId = viewRoot.getViewId();
					ViewDeclarationLanguage vdl = context.getApplication().getViewHandler().getViewDeclarationLanguage(context, viewId);
					vdl.buildView(context, viewRoot);
				}
				catch (Exception tt) {
					LOGGER.log(Level.SEVERE, tt.getMessage(), tt);
				}
			}

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
			writer.write(rootCause != null ? rootCause.getMessage() : t.getMessage());
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
			if (root != null)
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

	protected String getHostname() throws UnknownHostException {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "???unknown???";
		}
	}
}
