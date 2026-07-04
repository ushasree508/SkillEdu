package skill;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USER = "sasi";
    private static final String DB_PASS = "sasi123";

    private static final String INSERT_SQL =
        "INSERT INTO USERS (FULL_NAME, USERNAME, EMAIL, PASSWORD_HASH) VALUES (?, ?, ?, ?)";

    // ✅ Load driver ONCE
    @Override
    public void init() throws ServletException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new ServletException("Oracle JDBC Driver not found", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName = request.getParameter("fullname");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        // ✅ SERVER-SIDE VALIDATION (IMPORTANT)
        if (fullName == null || username == null || email == null ||
            password == null || confirmPassword == null ||
            fullName.isEmpty() || username.isEmpty() ||
            email.isEmpty() || password.isEmpty()) {

            request.setAttribute("error", "All fields are required");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, fullName);
            ps.setString(2, username);
            ps.setString(3, email);
            ps.setString(4, password); // (hash later)
            System.out.println("AutoCommit = " + conn.getAutoCommit());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "Registration failed");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // UNIQUE constraint
                request.setAttribute("error",
                        "Username or Email already exists");
            } else {
                request.setAttribute("error",
                        "Database error: " + e.getMessage());
            }
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
}
