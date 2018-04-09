package com.pradeip.poc.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;

public class ConnectionFactory implements Constants {

	static String local_node = "127.0.0.1";
	static String ngp_node = "127.0.0.1";
	static String pt_node = "127.0.0.1";
	static String ga_node = "127.0.0.1";
	static String dcName = "ngp_india"; // this is the DC name you used when created
	static String user = "user";
	static String password = "password";
	static Cluster cluster = null;
	static Session session;

	public static void buildMultiNodeCluster() {

		Cluster.Builder clusterBuilder = Cluster.builder().addContactPoints(local_node, ngp_node, pt_node)
				.withLoadBalancingPolicy(DCAwareRoundRobinPolicy.builder().withLocalDc(dcName).build()).withPort(9042)
				.withAuthProvider(new PlainTextAuthProvider(user, password));

	}

	public static boolean buildSingleNodeCluster() {

		try {
			Cluster.Builder clusterBuilder = Cluster.builder().addContactPoints(local_node).withPort(9042)
					.withAuthProvider(new PlainTextAuthProvider(user, password));
			cluster = clusterBuilder.build();
			session = cluster.connect();
			System.out.println(NL + "Cheers!..Cluster Instantiated.");
			return true;
		} catch (Exception e) {
			System.out.println(RED + L + "| Cassandra Server is down, try later |" + L);
			return false;
		}
	}

	public static Session getSession(String keyspace) {
		if (session == null)
			return null;
		if (!session.isClosed())
			System.out.println(NL + "Session is Active...");
		return session;
	}

	public static ResultSet executeStatement(Session session, Statement stmt) {
		return session.execute(stmt);
	}

	public static Statement buildQuery(String query) {
		// return QueryBuilder.insertInto("abc")
		return null;
		// QueryBuilder.
	}

}
