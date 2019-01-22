# Common module of Exasol JDBC Adapter for Virtual Schemas

This is one of the modules of Exasol JDBC Adapter for Virtual Schemas.
It's not supposed to be used by itself, separately.
You can find the full description of the project here: https://github.com/exasol/virtual-schemas


## Dependencies

### Run Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [GlassFish](https://javaee.github.io/jsonp/)                                 | JSON Processing                                        | CDDL-1.0                      |
| [Exasol Script API](TODO)                                                    | Accessing objects                                      | MIT License                   |
| [Google Guava](https://github.com/google/guava/)                             | Open-source set of common libraries for Java           | Apache License 2.0            |

### Build Time Dependencies

| Dependency                                                                   | Purpose                                                | License                       |
|------------------------------------------------------------------------------|--------------------------------------------------------|-------------------------------|
| [Apache Maven](https://maven.apache.org/)                                    | Build tool                                             | Apache License 2.0            |
| [Java Hamcrest](http://hamcrest.org/JavaHamcrest/)                           | Checking for conditions in code via existing matchers  | BSD                           |
| [JUnit](https://junit.org/junit5)                                            | Unit testing framework                                 | Eclipse Public License 1.0    |
| [Mockito](http://site.mockito.org/)                                          | Mocking framework                                      | MIT License                   |


### Open Tasks
[ ] find URL of script API and add it to the dependency list