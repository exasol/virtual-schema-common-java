package com.exasol.adapter.sql;

/**
 * List of all scalar functions supported by EXASOL. Note that predicates are defined separately in {@link Predicate}.
 */
public enum ScalarFunction {
    /**
     * Add scalar function.
     */
    ADD,
    /**
     * Sub scalar function.
     */
    SUB,
    /**
     * Mult scalar function.
     */
    MULT,
    /**
     * Float div scalar function.
     */
    FLOAT_DIV,
    /**
     * Neg scalar function.
     */
    NEG, //

    /**
     * Abs scalar function.
     */
    ABS,
    /**
     * Acos scalar function.
     */
    ACOS,
    /**
     * Asin scalar function.
     */
    ASIN,
    /**
     * Atan scalar function.
     */
    ATAN,
    /**
     * Atan 2 scalar function.
     */
    ATAN2,
    /**
     * Ceil scalar function.
     */
    CEIL,
    /**
     * Cos scalar function.
     */
    COS,
    /**
     * Cosh scalar function.
     */
    COSH,
    /**
     * Cot scalar function.
     */
    COT,
    /**
     * Degrees scalar function.
     */
    DEGREES,
    /**
     * Div scalar function.
     */
    DIV,
    /**
     * Exp scalar function.
     */
    EXP,
    /**
     * Floor scalar function.
     */
    FLOOR,
    /**
     * Greatest scalar function.
     */
    GREATEST,
    /**
     * Least scalar function.
     */
    LEAST,
    /**
     * Ln scalar function.
     */
    LN,
    /**
     * Log scalar function.
     */
    LOG,
    /**
     * Mod scalar function.
     */
    MOD,
    /**
     * Power scalar function.
     */
    POWER,
    /**
     * Radians scalar function.
     */
    RADIANS,
    /**
     * Rand scalar function.
     */
    RAND,
    /**
     * Round scalar function.
     */
    ROUND,
    /**
     * Sign scalar function.
     */
    SIGN,
    /**
     * Sin scalar function.
     */
    SIN,
    /**
     * Sinh scalar function.
     */
    SINH,
    /**
     * Sqrt scalar function.
     */
    SQRT,
    /**
     * Tan scalar function.
     */
    TAN,
    /**
     * Tanh scalar function.
     */
    TANH,
    /**
     * Trunc scalar function.
     */
    TRUNC,
    /**
     * WIDTH_BUCKET scalar function.
     */
    WIDTH_BUCKET,

    /**
     * Ascii scalar function.
     */
    ASCII,
    /**
     * Bit length scalar function.
     */
    BIT_LENGTH,
    /**
     * Chr scalar function.
     */
    CHR,
    /**
     * Cologne phonetic scalar function.
     */
    COLOGNE_PHONETIC,
    /**
     * Concat scalar function.
     */
    CONCAT,
    /**
     * Dump scalar function.
     */
    DUMP,
    /**
     * Edit distance scalar function.
     */
    EDIT_DISTANCE,
    /**
     * Initcap scalar function.
     */
    INITCAP,
    /**
     * Insert scalar function.
     */
    INSERT,
    /**
     * Instr scalar function.
     */
    INSTR,
    /**
     * Length scalar function.
     */
    LENGTH,
    /**
     * Locate scalar function.
     */
    LOCATE,
    /**
     * Lower scalar function.
     */
    LOWER,
    /**
     * Lpad scalar function.
     */
    LPAD,
    /**
     * Ltrim scalar function.
     */
    LTRIM,
    /**
     * Octet length scalar function.
     */
    OCTET_LENGTH,
    /**
     * Regexp instr scalar function.
     */
    REGEXP_INSTR,
    /**
     * Regexp replace scalar function.
     */
    REGEXP_REPLACE,
    /**
     * Regexp substr scalar function.
     */
    REGEXP_SUBSTR,
    /**
     * Repeat scalar function.
     */
    REPEAT,
    /**
     * Replace scalar function.
     */
    REPLACE,
    /**
     * Reverse scalar function.
     */
    REVERSE,
    /**
     * Right scalar function.
     */
    RIGHT,
    /**
     * Rpad scalar function.
     */
    RPAD,
    /**
     * Rtrim scalar function.
     */
    RTRIM,
    /**
     * Soundex scalar function.
     */
    SOUNDEX,
    /**
     * Space scalar function.
     */
    SPACE,
    /**
     * Substr scalar function.
     */
    SUBSTR,
    /**
     * Translate scalar function.
     */
    TRANSLATE,
    /**
     * Trim scalar function.
     */
    TRIM,
    /**
     * Unicode scalar function.
     */
    UNICODE,
    /**
     * Unicodechr scalar function.
     */
    UNICODECHR,
    /**
     * Upper scalar function.
     */
    UPPER, //

    /**
     * Add days scalar function.
     */
    ADD_DAYS,
    /**
     * Add hours scalar function.
     */
    ADD_HOURS,
    /**
     * Add minutes scalar function.
     */
    ADD_MINUTES,
    /**
     * Add months scalar function.
     */
    ADD_MONTHS,
    /**
     * Add seconds scalar function.
     */
    ADD_SECONDS,
    /**
     * Add weeks scalar function.
     */
    ADD_WEEKS,
    /**
     * Add years scalar function.
     */
    ADD_YEARS,
    /**
     * Convert tz scalar function.
     */
    CONVERT_TZ,
    /**
     * Current date scalar function.
     */
    CURRENT_DATE,
    /**
     * Current timestamp scalar function.
     */
    CURRENT_TIMESTAMP,
    /**
     * Date trunc scalar function.
     */
    DATE_TRUNC,
    /**
     * Day scalar function.
     */
    DAY,
    /**
     * Days between scalar function.
     */
    DAYS_BETWEEN,
    /**
     * Dbtimezone scalar function.
     */
    DBTIMEZONE,
    /**
     * Extract scalar function.
     */
    EXTRACT(false),
    /**
     * From posix time scalar function.
     */
    FROM_POSIX_TIME,
    /**
     * Hour scalar function.
     */
    HOUR,
    /**
     * Hours between scalar function.
     */
    HOURS_BETWEEN,
    /**
     * Localtimestamp scalar function.
     */
    LOCALTIMESTAMP,
    /**
     * Minute scalar function.
     */
    MINUTE,
    /**
     * Minutes between scalar function.
     */
    MINUTES_BETWEEN,
    /**
     * Month scalar function.
     */
    MONTH,
    /**
     * Months between scalar function.
     */
    MONTHS_BETWEEN,
    /**
     * Numtodsinterval scalar function.
     */
    NUMTODSINTERVAL,
    /**
     * Numtoyminterval scalar function.
     */
    NUMTOYMINTERVAL,
    /**
     * Posix time scalar function.
     */
    POSIX_TIME,
    /**
     * Second scalar function.
     */
    SECOND,
    /**
     * Seconds between scalar function.
     */
    SECONDS_BETWEEN,
    /**
     * Sessiontimezone scalar function.
     */
    SESSIONTIMEZONE,
    /**
     * Sysdate scalar function.
     */
    SYSDATE,
    /**
     * Systimestamp scalar function.
     */
    SYSTIMESTAMP,
    /**
     * Week scalar function.
     */
    WEEK,
    /**
     * Year scalar function.
     */
    YEAR,
    /**
     * Years between scalar function.
     */
    YEARS_BETWEEN, //

    /**
     * St x scalar function.
     */
    ST_X,
    /**
     * St y scalar function.
     */
    ST_Y,
    /**
     * St endpoint scalar function.
     */
    ST_ENDPOINT,
    /**
     * St isclosed scalar function.
     */
    ST_ISCLOSED,
    /**
     * St isring scalar function.
     */
    ST_ISRING,
    /**
     * St length scalar function.
     */
    ST_LENGTH,
    /**
     * St numpoints scalar function.
     */
    ST_NUMPOINTS,
    /**
     * St pointn scalar function.
     */
    ST_POINTN,
    /**
     * St startpoint scalar function.
     */
    ST_STARTPOINT,
    /**
     * St area scalar function.
     */
    ST_AREA,
    /**
     * St exteriorring scalar function.
     */
    ST_EXTERIORRING,
    /**
     * St interiorringn scalar function.
     */
    ST_INTERIORRINGN,
    /**
     * St numinteriorrings scalar function.
     */
    ST_NUMINTERIORRINGS,
    /**
     * St geometryn scalar function.
     */
    ST_GEOMETRYN,
    /**
     * St numgeometries scalar function.
     */
    ST_NUMGEOMETRIES,
    /**
     * St boundary scalar function.
     */
    ST_BOUNDARY,
    /**
     * St buffer scalar function.
     */
    ST_BUFFER,
    /**
     * St centroid scalar function.
     */
    ST_CENTROID,
    /**
     * St contains scalar function.
     */
    ST_CONTAINS,
    /**
     * St convexhull scalar function.
     */
    ST_CONVEXHULL,
    /**
     * St crosses scalar function.
     */
    ST_CROSSES,
    /**
     * St difference scalar function.
     */
    ST_DIFFERENCE,
    /**
     * St dimension scalar function.
     */
    ST_DIMENSION,
    /**
     * St disjoint scalar function.
     */
    ST_DISJOINT,
    /**
     * St distance scalar function.
     */
    ST_DISTANCE,
    /**
     * St envelope scalar function.
     */
    ST_ENVELOPE,
    /**
     * St equals scalar function.
     */
    ST_EQUALS,
    /**
     * St force 2 d scalar function.
     */
    ST_FORCE2D,
    /**
     * St geometrytype scalar function.
     */
    ST_GEOMETRYTYPE,
    /**
     * St intersection scalar function.
     */
    ST_INTERSECTION,
    /**
     * St intersects scalar function.
     */
    ST_INTERSECTS,
    /**
     * St isempty scalar function.
     */
    ST_ISEMPTY,
    /**
     * St issimple scalar function.
     */
    ST_ISSIMPLE,
    /**
     * St overlaps scalar function.
     */
    ST_OVERLAPS,
    /**
     * St setsrid scalar function.
     */
    ST_SETSRID,
    /**
     * St symdifference scalar function.
     */
    ST_SYMDIFFERENCE,
    /**
     * St touches scalar function.
     */
    ST_TOUCHES,
    /**
     * St transform scalar function.
     */
    ST_TRANSFORM,
    /**
     * St union scalar function.
     */
    ST_UNION,
    /**
     * St within scalar function.
     */
    ST_WITHIN, //

    /**
     * Cast scalar function.
     */
    CAST(false),
    /**
     * Is number scalar function.
     */
    IS_NUMBER,
    /**
     * Is boolean scalar function.
     */
    IS_BOOLEAN,
    /**
     * Is date scalar function.
     */
    IS_DATE,
    /**
     * Is dsinterval scalar function.
     */
    IS_DSINTERVAL,
    /**
     * Is yminterval scalar function.
     */
    IS_YMINTERVAL,
    /**
     * Is timestamp scalar function.
     */
    IS_TIMESTAMP,
    /**
     * To char scalar function.
     */
    TO_CHAR,
    /**
     * To date scalar function.
     */
    TO_DATE,
    /**
     * To dsinterval scalar function.
     */
    TO_DSINTERVAL,
    /**
     * To yminterval scalar function.
     */
    TO_YMINTERVAL,
    /**
     * To number scalar function.
     */
    TO_NUMBER,
    /**
     * To timestamp scalar function.
     */
    TO_TIMESTAMP, //

    /**
     * Bit and scalar function.
     */
    BIT_AND,
    /**
     * Bit check scalar function.
     */
    BIT_CHECK,
    /**
     * Bit lrotate scalar function.
     */
    BIT_LROTATE,
    /**
     * Bit lshift scalar function.
     */
    BIT_LSHIFT,
    /**
     * Bit not scalar function.
     */
    BIT_NOT,
    /**
     * Bit or scalar function.
     */
    BIT_OR,
    /**
     * Bit rrotate scalar function.
     */
    BIT_RROTATE,
    /**
     * Bit rshift scalar function.
     */
    BIT_RSHIFT,
    /**
     * Bit set scalar function.
     */
    BIT_SET,
    /**
     * Bit to num scalar function.
     */
    BIT_TO_NUM,
    /**
     * Bit xor scalar function.
     */
    BIT_XOR, //

    /**
     * Case scalar function.
     */
    CASE(false),
    /**
     * Current schema scalar function.
     */
    CURRENT_SCHEMA,
    /**
     * Current session scalar function.
     */
    CURRENT_SESSION,
    /**
     * Current statement scalar function.
     */
    CURRENT_STATEMENT,
    /**
     * Current user scalar function.
     */
    CURRENT_USER,
    /**
     * Hash md 5 scalar function.
     */
    HASH_MD5,
    /**
     * Hashtype md 5 scalar function.
     */
    HASHTYPE_MD5,
    /**
     * Hash sha 1 scalar function.
     */
    HASH_SHA1,
    /**
     * Hashtype sha 1 scalar function.
     */
    HASHTYPE_SHA1,
    /**
     * Hash sha 256 scalar function.
     */
    HASH_SHA256,
    /**
     * Hashtype sha 256 scalar function.
     */
    HASHTYPE_SHA256,
    /**
     * Hash sha 512 scalar function.
     */
    HASH_SHA512,
    /**
     * Hashtype sha 512 scalar function.
     */
    HASHTYPE_SHA512,
    /**
     * Hash tiger scalar function.
     */
    HASH_TIGER,
    /**
     * Hashtype tiger scalar function.
     */
    HASHTYPE_TIGER,
    /**
     * Nullifzero scalar function.
     */
    NULLIFZERO,
    /**
     * Sys guid scalar function.
     */
    SYS_GUID,
    /**
     * Zeroifnull scalar function.
     */
    ZEROIFNULL,
    /**
     * Json value scalar function.
     */
    JSON_VALUE,
    /**
     * Session parameter scalar function.
     */
    SESSION_PARAMETER,
    /**
     * Min scale scalar function.
     */
    MIN_SCALE,
    /**
     * Typeof scalar function.
     */
    TYPEOF,
    /**
     * Current cluster scalar function.
     */
    CURRENT_CLUSTER;

    private final boolean isSimple;

    /**
     * True if the function is simple, i.e. is handled by {@link SqlFunctionScalar}, and false if it has it's own
     * implementation.
     *
     * @return <code>true</code> if the function is simple
     */
    public boolean isSimple() {
        return this.isSimple;
    }

    ScalarFunction() {
        this.isSimple = true;
    }

    ScalarFunction(final boolean isSimple) {
        this.isSimple = isSimple;
    }
}
