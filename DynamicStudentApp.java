import java.sql.*;
import java.util.Scanner;

public class DynamicStudentApp {

    static final String URL = "jdbc:mysql://localhost:3306/school";
    static final String USER = "srinu";
    static final String PASSWORD = "srinu123";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully");

            // a. Create Student Table
            String createTable = "CREATE TABLE IF NOT EXISTS student("
                    + "rollno INT PRIMARY KEY,"
                    + "name VARCHAR(50),"
                    + "address VARCHAR(100))";

            PreparedStatement pst = con.prepareStatement(createTable);
            pst.executeUpdate();

            System.out.println("Student Table Created");

            // c. Insert Two Records
            String insert = "INSERT INTO student VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(insert);

            for (int i = 1; i <= 2; i++) {

                System.out.println("\nEnter Student " + i + " Details");

                System.out.print("Roll No: ");
                int roll = sc.nextInt();
                sc.nextLine();

                System.out.print("Name: ");
                String name = sc.nextLine();

                System.out.print("Address: ");
                String address = sc.nextLine();

                ps.setInt(1, roll);
                ps.setString(2, name);
                ps.setString(3, address);

                ps.executeUpdate();
            }

            System.out.println("\nTwo Records Inserted");

            // b. Display Records
            String display = "SELECT * FROM student";
            PreparedStatement psDisplay = con.prepareStatement(display);

            ResultSet rs = psDisplay.executeQuery();

            System.out.println("\nStudent Records");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("rollno") + " "
                                + rs.getString("name") + " "
                                + rs.getString("address"));
            }

            // d. Update One Record
            String update = "UPDATE student SET address=? WHERE rollno=?";
            PreparedStatement psUpdate = con.prepareStatement(update);

            System.out.print("\nEnter Roll No to Update: ");
            int r = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter New Address: ");
            String newAddress = sc.nextLine();

            psUpdate.setString(1, newAddress);
            psUpdate.setInt(2, r);

            psUpdate.executeUpdate();

            System.out.println("Record Updated");

            // e. Delete One Record
            String delete = "DELETE FROM student WHERE rollno=?";
            PreparedStatement psDelete = con.prepareStatement(delete);

            System.out.print("\nEnter Roll No to Delete: ");
            int d = sc.nextInt();

            psDelete.setInt(1, d);

            psDelete.executeUpdate();

            System.out.println("Record Deleted");

            // f. Display Records Again
            rs = psDisplay.executeQuery();

            System.out.println("\nFinal Student Records");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("rollno") + " "
                                + rs.getString("name") + " "
                                + rs.getString("address"));
            }

            rs.close();
            pst.close();
            ps.close();
            psDisplay.close();
            psUpdate.close();
            psDelete.close();
            con.close();
            sc.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
