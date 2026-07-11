import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class Jdbcdemo {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/enterprise_db";
    private static final String USER = "java_user";
    private static final String PASSWORD = "Password123!";

    public static void main(String[] args) {
        // Using try-with-resources to automatically close connections
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("✅ Connected to the database successfully!");

            // 1. CREATE Table
            createTable(conn);

            // 2. INSERT a Record
            insertEmployee(conn, "Alice Smith", "Engineering", 85000.00);
            insertEmployee(conn, "Bob Jones", "Marketing", 60000.00);

            // 3. READ (Display) Records
            System.out.println("\n--- Initial Employee List ---");
            displayEmployees(conn);

            // 4. UPDATE a Record
            updateEmployeeSalary(conn, "Alice Smith", 90000.00);

            // Display after update
            System.out.println("\n--- After Salary Update ---");
            displayEmployees(conn);

            // 5. DELETE a Record
            deleteEmployee(conn, "Bob Jones");

            // Final Display
            System.out.println("\n--- Final Employee List ---");
            displayEmployees(conn);

        } catch (SQLException e) {
            System.err.println("❌ Database error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- DATABASE OPERATIONS ---

    private static void createTable(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "name VARCHAR(100) NOT NULL, " +
                     "department VARCHAR(50), " +
                     "salary DECIMAL(10,2))";
                     
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("ℹ️ Table 'employees' verified/created.");
        }
    }

    private static void insertEmployee(Connection conn, String name, String dept, double salary) throws SQLException {
        // Using PreparedStatement to prevent SQL Injection
        String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, dept);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            System.out.println("➕ Inserted employee: " + name);
        }
    }

    private static void displayEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employees";
        
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            System.out.printf("%-5s %-20s %-15s %-10s\n", "ID", "Name", "Department", "Salary");
            System.out.println("---------------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String dept = rs.getString("department");
                double salary = rs.getDouble("salary");
                System.out.printf("%-5d %-20s %-15s %-10.2f\n", id, name, dept, salary);
            }
        }
    }

    private static void updateEmployeeSalary(Connection conn, String name, double newSalary) throws SQLException {
        String sql = "UPDATE employees SET salary = ? WHERE name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newSalary);
            pstmt.setString(2, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("🆙 Updated salary for: " + name);
            }
        }
    }

    private static void deleteEmployee(Connection conn, String name) throws SQLException {
        String sql = "DELETE FROM employees WHERE name = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("❌ Deleted employee: " + name);
            }
        }
    }
}
