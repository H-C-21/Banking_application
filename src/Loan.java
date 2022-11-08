import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static java.lang.Math.pow;

interface Loan_help {
    double interest_calculate();

    void installment_pay(double amount, int acc, int loan_id);

    void initialize_new_loan(String type, double amount_left, Loan obj);
}

public class Loan implements Time {
    private double principal_amount;
    private final String dateIssued = getTime();
    private double roi;
    private String type_of_loan;
    private int year;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType_of_loan() {
        return type_of_loan;
    }

    public void setType_of_loan(String type_of_loan) {
        this.type_of_loan = type_of_loan;
    }

    public double getPrincipal_amount() {
        return principal_amount;
    }

    public void setPrincipal_amount(double principal_amount) {
        this.principal_amount = principal_amount;
    }

    public double getRoi() {
        return roi;
    }

    public void setRoi(double roi) {
        this.roi = roi;
    }

    public String getDateIssued() {
        return dateIssued;
    }

}

abstract class Loan_class implements Loan_help {
    private Statement stmt;

    private int acc;

    private Loan object;

    public Loan getObject() {
        return object;
    }

    public void setObject(Loan object) {
        this.object = object;
    }

    public int getAcc() {
        return acc;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }

    public Loan_class(Statement stmt, int acc) {
        this.stmt = stmt;
        this.acc = acc;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }


    @Override
    public double interest_calculate() {
        double a = object.getPrincipal_amount() * pow((1 + (object.getRoi() / 100.00)), object.getYear());
        return a - object.getPrincipal_amount();
    }

    @Override
    public void installment_pay(double amount, int acc, int loan_id) {
        try {
            Withdraw_Deposit obj = new Withdraw_Deposit(stmt, String.valueOf(acc));
            if (!obj.withdraw(amount, "BANK(FOR LOAN)")){
                System.out.println("Insufficient balance. Try later!");
            }
            getStmt().execute("update loans " +
                    "set `amount left` = `amount left` - '" + amount + "'" +
                    "where loan_id = '" + loan_id + "';");
            getStmt().execute("update loans " +
                    "set `installment remaining` = `installment remaining` - 1 " +
                    "where loan_id = '" + loan_id + "';");
            System.out.println("Loan installment paid successfully.");
            ResultSet rst = stmt.executeQuery("select `amount left`, `installment remaining` from loans where loan_id = '" + loan_id + "'");
            rst.next();
            System.out.println("Remaining amount to be paid:" + rst.getDouble(1) + "\nInstallments remaining:" + rst.getInt(2));
            if (rst.getInt(2) == 0) {
                System.out.println("Loan amount completed. Thank You for paying us timely!!");
                getStmt().execute("delete from loans where `installment remaining` = 0;");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
