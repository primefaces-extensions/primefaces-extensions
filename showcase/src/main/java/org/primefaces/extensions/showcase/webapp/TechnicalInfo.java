/*
 * Copyright 2011-2021 PrimeFaces Extensions
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
 */
package org.primefaces.extensions.showcase.webapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.commons.lang3.StringUtils;

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
    private boolean mojarra = true;

    private List<String> newComponents = new ArrayList<String>();
    private List<String> updatedComponents = new ArrayList<String>();
    private List<String> deprecatedComponents = new ArrayList<String>();

    @PostConstruct
    protected void initialize() {
        ResourceBundle rb;
        try {
            rb = ResourceBundle.getBundle("pe-showcase");

            String strAppProps = rb.getString("application.properties");
            final int lastBrace = strAppProps.indexOf("}");
            strAppProps = strAppProps.substring(1, lastBrace);

            final Map<String, String> appProperties = new HashMap<String, String>();
            final String[] appProps = strAppProps.split("[\\s,]+");
            for (final String appProp : appProps) {
                final String[] keyValue = appProp.split("=");
                if (keyValue != null && keyValue.length > 1) {
                    appProperties.put(keyValue[0], keyValue[1]);
                }
            }

            primeFaces = "PrimeFaces: " + appProperties.get("primefaces.core.version");
            primeFacesExt = "PrimeFaces Extensions: " + appProperties.get("primefaces-extensions.core.version");
            jsfImpl = "JSF: " + appProperties.get("jsf-impl") + " " + appProperties.get("jsf-version");
            server = "Server: " + ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext())
                        .getServerInfo();

            final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            final Calendar calendar = Calendar.getInstance();

            if (appProperties.containsKey("timestamp")) {
                calendar.setTimeInMillis(Long.valueOf(appProperties.get("timestamp")));
            }

            buildTime = "Build time: " + formatter.format(calendar.getTime());
            mojarra = appProperties.get("jsf-impl").contains("mojarra");

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
            newComponents = new ArrayList<String>();
            updatedComponents = new ArrayList<String>();
            deprecatedComponents = new ArrayList<String>();
        }
    }
}
