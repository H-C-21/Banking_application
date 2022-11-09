import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Admin_page {

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
                    Enter 1 view information of a account
                    Enter 2 to view information of all accounts
                    Enter 3 to view and pay FD installments
                    Enter any other to go back and logout""");
            String key = sc.nextLine();
            if (Objects.equals(key, "1")) {
                info_account(stmt);
            } else if (Objects.equals(key, "2")) {
                info_all(stmt);
            } else if (Objects.equals(key, "3")) {
                view_fd(stmt);
            } else {
                System.out.println("Successfully logged out!!");
                return false;
            }
        }
    }
}
