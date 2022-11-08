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
        ResultSet rst = stmt.executeQuery("select * from loans NATURAL JOIN account where acc_no = '" + str + "';");
        int count = 0;
        while (rst.next()) {
            count++;
        }
        if (count != 0) {
            System.out.println("The customer have active loans. Press 1 to view them\nAnything else to exit");
            if (Objects.equals(sc.nextLine(), "1")) {
                Loan_main.display(stmt, Integer.parseInt(str));
            }
        }
    }

    public static void info_all(Statement stmt) throws SQLException {
        ResultSet rst = stmt.executeQuery("select acc_no, customer_name,balance from account;");
        while (rst.next()) {
            System.out.printf("Account Number:%-10d  Name:%-30s   Balance = %f\n", rst.getInt(1), rst.getString(2), rst.getDouble(3));
        }
    }

    public static void view_fd(Statement stmt) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to pay monthly installment of Fixed Deposit to accounts\nEnter 2 to view current pending FDs\nEnter anything else to exit");
        String key = sc.nextLine();
        if (Objects.equals(key, "1")) {
            double sum = 0;
            int count = 0;
            ResultSet rs = stmt.executeQuery("select * from fixed_deposits;");
            while (rs.next()) {
                int acc = rs.getInt(2);
                double amount = rs.getDouble(6) / rs.getInt(5);
                sum += amount;
                int id = rs.getInt(1);
                Statement stmt2 = Connection_establish.dbms_connect();
                fixed_deposit obj = new fixed_deposit(stmt2, acc);
                if (!obj.installment_pay(amount, acc, id)) {
                    count++;
                    System.out.println("Total of " + sum + " was paid to " + count + " unique FDs.");
                    return;
                }
                count++;
            }
            if (count == 0) {
                System.out.println("No active FDs in the bank!!");
            } else {
                System.out.println("Total of " + sum + " was paid to " + count + " unique FDs.");
            }
        } else if (Objects.equals(key, "2")) {
            ResultSet rst = stmt.executeQuery("select * from fixed_deposits;");
            int count = 0;
            while (rst.next()) {
                System.out.printf("FD ID:%-10d Account Number:%-10d Amount:%-15f Date Issued:%-20s Installments Remaining:%-5d Amount Remaining:%-15f\n",
                        rst.getInt(1), rst.getInt(2), rst.getDouble(3), rst.getDate(4), rst.getInt(5),
                        rst.getDouble(6));
                count++;
            }
            if (count == 0) {
                System.out.println("No active FDs in the bank!!");
            }
        }
    }

    public static boolean page(Statement stmt) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    Enter 1 to view queries
                    Enter 2 view information of a account
                    Enter 3 to view information of all accounts
                    Enter 4 to view and pay FD installments
                    Enter any other to go back and logout""");
            String key = sc.nextLine();
            if (Objects.equals(key, "1")) {
                view_queries(stmt);
            } else if (Objects.equals(key, "2")) {
                info_account(stmt);
            } else if (Objects.equals(key, "3")) {
                info_all(stmt);
            } else if (Objects.equals(key, "4")) {
                view_fd(stmt);
            } else {
                System.out.println("Successfully logged out!!");
                return false;
            }
        }
    }
}
