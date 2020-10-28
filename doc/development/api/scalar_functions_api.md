# Exasol Scalar Functions API

This page describes how Exasol scalar functions map to the Virtual Schemas push-down request API.

## Exasol Scalar Functions List

### Functions With a Common API

* [Functions without arguments](#functions-without-arguments);
* [Functions with a single argument](#functions-with-a-single-argument);
* [Functions with multiple arguments](#functions-with-multiple-arguments);
* [Arithmetic operators](#arithmetic-operators).

### Functions With a Special API

This section contains functions that have a special API mapping.
  
| Function Name     | API Mapping Link                            |
|-------------------|---------------------------------------------|
| EXTRACT           | [EXTRACT function](#extract-function)       |
| CASE              | [CASE function](#case-function)             |
| CAST              | [CAST function](#cast-function)             |
| JSON_VALUE        | [JSON_VALUE function](#json_value-function) |
     
### Functions Not Included in the API

This section contains Exasol functions which are not included in the API.
See [Early Function Evaluation](#early-function-evaluation) for an explanation why some function calls do not appear in pushdown requests.

| Function Name       | Comment                                         |
|---------------------|-------------------------------------------------|
| CEILING             | The API uses the CEIL function.                 |
| CHAR                | The API uses the CHR function.                  |
| CHARACTER_LENGTH    | The API uses the LENGTH function.               |
| COALESCE            | The API uses the CASE function.                 |
| CONNECT_BY_ISCYCLE  | Not included in the API (hierarchical queries). |
| CONNECT_BY_ISLEAF   | Not included in the API (hierarchical queries). |
| CONVERT             | The API uses the CAST function.                 |
| CUME_DIST           | Not included in the API (analytic functions).   |
| CURDATE             | The API uses the CURRENT_DATE function.         |
| DECODE              | The API uses the CASE function.                 |
| DENSE_RANK          | Not included in the API (analytic functions).   |
| HASH_SHA            | The API uses the HASH_SHA1 function.            |
| HASHTYPE_SHA        | The API uses the HASHTYPE_SHA1 function.        |
| IF                  | The API uses the CASE function.                 |
| IPROC               | Not included in the API.                        |
| JSON_EXTRACT        | Not included in the API.                        |
| LAG                 | Not included in the API (analytic functions).   |
| LCASE               | The API uses the LOWER function.                |
| LEAD                | Not included in the API (analytic functions).   |
| LEFT                | The API uses the SUBSTR function.               |
| LEVEL               | Not included in the API (hierarchical queries). |
| LOG2                | The API uses the LOG function.                  |
| LOG10               | The API uses the LOG function.                  |
| MID                 | The API uses the SUBSTR function.               |
| NOW                 | The API uses the CURRENT_TIMESTAMP function.    |
| NPROC               | Not included in the API.                        |
| NTH_VALUE           | Not included in the API (analytic functions).   |
| NTILE               | Not included in the API (analytic functions).   |
| NULLIF              | The API uses the CASE function.                 |
| NVL                 | The API uses the CASE function.                 |
| NVL2                | The API uses the CASE function.                 |
| PERCENT_RANK        | Not included in the API (analytic functions).   |
| PI                  | The API uses a `literal_double`.                |
| POSITION            | The API uses the INSTR function.                |
| RANDOM              | The API uses the RAND function.                 |
| RANK                | Not included in the API (analytic functions).   |
| RATIO_TO_REPORT     | Not included in the API (analytic functions).   |
| ROWID               | Not included in the API.                        |
| ROW_NUMBER          | Not included in the API (analytic functions).   |
| ROWNUM              | Not included in the API.                        |
| SCOPE_USER          | The API uses a `literal_string`.                |
| SUBSTRING           | The API uses the SUBSTR function.               |
| SYS_CONNECT_BY_PATH | Not included in the API (hierarchical queries). |
| TRUNCATE            | The API uses the TRUNC function.                |
| UCASE               | The API uses the UPPER function.                |
| USER                | The API uses the CURRENT_USER function.         |
| VALUE2PROC          | Not included in the API.                        |

## Scalar Functions API

### Functions Without Arguments

A scalar function without arguments has the following JSON structure:

```json
{
  "type": "function_scalar",
  "name": "<function name>",
  "arguments": []
}
```

### Functions With a Single Argument

A scalar function with a single argument has the following JSON structure:

```json
{
  "type": "function_scalar",
  "name": "<function name>",
  "arguments": [
    {
      ...
    }
  ]
}
```

For example, for the query `SELECT ABS(c5) FROM VIRTUAL_SCHEMA_EXASOL.ALL_EXASOL_TYPES` the scalar function part of the JSON request might look like this:

```json
{
  "type": "function_scalar",
  "name": "ABS",
  "arguments": [
    {
      "columnNr": 4,
      "name": "C5",
      "tableName": "ALL_EXASOL_TYPES",
      "type": "column"
    }
  ]
}
```

### Functions With Multiple Arguments

A scalar function with two arguments has the following JSON structure:

```json
{
  "type": "function_scalar",
  "name": "<function name>",
  "arguments": [
    {
      ...
    },
    {
      ...
    }
  ]
}
```

If a function has more than two arguments, the `arguments` list has a corresponding amount of nested elements.

Let us check an example of the API part containing the scalar function with two arguments for the following query `SELECT ATAN2(c5, c6) FROM VIRTUAL_SCHEMA_EXASOL.ALL_EXASOL_TYPES`:

```json
{
  "type": "function_scalar",
  "name": "ATAN2",
  "arguments": [
    {
      "columnNr": 4,
      "name": "C5",
      "tableName": "ALL_EXASOL_TYPES",
      "type": "column"
    },
    {
      "columnNr": 5,
      "name": "C6",
      "tableName": "ALL_EXASOL_TYPES",
      "type": "column"
    }
  ]
}
```

### Arithmetic Operators

Arithmetic operators use the common scalar function API, but have special names:

| Function Name | Operation     |
|---------------|---------------|
| NEG           | `-exp1`       |
| ADD           | `exp1 + exp2` |
| SUB           | `exp1 - exp2` |
| MULT          | `exp1 * exp2` |
| FLOAT_DIV     | `exp1 / exp2` |

```json
{
    "type": "function_scalar",
    "name": "ADD",
    "arguments": [
    {
        ...
    },
    {
        ...
    }
    ]
}
```

### EXTRACT Function

`EXTRACT(toExtract FROM exp1)` (requires scalar-function capability `EXTRACT`).

The EXTRACT function takes one argument and has a special field `toExtract`.

```json
{
  "type": "function_scalar_extract",
  "name": "EXTRACT",
  "toExtract": "<YEAR/MONTH/DAY/HOUR/MINUTE/SECOND>",
  "arguments": [
    {
      ...
    }
  ]
}
```

### CAST function

`CAST(exp1 AS dataType)` (requires scalar-function capability `CAST`).

The CAST function takes one argument and has a special field `dataType` describing the datatype to cast to.

```json
{
  "type": "function_scalar_cast",
  "name": "CAST",
  "arguments": [
    {
      ...
    }
  ],
  "dataType": {
    ...
  }
}
```
For example, for the query `SELECT CAST(c5 AS VARCHAR(10)) FROM VIRTUAL_SCHEMA_EXASOL.ALL_EXASOL_TYPES` the CAST function part of the JSON request will look like this:

```json
{
  "type": "function_scalar_cast",
  "name": "CAST",
  "arguments": [
    {
      "columnNr": 4,
      "name": "C5",
      "tableName": "ALL_EXASOL_TYPES",
      "type": "column"
    }
  ],
  "dataType": {
    "size": 10,
    "type": "VARCHAR"
  }
}
```

 ### CASE function

`CASE` (requires scalar-function capability `CASE`)

```sql
CASE basis WHEN exp1 THEN result1
           WHEN exp2 THEN result2
           ELSE result3
           END
```

```json
{
  "type": "function_scalar_case",
  "name": "CASE",
  "basis": {
    ...
  },
  "arguments": [
    ...
  ],
  "results": [
    ...
  ]
}
```

Notes:
* `arguments`: The different cases.
* `results`: The different results in the same order as the arguments. If present, the ELSE result is the last entry in the `results` array.
* `basis`: Optional.

Here is an example of a query containing a CASE function and its JSON representation (only the function part): 

```sql
SELECT CASE grade
          WHEN 1 THEN 'GOOD'
          WHEN 2 THEN 'FAIR' 
          WHEN 3 THEN 'POOR'
          ELSE 'INVALID'
          END
FROM VIRTUAL_SCHEMA_EXASOL.ALL_EXASOL_TYPES;
```

```json
{
  "type": "function_scalar_case",
  "name": "CASE",
  "basis": {
    "columnNr": 4,
    "name": "grade",
    "tableName": "ALL_EXASOL_TYPES",
    "type": "column"
  },
  "arguments": [
    {
      "type": "literal_exactnumeric",
      "value": "1"
    },
    {
      "type": "literal_exactnumeric",
      "value": "2"
    },
    {
      "type": "literal_exactnumeric",
      "value": "3"
    }
  ],
  "results": [
    {
      "type": "literal_string",
      "value": "GOOD"
    },
    {
      "type": "literal_string",
      "value": "FAIR"
    },
    {
      "type": "literal_string",
      "value": "POOR"
    },
    {
      "type": "literal_string",
      "value": "INVALID"
    }
  ]
}
```

### JSON_VALUE Function

`JSON_VALUE(arg1, arg2 RETURNING dataType {ERROR | NULL | DEFAULT exp1} ON EMPTY {ERROR | NULL | DEFAULT exp2} ON ERROR)`
 (requires scalar-function capability `JSON_VALUE`)
 
```json
{
    "type": "function_scalar_json_value",
    "name": "JSON_VALUE",
    "arguments":
    [
        {
            ...
        },
        {
            ...
        }
    ],
    "returningDataType": dataType,
    "emptyBehavior":
    {
        "type": "ERROR"
    },
    "errorBehavior":
    {
        "type": "DEFAULT",
        "expression": exp2
    }
}
```

Notes:

* `arguments`: Contains two entries: The JSON item and the path specification.
* `emptyBehavior` and `errorBehavior`: `type` is `"ERROR"`, `"NULL"`, or `"DEFAULT"`. Only for `"DEFAULT"` the member `expression` containing the default value exists.

## Early Function Evaluation

A scalar function, that does not contain any column references, might be executed before reaching Virtual Schemas.
That means the JSON request does not contain the scalar function, but a literal value representing its result.
For example, the query `SELECT ABS(-123), c5 FROM VIRTUAL_SCHEMA_EXASOL.ALL_EXASOL_TYPES` will have the following select list:
  
```json
{
  ...

  "selectList": [
    {
      "type": "literal_exactnumeric",
      "value": "123"
    },
    {
      "type": "column",
      "tableName": "ALL_EXASOL_TYPES",
      "columnNr": 4,
      "name": "C5"
    }
  ],
  
  ...
}
```
