# Common module of Exasol Virtual Schemas Adapters 16.2.0, released 2022-12-01

Code name: Dependency Upgrade

## Summary

Discontinuation of Exasol's artifactory maven.exasol.com caused some problems. In particular maven coordinates of `com.exasol:exasol-script-api:6.1.7` have changed to `com:exasol:udf-api-java:1.0.1`.

## Bugfixes

* #270: Updated dependencies

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:error-reporting-java:0.4.1` to `1.0.0`
* Updated `jakarta.json:jakarta.json-api:2.1.0` to `2.1.1`

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.10` to `3.12`
* Updated `org.junit.jupiter:junit-jupiter:5.8.2` to `5.9.1`
* Updated `org.mockito:mockito-junit-jupiter:4.6.1` to `4.9.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.2` to `1.2.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.7.0` to `2.9.1`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.15` to `0.16`
* Updated `org.apache.maven.plugins:maven-deploy-plugin:3.0.0-M1` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-javadoc-plugin:3.4.0` to `3.4.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.2.7` to `1.3.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.10.0` to `2.13.0`
