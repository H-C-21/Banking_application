import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Admin_page {

    public static void resolve_queries(Statement stmt) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the account number for which the query is to be resolved:");
        int acc = sc.nextInt();
        ResultSet rst = stmt.executeQuery("select * from queries where acc_no = " + acc + ";");
        boolean flag = false;
        String str = null;
        while (rst.next()) {
            str = rst.getString(3);
            flag = true;
        }
        if (flag && Objects.equals(str, "Unresolved")) {
            stmt.execute("update queries set status = 'Resolved' where acc_no = " + acc + ";");
            System.out.println("Query resolved successfully.");
        } else if (flag && Objects.equals(str, "Resolved")) {
            System.out.println("Query is already resolved.");
        } else if (!flag) {
            System.out.println("Account not registered with a query.");
        }
    }

    public static void view_queries(Statement stmt) throws SQLException {
        ResultSet rst = stmt.executeQuery("select * from queries;");
        boolean flag = false;
        while (rst.next()) {
            if (Objects.equals(rst.getString(3), "Solved")) {
                continue;
            }
            System.out.printf("Account Number:%-10d Query:%-100s Status:%s\n", rst.getInt(1), rst.getString(2), rst.getString(3));
            flag = true;
        }
        if (flag) {
            System.out.println("Enter 1 to resolve query\nEnter any other to continue");
            Scanner sc = new Scanner(System.in);
            String key = sc.nextLine();
            if (Objects.equals(key, "1")) {
                resolve_queries(stmt);
            }
        } else {
            System.out.println("There are no queries or all queries have been resolved.");
        }

    }

    public static void info_account(Statement stmt) throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the account number:");
        String str = sc.nextLine();
        Account_Main obj = new Account_Main(stmt);
        obj.display(Integer.parseInt(str));
    }

    public static void info_all(Statement stmt) throws SQLException {
        ResultSet rst = stmt.executeQuery("select acc_no, customer_name,balance from account;");
        while (rst.next()) {
            System.out.printf("Account Number:%-10d  Name:%-30s   Balance = %f\n", rst.getInt(1), rst.getString(2), rst.getDouble(3));
        }
    }

    public static boolean page(Statement stmt) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Enter 1 to view queries\nEnter 2 view information of a account\nEnter 3 to view information of all accounts\nEnter any other to go back and logout");
            String key = sc.nextLine();
            if (Objects.equals(key, "1")) {
                view_queries(stmt);
            } else if (Objects.equals(key, "2")) {
                info_account(stmt);
            } else if (Objects.equals(key, "3")) {
                info_all(stmt);
            } else {
                System.out.println("Successfully logged out!!");
                return false;
            }
        }
    }
}
