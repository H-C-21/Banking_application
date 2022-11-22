import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class Insurance_main {
    static Scanner sc = new Scanner(System.in);

    public void InsurancePage(Statement stmt, String acc) throws SQLException {

        System.out.println("************** Thank You For Considering Our Bank for Your Insurance Need *****************");
        System.out.println();


        while (true) {
            System.out.println("Press 1 to Register for a new Insurance Scheme");
            System.out.println("Press 2 to Check your Current Active Insurance Plans");
            System.out.println("Press 3 to Make a deposit for an Insurance Scheme");
            System.out.println("Press 4 to Claim An Insurance Scheme");
            System.out.println("Press 5 to Exit");

            String flag = sc.nextLine();

            switch (flag) {
                case "1":
                    InsuranceUtil.newInsurance(stmt, acc);
                    continue;

                case "2":
                    Insurance.display(stmt, acc);
                    continue;

                case "3":
                    Insurance.depositMain(stmt,acc);
                    continue;

                case "4":

                    Insurance.claimMain(stmt,acc);
                    continue;

                case "5":
                    System.out.println("Taking Back to user page");
                    break;

                default:
                    System.out.println("Wrong Key, Please Enter Again");
                    continue;

            }
            break;
        }
    }
}