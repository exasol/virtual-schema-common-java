package com.exasol.adapter;

/**
 * This enumeration represents the different strategies that the Virtual Schema adapter offers for handling binary
 * column types in the source database.
 * <p>
 * Choices are:
 * </p>
 * <dl>
 * <dt>IGNORE</dt>
 * <dd>Binary column is not mapped</dd>
 * <dt>ENCODE_BASE64</dt>
 * <dd>Content of the binary column is encoded with Base64 and put into a <code>VARCHAR</code></dd>
 * </dl>
 */
public enum BinaryColumnHandling {
    /**
     * Ignore binary column handling.
     */
    IGNORE,
    /**
     * Encode base 64 binary column handling.
     */
    ENCODE_BASE64
}