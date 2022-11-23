package Insurance;

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
            System.out.println("Press 3 to Search The Details of any of your Schemes");
            System.out.println("Press 4 to Make a deposit for an Insurance Scheme");
            System.out.println("Press 5 to Claim An Insurance Scheme");
            System.out.println("Press 6 to Exit");

            String flag = sc.nextLine();

            switch (flag) {
                case "1" -> {
                    InsuranceUtil.newInsurance(stmt, acc);
                    continue;
                }
                case "2" -> {
                    InsuranceUtil.display(stmt, acc);
                    continue;
                }
                case "3" -> {
                    System.out.println("Enter 1 to Check by ID");
                    System.out.println("Enter 2 to Check by Scheme");
                    String choice = sc.nextLine();
                    if (choice.equals("1")) {
                        System.out.println("Enter The Insurance ID of The Plan you Wish to Check");
                        String id = sc.nextLine();
                        int in_id = Integer.parseInt(id);
                        Insurance a = InsuranceUtil.getScheme(stmt, in_id);
                        if (a != null) {
                            if (a.authenticateAllInsurance(acc)) {
                                System.out.println(a);
                            }
                        }
                    }
                    continue;
                }
                case "4" -> {
                    Insurance.depositMain(stmt, acc);
                    continue;
                }
                case "5" -> {
                    Insurance.claimMain(stmt, acc);
                    continue;
                }
                case "6" -> System.out.println("Taking Back to user page");
                default -> {
                    System.out.println("Wrong Key, Please Enter Again");
                    continue;
                }
            }
            break;
        }
    }
}
