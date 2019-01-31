# Common module of Exasol Virtual Schemas Adapters

[![Build Status](https://travis-ci.com/exasol/virtual-schema-common-java.svg?branch=origin)](https://travis-ci.org/exasol/virtual-schema-common-java)

SonarCloud results:

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=security_rating)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=sqale_index)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=code_smells)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=coverage)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=exasol_virtual-schema-common-java&metric=ncloc)](https://sonarcloud.io/dashboard?id=exasol_virtual-schema-common-java)

<p style="border: 1px solid black;padding: 10px; background-color: #FFFFCC;"><span style="font-size:200%">&#9888;</span> Please note that this is an open source project which is officially supported by Exasol. For any question, you can contact our support team.</p>

This is one of the modules of Virtual Schemas Adapters.
The libraries provided by this project are the foundation of the adapter development, i.e. adapters must be implemented on top of them.
You can find the full description of the project here: https://github.com/exasol/virtual-schemas

Please note that the artifact name changed from "virtualschema-common" to "virtual-schema-common". First to unify the naming schemes, second to make sure the new adapters do not accidentally use the old line of libraries.

## Dependencies

### Run Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [JSON-P](https://javaee.github.io/jsonp/)                                    | JSON Processing                                        | CDDL-1.0                      |
| [Exasol Script API] (https://www.exasol.com/portal/display/DOC/User+Manual+6.1.0 (Sections 3.6, 3.7))|Accessing objects               | MIT License                 |
| [Google Guava](https://github.com/google/guava/)                             | Open-source set of common libraries for Java           | Apache License 2.0            |

### Build Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [Apache Maven](https://maven.apache.org/)                                    | Build tool                                             | Apache License 2.0            |
| [Java Hamcrest](http://hamcrest.org/JavaHamcrest/)                           | Checking for conditions in code via matchers           | BSD                           |
| [JUnit](https://junit.org/junit5)                                            | Unit testing framework                                 | Eclipse Public License 1.0    |
| [Mockito](http://site.mockito.org/)                                          | Mocking framework                                      | MIT License                   |
