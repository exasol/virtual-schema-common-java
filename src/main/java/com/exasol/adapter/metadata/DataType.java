package com.exasol.adapter.metadata;

import java.util.Objects;

/**
 * Represents an EXASOL datatype.
 */
public class DataType {
    public static final int MAX_EXASOL_CHAR_SIZE = 2000;
    public static final int MAX_EXASOL_VARCHAR_SIZE = 2000000;
    public static final int MAX_EXASOL_DECIMAL_PRECISION = 36;

    private ExaDataType exaDataType;
    private int precision;
    private int scale;
    private int size;
    private ExaCharset charset;
    private boolean withLocalTimezone;
    private int geometrySrid;
    private IntervalType intervalType;
    private int intervalFraction;

    public enum ExaDataType {
        UNSUPPORTED,
        DECIMAL,
        DOUBLE,
        VARCHAR,
        CHAR,
        DATE,
        TIMESTAMP,
        BOOLEAN,
        GEOMETRY,
        INTERVAL
    }

    public enum ExaCharset {
        UTF8,
        ASCII
    }

    public enum IntervalType {
        DAY_TO_SECOND,
        YEAR_TO_MONTH
    }

    private DataType() {
    }

    public static DataType createVarChar(final int size, final ExaCharset charset) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.VARCHAR;
        type.size = size;
        type.charset = charset;
        return type;
    }

    public static DataType createChar(final int size, final ExaCharset charset) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.CHAR;
        type.size = size;
        type.charset = charset;
        return type;
    }

    public static DataType createDecimal(final int precision, final int scale) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.DECIMAL;
        type.precision = precision;
        type.scale = scale;
        return type;
    }

    public static DataType createDouble() {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.DOUBLE;
        return type;
    }

    public static DataType createDate() {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.DATE;
        return type;
    }

    public static DataType createTimestamp(final boolean withLocalTimezone) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.TIMESTAMP;
        type.withLocalTimezone = withLocalTimezone;
        return type;
    }

    public static DataType createBool() {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.BOOLEAN;
        return type;
    }

    public static DataType createGeometry(final int srid) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.GEOMETRY;
        type.geometrySrid = srid;
        return type;
    }

    public static DataType createIntervalDaySecond(final int precision, final int fraction) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.INTERVAL;
        type.intervalType = IntervalType.DAY_TO_SECOND;
        type.precision = precision;
        type.intervalFraction = fraction;
        return type;
    }

    public static DataType createIntervalYearMonth(final int precision) {
        final DataType type = new DataType();
        type.exaDataType = ExaDataType.INTERVAL;
        type.intervalType = IntervalType.YEAR_TO_MONTH;
        type.precision = precision;
        return type;
    }

    public ExaDataType getExaDataType() {
        return this.exaDataType;
    }

    public int getPrecision() {
        return this.precision;
    }

    public int getScale() {
        return this.scale;
    }

    public int getSize() {
        return this.size;
    }

    public ExaCharset getCharset() {
        return this.charset;
    }

    public boolean isWithLocalTimezone() {
        return this.withLocalTimezone;
    }

    public int getGeometrySrid() {
        return this.geometrySrid;
    }

    public IntervalType getIntervalType() {
        return this.intervalType;
    }

    public int getIntervalFraction() {
        return this.intervalFraction;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        switch (this.exaDataType) {
            case UNSUPPORTED:
                builder.append("UNSUPPORTED");
                break;
            case DECIMAL:
                builder.append("DECIMAL(");
                builder.append(this.precision);
                builder.append(", ");
                builder.append(this.scale);
                builder.append(")");
                break;
            case DOUBLE:
                builder.append("DOUBLE");
                break;
            case VARCHAR:
                builder.append("VARCHAR(");
                builder.append(this.size);
                builder.append(") ");
                builder.append(this.charset.toString());
                break;
            case CHAR:
                builder.append("CHAR(");
                builder.append(this.size);
                builder.append(") ");
                builder.append(this.charset.toString());
                break;
            case DATE:
                builder.append("DATE");
                break;
            case TIMESTAMP:
                builder.append("TIMESTAMP");
                if (this.withLocalTimezone) {
                    builder.append(" WITH LOCAL TIME ZONE");
                }
                break;
            case BOOLEAN:
                builder.append("BOOLEAN");
                break;
            case GEOMETRY:
                builder.append("GEOMETRY");
                builder.append("(");
                builder.append(this.geometrySrid);
                builder.append(")");
                break;
            case INTERVAL:
                builder.append("INTERVAL ");
                if (this.intervalType == IntervalType.YEAR_TO_MONTH) {
                    builder.append("YEAR");
                    builder.append(" (");
                    builder.append(this.precision);
                    builder.append(")");
                    builder.append(" TO MONTH");
                } else {
                    builder.append("DAY");
                    builder.append(" (");
                    builder.append(this.precision);
                    builder.append(")");
                    builder.append(" TO SECOND");
                    builder.append(" (");
                    builder.append(this.intervalFraction);
                    builder.append(")");
                }
                break;
        }
        return builder.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DataType dataType = (DataType) o;
        return this.precision == dataType.precision && this.scale == dataType.scale && this.size == dataType.size &&
              this.withLocalTimezone == dataType.withLocalTimezone && this.geometrySrid == dataType.geometrySrid &&
              this.intervalFraction == dataType.intervalFraction && this.exaDataType == dataType.exaDataType &&
              this.charset == dataType.charset && this.intervalType == dataType.intervalType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.exaDataType, this.precision, this.scale, this.size, this.charset,
              this.withLocalTimezone, this.geometrySrid, this.intervalType, this.intervalFraction);
    }
}
