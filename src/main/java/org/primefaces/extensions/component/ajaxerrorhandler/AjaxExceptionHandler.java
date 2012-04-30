/*
 * Copyright 2011 PrimeFaces Extensions.
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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.*;
import javax.faces.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private ExceptionHandler exceptionHandler = null;


    // Constructors ---------------------------------------------------------------------------------------------------

    /**
     * Construct a new ajax exception handler around the given wrapped exception handler.
     * @param exceptionHandler The wrapped exception handler.
     */
    public AjaxExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public ExceptionHandler getWrapped() {
        return this.exceptionHandler;
    }

    /**
     * @see ExceptionHandlerWrapper@getHandledExceptionQueuedEvent()
     */
    public ExceptionQueuedEvent getHandledExceptionQueuedEvent() {

        return handled;

    }

    /**
     * @see javax.faces.context.ExceptionHandlerWrapper#handle()
     */
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
//                    Throwable unwrapped = getRootCause(t);

                    handlePartialResponseError(context.getContext(), t);

//                    if (unwrapped != null) {
//                        handlePartialResponseError(context.getContext(), unwrapped);
//                    } else {
//                        if (t instanceof FacesException) {
//                            handlePartialResponseError(context.getContext(), t);
//                        } else {
//                            handlePartialResponseError(context.getContext(),  new FacesException(t.getMessage(), t));
//                        }
//                    }
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

    /**
     * @see javax.faces.context.ExceptionHandlerWrapper#processEvent(javax.faces.event.SystemEvent)
     */
    public void processEvent(SystemEvent event) throws AbortProcessingException {

        if (event != null) {
            if (unhandledExceptions == null) {
                unhandledExceptions = new LinkedList<ExceptionQueuedEvent>();
            }
            unhandledExceptions.add((ExceptionQueuedEvent) event);
        }

    }

    /**
     * @see javax.faces.context.ExceptionHandlerWrapper#getUnhandledExceptionQueuedEvents()
     */
    public Iterable<ExceptionQueuedEvent> getUnhandledExceptionQueuedEvents() {

        return ((unhandledExceptions != null)
                ? unhandledExceptions
                : Collections.<ExceptionQueuedEvent>emptyList());

    }


    /**
     * @see javax.faces.context.ExceptionHandlerWrapper#getHandledExceptionQueuedEvents()
     * @return
     */
    public Iterable<ExceptionQueuedEvent> getHandledExceptionQueuedEvents() {

        return ((handledExceptions != null)
                ? handledExceptions
                : Collections.<ExceptionQueuedEvent>emptyList());

    }



    // --------------------------------------------------------- Private Methods

    private void handlePartialResponseError(FacesContext context, Throwable t) {
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
            MyAjaxErrorHandlerCache cache = new MyAjaxErrorHandlerCache(errorName);
            root.visitTree(VisitContext.createVisitContext(context), cache);

            UIComponent titleFacet = cache.findCurrentTitleFacet();
            if (titleFacet!=null) {
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

            UIComponent bodyFacet = cache.findCurrentBodyFacet();
            if (bodyFacet!=null) {
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

            List<UIComponent> customContent = cache.findCurrentChildren();
            if (customContent!=null && customContent.size()>0) {
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
//
//            // Update state - is ignored, because ajaxerrorhandler.js is not ready for this update ...
//            if (!context.getViewRoot().isTransient()) {
//                // Get the view state and write it to the response..
//                writer.startElement("updateViewState", null);
//                writer.startCDATA();
//                try {
//                    String state = context.getApplication().getStateManager().getViewState(context);
//                    writer.write(state);
//                } catch (Exception e) {
//                    LOGGER.log(Level.WARNING, "Rendering updateViewState in AjaxExceptionHandler throws exception!", e);
//                    writer.write("<exception />");
//                }
//
//                writer.endCDATA();
//                writer.endElement("updateViewState");
//            }

            writer.endElement("error");

            writer.endDocument();
            context.responseComplete();
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                        ioe.toString(),
                        ioe);
            }
        }
    }

    private boolean isRethrown(Throwable t) {

        return (!(t instanceof AbortProcessingException));

    }

    private void log(ExceptionQueuedEventContext exceptionContext) {

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

    private String getLoggingKey(boolean beforePhase, boolean afterPhase) {
        if (beforePhase) {
            return LOG_BEFORE_KEY;
        } else if (afterPhase) {
            return LOG_AFTER_KEY;
        } else {
            return LOG_KEY;
        }
    }

    private String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
            return "???unknown???";
        }
    }

    static private class MyAjaxErrorHandlerCache implements VisitCallback {
        List<AjaxErrorHandler> defaultAjaxErrorHandler = new LinkedList<AjaxErrorHandler>();
        List<AjaxErrorHandler> typeAjaxErrorHandler = new LinkedList<AjaxErrorHandler>();

        String type;
        MyAjaxErrorHandlerCache(String type) {
            this.type = type;
        }

        UIComponent facetTitle = null;
        UIComponent facetBody = null;
        List<UIComponent> customContent = null;
        boolean isResolved = false;
        void resolveFacetsAndCustomContent() {
            if (isResolved == true) return;
            isResolved = true;
            facetTitle = null;
            facetBody = null;
            customContent = null;

            List<AjaxErrorHandler> ajaxErrorHandlers = new LinkedList<AjaxErrorHandler>(defaultAjaxErrorHandler);
            ajaxErrorHandlers.addAll(typeAjaxErrorHandler);

            for (AjaxErrorHandler ajaxErrorHandler : ajaxErrorHandlers) {
                if (ajaxErrorHandler.getChildCount()>0) {
                    facetBody = null;
                    facetTitle = null;
                    customContent = ajaxErrorHandler.getChildren();
                    continue;
                }
                boolean isCustomContentRedefined =
                                ajaxErrorHandler.getTitle()!=null ||
                                ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.title.toString())!=null ||
                                ajaxErrorHandler.getBody()!=null ||
                                ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.body.toString())!=null ||
                                ajaxErrorHandler.getButton()!=null ||
                                ajaxErrorHandler.getButtonOnclick()!=null;

                if (isCustomContentRedefined) {
                    customContent = null;
                }

                ////// title ...
                // If later ajaxErrorHandler has TITLE attribute => early TITLE in FACET is ignored ...
                if (ajaxErrorHandler.getTitle()!=null) facetTitle = null;
                if (ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.title.toString()) !=null) facetTitle = ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.title.toString());

                ////// body ...
                // If later ajaxErrorHandler has BODY attribute => early TITLE in FACET is ignored ...
                if (ajaxErrorHandler.getBody()!=null) facetBody = null;
                if (ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.body.toString()) !=null) facetBody = ajaxErrorHandler.getFacet(AjaxErrorHandler.PropertyKeys.body.toString());
            }

        }

        UIComponent findCurrentTitleFacet() {
            resolveFacetsAndCustomContent();
            return facetTitle;
        }
        UIComponent findCurrentBodyFacet() {
            resolveFacetsAndCustomContent();
            return facetBody;
        }
        List<UIComponent> findCurrentChildren() {
            resolveFacetsAndCustomContent();
            return customContent;
        }

        public VisitResult visit(VisitContext context, UIComponent target) {
            if (!target.isRendered()) {
                return VisitResult.REJECT;
            }
            if (!(target instanceof AjaxErrorHandler)) return VisitResult.ACCEPT;

            AjaxErrorHandler handler = (AjaxErrorHandler) target;

            if (handler.getType()==null) {
                isResolved = false;
                defaultAjaxErrorHandler.add(handler);
            }
            else if (StringUtils.equals(handler.getType(), this.type)) {
                isResolved = false;
                typeAjaxErrorHandler.add(handler);
            }

            return VisitResult.REJECT;
        }
    }


//    private void findAjaxErrorHandlers(MyAjaxErrorHandlerCache cache, UIComponent component) {
//        if (component == null) return;
//        if (!component.isRendered()) return;
//        if (component instanceof AjaxErrorHandler) {
//            cache.add((AjaxErrorHandler)component);
//            return;
//        }
//        if (component.getChildCount()==0) return;
//        for (UIComponent child : component.getChildren()) {
//            findAjaxErrorHandlers(cache, child);
//        }
//    }
}
