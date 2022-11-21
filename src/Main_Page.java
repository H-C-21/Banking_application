import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Main_Page {

    public static void menu_print() {
        System.out.println("""
                Enter 1 to add an account
                Enter 2 to login(as customer)
                Enter 3 to login(as administrator)
                Enter 4 to submit and view Queries
                Enter 5 to submit feedback
                Enter anything else to exit""");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        if (args.length == 0) {
            try {
                Statement stmt;
                try {
                    stmt = Connection_establish.dbms_connect();
                } catch (Exception e) {
                    System.out.println("Error in connecting to the database!!");
                    return;
                }
                boolean login_flag = false;
                boolean admin_login = false;
                System.out.println("Welcome to the Banking Database System.");
                String str = null;
                while (true) {
                    if (!login_flag && !admin_login) {
                        menu_print();
                        Scanner sc = new Scanner(System.in);
                        String key = sc.nextLine();
                        if (Objects.equals(key, "1")) {
                            Account_Main obj1 = new Account_Main(stmt);
                            obj1.create();
                        } else if (Objects.equals(key, "2")) {
                            System.out.print("Enter the account number:");
                            String acc = sc.nextLine();
                            str = String.valueOf(acc);
                            User_login obj = new User_login(stmt, str);
                            login_flag = obj.checkAuthentication();
                        } else if (Objects.equals(key, "3")) {
                            Admin_login obj = new Admin_login(stmt);
                            admin_login = obj.checkAuthentication();
                        } else if (Objects.equals(key, "4")) {
                            Query_main obj = new Query_main(stmt);
                            obj.main();
                        } else if (Objects.equals(key, "5")) {
                            Feedback_main obj = new Feedback_main(stmt);
                            query_fdb obj1 = obj.take_input();
                            obj.submit(obj1);
                        } else {
                            System.out.println("Thank You for using the App!!");
                            break;
                        }
                    } else if (admin_login) {
                        admin_login = Admin_page.page(stmt);
                    } else {
                        try {
                            login_flag = User_Page.page(stmt, str);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }else {
            cmdArguments obj = new cmdArguments(args);
            obj.cmdRun();
        }
    }
}