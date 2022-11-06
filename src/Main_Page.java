import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Main_Page {

    public static void menu_print() {
        System.out.println("Enter 1 to add an account\nEnter 2 to login(as customer)\nEnter 3 to login(as administrator)\nEnter 4 to exit");
    }

    public static void main(String[] args) {
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
                    if (Objects.equals(key, "4")) {
                        System.out.println("Thank You! Please use our app again!!\nBye");
                        break;
                    } else if (Objects.equals(key, "1")) {
                        Account_Main obj1 = new Account_Main(stmt);
                        obj1.create();
                    } else if (Objects.equals(key, "2")) {
                        System.out.print("Enter the account number:");
                        int acc = sc.nextInt();
                        str = String.valueOf(acc);
                        User_login obj = new User_login(stmt, str);
                        login_flag = obj.checkAuthentication();
                    } else if (Objects.equals(key, "3")) {
                        Admin_login obj = new Admin_login(stmt);
                        admin_login = obj.checkAuthentication();
                    } else {
                        System.out.println("Enter the correct key value!!");
                    }
                } else if (admin_login) {
                    admin_login = Admin_page.page(stmt);
                } else {
                    try {
                        login_flag = User_Page.page(stmt, str);
                    } catch (Exception e) {
                        System.out.println("Unable to display login page");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}