package com.exasol.adapter.capabilities;

import com.exasol.adapter.sql.ScalarFunction;

/**
 * List of all scalar functions supported by EXASOL. Note that predicates are handled separately in
 * {@link PredicateCapability}.
 */
public enum ScalarFunctionCapability {
    /**
     * Add scalar function capability.
     */
    ADD,
    /**
     * Sub scalar function capability.
     */
    SUB,
    /**
     * Mult scalar function capability.
     */
    MULT,
    /**
     * Float div scalar function capability.
     */
    FLOAT_DIV,
    /**
     * Neg scalar function capability.
     */
    NEG,

    /**
     * Abs scalar function capability.
     */
    ABS,
    /**
     * Acos scalar function capability.
     */
    ACOS,
    /**
     * Asin scalar function capability.
     */
    ASIN,
    /**
     * Atan scalar function capability.
     */
    ATAN,
    /**
     * Atan 2 scalar function capability.
     */
    ATAN2,
    /**
     * Ceil scalar function capability.
     */
    CEIL,
    /**
     * Cos scalar function capability.
     */
    COS,
    /**
     * Cosh scalar function capability.
     */
    COSH,
    /**
     * Cot scalar function capability.
     */
    COT,
    /**
     * Degrees scalar function capability.
     */
    DEGREES,
    /**
     * Div scalar function capability.
     */
    DIV,
    /**
     * Exp scalar function capability.
     */
    EXP,
    /**
     * Floor scalar function capability.
     */
    FLOOR,
    /**
     * Greatest scalar function capability.
     */
    GREATEST,
    /**
     * Least scalar function capability.
     */
    LEAST,
    /**
     * Ln scalar function capability.
     */
    LN,
    /**
     * Log scalar function capability.
     */
    LOG,
    /**
     * Mod scalar function capability.
     */
    MOD,
    /**
     * Power scalar function capability.
     */
    POWER,
    /**
     * Radians scalar function capability.
     */
    RADIANS,
    /**
     * Rand scalar function capability.
     */
    RAND,
    /**
     * Round scalar function capability.
     */
    ROUND,
    /**
     * Sign scalar function capability.
     */
    SIGN,
    /**
     * Sin scalar function capability.
     */
    SIN,
    /**
     * Sinh scalar function capability.
     */
    SINH,
    /**
     * Sqrt scalar function capability.
     */
    SQRT,
    /**
     * Tan scalar function capability.
     */
    TAN,
    /**
     * Tanh scalar function capability.
     */
    TANH,
    /**
     * Trunc scalar function capability.
     */
    TRUNC,
    /**
     * WIDTH_BUCKET scalar function capability.
     */
    WIDTH_BUCKET,

    /**
     * Ascii scalar function capability.
     */
    ASCII,
    /**
     * Bit length scalar function capability.
     */
    BIT_LENGTH,
    /**
     * Chr scalar function capability.
     */
    CHR,
    /**
     * Cologne phonetic scalar function capability.
     */
    COLOGNE_PHONETIC,
    /**
     * Concat scalar function capability.
     */
    CONCAT,
    /**
     * Dump scalar function capability.
     */
    DUMP,
    /**
     * Edit distance scalar function capability.
     */
    EDIT_DISTANCE,
    /**
     * Initcap scalar function capability.
     */
    INITCAP,
    /**
     * Insert scalar function capability.
     */
    INSERT,
    /**
     * Instr scalar function capability.
     */
    INSTR,
    /**
     * Length scalar function capability.
     */
    LENGTH,
    /**
     * Locate scalar function capability.
     */
    LOCATE,
    /**
     * Lower scalar function capability.
     */
    LOWER,
    /**
     * Lpad scalar function capability.
     */
    LPAD,
    /**
     * Ltrim scalar function capability.
     */
    LTRIM,
    /**
     * Octet length scalar function capability.
     */
    OCTET_LENGTH,
    /**
     * Regexp instr scalar function capability.
     */
    REGEXP_INSTR,
    /**
     * Regexp replace scalar function capability.
     */
    REGEXP_REPLACE,
    /**
     * Regexp substr scalar function capability.
     */
    REGEXP_SUBSTR,
    /**
     * Repeat scalar function capability.
     */
    REPEAT,
    /**
     * Replace scalar function capability.
     */
    REPLACE,
    /**
     * Reverse scalar function capability.
     */
    REVERSE,
    /**
     * Right scalar function capability.
     */
    RIGHT,
    /**
     * Rpad scalar function capability.
     */
    RPAD,
    /**
     * Rtrim scalar function capability.
     */
    RTRIM,
    /**
     * Soundex scalar function capability.
     */
    SOUNDEX,
    /**
     * Space scalar function capability.
     */
    SPACE,
    /**
     * Substr scalar function capability.
     */
    SUBSTR,
    /**
     * Translate scalar function capability.
     */
    TRANSLATE,
    /**
     * Trim scalar function capability.
     */
    TRIM,
    /**
     * Unicode scalar function capability.
     */
    UNICODE,
    /**
     * Unicodechr scalar function capability.
     */
    UNICODECHR,
    /**
     * Upper scalar function capability.
     */
    UPPER,

    /**
     * Add days scalar function capability.
     */
    ADD_DAYS,
    /**
     * Add hours scalar function capability.
     */
    ADD_HOURS,
    /**
     * Add minutes scalar function capability.
     */
    ADD_MINUTES,
    /**
     * Add months scalar function capability.
     */
    ADD_MONTHS,
    /**
     * Add seconds scalar function capability.
     */
    ADD_SECONDS,
    /**
     * Add weeks scalar function capability.
     */
    ADD_WEEKS,
    /**
     * Add years scalar function capability.
     */
    ADD_YEARS,
    /**
     * Convert tz scalar function capability.
     */
    CONVERT_TZ,
    /**
     * Current date scalar function capability.
     */
    CURRENT_DATE,
    /**
     * Current timestamp scalar function capability.
     */
    CURRENT_TIMESTAMP,
    /**
     * Date trunc scalar function capability.
     */
    DATE_TRUNC,
    /**
     * Day scalar function capability.
     */
    DAY,
    /**
     * Days between scalar function capability.
     */
    DAYS_BETWEEN,
    /**
     * Dbtimezone scalar function capability.
     */
    DBTIMEZONE,
    /**
     * Extract scalar function capability.
     */
    EXTRACT,
    /**
     * From posix time scalar function capability.
     */
    FROM_POSIX_TIME,
    /**
     * Hour scalar function capability.
     */
    HOUR,
    /**
     * Hours between scalar function capability.
     */
    HOURS_BETWEEN,
    /**
     * Localtimestamp scalar function capability.
     */
    LOCALTIMESTAMP,
    /**
     * Minute scalar function capability.
     */
    MINUTE,
    /**
     * Minutes between scalar function capability.
     */
    MINUTES_BETWEEN,
    /**
     * Month scalar function capability.
     */
    MONTH,
    /**
     * Months between scalar function capability.
     */
    MONTHS_BETWEEN,
    /**
     * Numtodsinterval scalar function capability.
     */
    NUMTODSINTERVAL,
    /**
     * Numtoyminterval scalar function capability.
     */
    NUMTOYMINTERVAL,
    /**
     * Posix time scalar function capability.
     */
    POSIX_TIME,
    /**
     * Second scalar function capability.
     */
    SECOND,
    /**
     * Seconds between scalar function capability.
     */
    SECONDS_BETWEEN,
    /**
     * Sessiontimezone scalar function capability.
     */
    SESSIONTIMEZONE,
    /**
     * Sysdate scalar function capability.
     */
    SYSDATE,
    /**
     * Systimestamp scalar function capability.
     */
    SYSTIMESTAMP,
    /**
     * Week scalar function capability.
     */
    WEEK,
    /**
     * Year scalar function capability.
     */
    YEAR,
    /**
     * Years between scalar function capability.
     */
    YEARS_BETWEEN,

    /**
     * St x scalar function capability.
     */
    ST_X,
    /**
     * St y scalar function capability.
     */
    ST_Y,
    /**
     * St endpoint scalar function capability.
     */
    ST_ENDPOINT,
    /**
     * St isclosed scalar function capability.
     */
    ST_ISCLOSED,
    /**
     * St isring scalar function capability.
     */
    ST_ISRING,
    /**
     * St length scalar function capability.
     */
    ST_LENGTH,
    /**
     * St numpoints scalar function capability.
     */
    ST_NUMPOINTS,
    /**
     * St pointn scalar function capability.
     */
    ST_POINTN,
    /**
     * St startpoint scalar function capability.
     */
    ST_STARTPOINT,
    /**
     * St area scalar function capability.
     */
    ST_AREA,
    /**
     * St exteriorring scalar function capability.
     */
    ST_EXTERIORRING,
    /**
     * St interiorringn scalar function capability.
     */
    ST_INTERIORRINGN,
    /**
     * St numinteriorrings scalar function capability.
     */
    ST_NUMINTERIORRINGS,
    /**
     * St geometryn scalar function capability.
     */
    ST_GEOMETRYN,
    /**
     * St numgeometries scalar function capability.
     */
    ST_NUMGEOMETRIES,
    /**
     * St boundary scalar function capability.
     */
    ST_BOUNDARY,
    /**
     * St buffer scalar function capability.
     */
    ST_BUFFER,
    /**
     * St centroid scalar function capability.
     */
    ST_CENTROID,
    /**
     * St contains scalar function capability.
     */
    ST_CONTAINS,
    /**
     * St convexhull scalar function capability.
     */
    ST_CONVEXHULL,
    /**
     * St crosses scalar function capability.
     */
    ST_CROSSES,
    /**
     * St difference scalar function capability.
     */
    ST_DIFFERENCE,
    /**
     * St dimension scalar function capability.
     */
    ST_DIMENSION,
    /**
     * St disjoint scalar function capability.
     */
    ST_DISJOINT,
    /**
     * St distance scalar function capability.
     */
    ST_DISTANCE,
    /**
     * St envelope scalar function capability.
     */
    ST_ENVELOPE,
    /**
     * St equals scalar function capability.
     */
    ST_EQUALS,
    /**
     * St force 2 d scalar function capability.
     */
    ST_FORCE2D,
    /**
     * St geometrytype scalar function capability.
     */
    ST_GEOMETRYTYPE,
    /**
     * St intersection scalar function capability.
     */
    ST_INTERSECTION,
    /**
     * St intersects scalar function capability.
     */
    ST_INTERSECTS,
    /**
     * St isempty scalar function capability.
     */
    ST_ISEMPTY,
    /**
     * St issimple scalar function capability.
     */
    ST_ISSIMPLE,
    /**
     * St overlaps scalar function capability.
     */
    ST_OVERLAPS,
    /**
     * St setsrid scalar function capability.
     */
    ST_SETSRID,
    /**
     * St symdifference scalar function capability.
     */
    ST_SYMDIFFERENCE,
    /**
     * St touches scalar function capability.
     */
    ST_TOUCHES,
    /**
     * St transform scalar function capability.
     */
    ST_TRANSFORM,
    /**
     * St union scalar function capability.
     */
    ST_UNION,
    /**
     * St within scalar function capability.
     */
    ST_WITHIN,

    /**
     * Cast scalar function capability.
     */
    CAST,
    /**
     * Is number scalar function capability.
     */
    IS_NUMBER,
    /**
     * Is boolean scalar function capability.
     */
    IS_BOOLEAN,
    /**
     * Is date scalar function capability.
     */
    IS_DATE,
    /**
     * Is dsinterval scalar function capability.
     */
    IS_DSINTERVAL,
    /**
     * Is yminterval scalar function capability.
     */
    IS_YMINTERVAL,
    /**
     * Is timestamp scalar function capability.
     */
    IS_TIMESTAMP,
    /**
     * To char scalar function capability.
     */
    TO_CHAR,
    /**
     * To date scalar function capability.
     */
    TO_DATE,
    /**
     * To dsinterval scalar function capability.
     */
    TO_DSINTERVAL,
    /**
     * To yminterval scalar function capability.
     */
    TO_YMINTERVAL,
    /**
     * To number scalar function capability.
     */
    TO_NUMBER,
    /**
     * To timestamp scalar function capability.
     */
    TO_TIMESTAMP,

    /**
     * Bit and scalar function capability.
     */
    BIT_AND,
    /**
     * Bit check scalar function capability.
     */
    BIT_CHECK,
    /**
     * Bit lrotate scalar function capability.
     */
    BIT_LROTATE,
    /**
     * Bit lshift scalar function capability.
     */
    BIT_LSHIFT,
    /**
     * Bit not scalar function capability.
     */
    BIT_NOT,
    /**
     * Bit or scalar function capability.
     */
    BIT_OR,
    /**
     * Bit rrotate scalar function capability.
     */
    BIT_RROTATE,
    /**
     * Bit rshift scalar function capability.
     */
    BIT_RSHIFT,
    /**
     * Bit set scalar function capability.
     */
    BIT_SET,
    /**
     * Bit to num scalar function capability.
     */
    BIT_TO_NUM,
    /**
     * Bit xor scalar function capability.
     */
    BIT_XOR,

    /**
     * Case scalar function capability.
     */
    CASE,
    /**
     * Current schema scalar function capability.
     */
    CURRENT_SCHEMA,
    /**
     * Current session scalar function capability.
     */
    CURRENT_SESSION,
    /**
     * Current statement scalar function capability.
     */
    CURRENT_STATEMENT,
    /**
     * Current user scalar function capability.
     */
    CURRENT_USER,
    /**
     * Hash md 5 scalar function capability.
     */
    HASH_MD5,
    /**
     * Hashtype md 5 scalar function capability.
     */
    HASHTYPE_MD5,
    /**
     * Hash sha 1 scalar function capability.
     */
    HASH_SHA1,
    /**
     * Hashtype sha 1 scalar function capability.
     */
    HASHTYPE_SHA1,
    /**
     * Hash sha 256 scalar function capability.
     */
    HASH_SHA256,
    /**
     * Hashtype sha 256 scalar function capability.
     */
    HASHTYPE_SHA256,
    /**
     * Hash sha 512 scalar function capability.
     */
    HASH_SHA512,
    /**
     * Hashtype sha 512 scalar function capability.
     */
    HASHTYPE_SHA512,
    /**
     * Hash tiger scalar function capability.
     */
    HASH_TIGER,
    /**
     * Hashtype tiger scalar function capability.
     */
    HASHTYPE_TIGER,
    /**
     * Nullifzero scalar function capability.
     */
    NULLIFZERO,
    /**
     * Sys guid scalar function capability.
     */
    SYS_GUID,
    /**
     * Zeroifnull scalar function capability.
     */
    ZEROIFNULL,
    /**
     * Json value scalar function capability.
     */
    JSON_VALUE,
    /**
     * Session parameter scalar function capability.
     */
    SESSION_PARAMETER,
    /**
     * Min scale scalar function capability.
     */
    MIN_SCALE,
    /**
     * Typeof scalar function capability.
     */
    TYPEOF,
    /**
     * Current cluster scalar function capability.
     */
    CURRENT_CLUSTER;

    /**
     * Gets function.
     *
     * @return the function
     */
    public ScalarFunction getFunction() {
        return ScalarFunction.valueOf(name());
    }
}
