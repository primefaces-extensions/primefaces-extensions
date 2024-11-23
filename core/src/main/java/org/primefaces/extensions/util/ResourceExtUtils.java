/*
 * Copyright (c) 2011-2024 PrimeFaces Extensions
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.primefaces.extensions.util;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

import org.primefaces.config.PrimeEnvironment;
import org.primefaces.context.PrimeApplicationContext;

/**
 * Utility class for managing JSF resource components such as JavaScript and CSS files in a JSF application. This class provides methods to dynamically add
 * resources to the head or body of the HTML page rendered by the Faces context, ensuring proper uniqueness and avoiding duplicate resource inclusion.
 * <p>
 * It also includes MyFaces-specific handling for resource dependency IDs.
 * </p>
 * <p>
 * Features include:
 * </p>
 * <ul>
 * <li>Adding JavaScript or CSS resources to the view dynamically.</li>
 * <li>Adding inline scripts to either the head or body of the page.</li>
 * <li>MyFaces compatibility for setting unique resource IDs.</li>
 * </ul>
 * <p>
 * <b>Usage:</b>
 * </p>
 * 
 * <pre>
 * {@code
 * FacesContext context = FacesContext.getCurrentInstance();
 * ResourceExtUtils.addJavascriptResource(context, "libraryName", "script.js");
 * ResourceExtUtils.addCssResource(context, "libraryName", "styles.css");
 * ResourceExtUtils.addScriptToBody(context, "console.log('Hello!');");
 * }
 * </pre>
 *
 * @since 14.0.8
 */
public class ResourceExtUtils {

    /** Renderer type for CSS stylesheet resources in JSF. */
    public static final String RENDERER_TYPE_CSS = "javax.faces.resource.Stylesheet";

    /** Renderer type for JavaScript resources in JSF. */
    public static final String RENDERER_TYPE_JS = "javax.faces.resource.Script";

    /** MyFaces specific attribute key for resource dependency unique ID handling. */
    private static final String MYFACES_RESOURCE_DEPENDENCY_UNIQUE_ID = "oam.view.resourceDependencyUniqueId";

    /**
     * Adds a JavaScript resource to the view.
     * 
     * @param context The FacesContext
     * @param libraryName The library name containing the resource
     * @param resourceName The name of the JavaScript resource
     */
    public static void addJavascriptResource(FacesContext context, String libraryName, String resourceName) {
        addResource(context, RENDERER_TYPE_JS, libraryName, resourceName);
    }

    /**
     * Adds a CSS resource to the view.
     * 
     * @param context The FacesContext
     * @param libraryName The library name containing the resource
     * @param resourceName The name of the CSS resource
     */
    public static void addCssResource(FacesContext context, String libraryName, String resourceName) {
        addResource(context, RENDERER_TYPE_CSS, libraryName, resourceName);
    }

    /**
     * Adds a resource to the view if it hasn't been rendered yet.
     * 
     * @param context The FacesContext
     * @param type The renderer type (CSS or JS)
     * @param libraryName The library name containing the resource
     * @param resourceName The name of the resource
     */
    public static void addResource(FacesContext context, String type, String libraryName, String resourceName) {
        if (!context.getApplication().getResourceHandler().isResourceRendered(context, resourceName, libraryName)) {
            addScriptResourceToHead(context, type, libraryName, resourceName);
        }
    }

    /**
     * Adds a resource component to the specified target, avoiding duplicates based on ID.
     * 
     * @param context The FacesContext
     * @param type The renderer type
     * @param libraryName The library name
     * @param resourceName The resource name
     * @param target The target location ("head" or "body")
     * @return The added or existing UIComponent
     */
    private static UIComponent addScriptResourceToTarget(FacesContext context, String type, String libraryName, String resourceName, String target) {
        String id = (libraryName != null ? libraryName.replaceAll("\\W+", "_") + "_" : "") + resourceName.replaceAll("\\W+", "_");

        // Check for existing resource with same ID
        for (UIComponent existingResource : context.getViewRoot().getComponentResources(context)) {
            if (id.equals(existingResource.getId())) {
                return existingResource;
            }
        }

        UIOutput outputScript = createScriptResource(type);
        outputScript.setId(id);

        if (libraryName != null) {
            outputScript.getAttributes().put("library", libraryName);
        }

        outputScript.getAttributes().put("name", resourceName);
        return addComponentResource(context, outputScript, target);
    }

    /**
     * Creates a new UIOutput component with the specified renderer type.
     * 
     * @param type The renderer type
     * @return New UIOutput component
     */
    private static UIOutput createScriptResource(String type) {
        UIOutput outputScript = new UIOutput();
        outputScript.setRendererType(type);
        return outputScript;
    }

    /**
     * Adds a resource to the head section.
     */
    private static void addScriptResourceToHead(FacesContext context, String type, String libraryName, String resourceName) {
        addScriptResourceToTarget(context, type, libraryName, resourceName, "head");
    }

    /**
     * Adds a resource to the body section.
     */
    private static void addScriptResourceToBody(FacesContext context, String type, String libraryName, String resourceName) {
        addScriptResourceToTarget(context, type, libraryName, resourceName, "body");
    }

    /**
     * Adds a component resource to the specified target after ensuring it has a unique ID.
     * 
     * @param context The FacesContext
     * @param resource The component resource to add
     * @param target The target location ("head" or "body")
     * @return The added component
     */
    private static UIComponent addComponentResource(FacesContext context, UIComponent resource, String target) {
        if (resource.getId() == null) {
            setComponentResourceUniqueId(context, resource);
        }

        context.getViewRoot().addComponentResource(context, resource, target);
        return resource;
    }

    /**
     * Adds inline JavaScript code to the body section.
     * 
     * @param context The FacesContext
     * @param script The JavaScript code to add
     */
    public static void addScriptToBody(FacesContext context, String script) {
        addScriptToTarget(context, script, "body");
    }

    /**
     * Adds inline JavaScript code to the head section.
     * 
     * @param context The FacesContext
     * @param script The JavaScript code to add
     */
    public static void addScriptToHead(FacesContext context, String script) {
        addScriptToTarget(context, script, "head");
    }

    /**
     * Adds inline JavaScript code to the specified target section.
     * 
     * @param context The FacesContext
     * @param script The JavaScript code to add
     * @param target The target location ("head" or "body")
     */
    public static void addScriptToTarget(FacesContext context, String script, String target) {
        UIOutput outputScript = createScriptResource(RENDERER_TYPE_JS);
        UIOutput content = new UIOutput();
        content.setValue(script);
        outputScript.getChildren().add(content);
        addComponentResource(context, outputScript, target);
    }

    /**
     * Sets a unique ID for a component resource, handling MyFaces-specific ID generation. MyFaces requires a special flag to be set during ID generation for
     * resources.
     * 
     * @param context The FacesContext
     * @param resource The component resource needing a unique ID
     */
    public static void setComponentResourceUniqueId(FacesContext context, UIComponent resource) {
        UIViewRoot view = context.getViewRoot();
        PrimeEnvironment environment = PrimeApplicationContext.getCurrentInstance(context).getEnvironment();
        boolean isMyFacesUsed = !environment.isMojarra();

        if (isMyFacesUsed) {
            view.getAttributes().put(MYFACES_RESOURCE_DEPENDENCY_UNIQUE_ID, Boolean.TRUE);
        }

        try {
            resource.setId(view.createUniqueId(context, null));
        }
        finally {
            if (isMyFacesUsed) {
                view.getAttributes().put(MYFACES_RESOURCE_DEPENDENCY_UNIQUE_ID, Boolean.FALSE);
            }
        }
    }
}