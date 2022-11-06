import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

interface login{
    void input();
    boolean checkAuthentication() throws SQLException;
}
