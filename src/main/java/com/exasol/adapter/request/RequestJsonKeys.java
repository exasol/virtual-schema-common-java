package com.exasol.adapter.request;

public class RequestJsonKeys {
    public static final String ORDER_BY_KEY = "orderBy";
    public static final String GROUP_BY = "groupBy";
    public static final String FROM = "from";
    public static final String LIMIT = "limit";
    public static final String HAVING = "having";
    public static final String FILTER = "filter";
    public static final String SELECT_LIST = "selectList";
    public static final String EXPRESSION = "expression";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String VALUE = "value";
    public static final String ARGUMENTS_KEY = "arguments";
    public static final String DISTINCT_KEY = "distinct";
    public static final String DATA_TYPE = "dataType";
    public static final String SEPARATOR_KEY = "separator";
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

    private RequestJsonKeys() {
        // no instantiation required
    }
}
