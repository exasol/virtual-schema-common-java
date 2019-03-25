package com.exasol.adapter.sql;

import com.exasol.adapter.AdapterException;
import com.exasol.adapter.metadata.DataType;

public class SqlLiteralInterval extends SqlNode {
    private final String value;   // stored as YYYY-MM-DD HH:MI:SS.FF6
    private final DataType type;

    public SqlLiteralInterval(final String value, final DataType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return this.value;
    }

    public DataType getDataType() {
        return this.type;
    }

    @Override
    public String toSimpleSql() {
        if (this.type.getIntervalType() == DataType.IntervalType.YEAR_TO_MONTH) {
            return "INTERVAL '" + this.value + "' YEAR (" + this.type.getPrecision() + ") TO MONTH";
        } else {
            return "INTERVAL '" + this.value + "' DAY (" + this.type.getPrecision()
                    + ") TO SECOND (" + this.type.getIntervalFraction() + ")";
        }
    }

    @Override
    public SqlNodeType getType() {
        return SqlNodeType.LITERAL_INTERVAL;
    }

    @Override
    public <R> R accept(final SqlNodeVisitor<R> visitor) throws AdapterException {
        return visitor.visit(this);
    }
}
