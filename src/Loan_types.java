import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Loan_types extends Loan_class {
    public Loan_types(Statement stmt, int acc) {
        super(stmt, acc);
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

    @Override
    public void loan_approve(double ROI, String type) {
        Loan obj = loan_input(ROI);
        setObject(obj);
        double a = interest_calculate();
        System.out.println("Your interest will be:" + a);
        double amount_left = obj.getPrincipal_amount() + a;
        System.out.println("You have to pay an amount of " + amount_left / (obj.getYear() * 12) + " monthly.");
        initialize_new_loan(type, amount_left, obj);
    }

    public void Loan_take() {
        print();
        Scanner sc = new Scanner(System.in);
        String key = sc.nextLine();
        if (Objects.equals(key, "1")) {
            loan_approve(8, "HOME");
        } else if (Objects.equals(key, "2")) {
            loan_approve(6.5, "EDUCATION");
        } else if (Objects.equals(key, "3")) {
            loan_approve(8.5, "PERSONAL");
        } else if (Objects.equals(key, "4")) {
            loan_approve(7, "CAR");
        }
    }

}
