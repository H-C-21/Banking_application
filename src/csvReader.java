import java.io.*;
import java.sql.*;

public class csvReader {
    String csvFilePath;

    public csvReader(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    final String jdbcURL = "jdbc:mysql://localhost:3306/banking_database";
    final String username = "root";
    final String password = "Root@123";
    Connection connection = null;

    public void Transactions() {
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO transactions (acc_no, amount, date, transaction_to) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String acc_no = data[0];
                String amount = data[1];
                String date = data[2];
                String transaction_to = data[3];
                statement.setString(1, acc_no);
                statement.setString(2, amount);
                statement.setString(3, date);
                statement.setString(4, transaction_to);
                statement.addBatch();
                statement.executeBatch();
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("The data was added successfully.");
        } catch (IOException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
        } catch (SQLException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        }
    }

    public void Accounts() {
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO account (acc_no, customer_name, password, balance) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String acc_no = data[0];
                String customer_name = data[1];
                String password = data[2];
                String balance = data[3];
                statement.setString(1, acc_no);
                statement.setString(2, customer_name);
                statement.setString(3, password);
                statement.setString(4, balance);
                statement.addBatch();
                statement.executeBatch();
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("The data was added successfully.");
        } catch (IOException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
        } catch (SQLException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        }
    }

    public void FixedDeposits(){
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO fixed_deposits (`fd id`, acc_no, amount, date_issued, installment_remaining, amount_remaining) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String fd_id = data[0];
                String acc_no = data[1];
                String amount = data[2];
                String date_issued = data[3];
                String installment_remaining = data[4];
                String amount_remaining = data[5];
                statement.setString(1, fd_id);
                statement.setString(2, acc_no);
                statement.setString(3, amount);
                statement.setString(4, date_issued);
                statement.setString(5, installment_remaining);
                statement.setString(6, amount_remaining);
                statement.addBatch();
                statement.executeBatch();
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("The data was added successfully.");
        } catch (IOException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
        } catch (SQLException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        }
    }

    public void Feedback(){
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO Feedback (Name, email_id, text, rating, time) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String Name = data[0];
                String email_id = data[1];
                String text = data[2];
                String rating = data[3];
                String time = data[4];
                statement.setString(1, Name);
                statement.setString(2, email_id);
                statement.setString(3, text);
                statement.setString(4, rating);
                statement.setString(5, time);
                statement.addBatch();
                statement.executeBatch();
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("The data was added successfully.");
        } catch (IOException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
        } catch (SQLException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        }
    }

    public void Insurance(){
        try {
            connection = DriverManager.getConnection(jdbcURL, username, password);
            connection.setAutoCommit(false);
            String sql = "INSERT INTO insurance (acc_no, ins_type, ins_id, date_issued, Status, amount_dep) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            BufferedReader lineReader = new BufferedReader(new FileReader(csvFilePath));
            String lineText;
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");
                String acc_no = data[0];
                String ins_type = data[1];
                String ins_id = data[2];
                String date_issued = data[3];
                String Status = data[4];
                String amount_dep = data[5];
                statement.setString(1, acc_no);
                statement.setString(2, ins_type);
                statement.setString(3, ins_id);
                statement.setString(4, date_issued);
                statement.setString(5, Status);
                statement.setString(6, amount_dep);
                statement.addBatch();
                statement.executeBatch();
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("The data was added successfully.");
        } catch (IOException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
        } catch (SQLException ex) {
            System.out.println("Illegal argument or arguments does not match!!");
            try {
                assert connection != null;
                connection.rollback();
            } catch (SQLException e) {
                System.out.println("Illegal argument or arguments does not match!!");
            }
        }
    }
}