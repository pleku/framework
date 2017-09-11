/*
 * Copyright 2000-2014 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.vaadin.server.communication;

import javax.servlet.http.HttpServletResponse;

import com.vaadin.server.BootstrapHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinServletResponse;
import com.vaadin.server.VaadinServletService;

public class ServletBootstrapHandler extends BootstrapHandler {
    @Override
    protected String getServiceUrl(BootstrapContext context) {
        VaadinRequest request = context.getRequest();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            pathInfo = "";
        }

        String servletPath = "";
        if (request instanceof VaadinServletRequest) {
            servletPath = ((VaadinServletRequest) request).getServletPath();
            if(servletPath == null) {
                servletPath = "";
            }
            if (!servletPath.isEmpty() && !servletPath.endsWith("/")) {
                servletPath += "/";
            }
        }

        /*
         * Make a relative URL to the servlet by adding one ../ for each path
         * segment in pathInfo (i.e. the part of the requested path that comes
         * after the servlet mapping)
         */
        HttpServletResponse r = ((VaadinServletResponse) context.getResponse())
                .getHttpServletResponse();
        return r.encodeURL(servletPath
                + VaadinServletService.getCancelingRelativePath(pathInfo));

    }

    @Override
    public String getThemeName(BootstrapContext context) {
        String themeName = context.getRequest()
                .getParameter(VaadinServlet.URL_PARAMETER_THEME);
        if (themeName == null) {
            themeName = super.getThemeName(context);
        }
        return themeName;
    }
}
