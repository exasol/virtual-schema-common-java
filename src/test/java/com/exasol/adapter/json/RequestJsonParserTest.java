package com.exasol.adapter.json;

import com.exasol.adapter.metadata.ColumnMetadata;
import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.SchemaMetadataInfo;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.request.AdapterRequest;
import com.exasol.adapter.request.PushdownRequest;
import com.exasol.adapter.request.SetPropertiesRequest;
import com.exasol.adapter.sql.JoinType;
import com.exasol.adapter.sql.SqlJoin;
import com.exasol.adapter.sql.SqlNodeType;
import com.exasol.adapter.sql.SqlStatementSelect;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

class RequestJsonParserTest {
    @Test
    void testParsePushdownRequest() throws Exception {
        final String file = "target/test-classes/pushdown_request.json";
        final String json = Files.toString(new File(file), Charsets.UTF_8);

        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("HIVE_SERVER", "my-hive-server");
        properties.put("HIVE_DB", "my-hive-db");
        properties.put("HIVE_USER", "my-hive-user");
        final SchemaMetadataInfo expectedSchemaMetaInfo = new SchemaMetadataInfo("MY_HIVE_VSCHEMA",
              "{\"lastRefreshed\":\"2015-03-01 12:10:01\",\"key\":\"Any custom schema state here\"}", properties);

        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        final String tableName = "CLICKS";
        final String tableAdapterNotes = "";
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(new ColumnMetadata("ID", "", DataType.createDecimal(22, 0), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("USER_ID", "", DataType.createDecimal(18, 0), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("URL", "", DataType.createVarChar(1000, DataType.ExaCharset.UTF8), true, false,
                    "", ""));
        tableColumns.add(new ColumnMetadata("REQUEST_TIME", "", DataType.createTimestamp(false), true, false, "", ""));
        final String tableComment = "";
        expectedInvolvedTablesMetadata.add(new TableMetadata(tableName, tableAdapterNotes, tableColumns, tableComment));

        final RequestJsonParser parser = new RequestJsonParser();
        final AdapterRequest request = parser.parseRequest(json);
        assertObjectEquals(expectedSchemaMetaInfo, request.getSchemaMetadataInfo());
        assertObjectEquals(expectedInvolvedTablesMetadata, ((PushdownRequest) request).getInvolvedTablesMetadata());
    }

    @Test
    void testParsePushdownRequestAllTypes() throws Exception {
        final String file = "target/test-classes/pushdown_request_alltypes.json";
        final String json = Files.toString(new File(file), Charsets.UTF_8);

        final Map<String, String> properties = new HashMap<String, String>();
        final SchemaMetadataInfo expectedSchemaMetaInfo = new SchemaMetadataInfo("VS", "", properties);

        final List<TableMetadata> expectedInvolvedTablesMetadata = new ArrayList<>();
        final String tableName = "T1";
        final String tableAdapterNotes = "";
        final String tableComment = "";
        final List<ColumnMetadata> tableColumns = new ArrayList<>();
        tableColumns.add(new ColumnMetadata("C_DECIMAL", "", DataType.createDecimal(18, 2), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_DOUBLE", "", DataType.createDouble(), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_VARCHAR_UTF8_1", "", DataType.createVarChar(10000, DataType.ExaCharset.UTF8),
                    true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_VARCHAR_UTF8_2", "", DataType.createVarChar(10000, DataType.ExaCharset.UTF8),
                    true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_VARCHAR_ASCII", "", DataType.createVarChar(10000, DataType.ExaCharset.ASCII),
                    true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_UTF8_1", "", DataType.createChar(3, DataType.ExaCharset.UTF8), true,
              false, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_UTF8_2", "", DataType.createChar(3, DataType.ExaCharset.UTF8), true,
              false, "", ""));
        tableColumns.add(new ColumnMetadata("C_CHAR_ASCII", "", DataType.createChar(3, DataType.ExaCharset.ASCII), true,
              false, "", ""));
        tableColumns.add(new ColumnMetadata("C_DATE", "", DataType.createDate(), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_1", "", DataType.createTimestamp(false), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_2", "", DataType.createTimestamp(false), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_TIMESTAMP_3", "", DataType.createTimestamp(true), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_BOOLEAN", "", DataType.createBool(), true, false, "", ""));
        tableColumns.add(new ColumnMetadata("C_GEOMETRY", "", DataType.createGeometry(1), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_DS_1", "", DataType.createIntervalDaySecond(2, 3), true, false, "",
                    ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_DS_2", "", DataType.createIntervalDaySecond(3, 4), true, false, "",
                    ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_YM_1", "", DataType.createIntervalYearMonth(2), true, false, "", ""));
        tableColumns
              .add(new ColumnMetadata("C_INTERVAL_YM_2", "", DataType.createIntervalYearMonth(3), true, false, "", ""));
        expectedInvolvedTablesMetadata.add(new TableMetadata(tableName, tableAdapterNotes, tableColumns, tableComment));

        final RequestJsonParser parser = new RequestJsonParser();
        final AdapterRequest request = parser.parseRequest(json);
        assertObjectEquals(expectedSchemaMetaInfo, request.getSchemaMetadataInfo());
        assertObjectEquals(expectedInvolvedTablesMetadata, ((PushdownRequest) request).getInvolvedTablesMetadata());
    }

    @Test
    void testParseSetPropertiesRequest() throws Exception {
        final String file = "target/test-classes/set_properties_request.json";
        final String json = Files.toString(new File(file), Charsets.UTF_8);

        final Map<String, String> expectedOldSchemaProperties = new HashMap<String, String>();
        expectedOldSchemaProperties.put("EXISTING_PROP_1", "Old Value 1");
        expectedOldSchemaProperties.put("EXISTING_PROP_2", "Old Value 2");
        final SchemaMetadataInfo expectedSchemaMetaInfo = new SchemaMetadataInfo("VS", "", expectedOldSchemaProperties);

        final Map<String, String> expectedNewProperties = new HashMap<String, String>();
        expectedNewProperties.put("EXISTING_PROP_1", "New Value");
        expectedNewProperties.put("EXISTING_PROP_2", null);
        expectedNewProperties.put("NEW_PROP", "VAL2");
        expectedNewProperties.put("DELETED_PROP_NON_EXISTING", null);

        final RequestJsonParser parser = new RequestJsonParser();
        final AdapterRequest request = parser.parseRequest(json);
        assertObjectEquals(expectedSchemaMetaInfo, request.getSchemaMetadataInfo());
        assertObjectEquals(expectedNewProperties, ((SetPropertiesRequest) request).getProperties());
    }

    @Test
    void testSimpleInnerJoinRequest() throws Exception {
        final String req = "{" + "\"involvedTables\" :" + "            [" + "                {" +
              "                    \"columns\" :" + "                    [" + "                        {" +
              "                            \"dataType\" :" + "                            {" +
              "                                \"precision\" : 18," + "                                \"scale\" : 0," +
              "                                \"type\" : \"DECIMAL\"" + "                            }," +
              "                            \"name\" : \"ID\"" + "                        }" + "                    ]," +
              "                    \"name\" : \"T1\"" + "                }," + "                {" +
              "                    \"columns\" :" + "                    [" + "                        {" +
              "                            \"dataType\" :" + "                            {" +
              "                                \"precision\" : 18," + "                                \"scale\" : 0," +
              "                                \"type\" : \"DECIMAL\"" + "                            }," +
              "                            \"name\" : \"ID\"" + "                        }" + "                    ]," +
              "                    \"name\" : \"T2\"" + "                }" + "            ]," +
              "    \"pushdownRequest\" : " + "    {" + "        \"type\" : \"select\"," + "        \"from\" : " +
              "        {" + "            \"type\": \"join\"," + "            \"join_type\": \"inner\"," +
              "            \"left\":" + "            {" + "                \"name\" : \"T1\"," +
              "                \"type\" : \"table\"" + "            }," + "            \"right\":" + "            {" +
              "                \"name\" : \"T2\"," + "                \"type\" : \"table\"" + "            }," +
              "            \"condition\":" + "            {" + "                \"left\" :" + "                {" +
              "                        \"columnNr\" : 0," + "                        \"name\" : \"ID\"," +
              "                        \"tableName\" : \"T1\"," + "                        \"type\" : \"column\"" +
              "                }," + "                \"right\" :" + "                {" +
              "                        \"columnNr\" : 0," + "                        \"name\" : \"ID\"," +
              "                        \"tableName\" : \"T2\"," + "                        \"type\" : \"column\"" +
              "                }," + "                \"type\" : \"predicate_equal\"" + "            }" + "        }" +
              "    }," + "    \"type\" : \"pushdown\"" + "}";

        final RequestJsonParser parser = new RequestJsonParser();
        final AdapterRequest request = parser.parseRequest(req);
        final PushdownRequest pushdown = (PushdownRequest) request;

        final SqlStatementSelect select = (SqlStatementSelect) pushdown.getSelect();
        final SqlJoin from = (SqlJoin) select.getFromClause();

        assertSame(SqlNodeType.JOIN, from.getType());
        assertSame(JoinType.INNER, from.getJoinType());
        assertSame(SqlNodeType.PREDICATE_EQUAL, from.getCondition().getType());
        assertSame(SqlNodeType.TABLE, from.getLeft().getType());
        assertSame(SqlNodeType.TABLE, from.getRight().getType());
    }

    private <T> void assertObjectEquals(final T expected, final T actual) {
        assertTrue("Expected:\n" + expected + "\nactual:\n" + actual,
              new ReflectionEquals(actual, (String[]) null).matches(expected));
    }
}