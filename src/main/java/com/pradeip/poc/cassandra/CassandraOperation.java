package com.pradeip.poc.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

public interface CassandraOperation {

	/**
	 * 
	 * @param path
	 */
	public void executeDDLScript(String path);
	/**
	 * 
	 * @param session
	 */
	public void insertBulkData(int numberOfRecords, String keyspace, String table);
	/**
	 * 
	 * @param session
	 * @param keyspace
	 * @param table
	 * @return
	 */
	public TableMetadata getMetadata(Session session, String keyspace, String table);

	public void executeQuery(String query);
}
