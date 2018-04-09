package com.pradeip.poc.cassandra;

import java.util.Scanner;

import com.datastax.driver.core.Session;

/**
 * 
 * @author Pradeip
 *
 */
public class App implements Constants {

	static Session session;

	public static void main(String[] args) {

		if (ConnectionFactory.buildSingleNodeCluster()) {
			System.out.println(PURPLE_BOLD + L + WELCOME + L + RESET);
			session = ConnectionFactory.getSession(null);
			CassandraOperation caop = new CassandraOperationImpl(session);
			String cmd = null;
			recurse(cmd, caop);
			System.out.println("Bye Bye !");
			if (!session.isClosed())
				session.close();
			System.exit(0);
		} else {
			System.out.println("Something went wrong with the connection.Failed to obtain session..");
		}

	}

	@SuppressWarnings("resource")
	public static void recurse(String cmd, CassandraOperation caop) {
		if ("exit".equals(cmd))
			return;
		System.out.println("\nHow can I assit you. Type help for more assistance");
		Scanner keyboard = new Scanner(System.in);
		cmd = keyboard.nextLine();
		try {
			switch (cmd) {

			case "rs":
				System.out.print("Enter full path of the script file to use ");
				String path = SCRIPT_FOLDER_PATH + SCRIPT_NAME;
				path = keyboard.nextLine();
				System.out.println(NL + GREEN + "Reading script file from disk:" + SCRIPT_FOLDER_PATH + SCRIPT_NAME);
				caop.executeDDLScript(SCRIPT_FOLDER_PATH + SCRIPT_NAME);
				cmd = null;
				break;
			case "bi":
				System.out.print("Enter keyspace to use ");
				String keyspace = keyboard.nextLine();
				System.out.print("Enter table to use ");
				String table = keyboard.nextLine();
				System.out.print("Enter number of records to insert ");
				String n = keyboard.nextLine();
				caop.insertBulkData(Integer.parseInt(n), keyspace, table);
				cmd = null;
				break;
			case "q":
				System.out.print("Enter query to use ");
				String query = keyboard.nextLine();
				caop.executeQuery(query);
				cmd = null;
				break;

			case "e":
				cmd = null;
				break;
			case "exit":
				cmd = "exit";
				session.close();
				break;
			case "help":
				System.out.println(BLUE + L + HELP + L + RESET);
				cmd = null;
				break;
			default:
				System.out.println("Invalid command -- type help for assistance");
				break;
			}

		} catch (Exception e) {
			// Terminate JVM
            System.exit(0);
			System.out.println("unexpected input recieved,please try again");
		}

		recurse(cmd, caop);
	}

}
