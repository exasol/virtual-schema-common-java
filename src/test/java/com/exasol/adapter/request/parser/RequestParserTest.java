package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.json.JsonEntry.*;
import static com.exasol.adapter.request.parser.json.JsonEntry.array;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.exasol.adapter.metadata.DataType;
import com.exasol.adapter.metadata.TableMetadata;
import com.exasol.adapter.request.*;
import com.exasol.adapter.request.parser.json.JsonEntry;
import com.exasol.adapter.request.parser.json.JsonKeyValue;

class RequestParserTest {
    private static final JsonKeyValue SCHEMA_METADATA_INFO = JsonEntry.entry("schemaMetadataInfo",
            group(entry("name", "foo")));
    private RequestParser parser;

    @BeforeEach
    void beforeEach() {
        this.parser = RequestParser.create();
    }

    @Test
    void requestTypeUnknown_ThrowsException() {
        final String rawRequest = JsonEntry.group(//
                entry("type", "UNKNOWN"), SCHEMA_METADATA_INFO).render();
        final Exception exception = assertThrows(RequestParserException.class, () -> this.parser.parse(rawRequest));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-16"));
    }

    @Test
    void setPropertiesRequest() {
        final String rawRequest = JsonEntry.group(//
                entry("type", "setProperties"), //
                entry("properties", group( //
                        entry("A", "value A"), //
                        entry("B", "42"), //
                        entry("PI", 3.14), //
                        entry("YES", true), //
                        entry("NO", false), //
                        entry("NULL_value", JsonEntry.nullValue()))),
                SCHEMA_METADATA_INFO).render();
        final AdapterRequest request = this.parser.parse(rawRequest);
        assertThat("Request class", request, instanceOf(SetPropertiesRequest.class));
        final Map<String, String> properties = ((SetPropertiesRequest) request).getProperties();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequestType.SET_PROPERTIES)),
                () -> assertThat(properties, aMapWithSize(6)),
                () -> assertThat(properties, hasEntry(equalTo("A"), equalTo("value A"))),
                () -> assertThat(properties, hasEntry(equalTo("B"), equalTo("42"))),
                () -> assertThat(properties, hasEntry(equalTo("PI"), equalTo("3.14"))),
                () -> assertThat(properties, hasEntry(equalTo("YES"), equalTo("true"))),
                () -> assertThat(properties, hasEntry(equalTo("NO"), equalTo("false"))),
                () -> assertThat(properties, hasEntry(equalTo("NULL_value"), equalTo(null))));
    }

    @Test
    void unsupportedPropertyType() {
        final String rawRequest = JsonEntry.group(//
                entry("type", "setProperties"), //
                entry("properties", group( //
                        entry("A", group(entry("value", "some value"))))),
                SCHEMA_METADATA_INFO).render();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.parser.parse(rawRequest));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-7"));
    }

    private static final JsonEntry PUSHDOWN_REQUEST = entry("pushdownRequest", group( //
            entry("type", "select"), //
            entry("from", group( //
                    entry("name", "FOO"), //
                    entry("type", "table") //
            ))));

    private static final JsonEntry INVOLVED_TABLES = entry("involvedTables", array(group( //
            entry("name", "FOO"), //
            entry("columns", array(group( //
                    entry("name", "BAR"), //
                    entry("dataType", group( //
                            entry("precision", 18), //
                            entry("scale", 0), //
                            entry("type", "DECIMAL") //
                    ))))))));

    @Test
    void pushDownRequest() {
        final String rawRequest = JsonEntry.group( //
                entry("type", "pushdown"), //
                PUSHDOWN_REQUEST, //
                INVOLVED_TABLES, //
                SCHEMA_METADATA_INFO).render();
        final AdapterRequest request = this.parser.parse(rawRequest);
        assertThat("Request class", request, instanceOf(PushDownRequest.class));
        final List<TableMetadata> involvedTables = ((PushDownRequest) request).getInvolvedTablesMetadata();
        final List<DataType> selectListDataTypes = ((PushDownRequest) request).getSelectListDataTypes();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequestType.PUSHDOWN)),
                () -> assertThat(involvedTables, iterableWithSize(1)),
                () -> assertThat(involvedTables.get(0).getName(), equalTo("FOO")),
                () -> assertThat(selectListDataTypes, empty()));
    }

    @Test
    void pushDownRequestWithSelectListDataTypes() {
        final String rawRequest = JsonEntry.group( //
                entry("type", "pushdown"), //
                PUSHDOWN_REQUEST, //
                entry("selectListDataTypes", array( //
                        group(entry("type", "DECIMAL"), //
                                entry("precision", 9), //
                                entry("scale", 10)), //
                        group(entry("type", "DOUBLE")))), //
                INVOLVED_TABLES, //
                SCHEMA_METADATA_INFO).render();
        final AdapterRequest request = this.parser.parse(rawRequest);
        final List<DataType> selectListDataTypes = ((PushDownRequest) request).getSelectListDataTypes();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequestType.PUSHDOWN)),
                () -> assertThat(selectListDataTypes, iterableWithSize(2)),
                () -> assertThat(selectListDataTypes.get(0), equalTo(DataType.createDecimal(9, 10))));
    }

    @Test
    void refreshRequestWithoutTableFilter() {
        final String rawRequest = JsonEntry.group(//
                entry("type", "refresh"), //
                SCHEMA_METADATA_INFO) //
                .render();
        final RefreshRequest request = (RefreshRequest) this.parser.parse(rawRequest);
        assertAll(() -> assertThat(request.refreshesOnlySelectedTables(), equalTo(false)),
                () -> assertThat(request.getTables(), Matchers.nullValue()));
    }

    @Test
    void refreshRequestWithTableFilter() {
        final String rawRequest = JsonEntry.group(//
                entry("type", "refresh"), //
                entry("requestedTables", array(value("T1"), value("T2"))), //
                SCHEMA_METADATA_INFO) //
                .render();
        final RefreshRequest request = (RefreshRequest) this.parser.parse(rawRequest);
        assertAll(() -> assertThat(request.refreshesOnlySelectedTables(), equalTo(true)),
                () -> assertThat(request.getTables(), containsInAnyOrder("T1", "T2")));
    }

    @Test
    void requestWithoutSchemaMetadata() {
        final String rawRequest = JsonEntry.group(entry("type", "refresh")).render();
        final AdapterRequest request = this.parser.parse(rawRequest);
        assertThat(request.getVirtualSchemaName(), equalTo("UNKNOWN"));
    }
}