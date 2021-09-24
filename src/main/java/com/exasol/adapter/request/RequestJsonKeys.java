package com.exasol.adapter.request;

/**
 * This class contains constants for keywords of the JSON representation of the SqlNode structure.
 */
public class RequestJsonKeys {
    public static final String ORDER_BY = "orderBy";
    public static final String GROUP_BY = "groupBy";
    public static final String FROM = "from";
    public static final String LIMIT = "limit";
    public static final String HAVING = "having";
    public static final String FILTER = "filter";
    public static final String SELECT_LIST = "selectList";
    public static final String EXPRESSION = "expression";
    public static final String EXPRESSIONS = "expressions";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String VALUE = "value";
    public static final String ARGUMENTS = "arguments";
    public static final String DISTINCT = "distinct";
    public static final String DATA_TYPE = "dataType";
    public static final String SEPARATOR = "separator";
    public static final String OVERFLOW_BEHAVIOUR = "overflowBehavior";
    public static final String TYPE = "type";
    public static final String TRUNCATION_TYPE = "truncationType";
    public static final String TRUNCATION_FILLER = "truncationFiller";
    public static final String NAME = "name";
    public static final String NUM_ARGS = "numArgs";
    public static final String ALIAS = "alias";
    public static final String CONDITION = "condition";
    public static final String TABLE_NAME = "tableName";
    public static final String TABLE_ALIAS = "tableAlias";
    public static final String RETURNING_DATA_TYPE = "returningDataType";
    public static final String EMPTY_BEHAVIOR = "emptyBehavior";
    public static final String ERROR_BEHAVIOR = "errorBehavior";
    public static final String SRID = "srid";
    public static final String COLUMN_NR = "columnNr";
    public static final String RESULTS = "results";
    public static final String BASIS = "basis";
    public static final String PRECISION = "precision";
    public static final String SCALE = "scale";
    public static final String CHARACTER_SET = "characterSet";
    public static final String SIZE = "size";
    public static final String WITH_LOCAL_TIME_ZONE = "withLocalTimeZone";
    public static final String FROM_TO = "fromTo";
    public static final String FRACTION = "fraction";
    public static final String NUM_ELEMENTS = "numElements";
    public static final String OFFSET = "offset";
    public static final String ORDER_BY_ELEMENT = "order_by_element";
    public static final String IS_ASCENDING = "isAscending";
    public static final String NULLS_LAST = "nullsLast";
    public static final String TYPE_CONSTRAINT = "typeConstraint";
    public static final String KEY_UNIQUENESS_CONSTRAINT = "keyUniquenessConstraint";
    public static final String PATTERN = "pattern";
    public static final String ESCAPE_CHAR = "escapeChar";
    public static final String JOIN_TYPE = "join_type";
    public static final String TO_EXTRACT = "toExtract";
    public static final String BYTE_SIZE = "bytesize";
    public static final String AGGREGATION_TYPE = "aggregationType";
    public static final String AGGREGATION_TYPE_SINGLE_GROUP = "single_group";

    private RequestJsonKeys() {
        // no instantiation required
    }
}
