import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Insurance extends InsuranceUtil implements Date_Time {
    static int count = 1;
    protected int ins_id;
    static protected int rate;
    static protected int Claim;
    protected Statement stmt;
    public String scheme = getClass().getSimpleName();     //For Telling what type of insurance this is by name (Health,life etc...)

    Insurance() {
    }
    Insurance(int ins_id) {
        ins_id = this.ins_id;
    }
    Insurance(int ins_id,Statement stmt){
        this.ins_id = ins_id;
        this.stmt = stmt;
    }

    public  int getRate() {
        return rate;
    }

    public int getClaim() {
        return Claim;
    }

    protected int getIns_id() {
        return this.ins_id;
    }

    public double balance(int acc, Statement stmt) throws SQLException {      //YEh main mein implment krdena ???
        ResultSet rst1 = stmt.executeQuery("select balance from account where acc_no = " + acc + ";");
        return rst1.getDouble("balance");
    }

    protected int print() {

        System.out.println("You Have Selected " + scheme + " Insurance");
        System.out.println("Daily Rate ---->  " + this.getRate() + "    " + "  Claim  ---->  " + this.getClaim());

        System.out.println("Press 1 to Proceed to Confirm");
        System.out.println("Press Any Other Key to Go Back");

        return this.getClaim();
    }

    public String toString(){
        try {
            ResultSet rst = stmt.executeQuery("Select * from insurance where ins_id = " + this.getIns_id());
            rst.next();

            int id = rst.getInt("ins_id");
            String type = rst.getString("ins_type");
            Date iss = rst.getDate("date_issued");
            int amt = rst.getInt("amount_dep");
            boolean status = rst.getBoolean("status");
            String stat;
            if(!status){
                stat = RED+"NOT ACTIVE"+ANSI_RESET;
            }
            else
                stat = GREEN+"ACTIVE"+ANSI_RESET;

            String today = Date_Time.getDate();
            int n = Date_Time.getDateDiff(String.valueOf(iss));

            System.out.println("You Have Selected the "+type+" Scheme with Insurance ID -> "+id+".");
            System.out.println("Insurance ID         Insurance Type           Date Issued            Insured For             Status");

            int rate1 = getSchemeRate(type);
            int diff = 0;
            try {
                diff = (int) ((amt - (n * rate1)) / rate1);
            } catch (NumberFormatException z) {
                System.out.println(z);
            }
            if(diff>0) {
                String t = Date_Time.getDateMonYear(diff);
                System.out.printf("    " + "%-20d %-20s %-20s %-20s %-20s\n", id, type, iss, t,stat);
            }
            else {

                String t1 = Date_Time.getDateMonYear(diff*(-1));
                String t2 = (ANSI_YELLOW + "Behind by "+ t1 + ANSI_RESET);
                System.out.printf("    " + "%-20d %-20s %-20s %-20s %-20s\n", id, type, iss, t2,stat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "";
    }

    boolean authenticateAllInsurance(String acc){
        try {
            ResultSet rs = stmt.executeQuery("Select * from insurance where ins_id = "+ins_id+" AND acc_no = "+acc+ ";");
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

    public void newImp(Statement stmt, String acc) {
        try {

            ResultSet cnt = stmt.executeQuery("select max(ins_id) as k from insurance");

            double count2 = 1;
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
                int l = (int) (count2 + count2 / 110);

                String date1 = Date_Time.getDate();
                stmt.execute("insert into insurance (acc_no, ins_type, ins_id, date_issued) " +
                        "values(" + account + ", '" + scheme +
                        "', " + l + ", '" + date1 + "');");

                System.out.println("You Have Successfully Opted for " + scheme + " Insurance, Your Insurance ID is " + l);
                System.out.println("The Default Starting amount of 500 has been deducted from your Account, this will go towards future premium payments");
                System.out.println();
            } else
                System.out.println("You Need a Minimum Balance of 500 in your account to Purchase the Opted Insurance");
        } catch (SQLException e) {
            System.out.println(e);

        }
    }

    public void claimInsurance(Statement stmt, String str) throws SQLException {
        try {

            if (authenticateInsurance(stmt, str, getIns_id())) {
                //Insurance claim = getScheme(stmt, ins_id);
                Withdraw_Deposit n = new Withdraw_Deposit(stmt, str);
                if (n.deposit(this.getClaim(), "INSURANCE CLAIM -> ID:"+getIns_id())) {

                    stmt.execute("update insurance set status = 0 where acc_no =" + str + " AND ins_id = " + ins_id + ";");

                    System.out.println("Your Insurance Claim amounting to " + getClaim() + " for the scheme No." + this.getIns_id() + " has been transferred to your Account,");
                    System.out.println("The Claimed Insurance Scheme with the Insurance is Henceforth no longer active");
                    System.out.println("Thank You for using our services");
                }
            } else
                System.out.println("Failed To Claim Insurance Please Enter Correct ID");
        } catch (SQLException b) {
            System.out.println(b);
        }
    }

    public boolean depositPremium(Statement stmt, String str) throws SQLException {
        System.out.println("Enter The Amount You Wish to Deposit");
        double dep = 0;
        try {
            dep = sc.nextDouble();
        } catch (Exception IOException) {
            System.out.println();
        }  //MAKE SURE DEP IS POSITIVE ALWAYS


        Withdraw_Deposit n5 = new Withdraw_Deposit(stmt, str);
        if (dep > 0) {
            if (n5.withdraw(dep, "INSURANCE PREMIUM -> ID:"+getIns_id())) {
                try {
                    //stmt.execute("update insurance SET amount_dep = 1000 where acc_no = "+str+" AND ins_id = " + ins_id + " ;");
                    stmt.execute("update insurance set amount_dep = amount_dep + " + dep + " where acc_no ="
                            + str + " AND ins_id = " + ins_id + " ;");

                    System.out.println("Transaction Successful, Rs."+ dep + " Have been Deducted from your account");
                    return true;
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else
                System.out.println("Please Ensure You Have Sufficient Balance in Your Account");
            return false;
        }
        System.out.println("Please Enter a Valid Value");
        return false;
    }
}
class Health extends Insurance{
  Health(){}
  Health(int ins_id){
      this.ins_id = ins_id;
  }

  Health(int ins_id,Statement stmt){
        this.ins_id = ins_id;
        this.stmt = stmt;
    }

  static {
        rate = 10;
        Claim = 250000;
    }
}

class Life extends Insurance{

    Life(){}
    Life(int ins_id) {
        this.ins_id = ins_id;
    }
    Life(int ins_id,Statement stmt){
        this.ins_id = ins_id;
        this.stmt = stmt;
    }

    static{
        rate = 15;
        Claim = 500000;
    }
}

class Car extends Insurance{

    Car(){}
    Car(int ins_id){
        this.ins_id = ins_id;
    }
   Car(int ins_id,Statement stmt){
        this.ins_id = ins_id;
        this.stmt = stmt;
    }

    static{
        rate = 5;
        Claim = 150000;
    }
}

class Home extends Insurance{

    Home(){}
    Home(int ins_id){
        this.ins_id = ins_id;
    }
    Home(int ins_id,Statement stmt){
        this.ins_id = ins_id;
        this.stmt = stmt;
    }
    static{
        rate = 5;
        Claim = 350000;
    }
}



