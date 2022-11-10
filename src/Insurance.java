import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Insurance implements Date_Time {
    static int count = 1;
    private String name;
    static protected double rate;
    static protected int Claim;
    public String scheme = getClass().getSimpleName();     //For Telling what type of insurance this is by name (Health,life etc...)

    public double getRate() {
        return rate;
    }

    public int getClaim() {
        return Claim;
    }

    protected void setClaim(int claim) {
        Claim = claim;
    }

    public double balance(int acc, Statement stmt) throws SQLException {      //YEh main mein implment krdena ???
        ResultSet rst1 = stmt.executeQuery("select balance from account where acc_no = " + acc + ";");
        return rst1.getDouble("balance");
    }

    protected void print() {

        System.out.println("You Have Selected " + scheme + " Insurance");
        System.out.println("Daily Rate ----  " + this.getRate() + "    " + "  Claim  ----  Inr" + this.getClaim());

        System.out.println("Press 1 to Proceed to Confirm");
        System.out.println("Press Any Other Key to Go Back");
    }

    protected void newInsurance(Statement stmt, String acc) {
        try {

            ResultSet cnt = stmt.executeQuery("select max(ins_id) as k from insurance");

            double count2 = 0;
            while (cnt.next()) {
                double count1 = cnt.getDouble(1);
                count2 = count1;
            }

            int account = Integer.parseInt(acc);
            Withdraw_Deposit n = new Withdraw_Deposit(stmt, acc);
            if (n.withdraw(500, "INSURANCE REGISTRATION")) {

           /*
              stmt.execute("insert into insurance (acc_no,ins_type,ins_id,date_issued)"  +      //WTF
              "VALUES(" + account + ",'" + scheme + "',4,'"+ date + "')");
            */

                String date1 = getDate();
                stmt.execute("insert into insurance (acc_no, ins_type, ins_id, date_issued) " +
                        "values(" + account + ", '" + scheme +
                        "', " + ((count2 + count2 / 110) + 101) + ", '" + date1 + "');");

                int l = (int) ((count2 + count2 / 110) + 101);
                System.out.println("You Have Successfully Opted for " + scheme + " Insurance, Your Insurance ID is " + l);
                System.out.println("Default Starting amount of 500 has been deducted from your Account, this will go towards future premium payments");
                System.out.println();
            } else
                System.out.println("You Need a Minimum Balance of 500 to Purchase the Opted Insurance");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static boolean ClaimInsurance(Statement stmt, String str, int ins_id) throws SQLException {
        try {


            stmt.execute("update account set balance = balance + '" + Claim + "' where acc_no =" + str + ";");
            stmt.execute("insert into transactions (acc_no, amount)" +
                    "select account.acc_no, '+" + Claim + "' from account where account.acc_no = '" + str + "' LIMIT 1");

            System.out.println("Your Insurance Claim has been transferred to your Account, Thank You for using our services");
            return true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        System.out.println("Claim Failed");
        return false;
    }
}


class Health extends Insurance {

    static {
        rate = 100;
        Claim = 200000;
    }
}

class Life extends Insurance {

    static {
        rate = 150;
        Claim = 300000;
    }
}

class Car extends Insurance {

    static {
        rate = 50;
        Claim = 150000;
    }
}

class Home extends Insurance {

    static {
        rate = 50;
        Claim = 200000;
    }
}

