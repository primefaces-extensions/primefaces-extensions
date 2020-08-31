[![Maven](https://img.shields.io/maven-central/v/org.primefaces.extensions/primefaces-extensions.svg)](https://repo1.maven.org/maven2/org/primefaces/extensions/primefaces-extensions/)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)
[![Join the chat at https://gitter.im/primefaces-extensions/primefaces-extensions](https://badges.gitter.im/primefaces-extensions/primefaces-extensions.svg)](https://gitter.im/primefaces-extensions/primefaces-extensions?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Build Status](https://travis-ci.org/primefaces-extensions/core.svg?branch=master)](https://travis-ci.org/primefaces-extensions/core)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.primefaces.extensions%3Aprimefaces-extensions-parent&metric=security_rating)](https://sonarcloud.io/dashboard?id=org.primefaces.extensions%3Aprimefaces-extensions-parent)
[![Stackoverflow](https://img.shields.io/badge/StackOverflow-primefaces-chocolate.svg)](https://stackoverflow.com/questions/tagged/primefaces-extensions)



PrimeFaces Extensions
==========================

This is an overview page, please visit [primefaces-extensions.github.io](http://primefaces-extensions.github.io/) for more information.

[![PrimeFaces Extensions Logo](http://primefaces-extensions.github.io/reports/images/title.png)](https://www.primefaces.org/showcase-ext/)

##### Maven

```xml
<dependency>
    <groupId>org.primefaces.extensions</groupId>
    <artifactId>primefaces-extensions</artifactId>
    <version>8.0.4</version>
</dependency>
<dependency>
    <groupId>org.primefaces.extensions</groupId>
    <artifactId>resources-ckeditor</artifactId>
    <version>8.0.4</version>
</dependency>
```
 ##### Namespaces
 
 The PrimeFaces Extensions namespace is necessary to add PrimeFaces Extensions components to your pages.
 
 ```xml
 xmlns:pe="http://primefaces.org/ui/extensions"
 ```

### Getting Started
***
Please refer to the [Getting Started Guide](https://github.com/primefaces-extensions/primefaces-extensions.github.com/wiki/Getting-Started) to see what you need to get started and any optional libraries.

### Demo
***
Please refer to the Showcase submodule in order to see the full usage of the components. Simply do the following to run the Showcase:
```
cd showcase
mvn clean jetty:run
```
Then open your web browser to http://localhost:8080/primeext-showcase/

### Issues
***
Please report all issues [here](https://github.com/primefaces-extensions/primefaces-extensions/issues).

### Releasing
***
- Run `mvn versions:set -DgenerateBackupPoms=false -DnewVersion=8.0.2` to update all modules versions
- Commit and push the changes to GitHub
- In GitHub create a new Release titled `8.0.2` to tag this release
- Run `mvn clean deploy -Prelease` to push to Maven Central

### Licenses
***
#### Extensions
Licensed under the [Apache-2.0](https://www.apache.org/licenses/LICENSE-2.0) license.

`SPDX-License-Identifier: Apache-2.0`

#### CKEditor (IMPORTANT!!!)
Copyright (c) 2003-2020, CKSource - Frederico Knabben. All rights reserved.<br>
For licensing see: [https://ckeditor.com/legal/ckeditor-oss-license](https://ckeditor.com/legal/ckeditor-oss-license)
