[![Maven](https://img.shields.io/maven-central/v/org.primefaces.extensions/primefaces-extensions.svg)](https://repo1.maven.org/maven2/org/primefaces/extensions/primefaces-extensions/)
[![Actions Status](https://github.com/primefaces-extensions/primefaces-extensions/workflows/Java%20CI/badge.svg)](https://github.com/primefaces-extensions/primefaces-extensions/actions)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=org.primefaces.extensions%3Aprimefaces-extensions-parent&metric=bugs)](https://sonarcloud.io/dashboard?id=org.primefaces.extensions%3Aprimefaces-extensions-parent)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=org.primefaces.extensions%3Aprimefaces-extensions-parent&metric=security_rating)](https://sonarcloud.io/dashboard?id=org.primefaces.extensions%3Aprimefaces-extensions-parent)
[![Discord Chat](https://img.shields.io/discord/591914197219016707.svg?color=7289da&label=chat&logo=discord&style=flat-square)](https://discord.gg/gzKFYnpmCY)
[![Stackoverflow](https://img.shields.io/badge/StackOverflow-primefaces-chocolate.svg)](https://stackoverflow.com/questions/tagged/primefaces-extensions)
[![License](http://img.shields.io/:license-apache-yellow.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)



PrimeFaces Extensions
==========================

This is an overview page, please visit [primefaces-extensions.github.io](http://primefaces-extensions.github.io/) for more information.

[![PrimeFaces Extensions Logo](http://primefaces-extensions.github.io/reports/images/title.png)](https://www.primefaces.org/showcase-ext/)

### Maven

##### Release (javax)

```xml

<dependency>
    <groupId>org.primefaces.extensions</groupId>
    <artifactId>primefaces-extensions</artifactId>
    <version>${primefaces-extensions.version}</version>
</dependency>
<dependency>
<groupId>org.primefaces.extensions</groupId>
<artifactId>resources-ckeditor</artifactId>
<version>${primefaces-extensions.version}</version>
</dependency>
```

##### Release (jakarta)

```xml

<dependency>
    <groupId>org.primefaces.extensions</groupId>
    <artifactId>primefaces-extensions</artifactId>
    <version>${primefaces-extensions.version}</version>
    <classifier>jakarta</classifier>
</dependency>
```

##### Snapshots

```xml

<repository>
    <id>sonatype-snapshots</id>
    <name>Sonatype Snapshot Repository</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
<dependency>
<groupId>org.primefaces.extensions</groupId>
<artifactId>primefaces-extensions</artifactId>
<version>${primefaces-extensions.version}</version>
</dependency>
```

### Namespaces

The PrimeFaces Extensions namespace is necessary to add PrimeFaces Extensions components to your pages.

 ```xml
 xmlns:pe="http://primefaces.org/ui/extensions"
 ```

### Getting Started

***
Please refer to the [Getting Started Guide](https://github.com/primefaces-extensions/primefaces-extensions.github.com/wiki/Getting-Started) to see what you need
to get started and any optional libraries.

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
