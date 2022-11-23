import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static java.lang.Math.pow;
import static java.lang.System.exit;

public class cmdArguments implements Date_Time {
    String[] args;
    Statement stmt = Connection_establish.dbms_connect();

    public cmdArguments(String[] args) throws SQLException, ClassNotFoundException {
        this.args = args;
    }

    boolean admin_login(String pass) throws SQLException {
        ResultSet rs = stmt.executeQuery("select password from administrators where s_no = 1;");
        while (rs.next()) {
            if (Objects.equals((pass), rs.getString(1))) {
                return true;
            }
        }
        return false;
    }

    boolean user_login(int acc, String pass) throws SQLException {
        ResultSet rs = stmt.executeQuery("select * from account where acc_no = '" + acc + "' AND password = '" + pass + "'");
        int counter = 0;
        while (rs.next()) {
            counter++;
        }
        return counter != 0;
    }

    void cmdRun() throws SQLException, ClassNotFoundException {
        if (Objects.equals(args[0], "--add")) {
            Account_Main obj = new Account_Main(stmt);
            obj.create();
            exit(0);
        }

        //LOAN FD CMD

        else if (Objects.equals(args[0], "--u") && Objects.equals(args[3], "loanpay")) {
            try {
                int acc = Integer.parseInt(args[1]);
                int loan_id = Integer.parseInt(args[4]);
                if (user_login(acc, args[2])) {
                    ResultSet rst = stmt.executeQuery("select `amount left`, `installment remaining` from loans where loan_id = " + loan_id + ";");
                    rst.next();
                    double amount = rst.getDouble(1) / rst.getInt(2);
                    Loan_helper obj = new Loan_helper(stmt, acc);
                    obj.installment_pay(amount, acc, loan_id);
                } else {
                    System.out.println("Incorrect password!");
                }
            } catch (Exception e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
            exit(0);
        } else if (Objects.equals(args[0], "--u") && Objects.equals(args[3], "loanview")) {
            try {
                int acc = Integer.parseInt(args[1]);
                if (user_login(acc, args[2])) {
                    Loan_main.display(stmt, acc);
                } else {
                    System.out.println("Incorrect password!");
                }
            } catch (Exception e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        } else if (Objects.equals(args[0], "--a") && Objects.equals(args[2], "loantype")) {
            try {
                if (admin_login(args[1])) {
                    ResultSet rst = stmt.executeQuery("select * from loans where `loan type` = '" + args[3] + "';");
                    while (rst.next()) {
                        System.out.printf("Acc No: %-10s Loan ID:%-10d Loan Amount:%-15f Date Issued:%-20s Amount Left:%-15f Installments Remaining:%-5d\n",
                                rst.getInt(2), rst.getInt(1), rst.getDouble(3), rst.getDate(4),
                                rst.getDouble(6), rst.getInt(7));
                    }
                } else {
                    System.out.println("Incorrect password!");
                }
            } catch (Exception e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
            exit(0);
        } else if (Objects.equals(args[0], "--a") && Objects.equals(args[2], "fdpay")) {
            try {
                if (admin_login(args[1])) {
                    Admin_page.view_fd(stmt);
                    exit(0);
                } else {
                    System.out.println("Incorrect password!");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
            exit(0);
        } else if (Objects.equals(args[0], "--a") && Objects.equals(args[2], "deleteloan")) {
            try {
                if (admin_login(args[1])) {
                    stmt.execute("delete from loans where loan_id = '" + args[3] + "';");
                    ResultSet rst = stmt.executeQuery("select * from loans;");
                    System.out.println("Loan deleted. Pending loans are:");
                    while (rst.next()) {
                        System.out.printf("Acc No: %-10s Loan ID:%-10d Loan Amount:%-15f Date Issued:%-20s Loan Type:%-10s Amount Left:%-15f Installments Remaining:%-5d\n",
                                rst.getInt(2), rst.getInt(1), rst.getDouble(3), rst.getDate(4), rst.getString(5),
                                rst.getDouble(6), rst.getInt(7));
                    }
                } else {
                    System.out.println("Incorrect password!");
                }
            } catch (Exception e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        } else if (Objects.equals(args[0], "--u") && Objects.equals(args[3], "addfd")) {
            try {
                int acc = Integer.parseInt(args[1]);
                double a = Double.parseDouble(args[4]);
                int year = Integer.parseInt(args[5]);
                if (user_login(acc, args[2])) {
                    double ROI = 5 + year / 10.00;
                    double amount_left = a * pow((1 + (ROI / 100.00)), year);
                    Withdraw_Deposit obj = new Withdraw_Deposit(stmt, String.valueOf(acc));
                    if (!obj.withdraw(a, "FD AMOUNT")) {
                        System.out.println("Insufficient balance for Fixed Deposit!!");
                        return;
                    }
                    stmt.execute("insert into fixed_deposits (acc_no, amount, date_issued, installment_remaining, amount_remaining) " +
                            "values(" + acc + ", " + a +
                            ", '" + obj.getDate_Time() + "', " + year * 12 + ", '" + amount_left + "');");
                    System.out.println("FD amount successfully withdrawn from your bank account.");
                }
            } catch (Exception e) {
                System.out.println("Illegal argument or argument does not match!!");
            }
        }

        // QUERY FEEDBACK CMD

        else if (Objects.equals(args[0], "--querybystatus") && Objects.equals(args[1], "resolved")) {
            ResultSet rst = stmt.executeQuery("select * from Query where Status = 'Resolved';");
            Admin_page.display_resultSet(rst, "Solved");
        } else if (Objects.equals(args[0], "--querybystatus") && Objects.equals(args[1], "unresolved")) {
            ResultSet rst = stmt.executeQuery("select * from Query where Status = 'Unresolved';");
            Admin_page.display_resultSet(rst, "Unresolved");
        } else if (Objects.equals(args[0], "--querysolve")) {
            try {
                stmt.execute("update Query set Status = 'Resolved' where `Query ID` = '" + args[1] + "';");
                System.out.println("Query resolved successfully.");
            } catch (Exception e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        } else if (Objects.equals(args[0], "--querydisplay")) {
            try {
                ResultSet rst = stmt.executeQuery("select * from Query where `Query ID` = '" + args[1] + "';");
                rst.next();
                System.out.printf("Problem:%-100s Status:%-20s Date Submitted:%-10s\n",
                        rst.getString(5), rst.getString(4), rst.getDate(6));
            } catch (Exception e) {
                System.out.println("Illegal argument or argument does not match!!");
            }
        } else if (Objects.equals(args[0], "--querydelete")) {
            try {
                stmt.execute("delete from Query where `Query ID` ='" + args[1] + "';");
                ResultSet rst = stmt.executeQuery("select * from Query;");
                while (rst.next()) {
                    System.out.printf("Query ID:%-10d Problem:%-100s Date Submitted:%-10s\n",
                            rst.getInt(1), rst.getString(5), rst.getDate(6));
                }
            } catch (Exception e) {
                System.out.println("Illegal argument or argument does not match!!");
            }
        } else if (Objects.equals(args[0], "--viewfeedback")) {
            Feedback_main obj = new Feedback_main(stmt);
            obj.list_all();
        } else if (Objects.equals(args[0], "--addfeedback")) {
            try {
                stmt.execute("insert into Feedback values ('" + args[1] + "', '" + args[2] + "', '" +
                        "" + args[3] + "',  '" + args[4] + "',  '" + getDate_Time() + "');");
                System.out.println("Feedback submitted successfully. Thanks for giving us your valuable feedback.");
            } catch (Exception e) {
                System.out.println("Illegal argument or argument does not match!!");
            }
        }

        else if(Objects.equals(args[0], "--u") && Objects.equals(args[3], "viewinsurance")){
            try {
                int acc = Integer.parseInt(args[1]);
                if (user_login(acc, args[2])) {
                    InsuranceUtil.display(stmt,args[1]);
                }

        } catch (SQLException e) {
                throw new RuntimeException(e);
            }}

        else if(Objects.equals(args[0], "--u") && Objects.equals(args[3], "newinsurance")){
            int acc = Integer.parseInt(args[1]);
            if (user_login(acc, args[2])) {
                Insurance.newInsurance(stmt,args[1]);
            }
        }

        else if(Objects.equals(args[0], "--u") && Objects.equals(args[3],"claiminsurance")){
            int acc = Integer.parseInt(args[1]);
            if (user_login(acc, args[2])) {
                InsuranceUtil.claimMain(stmt,args[1]);
            }
        }

        else if(Objects.equals(args[0], "--u") && Objects.equals(args[3],"depositinsurance")){
            int acc = Integer.parseInt(args[1]);
            if(user_login(acc, args[2])){
                InsuranceUtil.depositMain(stmt,args[1]);
            }
        }
        else if(Objects.equals(args[0], "--u") && Objects.equals(args[3],"searchinsname")){
            int acc = Integer.parseInt(args[1]);     //--u account_no password searchinsname "searchterm"
            if(user_login(acc, args[2])){
                Insurance b = new Insurance(stmt);
                b.getUserSchemes(args[4],args[1]);
            }
        }

        else if (Objects.equals(args[0], "--u") && Objects.equals(args[3], "searchinsid")){
            int acc = Integer.parseInt(args[1]);
            if (user_login(acc, args[2])) {
                try {
                    String id = args[4];
                    int in_id = Integer.parseInt(id);

                    Insurance a = InsuranceUtil.getScheme(stmt, in_id);
                    if(a != null){
                        if(a.authenticateAllInsurance(args[1])){
                            System.out.println(a);
                        }}
                    else {
                        System.out.println();
                    }} catch(NumberFormatException a){
                    System.out.println("Please Enter a Correct ID");
                }}
            }

         else if (Objects.equals(args[0],"--")){

            System.out.println("Illegal argument or arguments does not match!!");
        }
    }
}
