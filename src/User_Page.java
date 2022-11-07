import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class User_Page {
    public static void print() {
        System.out.println("""
                Enter 1 to view account details
                Enter 2 to edit account details
                Enter 3 to view Queries
                Enter 4 to withdraw/deposit
                Enter 5 to send money
                Enter 6 to delete account
                Enter 7 to enter Loan page
                Enter 8 to exit""");
    }

    public static void display_transactions(Statement stmt, String str) throws SQLException {
        ResultSet rst = stmt.executeQuery("select amount, date, transaction_to from transactions where acc_no = " + str + ";");
        int count = 0;
        System.out.println("List of transactions:");
        while (rst.next()) {
            count++;
            try {
                System.out.printf(count + ") Amount:%-20s" + "Date:" + rst.getDate(2) + "        " +
                        "Time:" + rst.getTime(2) + "        Transaction To/From:%s\n", rst.getString(1), rst.getString(3));
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        if (count == 0) {
            System.out.println("No transaction record found.");
        }
    }

    public static void display_account_info(Statement stmt, String str) throws SQLException {
        Account_Main obj = new Account_Main(stmt);
        obj.display(Integer.parseInt(str));
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 1 to show transactions\nEnter any other to exit");
        int key = sc.nextInt();
        if (key == 1) {
            display_transactions(stmt, str);
        }
    }

    public static void query_check(Statement stmt, String str) throws SQLException {
        ResultSet rst = stmt.executeQuery("select status from queries where acc_no = '" + str + "'");
        int counter = 0;
        boolean flag = false;
        Scanner sc = new Scanner(System.in);
        while (rst.next()) {
            if (Objects.equals(rst.getString(1), "Resolved")) {
                System.out.println("Your Query has been resolved. Delete the query to be able to file a new query.");
                flag = true;
            }
            counter++;
        }
        if (counter == 0) {
            System.out.println("You have not updated any query\nEnter 1 to add a query\nEnter any other number to go back");
            String key2 = sc.nextLine();
            if (Objects.equals(key2, "1")) {
                Query.submit_query(stmt, str);
            }
        } else {
            if (!flag) {
                System.out.println("Enter 1 to see the status of your query\nEnter 2 to update query\nEnter 3 to delete the submitted query\nEnter any other to go back");
                int key = sc.nextInt();
                if (key == 1) {
                    Query.display_status(stmt, str);
                } else if (key == 2) {
                    Query.update_query(stmt, str);
                } else if (key == 3) {
                    Query.delete_query(stmt, str);
                }
            } else {
                System.out.println("Enter 1 to see the status of your query\nEnter 2 to delete the submitted query\nEnter any other to go back");
                int key = sc.nextInt();
                if (key == 1) {
                    Query.display_status(stmt, str);
                } else if (key == 2) {
                    Query.delete_query(stmt, str);
                }
            }
        }
    }

    public static boolean page(Statement stmt, String str) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            print();
            String key = sc.nextLine();
            if (Objects.equals(key, "8")) {
                System.out.println("Logout successful.");
                return false;
            } else if (Objects.equals(key, "1")) {
                display_account_info(stmt, str);
            } else if (Objects.equals(key, "2")) {
                Edit_Account_Details obj = new Edit_Account_Details(stmt, str);
                System.out.println("Enter 1 to change name\nEnter 2 to change password");
                String key2 = sc.nextLine();
                if (Objects.equals(key2, "1")) {
                    obj.change_name();
                } else if (Objects.equals(key2, "2")) {
                    obj.change_pass();
                } else {
                    System.out.println("Enter the correct key value!!");
                }
            } else if (Objects.equals(key, "3")) {
                query_check(stmt, str);
            } else if (Objects.equals(key, "4")) {
                Withdraw_Deposit obj = new Withdraw_Deposit(stmt, str);
                System.out.println("Enter 1 to deposit\nEnter 2 to withdraw\nEnter anything else to cancel");
                String key2 = sc.nextLine();
                if (Objects.equals(key2, "1")) {
                    System.out.print("Enter amount to be deposited:");
                    double amount = sc.nextDouble();
                    sc.nextLine();
                    if (obj.deposit(amount, "SELF")) {
                        System.out.println("Amount deposited successfully");
                    } else {
                        System.out.println("Deposit failed.");
                    }
                } else if (Objects.equals(key2, "2")) {
                    ResultSet rst = stmt.executeQuery("select password, balance from account where acc_no =" + str + ";");
                    String pass = null;
                    double balance = 0;
                    while (rst.next()) {
                        pass = rst.getString(1);
                        balance = rst.getDouble(2);
                    }
                    System.out.print("Enter password:");
                    if (Objects.equals(sc.nextLine(), pass)) {
                        System.out.print("Enter the amount to be withdrawn:");
                        double amount = sc.nextDouble();
                        sc.nextLine();
                        if (obj.withdraw(amount, "SELF")) {
                            System.out.println("Amount successfully withdrawn. Remaining balance is:" + (balance - amount));
                        } else {
                            System.out.println("Insufficient balance. Try taking a loan...");
                        }

                    } else {
                        System.out.println("Incorrect password. Withdrawal failed.");
                    }
                }
            } else if (Objects.equals(key, "5")) {
                Transaction_InterAccounts.doTransaction(stmt, str);
                ResultSet rst = stmt.executeQuery("select balance from account where acc_no = '" + str + "';");
                while (rst.next()) {
                    System.out.println("Remaining balance is:" + rst.getDouble(1));
                }
            } else if (Objects.equals(key, "6")) {
                Account_Main obj1 = new Account_Main(stmt);
                return obj1.delete(Integer.parseInt(str));
            } else if (Objects.equals(key, "7")) {
                Loan_main.loan_main(stmt, Integer.parseInt(str));
            } else {
                System.out.println("Enter a valid key!!");
            }
        }
    }
}
