package com.pradeip.poc.cassandra;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;
import com.datastax.driver.core.querybuilder.Insert;

public class CassandraOperationImpl implements CassandraOperation, Constants {

	private Session session;

	public CassandraOperationImpl(Session session) {
		super();
		this.session = session;
	}

	@Override
	public void insertBulkData(int numberOfRecords, String keyspace, String table) {
		long T1 = System.currentTimeMillis();
		try {
			int i = 0;
			TableMetadata metadata = getMetadata(session, keyspace, table);
			if (metadata == null) {
				return;
			}
			session.execute("use " + keyspace);
			while (i++ <= numberOfRecords) {
				Insert insert = Utility.prepareInsertData(metadata, table);
				System.out.println(i + ": Insert was appied: " + session.execute(insert).wasApplied());
			}

			System.out.println(L + i + "| Records Inserted successfully |" + L);

		} catch (Exception e) {

		}
		System.out.println("insertBulkData: Total time elapsed in seonds:" + (System.currentTimeMillis() - T1) / 1000);

	}

	@Override
	public TableMetadata getMetadata(Session session, String keyspace, String table) {
		long T1 = System.currentTimeMillis();
		try {
			System.out.println("getMetadata: Total time elapsed in seonds:" + (System.currentTimeMillis() - T1) / 1000);
			return Utility.getMetadata(session, keyspace, table);
		} catch (Exception e) {

			return null;
		}

	}

	@Override
	public void executeDDLScript(String path) {
		long T1 = System.currentTimeMillis();
		File file = new File(path);

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				System.out.println(line);
				if (!line.isEmpty())
					session.execute(line);
			}
			System.out.println(BLUE_BOLD + L + "All queries executed." + L + RESET);
			session.close();
			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out
				.println("executeDDLScript: Total time elapsed in seonds:" + (System.currentTimeMillis() - T1) / 1000);

	}

	@Override
	public void executeQuery(String query) {
		int i = 0;
		System.out.println(query);
		session.execute(query);
		for (Row rs : session.execute(query)) {
			System.out.println(rs.getTupleValue(i++));
			
		}
	}

}
