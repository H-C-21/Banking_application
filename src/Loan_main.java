import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.Scanner;

public class Loan_main {

    public static int display(Statement stmt, int acc) {
        int count = 0;
        try {
            ResultSet rst = stmt.executeQuery("select loan_id, loan_amount, date_issued, `loan type`, `amount left`, `installment remaining` " +
                    "from loans where acc_no = '" + acc + "';");
            while (rst.next()) {
                System.out.printf("Loan ID:%-10d Loan Amount:%-15f Date Issued:%-20s Loan Type: %-10s Amount Left:%-15f Installments Remaining:%-5d\n",
                        rst.getInt(1), rst.getDouble(2), rst.getDate(3), rst.getString(4),
                        rst.getDouble(5), rst.getInt(6));
                count++;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return count;
    }

    public static void loan_main(Statement stmt, int acc) {
        while (true) {
            System.out.println("""
                    Enter 1 to view your loans
                    Enter 2 to pay your installment
                    Enter 3 to take a loan
                    Enter anything else to exit""");
            Scanner sc = new Scanner(System.in);
            String key = sc.nextLine();
            if (Objects.equals(key, "1")) {
                if (display(stmt, acc) == 0) {
                    System.out.println("You do not have any active loan currently.");
                }
            } else if (Objects.equals(key, "2")) {
                System.out.println("Fetching your loans....");
                if (display(stmt, acc) == 0) {
                    System.out.println("You do not have any active loan currently");
                    continue;
                }
                System.out.print("Enter corresponding loan id for which to pay installment:");
                int id;
                while (true) {
                    try {
                        id = sc.nextInt();
                        break;
                    } catch (Exception e) {
                        System.out.println("Enter numeric values only.");
                        sc.nextLine();
                    }
                }
                try {
                    ResultSet rst = stmt.executeQuery("select `amount left`, `installment remaining` from loans where loan_id = " + id + ";");
                    rst.next();
                    double amount = rst.getDouble(1) / rst.getInt(2);
                    Loan_helper obj = new Loan_helper(stmt, acc);
                    obj.installment_pay(amount, acc, id);
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else if (Objects.equals(key, "3")) {
                Loan_helper obj = new Loan_helper(stmt, acc);
                obj.Loan_take();
            } else {
                break;
            }
        }
    }
}
