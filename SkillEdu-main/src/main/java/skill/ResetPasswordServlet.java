package skill;

import java.io.IOException;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("forgotPassword.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");

        // Password match check
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.getRequestDispatcher("resetPassword.jsp").forward(request, response);
            return;
        }

        try {
            Class.forName("oracle.jdbc.OracleDriver");

            Connection con = DriverManager.getConnection(
            		"jdbc:oracle:thin:@localhost:1521:XE",
            	    "sasi",
            	    "sasi123");

            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET PASSWORD_HASH=? WHERE email=?");

            ps.setString(1, newPassword);
            ps.setString(2, email);
            ps.executeUpdate();

            // 🔥 AUTO LOGIN AFTER RESET
            session.setAttribute("user", email);

            // OPTIONAL: fetch name
            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT FULL_NAME FROM users WHERE email=?");
            ps2.setString(1, email);
            ResultSet rs = ps2.executeQuery();

            if (rs.next()) {
                session.setAttribute("fullName", rs.getString("FULL_NAME"));
            }

            rs.close();
            ps2.close();
            ps.close();
            con.close();

            // 🚀 Redirect to dashboard
            response.sendRedirect("dashboard.jsp");

        }catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("ERROR: " + e.getMessage());
        }
    }
}