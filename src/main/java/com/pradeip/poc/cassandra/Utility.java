package com.pradeip.poc.cassandra;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import com.datastax.driver.core.ColumnDefinitions.Definition;
import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.annotations.Query;

public class Utility implements Constants {

	static final String SCRIPT = "sample.cql";

	public void executeFile(Session session) {
		if (session != null) {
			try {
				session.execute("drop keyspace curiosity");
			} catch (Exception e) {
				System.out.println(e.getLocalizedMessage());
			}

		} else {
			return;
		}
		File file = new File("F:\\7_Dev_Project\\sample.cql");
		// File file = new File(classLoader.getResource(SCRIPT).getFile());

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
				if (!line.isEmpty())
					session.execute(line);
			}
			System.out
					.println("------------------\n All queries executed.\n-------------------");
			session.close();
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void insertBulkRecords(int numberOfRecords, Session session,
			String keyspace, String table) {
		int i = 0;
		TableMetadata metadata = getMetadata(session, keyspace, table);
		if (metadata == null) {
			System.out
					.println("\n----------------Operation failed\n------------------");
			return;
		}
		session.execute("use " + keyspace);
		while (i++ <= numberOfRecords) {
			Insert insert = prepareInsertData(metadata, table);
			System.out.println("Insert was appied: "
					+ session.execute(insert).wasApplied());
		}

		System.out.println(L + i + " Records Inserted successfully" + L);
	}

	public static TableMetadata getMetadata(Session session, String keyspace,
			String table) {

		TableMetadata metadata = null;
		try {
			metadata = session.getCluster().getMetadata().getKeyspaces().get(0)
					.getTable(table);
			return metadata;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out
					.println(RED
							+ "\n ------------------------------------------\n| Operation failed, failed to get metadata |\n ------------------------------------------");
			return null;
		}
	}

	public static Insert prepareInsertData(TableMetadata metadata, String table) {

		if (metadata == null)
			return null;

		List<String> columns = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		for (ColumnMetadata row : metadata.getColumns()) {
			columns.add(row.getName());
			values.add(getValue(row.getType()));
			// System.out.println(row.getName() + " : " +
			// getValue(row.getType()));
		}
		Insert insert = QueryBuilder.insertInto(table).values(columns, values)
				.ifNotExists(); //

		return insert;

	}

	public static Object getValue(DataType dataType) {
		Object value;
		switch (dataType.toString()) {
		case "text":
			value = UUID.randomUUID().toString();
			break;
		case "boolean":
			value = true;
			break;
		case "int":
			value = (int) Math.random();
			break;
		case "timestamp":
			value = new Date();
			break;
		case "uuid":
			value = UUID.randomUUID();
			break;
		default:
			value = UUID.randomUUID().toString();
			break;
		}
		return value;

	}

	public static Object getRowValue(Definition definition, Row row) {
		Object value;
		switch (definition.getType().toString()) {
		case "varchar":
			value = row.getString(definition.getName());
			break;
		case "text":
			value = row.getString(definition.getName());
			break;
		case "boolean":
			value = row.getBool(definition.getName());
			break;
		case "int":
			value = row.getInt(definition.getName());
			break;
		case "timestamp":
			value = row.getTimestamp(definition.getName());
			break;

		default:
			value = row.getString(definition.getName());
			System.out.println(RED + "Datatype mismatch");
			break;
		}
		return value;

	}

	public void getByPartitionKey(String keyspace, String table,
			String partitionKey) {
		// Query query =
		// QueryBuilder.select().all().from(keyspace,table).where();

	}

	public static void executeCountQuery(Session session, String query) {
		List<Row> reset = session.execute(query).all();
		Row s = reset.get(0);
		System.out.println();
		System.out.println(NL + reset.get(0));

	}

}
