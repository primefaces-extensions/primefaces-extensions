/*
 * Copyright (c) 2011-2023 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.webapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;
import org.omnifaces.util.Faces;

/**
 * TechnicalInfo.
 *
 * @author Oleg Varaksin / last modified by Melloware
 * @version $Revision$
 */
@ApplicationScoped
@Named
public class TechnicalInfo {

    private static final Logger LOGGER = Logger.getLogger(TechnicalInfo.class.getName());
    private String primeFaces;
    private String primeFacesExt;
    private String jsfImpl;
    private String server;
    private String buildTime;
    private boolean mojarra = false;

    private List<String> newComponents = new ArrayList<>();
    private List<String> updatedComponents = new ArrayList<>();
    private List<String> deprecatedComponents = new ArrayList<>();

    @PostConstruct
    protected void initialize() {
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle("pe-showcase");

            String strAppProps = rb.getString("application.properties");
            final int lastBrace = strAppProps.indexOf("}");
            strAppProps = strAppProps.substring(1, lastBrace);

            final Map<String, String> appProperties = new HashMap<>();
            final String[] appProps = strAppProps.split("[\\s,]+");
            for (final String appProp : appProps) {
                final String[] keyValue = appProp.split("=");
                if (keyValue != null && keyValue.length > 1) {
                    appProperties.put(keyValue[0], keyValue[1]);
                }
            }

            // PF version
            primeFaces = "PrimeFaces: " + StringUtils.defaultIfEmpty(org.primefaces.util.Constants.class.getPackage().getImplementationVersion(), "???");
            primeFacesExt = "PrimeFaces Extensions: " + StringUtils
                        .defaultIfEmpty(org.primefaces.extensions.util.Constants.class.getPackage().getImplementationVersion(), "???");
            jsfImpl = "JSF: " + Faces.getImplInfo();
            server = "Server: " + ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
                        .getServerInfo();

            final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            final Calendar calendar = Calendar.getInstance();

            if (appProperties.containsKey("timestamp")) {
                calendar.setTimeInMillis(Long.valueOf(appProperties.get("timestamp")));
            }

            buildTime = "Build time: " + formatter.format(calendar.getTime());
            mojarra = Faces.getImplInfo().contains("mojarra");

            // process new and updated components
            processComponentTypes(appProperties.get("primefaces-extensions.new-components"),
                        appProperties.get("primefaces-extensions.updated-components"),
                        appProperties.get("primefaces-extensions.deprecated-components"));
        }
        catch (final MissingResourceException e) {
            LOGGER.warning("Resource bundle 'pe-showcase' was not found");
        }
    }

    public String getPrimeFaces() {
        return primeFaces;
    }

    public String getPrimeFacesExt() {
        return primeFacesExt;
    }

    public String getJsfImpl() {
        return jsfImpl;
    }

    public String getServer() {
        return server;
    }

    public String getBuildTime() {
        return buildTime;
    }

    public boolean isMojarra() {
        return mojarra;
    }

    public String getMenuitemIconStyleClass(final String page) {
        final String check = page.toLowerCase();
        if (newComponents.contains(check)) {
            return "ui-icon-new-comp";
        }

        if (updatedComponents.contains(check)) {
            return "ui-icon-updated-comp";
        }

        if (deprecatedComponents.contains(check)) {
            return "ui-icon-deprecated-comp";
        }

        return "ui-icon-none";
    }

    private void processComponentTypes(final String newComp, final String updatedComp, final String deprecatedComp) {
        try {
            if (StringUtils.isNotBlank(newComp)) {
                final String[] newCompArray = newComp.toLowerCase().split(";");
                Collections.addAll(newComponents, newCompArray);
            }

            if (StringUtils.isNotBlank(updatedComp)) {
                final String[] updatedCompArray = updatedComp.toLowerCase().split(";");
                Collections.addAll(updatedComponents, updatedCompArray);
            }

            if (StringUtils.isNotBlank(deprecatedComp)) {
                final String[] deprecatedCompArray = deprecatedComp.toLowerCase().split(";");
                Collections.addAll(deprecatedComponents, deprecatedCompArray);
            }
        }
        catch (final Exception ex) {
            newComponents = new ArrayList<>();
            updatedComponents = new ArrayList<>();
            deprecatedComponents = new ArrayList<>();
        }
    }
}
