# Common module of Exasol Virtual Schemas Adapters

[![Build Status](https://api.travis-ci.org/exasol/virtual-schema-common-java.svg?branch=master)](https://travis-ci.org/exasol/virtual-schema-common-java)

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

Please note that the artifact name changed from "virtualschema-common" to "virtual-schema-common". First to unify the naming schemes, second to make sure the new adapters do not accidentally use the old line of libraries.

## Dependencies

### Run Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [JSON-P](https://javaee.github.io/jsonp/)                                    | JSON Processing                                        | CDDL-1.0                      |
| [Exasol Script API](https://www.exasol.com/portal/display/DOC/User+Manual+6.1.0 (Sections 3.6, 3.7)) | Accessing Exasol features      | MIT License                   |
| [Google Guava](https://github.com/google/guava/)                             | Open-source set of common libraries for Java           | Apache License 2.0            |

### Build Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [Apache Maven](https://maven.apache.org/)                                    | Build tool                                             | Apache License 2.0            |
| [Java Hamcrest](http://hamcrest.org/JavaHamcrest/)                           | Checking for conditions in code via matchers           | BSD                           |
| [JUnit](https://junit.org/junit5)                                            | Unit testing framework                                 | Eclipse Public License 1.0    |
| [Mockito](http://site.mockito.org/)                                          | Mocking framework                                      | MIT License                   |

## Open Source Project Support

Please note that this is an open source project which is officially supported by Exasol. This module is part of a larger project called [Virtual Schemas](https://github.com/exasol/virtual-schema).

Unless you are writing your own adapter based on this common module here, please check for problem solutions in the master project first.
