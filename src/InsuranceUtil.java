import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class InsuranceUtil {

    static Scanner sc = new Scanner(System.in);

    public static void display(Statement stmt, String acc) {

        System.out.println("Inside display");
        int counti = 0;
        try {
            ResultSet rst1 = stmt.executeQuery("select ins_id, ins_type, date_issued,amount_dep " + "from insurance where acc_no = " + acc + ";");
            System.out.println("Insurance ID         Insurance Type           Date Issued          Insured till");


            while (rst1.next()) {

                int id = rst1.getInt("ins_id");
                String type = rst1.getString("ins_type");
                Date iss = rst1.getDate("date_issued");
                int amt = rst1.getInt("amount_dep");
                System.out.printf("    " + "%-20d %-20s %-20s %-20d\n", id, type, iss, amt);
                ++counti;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (counti == 0) {
            System.out.println();
            System.out.println("You are Currently Not registered under any Insurance Schemes, Press 1 to opt");
        }

    }

    public static void newInsurance(Statement stmt, String acc) {

        boolean running = true;
        int a = Integer.parseInt(acc);
        while (running) {
            System.out.println("Select The Type of Insurance You Want to Opt for - ");
            System.out.println("Press 1 for Health Insurance");
            System.out.println("Press 2 for Life Insurance");
            System.out.println("Press 3 for Car Insurance");
            System.out.println("Press 4 for Home Insurance");
            System.out.println("Press 5 to exit");
            String flag = sc.nextLine();

            switch (flag) {
                case "1":
                    Health ins1 = new Health();
                    ins1.print();                         //Shows Insurance Plan Information and ask whether to proceed or not
                    String h1 = sc.nextLine();
                    if (Objects.equals(h1, "1")) {
                        ins1.newInsurance(stmt, acc);
                    }
                    break;
                case "2":
                    Life ins2 = new Life();
                    ins2.print();
                    String h2 = sc.nextLine();
                    if (Objects.equals(h2, "1")) {
                        ins2.newInsurance(stmt, acc);
                    }
                    break;
                case "3":
                    Car ins3 = new Car();
                    ins3.print();
                    String h3 = sc.nextLine();
                    if (Objects.equals(h3, "1")) {
                        ins3.newInsurance(stmt, acc);
                    }
                    break;
                case "4":
                    Home ins4 = new Home();
                    ins4.print();
                    String h4 = sc.nextLine();
                    if (Objects.equals(h4, "1")) {
                        ins4.newInsurance(stmt, acc);
                    }
                    break;

                case "5":
                    running = false;
                    break;

                default:
                    System.out.println("You have entered a Wrong key please select again");
                    break;
            }
            //stmt.execute("insert into insurance (acc_no, loan_am, date_issued, `ins_type`, `amount left`, `installment remaining`) " +         //"values(" + getAcc() + ", " + obj.getPrincipal_amount() +
            //", '" + obj.getTime() + "', '" + type + "', " + amount_left + ", '" + obj.getYear() * 12 + "');
        }
    }
}
