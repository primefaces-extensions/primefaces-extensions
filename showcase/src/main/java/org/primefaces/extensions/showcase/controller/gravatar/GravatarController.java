/*
 * Copyright 2011-2020 PrimeFaces Extensions
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
package org.primefaces.extensions.showcase.controller.gravatar;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Example Bean for Gravatar component
 *
 * @author f.strazzullo
 */
@Named
@ViewScoped
public class GravatarController implements Serializable {

    private static final long serialVersionUID = 1L;

    private int size = 200;
    private String notFound = "default";
    private String username = "iamadummygravataruser@gmail.com";

    public String getNotFound() {
        return notFound;
    }

    public void setNotFound(final String notFound) {
        this.notFound = notFound;
    }

    public int getSize() {
        return size;
    }

    public void setSize(final int size) {
        this.size = size;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
