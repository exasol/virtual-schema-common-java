# User Guide

This user guide documents the request properties from this module that control logging and telemetry behavior.

## Virtual Schema Properties

This library supports the following virtual schema properties. You can set properties when creating a virtual schema:

```sql
CREATE VIRTUAL SCHEMA hive USING adapter.jdbc_adapter
WITH
 CONNECTION_STRING   = 'jdbc:hive2://localhost:10000/default'
 DEBUG_ADDRESS       = 'localhost:3000'
 LOG_LEVEL           = 'FINE'
 TELEMETRY           = 'false';
```

### Logging Properties

The `LoggingConfiguration` class supports the following request properties:

| Property | Required | Description |
| --- | --- | --- |
| `DEBUG_ADDRESS` | No | Enables remote logging. Accepted formats are `host` or `host:port`. If only `host` is given, the adapter uses port `3000`. |
| `LOG_LEVEL` | No | Sets the logging level `SEVERE`, `WARNING`, `INFO`, `CONFIG`, `FINE`, `FINER`, or `FINEST`. Default: `INFO`. |

If `DEBUG_ADDRESS` is not set, the adapter uses local logging only. See the [Exasol documentation](https://docs.exasol.com/db/latest/database_concepts/virtual_schema/logging.htm) for details on how to use remote logging.

### Telemetry Property

The `AdapterTelemetryConfig` class supports the following request property:

| Property | Required | Description |
| --- | --- | --- |
| `TELEMETRY` | No | Controls anonymous feature tracking. Telemetry is enabled by default. Set this property to `false` to disable telemetry. Any other value, including `true` or an unset property, keeps telemetry enabled. |

See [documentation of telemetry-java](https://github.com/exasol/telemetry-java/blob/main/doc/app-user-guide.md) for details.
