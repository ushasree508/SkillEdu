package skill;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE";
    private static final String DB_USER = "sasi";
    private static final String DB_PASS = "sasi123";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("user");
        List<String> appliedInternships = new ArrayList<>();
        List<String> postedInternships = new ArrayList<>();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

                // Applied internships
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT internship_title FROM internship_applications WHERE username = ?")) {
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) appliedInternships.add(rs.getString("internship_title"));
                }

                // Posted internships
                try (PreparedStatement ps = conn.prepareStatement(
                        "SELECT position FROM internships WHERE posted_by = ?")) {
                    ps.setString(1, username);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) postedInternships.add(rs.getString("position"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }

        request.setAttribute("appliedInternships", appliedInternships);
        request.setAttribute("postedInternships", postedInternships);
        RequestDispatcher dispatcher = request.getRequestDispatcher("profile.jsp");
        dispatcher.forward(request, response);
    }
}