# Common module of Exasol Virtual Schemas Adapters

[![Build Status](https://api.travis-ci.org/exasol/virtual-schema-common-java.svg?branch=master)](https://travis-ci.org/exasol/virtual-schema-common-java)
[![Maven Central](https://img.shields.io/maven-central/v/com.exasol/virtual-schema-common-java)](https://search.maven.org/artifact/com.exasol/virtual-schema-common-java)

SonarCloud results:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)

This is one of the modules of Virtual Schemas Adapters.
The libraries provided by this project are the foundation of the adapter development, i.e. adapters must be implemented on top of them.
You can find the full description of the project here: https://github.com/exasol/virtual-schemas

Please note that the artifact name changed from "virtualschema-common" to "virtual-schema-common-java". First to unify the naming schemes, second to make sure the new adapters do not accidentally use the old line of libraries.

## Information for Users

* [Changelog](doc/changes/changelog.md)

## Information for Developers

* [Virtual Schema API](doc/development/api/virtual_schema_api.md)
* [Capabilities list](doc/development/api/capabilities_list.md)

## Dependencies

### Run Time Dependencies

| Dependency                                                                          | Purpose                                                | License                       |
|-------------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [JSON-P](https://javaee.github.io/jsonp/)                                           | JSON Processing                                        | CDDL-1.0                      |
| [Exasol Script API](https://docs.exasol.com/database_concepts/udf_scripts.htm)      | Accessing Exasol features                              | MIT License                   |

### Build Time Dependencies

| Dependency                                                                          | Purpose                                                | License                       |
|-------------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [Apache Maven](https://maven.apache.org/)                                           | Build tool                                             | Apache License 2.0            |
| [Java Hamcrest](http://hamcrest.org/JavaHamcrest/)                                  | Checking for conditions in code via matchers           | BSD License                   |
| [JSONassert](http://jsonassert.skyscreamer.org/)                                    | Compare JSON documents for semantic equality           | Apache License 2.0            |
| [JUnit](https://junit.org/junit5)                                                   | Unit testing framework                                 | Eclipse Public License 1.0    |
| [Mockito](http://site.mockito.org/)                                                 | Mocking framework                                      | MIT License                   |
| [JUnit 5 System Extensions](https://github.com/itsallcode/junit5-system-extensions) | Capturing `STDOUT` and `STDERR`                        | Eclipse Public License 2.0    |
| [Equals Verifier](https://jqno.nl/equalsverifier/)                                  | Testing `equals(...)` and `hashCode()` contracts       | Apache License 2.0            |

### Maven Plug-ins

| Plug-in                                                                        | Purpose                                                | License                       |
---------------------------------------------------------------------------------|--------------------------------------------------------|--------------------------------
| [Maven Compiler Plugin][maven-compiler-plugin]                                 | Setting required Java version                          | Apache License 2.0            |
| [Maven GPG Plugin](https://maven.apache.org/plugins/maven-gpg-plugin/)         | Signs JARs                                             | Apache License 2.0            |
| [Maven Enforcer Plugin][maven-enforcer-plugin]                                 | Controlling environment constants                      | Apache License 2.0            |
| [Maven Jacoco Plugin](https://www.eclemma.org/jacoco/trunk/doc/maven.html)     | Code coverage metering                                 | Eclipse Public License 2.0    |
| [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/) | Creates JavaDoc JARs                                   | Apache License 2.0            |
| [Maven Source Plugin](https://maven.apache.org/plugins/maven-source-plugin/)   | Creates source JARs                                    | Apache License 2.0            |
| [Maven Surefire Plugin][maven-surefire-plugin]                                 | Unit testing                                           | Apache License 2.0            |
| [Sonatype OSS Index Maven Plugin][sonatype-oss-index-maven-plugin]             | Checking Dependencies Vulnerability                    | ASL2                          |
| [Versions Maven Plugin][versions-maven-plugin]                                 | Checking if dependencies updates are available         | Apache License 2.0            |

[maven-compiler-plugin]: https://maven.apache.org/plugins/maven-compiler-plugin/
[maven-enforcer-plugin]: http://maven.apache.org/enforcer/maven-enforcer-plugin/
[maven-surefire-plugin]: https://maven.apache.org/surefire/maven-surefire-plugin/
[sonatype-oss-index-maven-plugin]: https://sonatype.github.io/ossindex-maven/maven-plugin/
[versions-maven-plugin]: https://www.mojohaus.org/versions-maven-plugin/

## Open Source Project Support

Please note that this is an open source project which is officially supported by Exasol. This module is part of a larger project called [Virtual Schemas](https://github.com/exasol/virtual-schema).

Unless you are writing your own adapter based on this common module here, please check for problem solutions in the master project first.