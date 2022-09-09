package com.exasol.adapter.request.parser;

import static com.exasol.adapter.request.parser.json.builder.JsonEntry.*;
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
import com.exasol.adapter.request.parser.json.builder.*;

class RequestParserTest {
    private static final JsonKeyValue SCHEMA_METADATA_INFO = JsonEntry.entry("schemaMetadataInfo",
            object(entry("name", "foo")));
    private RequestParser parser;

    @BeforeEach
    void beforeEach() {
        this.parser = RequestParser.create();
    }

    @Test
    void requestTypeUnknown_ThrowsException() {
        final String rawRequest = JsonEntry.object(//
                entry("type", "UNKNOWN"), SCHEMA_METADATA_INFO).render();
        final Exception exception = assertThrows(RequestParserException.class, () -> this.parser.parse(rawRequest));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-16"));
    }

    @Test
    void setPropertiesRequest() {
        final String rawRequest = JsonEntry.object(//
                entry("type", "setProperties"), //
                entry("properties", object( //
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
        final String rawRequest = JsonEntry.object(//
                entry("type", "setProperties"), //
                entry("properties", object( //
                        entry("A", object(entry("value", "some value"))))),
                SCHEMA_METADATA_INFO).render();
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> this.parser.parse(rawRequest));
        assertThat(exception.getMessage(), containsString("E-VSCOMJAVA-7"));
    }

    @Test
    void classicPushDownRequest() {
        final AdapterRequest request = this.parser.parse(createPushDownRequest().render());
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
        final String rawRequest = createPushDownRequest().addChild( //
                entry("selectListDataTypes", array( //
                        object(entry("type", "DECIMAL"), //
                                entry("precision", 9), //
                                entry("scale", 10)), //
                        object(entry("type", "DOUBLE"))))) //
                .render();
        final AdapterRequest request = this.parser.parse(rawRequest);
        final List<DataType> selectListDataTypes = ((PushDownRequest) request).getSelectListDataTypes();
        assertAll(() -> assertThat(request.getType(), equalTo(AdapterRequestType.PUSHDOWN)),
                () -> assertThat(selectListDataTypes, iterableWithSize(2)),
                () -> assertThat(selectListDataTypes.get(0), equalTo(DataType.createDecimal(9, 10))));
    }

    @Test
    void refreshRequestWithoutTableFilter() {
        final String rawRequest = JsonEntry.object(//
                entry("type", "refresh"), //
                SCHEMA_METADATA_INFO) //
                .render();
        final RefreshRequest request = (RefreshRequest) this.parser.parse(rawRequest);
        assertAll(() -> assertThat(request.refreshesOnlySelectedTables(), equalTo(false)),
                () -> assertThat(request.getTables(), Matchers.nullValue()));
    }

    @Test
    void refreshRequestWithTableFilter() {
        final String rawRequest = JsonEntry.object(//
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
        final String rawRequest = JsonEntry.object(entry("type", "refresh")).render();
        final AdapterRequest request = this.parser.parse(rawRequest);
        assertThat(request.getVirtualSchemaName(), equalTo("UNKNOWN"));
    }

    private JsonParent createPushDownRequest() {
        return JsonEntry.object( //
                entry("type", "pushdown"), //
                entry("pushdownRequest", object( //
                        entry("type", "select"), //
                        entry("from", object( //
                                entry("name", "FOO"), //
                                entry("type", "table") //
                        )))), //
                entry("involvedTables", array(object( //
                        entry("name", "FOO"), //
                        entry("columns", array(object( //
                                entry("name", "BAR"), //
                                entry("dataType", object( //
                                        entry("precision", 18), //
                                        entry("scale", 0), //
                                        entry("type", "DECIMAL") //
                                )))))))), //
                SCHEMA_METADATA_INFO);
    }
}