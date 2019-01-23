# Common module of Exasol Virtual Schemas Adapters

This is one of the modules of Virtual Schemas Adapters.
The libraries provided by this project are the foundation of the adapter development, i.e. adapters must be implemented on top of them.
You can find the full description of the project here: https://github.com/exasol/virtual-schemas

Please note that the artifact name changed from "virtualschema-common" to "virtual-schema-common". First to unify the naming schemes, second to make sure the new adapters do not accidentally use the old line of libraries.

## Dependencies

### Run Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [JSON-P](https://javaee.github.io/jsonp/)                                    | JSON Processing                                        | CDDL-1.0                      |
| [Exasol Script API] (https://www.exasol.com/portal/display/DOC/User+Manual+6.1.0 (Sections 3.6, 3.7))|Accessing objects                  | MIT License                 |
| [Google Guava](https://github.com/google/guava/)                             | Open-source set of common libraries for Java           | Apache License 2.0            |

### Build Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [Apache Maven](https://maven.apache.org/)                                    | Build tool                                             | Apache License 2.0            |
| [Java Hamcrest](http://hamcrest.org/JavaHamcrest/)                           | Checking for conditions in code via existing matchers  | BSD                           |
| [JUnit](https://junit.org/junit5)                                            | Unit testing framework                                 | Eclipse Public License 1.0    |
| [Mockito](http://site.mockito.org/)                                          | Mocking framework                                      | MIT License                   |
