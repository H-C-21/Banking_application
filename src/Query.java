import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Query {
    public static void submit_query(Statement stmt, String str) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the query you want to submit:");
        String query = sc.nextLine();
        try {
            stmt.execute("insert into queries (acc_no, query)" +
                    "SELECT account.acc_no, '" + query + "' from account where account.acc_no = '" + str + "'" +
                    "LIMIT 1;");
            System.out.println("Your query has been successfully uploaded.");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void display_status(Statement stmt, String str) throws SQLException {
        ResultSet rst = stmt.executeQuery("select query, status from queries where acc_no = '" + str + "'");
        System.out.println("The status of the query is shown below:");
        while (rst.next()) {
            System.out.println("Query --> " + rst.getString(1));
            System.out.println("Status --> " + rst.getString(2));
        }
    }

    public static void delete_query(Statement stmt, String str) throws SQLException {
        stmt.execute("delete from queries where acc_no = " + str);
        System.out.println("Query successfully deleted.");
    }

    public static void update_query(Statement stmt, String str) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the new query:");
        String query = sc.nextLine();
        try {
            stmt.execute("update queries set query = '" + query + "', status =  'Unresolved' where acc_no =  '" + str + "';");
            System.out.println("Query updated successfully.");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
