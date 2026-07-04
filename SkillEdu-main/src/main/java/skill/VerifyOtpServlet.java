package skill;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/VerifyOtpServlet")
public class VerifyOtpServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String enteredOtp = request.getParameter("otp");
        if (enteredOtp != null) enteredOtp = enteredOtp.trim();

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("forgotPassword.jsp");
            return;
        }

        String storedOtp = (String) session.getAttribute("otp");
        if (storedOtp != null) storedOtp = storedOtp.trim();

        Long otpTime = (Long) session.getAttribute("otpTime");

        long currentTime = System.currentTimeMillis();

        // Expiry check
        if (otpTime == null || (currentTime - otpTime) > (5 * 60 * 1000)) {
            session.invalidate();
            request.setAttribute("errorMessage", "OTP expired.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
            return;
        }

        System.out.println("Entered OTP: " + enteredOtp);
        System.out.println("Stored OTP: " + storedOtp);

        if (storedOtp != null && storedOtp.equals(enteredOtp)) {
            response.sendRedirect("resetPassword.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid OTP.");
            request.getRequestDispatcher("verifyotp.jsp").forward(request, response);
        }
    }
}