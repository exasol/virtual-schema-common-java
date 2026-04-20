package com.exasol.adapter;

/**
 * Factory that creates a {@link VirtualSchemaAdapter}
 */
public interface AdapterFactory {
    /**
     * Create a new {@link VirtualSchemaAdapter}
     *
     * @param context context information for the adapter
     * @return new instance
     */
    public VirtualSchemaAdapter createAdapter(AdapterContext context);

    /**
     * Get the version of the {@link VirtualSchemaAdapter}. This version will be used for logging and telemetry.
     * <p>
     * Adapters can use {@link com.exasol.logging.VersionCollector} to fetch the version from the metadata in the jar file. For example:
     * 
     * <pre>
     * new VersionCollector("META-INF/maven/com.exasol/mysql-virtual-schema/pom.properties").getVersionNumber()
     * </pre>
     *
     * @return Virtual Schema Adapter version
     */
    public String getAdapterVersion();

    /**
     * Get the name of the {@link VirtualSchemaAdapter}. This name will be used for logging.
     * <p>
     * Example values:
     * <ul>
     * <li>JDBC based adapters: {@code MYSQL JDBC Adapter}, {@code POSTGRESQL JDBC Adapter}, {@code EXASOL JDBC Adapter}</li>
     * <li>Document based adapters: {@code DYNAMO_DB}, {@code S3_DOCUMENT_FILES}, {@code AZURE_DATA_LAKE_STORAGE_GEN2_DOCUMENT_FILES}</li>
     * </ul>
     *
     * @return Virtual Schema Adapter name
     */
    public String getAdapterName();

    /**
     * Get a short tag for the adapter project. This will be used for telemetry to identify products.
     * <p>
     * The short tag is defined in file {@code error_code_config.yml} of each adapter project.
     * <p>
     * Example values: {@code VSMYSQL}, {@code VSPG} (Postgres VS), {@code VSEXA}, {@code VSDY} (DynamoDB VS), {@code VSS3}, {@code VSADLG2}
     * 
     * @return short tag for the adapter project
     */
    public String getAdapterProjectShortTag();
}
