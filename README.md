# Common module of Exasol Virtual Schemas Adapters

[![Build Status](https://github.com/exasol/virtual-schema-common-java/actions/workflows/ci-build.yml/badge.svg)](https://github.com/exasol/virtual-schema-common-java/actions/workflows/ci-build.yml)
[![Maven Central &ndash; Common module of Exasol Virtual Schemas Adapters](https://img.shields.io/maven-central/v/com.exasol/virtual-schema-common-java)](https://search.maven.org/artifact/com.exasol/virtual-schema-common-java)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Avirtual-schema-common-java&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Avirtual-schema-common-java)

This is one of the modules of Virtual Schemas Adapters. The libraries provided by this project are the foundation of the adapter development, i.e. adapters must be implemented on top of them.

This module is part of a larger project called [Virtual Schemas](https://github.com/exasol/virtual-schemas) covering JDBC based dialects as well as others.

A Virtual Schema adapter is basically a [UDF](https://docs.exasol.com/database_concepts/udf_scripts.htm). The Exasol core database communicates with this UDF using JSON strings. There are different types of messages, that define the API for a virtual Schema adapter ([protocol reference](doc/development/api/virtual_schema_api.md)). This repository wraps this JSON API with a Java API to facilitate the implementation of Virtual Schema adapters in Java.

## Information for Users

* [List of Virtual Schema dialects](https://github.com/exasol/virtual-schemas/blob/main/doc/user-guide/dialects.md)
* [Changelog](doc/changes/changelog.md)
* [User Requirements](doc/user_requirements.md)
* [Dependencies](dependencies.md)

## Information for Developers

* [Virtual Schema API](doc/development/api/virtual_schema_api.md)
* [Software Design](doc/design.md)
* [Capabilities list](doc/development/api/capabilities_list.md)
* [System Requirements](doc/system_requirements.md)
* [Design](doc/design.md)
* [Developer Guide](doc/development/developer_guide.md)

## Open Source Project Support

Please note that this is an open source project which is officially supported by Exasol.

Unless you are writing your own adapter based on this common module here, please check for problem solutions in the main project first.

