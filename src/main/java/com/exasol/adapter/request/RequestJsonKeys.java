package com.exasol.adapter.request;

/**
 * This class contains constants for keywords of the JSON representation of the SqlNode structure.
 */
public class RequestJsonKeys {
    /**
     * The constant ORDER_BY.
     */
    public static final String ORDER_BY = "orderBy";
    /**
     * The constant GROUP_BY.
     */
    public static final String GROUP_BY = "groupBy";
    /**
     * The constant FROM.
     */
    public static final String FROM = "from";
    /**
     * The constant LIMIT.
     */
    public static final String LIMIT = "limit";
    /**
     * The constant HAVING.
     */
    public static final String HAVING = "having";
    /**
     * The constant FILTER.
     */
    public static final String FILTER = "filter";
    /**
     * The constant SELECT_LIST.
     */
    public static final String SELECT_LIST = "selectList";
    /**
     * The constant EXPRESSION.
     */
    public static final String EXPRESSION = "expression";
    /**
     * The constant EXPRESSIONS.
     */
    public static final String EXPRESSIONS = "expressions";
    /**
     * The constant LEFT.
     */
    public static final String LEFT = "left";
    /**
     * The constant RIGHT.
     */
    public static final String RIGHT = "right";
    /**
     * The constant VALUE.
     */
    public static final String VALUE = "value";
    /**
     * The constant ARGUMENTS.
     */
    public static final String ARGUMENTS = "arguments";
    /**
     * The constant DISTINCT.
     */
    public static final String DISTINCT = "distinct";
    /**
     * The constant DATA_TYPE.
     */
    public static final String DATA_TYPE = "dataType";
    /**
     * The constant SEPARATOR.
     */
    public static final String SEPARATOR = "separator";
    /**
     * The constant OVERFLOW_BEHAVIOUR.
     */
    public static final String OVERFLOW_BEHAVIOUR = "overflowBehavior";
    /**
     * The constant TYPE.
     */
    public static final String TYPE = "type";
    /**
     * The constant TRUNCATION_TYPE.
     */
    public static final String TRUNCATION_TYPE = "truncationType";
    /**
     * The constant TRUNCATION_FILLER.
     */
    public static final String TRUNCATION_FILLER = "truncationFiller";
    /**
     * The constant NAME.
     */
    public static final String NAME = "name";
    /**
     * The constant NUM_ARGS.
     */
    public static final String NUM_ARGS = "numArgs";
    /**
     * The constant ALIAS.
     */
    public static final String ALIAS = "alias";
    /**
     * The constant CONDITION.
     */
    public static final String CONDITION = "condition";
    /**
     * The constant TABLE_NAME.
     */
    public static final String TABLE_NAME = "tableName";
    /**
     * The constant TABLE_ALIAS.
     */
    public static final String TABLE_ALIAS = "tableAlias";
    /**
     * The constant RETURNING_DATA_TYPE.
     */
    public static final String RETURNING_DATA_TYPE = "returningDataType";
    /**
     * The constant EMPTY_BEHAVIOR.
     */
    public static final String EMPTY_BEHAVIOR = "emptyBehavior";
    /**
     * The constant ERROR_BEHAVIOR.
     */
    public static final String ERROR_BEHAVIOR = "errorBehavior";
    /**
     * The constant SRID.
     */
    public static final String SRID = "srid";
    /**
     * The constant COLUMN_NR.
     */
    public static final String COLUMN_NR = "columnNr";
    /**
     * The constant RESULTS.
     */
    public static final String RESULTS = "results";
    /**
     * The constant BASIS.
     */
    public static final String BASIS = "basis";
    /**
     * The constant PRECISION.
     */
    public static final String PRECISION = "precision";
    /**
     * The constant SCALE.
     */
    public static final String SCALE = "scale";
    /**
     * The constant CHARACTER_SET.
     */
    public static final String CHARACTER_SET = "characterSet";
    /**
     * The constant SIZE.
     */
    public static final String SIZE = "size";
    /**
     * The constant WITH_LOCAL_TIME_ZONE.
     */
    public static final String WITH_LOCAL_TIME_ZONE = "withLocalTimeZone";
    /**
     * The constant FROM_TO.
     */
    public static final String FROM_TO = "fromTo";
    /**
     * The constant FRACTION.
     */
    public static final String FRACTION = "fraction";
    /**
     * The constant NUM_ELEMENTS.
     */
    public static final String NUM_ELEMENTS = "numElements";
    /**
     * The constant OFFSET.
     */
    public static final String OFFSET = "offset";
    /**
     * The constant ORDER_BY_ELEMENT.
     */
    public static final String ORDER_BY_ELEMENT = "order_by_element";
    /**
     * The constant IS_ASCENDING.
     */
    public static final String IS_ASCENDING = "isAscending";
    /**
     * The constant NULLS_LAST.
     */
    public static final String NULLS_LAST = "nullsLast";
    /**
     * The constant TYPE_CONSTRAINT.
     */
    public static final String TYPE_CONSTRAINT = "typeConstraint";
    /**
     * The constant KEY_UNIQUENESS_CONSTRAINT.
     */
    public static final String KEY_UNIQUENESS_CONSTRAINT = "keyUniquenessConstraint";
    /**
     * The constant PATTERN.
     */
    public static final String PATTERN = "pattern";
    /**
     * The constant ESCAPE_CHAR.
     */
    public static final String ESCAPE_CHAR = "escapeChar";
    /**
     * The constant JOIN_TYPE.
     */
    public static final String JOIN_TYPE = "join_type";
    /**
     * The constant TO_EXTRACT.
     */
    public static final String TO_EXTRACT = "toExtract";
    /**
     * The constant BYTE_SIZE.
     */
    public static final String BYTE_SIZE = "bytesize";
    /**
     * The constant AGGREGATION_TYPE.
     */
    public static final String AGGREGATION_TYPE = "aggregationType";
    /**
     * The constant AGGREGATION_TYPE_SINGLE_GROUP.
     */
    public static final String AGGREGATION_TYPE_SINGLE_GROUP = "single_group";

    private RequestJsonKeys() {
        // no instantiation required
    }
}
