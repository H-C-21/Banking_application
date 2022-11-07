import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Loan_types extends Loan_class {

    public Loan_types(Statement stmt, int acc) {
        super(stmt, acc);
    }

    @Override
    public void initialize_new_loan(String type, double amount_left, Loan obj) {
        try {
            getStmt().execute("insert into loans (acc_no, loan_amount, date_issued, `loan type`, `amount left`, `installment remaining`) " +
                    "values(" + getAcc() + ", " + obj.getPrincipal_amount() +
                    ", '" + obj.getTime() + "', '" + type + "', " + amount_left + ", '" + obj.getYear()*12 + "');");
            Withdraw_Deposit dep = new Withdraw_Deposit(getStmt(), String.valueOf(getAcc()));
            dep.deposit(obj.getPrincipal_amount(), "LOAN AMOUNT");
            System.out.println("Loan approved successfully. Amount deposited in your bank.");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public static void print() {
        System.out.println("""
                Press 1 to take Home loan(ROI = 8%)
                Press 2 to take Education Loan(ROI = 6.5%)
                Press 3 to take Personal Loan(ROI 8.5%)
                Press 4 to take Car Loan(ROI 7%)
                Press anything else for exit""");
    }

    public static Loan loan_input(double ROI) {
        Scanner sc = new Scanner(System.in);
        Loan obj = new Loan();
        obj.setRoi(ROI);
        System.out.print("Enter the amount you want to take loan:");
        while (true) {
            try {
                obj.setPrincipal_amount(sc.nextDouble());
                break;
            } catch (Exception e) {
                System.out.println("Enter numeric values only.");
                sc.nextLine();
            }
        }
        System.out.print("Enter the amount of time in years you want to take loan:");
        while (true) {
            try {
                obj.setYear(sc.nextInt());
                break;
            } catch (Exception e) {
                System.out.println("Enter numeric values only.");
                sc.nextLine();
            }
        }
        return obj;
    }

    public void Loan_take() {
        print();
        Scanner sc = new Scanner(System.in);
        String key = sc.nextLine();
        if (Objects.equals(key, "1")) {
            Loan home = loan_input(8);
            setObject(home);
            System.out.println("Your interest will be:" + interest_calculate());
            double amount_left = home.getPrincipal_amount() + interest_calculate();
            System.out.println("You have to pay an amount of " + amount_left / (home.getYear() * 12) + " monthly.");
            initialize_new_loan("HOME", amount_left, home);
        } else if (Objects.equals(key, "2")) {
            Loan education = loan_input(6.5);
            setObject(education);
            System.out.println("Your interest will be:" + interest_calculate());
            double amount_left = education.getPrincipal_amount() + interest_calculate();
            System.out.println("You have to pay an amount of " + amount_left / (education.getYear() * 12) + " monthly.");
            initialize_new_loan("EDUCATION", amount_left, education);
        } else if (Objects.equals(key, "3")) {
            Loan personal = loan_input(8);
            setObject(personal);
            System.out.println("Your interest will be:" + interest_calculate());
            double amount_left = personal.getPrincipal_amount() + interest_calculate();
            System.out.println("You have to pay an amount of " + amount_left / (personal.getYear() * 12) + " monthly.");
            initialize_new_loan("PERSONAL", amount_left, personal);
        } else if (Objects.equals(key, "4")) {
            Loan car = loan_input(7);
            setObject(car);
            System.out.println("Your interest will be:" + interest_calculate());
            double amount_left = car.getPrincipal_amount() + interest_calculate();
            System.out.println("You have to pay an amount of " + amount_left / (car.getYear() * 12) + " monthly.");
            initialize_new_loan("CAR", amount_left, car);
        }
    }

}
