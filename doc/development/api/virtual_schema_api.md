# Virtual Schema API Documentation

## Table of Contents
- [Introduction](#introduction)
- [Requests and Responses](#requests-and-responses)
  - [Create Virtual Schema](#create-virtual-schema)
  - [Refresh](#refresh)
  - [Set Properties](#set-properties)
  - [Drop Virtual Schema](#drop-virtual-schema)
  - [Get Capabilities](#get-capabilities)
  - [Pushdown](#pushdown)
- [Embedded Commonly Used JSON Elements](#embedded-commonly-used-json-elements)
  - [Schema Metadata Info](#schema-metadata-info)
  - [Schema Metadata](#schema-metadata)
- [Expressions](#expressions)
  - [Table](#table)
  - [Join](#join)
  - [Column Lookup](#column-lookup)
  - [Order By Element](#order-by-element)
  - [Data Types](#data-types)
  - [Literal](#literal)
  - [Predicates](#predicates)
  - [Scalar Functions](#scalar-functions)
  - [Aggregate Functions](#aggregate-functions)

## Introduction

There are the following request and response types:

| Type                        | Called ...                                                            |
| :-------------------------- | :-------------------------------------------------------------------- |
| **Create Virtual Schema**   | &hellip; for each `CREATE VIRTUAL SCHEMA ...` statement               |
| **Refresh**                 | &hellip; for each `ALTER VIRTUAL SCHEMA ... REFRESH ...` statement.   |
| **Set Properties**          | &hellip; for each `ALTER VIRTUAL SCHEMA ... SET ...` statement.       |
| **Drop Virtual Schema**     | &hellip; for each `DROP VIRTUAL SCHEMA ...` statement.                |
| **Get Capabilities**        | &hellip; whenever a virtual table is queried in a `SELECT` statement. |
| **Pushdown**                | &hellip; whenever a virtual table is queried in a `SELECT` statement. |

We describe each of the types in the following sections.

**Please note:** To keep the documentation concise we defined the elements which are commonly in separate sections below, e.g. `schemaMetadataInfo` and `schemaMetadata`.

## Requests and Responses

### Create Virtual Schema

Informs the Adapter about the request to create a Virtual Schema, and asks the Adapter for the metadata (tables and columns).

The Adapter is allowed to throw an Exception if the user missed to provide mandatory properties or in case of any other problems (e.g. connectivity).

**Request:**

```json
{
    "type": "createVirtualSchema",
    "schemaMetadataInfo": {
        ...
    }
}
```

**Response:**

```json
{
    "type": "createVirtualSchema",
    "schemaMetadata": {
        ...
    }
}
```

Notes
* `schemaMetadata` is mandatory. However, it is allowed to contain no tables.


### Refresh

Request to refresh the metadata for the whole Virtual Schema, or for specified tables.

**Request:**

```json
{
    "type": "refresh",
    "schemaMetadataInfo": {
        ...
    },
    "requestedTables": ["T1", "T2"]
}
```

Notes
* `requestedTables` is optional. If existing, only the specified tables shall be refreshed. The specified tables do not have to exist, it just tell Adapter to update these tables (which might be changed, deleted, added, or non-existing).

**Response:**

```json
{
    "type": "refresh",
    "schemaMetadata": {
        ...
    },
    "requestedTables": ["T1", "T2"]
}
```

Notes
* `schemaMetadata` is optional. It can be skipped if the adapter does not want to refresh (e.g. because it detected that there is no change).
* `requestedTables` must exist if and only if the element existed in the request. The values must be the same as in the request (to make sure that Adapter only refreshed these tables).

### Set Properties

Request to set properties. The Adapter can decide whether it needs to send back new metadata. The Adapter is allowed to throw an Exception if the user provided invalid properties or in case of any other problems (e.g. connectivity).

**Request:**

```json
{
    "type": "setProperties",
    "schemaMetadataInfo": {
        ...
    },
    "properties": {
        "JDBC_CONNECTION_STRING": "new-jdbc-connection-string",
        "NEW_PROPERTY": "value of a not yet existing property",
        "DELETED_PROPERTY": null
    }
}
```

**Response:**

```json
{
    "type": "setProperties",
    "schemaMetadata": {
        ...
    }
}
```

Notes
* Request: A property set to null means that this property was asked to be deleted. Properties set to null might also not have existed before.
* Response: `schemaMetadata` is optional. It only exists if the adapter wants to send back new metadata. The existing metadata are overwritten completely.


### Drop Virtual Schema

Inform the Adapter that a Virtual Schema is about to be dropped. The Adapter can update external dependencies if it has such. The Adapter is not expected to throw an exception, and if it does, it will be ignored.

**Request:**

```json
{
    "type": "dropVirtualSchema",
    "schemaMetadataInfo": {
        ...
    }
}
```

**Response:**

```json
{
    "type": "dropVirtualSchema"
}
```


### Get Capabilities

Request the list of capabilities supported by the Adapter. Based on these capabilities, the database will collect everything that can be pushed down in the current query and sends a pushdown request afterwards.

**Request:**

```json
{
    "type": "getCapabilities",
    "schemaMetadataInfo": {
        ...
    }
}
```

**Response:**

```json
{
    "type": "getCapabilities",
    "capabilities": [
        "ORDER_BY_COLUMN",
        "AGGREGATE_SINGLE_GROUP",
        "LIMIT",
        "AGGREGATE_GROUP_BY_TUPLE",
        "FILTER_EXPRESSIONS",
        "SELECTLIST_EXPRESSIONS",
        "SELECTLIST_PROJECTION",
        "AGGREGATE_HAVING",
        "ORDER_BY_EXPRESSION",
        "AGGREGATE_GROUP_BY_EXPRESSION",
        "LIMIT_WITH_OFFSET",
        "AGGREGATE_GROUP_BY_COLUMN",
        "FN_PRED_LESSEQUALS",
        "FN_AGG_COUNT",
        "LITERAL_EXACTNUMERIC",
        "LITERAL_DATE",
        "LITERAL_INTERVAL",
        "LITERAL_TIMESTAMP_UTC",
        "LITERAL_TIMESTAMP",
        "LITERAL_NULL",
        "LITERAL_STRING",
        "LITERAL_DOUBLE",
        "LITERAL_BOOL"
    ]
}
```

The set of capabilities in the example above would be sufficient to pushdown all aspects of the following query:
```sql
SELECT user_id, COUNT(url)
FROM   vs.clicks
WHERE  user_id>1
GROUP  BY user_id
HAVING count(url)>1
ORDER  BY user_id
LIMIT  10;
```

##### Capability Prefixes

- Main Capabilities: No prefix
- Literal Capabilities: LITERAL_
- Predicate Capabilities: FN_PRED_
- Scalar Function Capabilities: FN_
- Aggregate Function Capabilities: FN_AGG_

See also [a list of supported Capabilities](capabilities_list.md).

Capabilities can be also found in the sources of the Virtual Schema Common Java:
* [Main Capabilities](https://github.com/exasol/virtual-schema-common-java/blob/master/src/main/java/com/exasol/adapter/capabilities/MainCapability.java)
* [Literal Capabilities](https://github.com/exasol/virtual-schema-common-java/blob/master/src/main/java/com/exasol/adapter/capabilities/LiteralCapability.java)
* [Predicate Capabilities](https://github.com/exasol/virtual-schema-common-java/blob/master/src/main/java/com/exasol/adapter/capabilities/PredicateCapability.java)
* [Scalar Function Capabilities](https://github.com/exasol/virtual-schema-common-java/blob/master/src/main/java/com/exasol/adapter/capabilities/ScalarFunctionCapability.java)
* [Aggregate Function Capabilities](https://github.com/exasol/virtual-schema-common-java/blob/master/src/main/java/com/exasol/adapter/capabilities/AggregateFunctionCapability.java)

### Pushdown

Contains an abstract specification of what to be pushed down, and requests an pushdown SQL statement from the Adapter which can be used to retrieve the requested data.

**Request:**

Running the following query
```sql
SELECT user_id, COUNT(url)
FROM   vs.clicks
WHERE  user_id>1
GROUP  BY user_id
HAVING count(url)>1
ORDER  BY user_id
LIMIT  10;
```
will produce the following Request, assuming that the Adapter has all required capabilities.

```json
{
    "type": "pushdown",
    "pushdownRequest": {
        "type" : "select",
        "aggregationType" : "group_by",
        "from" :
        {
            "type" : "table",
            "name" : "CLICKS"
        },
        "selectList" :
        [
            {
                "type" : "column",
                "name" : "USER_ID",
                "columnNr" : 1,
                "tableName" : "CLICKS"
            },
            {
                "type" : "function_aggregate",
                "name" : "count",
                "arguments" :
                [
                    {
                        "type" : "column",
                        "name" : "URL",
                        "columnNr" : 2,
                        "tableName" : "CLICKS"
                    }
                ]
            }
        ],
        "filter" :
        {
            "type" : "predicate_less",
            "left" :
            {
                "type" : "literal_exactnumeric",
                "value" : "1"
            },
            "right" :
            {
                "type" : "column",
                "name" : "USER_ID",
                "columnNr" : 1,
                "tableName" : "CLICKS"
            }
        },
        "groupBy" :
        [
            {
                "type" : "column",
                "name" : "USER_ID",
                "columnNr" : 1,
                "tableName" : "CLICKS"
            }
        ],
        "having" :
        {
            "type" : "predicate_less",
            "left" :
            {
                "type" : "literal_exactnumeric",
                "value" : "1"
            },
            "right" :
            {
                "type" : "function_aggregate",
                "name" : "count",
                "arguments" :
                [
                    {
                        "type" : "column",
                        "name" : "URL",
                        "columnNr" : 2,
                        "tableName" : "CLICKS"
                    }
                ]
            }
        },
        "orderBy" :
        [
            {
                "type" : "order_by_element",
                "expression" :
                {
                    "type" : "column",
                    "columnNr" : 1,
                    "name" : "USER_ID",
                    "tableName" : "CLICKS"
                },
                "isAscending" : true,
                "nullsLast" : true
            }
        ],
        "limit" :
        {
            "numElements" : 10
        }
    },
    "involvedTables": [
    {
        "name" : "CLICKS",
        "columns" :
        [
            {
                "name" : "ID",
                "dataType" :
                {
                    "type" : "DECIMAL",
                    "precision" : 18,
                    "scale" : 0
                }
            },
            {
                "name" : "USER_ID",
                "dataType" :
                {
                   "type" : "DECIMAL",
                   "precision" : 18,
                    "scale" : 0
                }
            },
            {
                "name" : "URL",
                "dataType" :
                {
                   "type" : "VARCHAR",
                   "size" : 1000
                }
            },
            {
                "name" : "REQUEST_TIME",
                "dataType" :
                {
                    "type" : "TIMESTAMP"
                }
            }
        ]
    }
    ],
    "schemaMetadataInfo": {
        ...
    }
}
```

Notes
* `pushdownRequest`: Specification what needs to be pushed down. You can think of it like a parsed SQL statement.
  * `from`: The requested from clause. This can be a table or a join.
  * `selectList`: The requested select list. There are three options for this field:
    * `selectList` is not given: This means `SELECT *`. The field `involvedTables` may be used to get the list of columns.
    * `selectList` is an empty array: Select any column/expression. This is used, for example, if a query can not be pushed down completely. The adapter may choose something like `SELECT TRUE` to get the correct number of rows.
    * Otherwise `selectList` contains the requested select list elements, a list of expressions. The order of the elements matters.
  * `filter`: The requested filter (`where` clause), a single expression.
  * `aggregationType`Optional element, set if an aggregation is requested. Either `group_by` or `single_group`, if a aggregate function is used but no group by.
  * `groupBy`: The requested group by clause, a list of expressions.
  * `having`: The requested having clause, a single expression.
  * `orderBy`: The requested order-by clause, a list of `order_by_element` elements.
  * `limit` The requested limit of the result set, with an optional offset.
* `involvedTables`: Metadata of the involved tables, encoded like in schemaMetadata.

**Response:**

Following the example above, a valid result could look like this:

```json
{
    "type": "pushdown",
    "sql": "IMPORT FROM JDBC AT 'jdbc:exa:remote-db:8563;schema=native' USER 'sys' IDENTIFIED BY 'exasol' STATEMENT 'SELECT USER_ID, count(URL) FROM NATIVE.CLICKS WHERE 1 < USER_ID GROUP BY USER_ID HAVING 1 < count(URL) ORDER BY USER_ID LIMIT 10'"
}
```

Notes
* `sql`: The pushdown SQL statement. It must be either an `SELECT` or `IMPORT` statement.

## Embedded Commonly Used JSON Elements

The following Json objects can be embedded in a request or response. They have a fixed structure.

### Schema Metadata Info
This document contains the most important metadata of the virtual schema and is sent to the adapter just "for information" with each request. It is the value of an element called `schemaMetadataInfo`.

```json
{"schemaMetadataInfo":{
    "name": "MY_HIVE_VSCHEMA",
    "adapterNotes": "<serialized adapter state>",
    "properties": {
        "HIVE_SERVER": "my-hive-server",
        "HIVE_DB": "my-hive-db",
        "HIVE_USER": "my-hive-user"
    }
}}
```

### Schema Metadata

This document is usually embedded in responses from the Adapter and informs the database about all metadata of the Virtual Schema, especially the contained Virtual Tables and its columns.

The Adapter can optionally store so called `adapterNotes` on each level (schema, table, column), to remember information which might be relevant for the Adapter in future. Adapter notes are simple strings. You can serialize objects into those strings of course, but keep in mind that the strings are embedded inside the Virtual Schemas JSON protocol, which makes quoting of conflicting characters necessary.

Some options to deal with the embedding issue:

1. After serialization use Base64 encoding or
1. Use a serialization that does not have conflicting characters like a simple CSV or key-value format or
1. Quote conflicting characters

Which variant you should choose depends on considerations like amount of data to be transmitted, original data format and encoding overhead.

In the example below, the Adapter remembers the table partitioning and the data type of a column which is not directly supported in Exasol. The Adapter has this information during push-down and can consider the table partitioning during push-down or can add an appropriate cast for the column.

This example also demonstrates serialization in adapter notes via key-value encoding. As mentioned above more sophisticated serializations are possible as long as you make sure adapter notes are a valid string in the JSON format by encoding or quoting.

```json
{"schemaMetadata":{
    "adapterNotes": "lastRefreshed=2015-03-01 12:10:01;anotherKey=More custom schema state here",
    "tables": [
    {
        "type": "table",
        "name": "EXASOL_CUSTOMERS",
        "adapterNotes": "hivePartitionColumns=CREATED,COUNTRY_ISO",
        "columns": [
        {
            "name": "ID",
            "dataType": {
                "type": "DECIMAL",
                "precision": 18,
                "scale": 0
            },
            "isIdentity": true
        },
        {
            "name": "COMPANY_NAME",
            "dataType": {
                "type": "VARCHAR",
                "size": 1000,
                "characterSet": "UTF8"
            },
            "default": "foo",
            "isNullable": false,
            "comment": "The official name of the company",
            "adapterNotes": "hiveDataType=List<String>"
        },
        {
            "name": "DISCOUNT_RATE",
            "dataType": {
                "type": "DOUBLE"
            }
        }
        ]
    },
    {
        "type": "table",
        "name": "TABLE_2",
        "columns": [
        {
            "name": "COL1",
            "dataType": {
                "type": "DECIMAL",
                "precision": 18,
                "scale": 0
            }
        },
        {
            "name": "COL2",
            "dataType": {
                "type": "VARCHAR",
                "size": 1000
            }
        }
        ]
    }
    ]
}}
```

## Expressions

This section handles the expressions that can occur in a pushdown request. Expressions are consistently encoded in the following way. This allows easy and consisting parsing and serialization.

```json
{
    "type": "<type-of-expression>",
    ...
}
```

Each expression-type can have any number of additional fields of arbitrary type. In the following sections we define the known expressions.

### Table

This element currently only occurs in from clause

```json
{
    "type": "table",
    "name": "CLICKS",
    "alias": "A"
}
```

Notes
* **alias**: This is an optional property and is added if the table has an alias in the original query.

### Join

This element currently only occurs in from clause

```json
{
    "type": "join",
    "join_type": "inner",
    "left": { 
        ... 
    },
    "right" : { 
        ... 
    },
    "condition" : { 
        ... 
    }
}
```

Notes
* **join_type**: Can be `inner`, `left_outer`, `right_outer` or `full_outer`.
* **left**: This can be a `table` or a `join`.
* **right**: This can be a `table` or a `join`.
* **condition**: This can be an arbitrary expression.

### Column Lookup

A column lookup is a reference to a table column. It can reference the table directly or via an alias.

```json
{
    "type": "column",
    "tableName": "T",
    "tableAlias": "A",
    "columnNr": 0,
    "name": "ID"
}
```

Notes
* **tableAlias**: This is an optional property and is added if the referenced table has an alias.
* **columnNr**: Column number in the virtual table, starting with 0.

### Order By Element

```json
{
    "type": "order_by_element",
    "expression":
    {
        ...
    },
    "isAscending": true,
    "nullsLast": true
}
```

Notes
* The field `expression` contains the expression to order by.

### Data Types

Refer to the [Exasol Data Types API Documentation](data_types_api.md)

### Literal

```json
{
    "type": "literal_null"
}
```

```json
{
    "type": "literal_string",
    "value": "my string"
}
```

```json
{
    "type": "literal_double",
    "value": "1.234"
}
```

```json
{
    "type": "literal_exactnumeric",
    "value": "12345"
}
```

```json
{
    "type": "literal_bool",
    "value": true
}
```

```json
{
    "type": "literal_date",
    "value": "2015-12-01"
}
```

```json
{
    "type": "literal_timestamp",
    "value": "2015-12-01 12:01:01.1234"
}
```

```json
{
    "type": "literal_timestamputc",
    "value": "2015-12-01 12:01:01.1234"
}
```

```json
{
    "type": "literal_interval",
    "value": "+2-01",
    "dataType": {
            "type": "INTERVAL",
            "fromTo": "YEAR TO MONTH",
            "precision": 2
        }
}
```

```json
{
    "type": "literal_interval",
    "value": "+0 00:00:02.000",
    "dataType": {
            "type": "INTERVAL",
            "fromTo": "DAY TO SECONDS",
            "precision": 2,
            "fraction": 2
        }
}
```

### Predicates

Whenever there is `...` this is a shortcut for an arbitrary expression.

##### AND / OR

```json
{
    "type": "predicate_and",
    "expressions": [
        ...
    ]
}
```

The same can be used for `predicate_or`.

##### NOT / IS NULL / IS NOT NULL

```json
{
    "type": "predicate_not",
    "expression": {
        ...
    }
}
```

The same can be used for `predicate_is_null`, `predicate_is_not_null`.

##### Comparison operators

```json
{
    "type": "predicate_equal",
    "left": {
        ...
    },
    "right": {
        ...
    }
}
```

The same can be used for `predicate_notequal`, `predicate_less` and `predicate_lessequal`.

##### LIKE

```json
{
    "type": "predicate_like",
    "expression": {
        ...
    },
    "pattern": {
        ...
    },
    "escapeChar": {
        ...
    }
}
```

Notes:
* `escapeChar` is optional.

##### REGEXP_LIKE

```json
{
    "type": "predicate_like_regexp",
    "expression": {
        ...
    },
    "pattern": {
        ...
    }
}
```

##### BETWEEN

```json
{
    "type": "predicate_between",
    "expression": {
        ...
    },
    "left": {
        ...
    },
    "right": {
        ...
    }
}
```

##### IN

`<exp> IN (<const1>, <const2>)`

```json
{
    "type": "predicate_in_constlist",
    "expression": {
        ...
    }
    "arguments": [
        ...
    ]
}
```

##### IS JSON / IS NOT JSON

`exp1 IS JSON {VALUE | ARRAY | OBJECT | SCALAR} {WITH | WITHOUT} UNIQUE KEYS`
 (requires predicate capability `IS_JSON`)

```json
{
    "type": "predicate_is_json",
    "expression": {
        ...
    },
    "typeConstraint": "VALUE",
    "keyUniquenessConstraint": "WITHOUT UNIQUE KEYS"
}
```

Notes:

- typeConstraint is `"VALUE"`, `"ARRAY"`, `"OBJECT"`, or `"SCALAR"`.
- keyUniquenessConstraint is `"WITH UNIQUE KEYS"` or `"WITHOUT UNIQUE KEYS"`.

The same can be used for a predicate type `predicate_is_not_json` (requires predicate capability `IS_NOT_JSON`).

### Scalar Functions

Refer to the [Exasol Scalar Functions API Documentation](scalar_functions_api.md)

### Aggregate Functions

Refer to the [Exasol Aggregate Functions API Documentation](aggregate_functions_api.md)
