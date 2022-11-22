import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public abstract class InsuranceUtil extends ConsoleColors implements Date_Time {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";


    static Scanner sc = new Scanner(System.in);

    protected static Insurance getScheme(Statement stmt,int ins_id) throws SQLException {

        ResultSet rst = stmt.executeQuery("Select ins_type from insurance where ins_id = " + ins_id + ";");
        int count = 0;
        while (rst.next()) {
            if (Objects.equals(rst.getString(1), "Health")) {
                return new Health(ins_id,stmt);
            } else if (Objects.equals(rst.getString(1), "Life")) {
                return new Life(ins_id,stmt);
            } else if (Objects.equals(rst.getString(1), "Car")) {
                return new Car(ins_id,stmt);
            } else if (Objects.equals(rst.getString(1), "Home")) {
                return new Home(ins_id,stmt);
            }
        }
        System.out.println("Invalid ID");
        return null;
    }

    protected static int getSchemeRate(String s) throws SQLException {
        if(s.equals("Health")){
            return 10;
        }
        else if (s.equals("Life")){
            return 15;
        }
        else if (s.equals("Car")){
            return 5;
        }
        else if (s.equals("Home")){
            return 5;
        }
        else
            return 0;
    }


    public static void display(Statement stmt,String acc) throws SQLException {

        int counti = 0;
        try {
            ResultSet rst1 = stmt.executeQuery("select ins_id, ins_type, date_issued,amount_dep " + "from insurance where acc_no = "
                    + acc + " AND status = 1;");
            System.out.println("Insurance ID         Insurance Type           Date Issued              Insured For");


            while (rst1.next()) {

                int id = rst1.getInt("ins_id");
                String type = rst1.getString("ins_type");
                Date iss = rst1.getDate("date_issued");
                int amt = rst1.getInt("amount_dep");

                String today = Date_Time.getDate();
                int n = Date_Time.getDateDiff(String.valueOf(iss));

               int rate1 = getSchemeRate(type);
                int diff = 0;
                try {
                    diff = (int) ((amt - (n * rate1)) / rate1);
                } catch (NumberFormatException z) {
                    System.out.println(z);
                }
                if(diff>0) {
                    String t = GREEN+Date_Time.getDateMonYear(diff)+ANSI_RESET;
                    System.out.printf("    " + "%-20d %-20s %-20s %-20s\n", id, type, iss, t);
                }
                else {

                    String t1 = Date_Time.getDateMonYear(diff*(-1));
                    String t2 = (RED + "Behind by "+ t1 + ANSI_RESET);
                    System.out.printf("    " + "%-20d %-20s %-20s %-20s\n", id, type, iss, t2);
                }
                ++counti;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (counti == 0){
            System.out.println();
            System.out.println("You are Currently Not registered under any Insurance Schemes, Press 1 to opt");
        }

    }
     abstract boolean authenticateAllInsurance(String acc);

    protected static boolean authenticateInsurance(Statement stmt,String str,int ins_id) throws SQLException {
        try {
            ResultSet rs = stmt.executeQuery("Select * from insurance where ins_id = "+ins_id+" AND acc_no = "+str+" AND status = 1;");
            while(rs.next()){
                return true;
            };
            System.out.println("Invalid Insurance ID");
            return false;

        }catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }


    public static void newInsurance(Statement  stmt, String acc){

        boolean running = true;
        int a = Integer.parseInt(acc);
        while(running) {
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
                        ins1.newImp(stmt, acc);
                    }
                    break;

                case "2":
                    Life ins2 = new Life();
                    ins2.print();
                    String h2 = sc.nextLine();

                    if (Objects.equals(h2, "1")) {
                        ins2.newImp(stmt, acc);
                    }
                    break;

                case "3":
                    Car ins3 = new Car();
                    ins3.print();
                    String h3 = sc.nextLine();

                    if (Objects.equals(h3, "1")) {
                        ins3.newImp(stmt, acc);
                    }
                    break;
                case "4":
                    Home ins4 = new Home();
                    ins4.print();
                    String h4 = sc.nextLine();

                    if (Objects.equals(h4, "1")) {
                        ins4.newImp(stmt, acc);
                    }
                    break;

                case "5":
                    running = false;
                    break;

                default:
                    System.out.println("Wrong Key, please enter Correctly!!!");
                    break;
            }}}

    public static void claimMain(Statement stmt,String acc) throws SQLException {
        System.out.println("Please Enter the Insurance ID of the Insurance you wish to claim");
        int id = sc.nextInt();
        if(authenticateInsurance(stmt,acc,id)){
        Insurance cl = getScheme(stmt,id);
        cl.claimInsurance(stmt,acc);
    }
        else
            System.out.println("Please Enter an Active Insurance ID");

    }

    public static void depositMain(Statement stmt,String acc) throws SQLException {

        System.out.println("Please Enter the Insurance ID of the Insurance you wish to pay the premiums for");
        int id = sc.nextInt();

        if(authenticateInsurance(stmt,acc,id)){
        Insurance n = getScheme(stmt,id);
        n.depositPremium(stmt,acc);
    }
        else
            System.out.println("Please Enter a Valid Insurance ID");
    }




}

//stmt.execute("insert into insurance (acc_no, loan_am, date_issued, `ins_type`, `amount left`, `installment remaining`) " +         //"values(" + getAcc() + ", " + obj.getPrincipal_amount() +
//", '" + obj.getTime() + "', '" + type + "', " + amount_left + ", '" + obj.getYear() * 12 + "');


