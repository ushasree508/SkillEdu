package skill;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import jakarta.mail.*;
import jakarta.mail.internet.*;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    private static final String EMAIL_FROM = "ushasreeorsu56@gmail.com";
    private static final String APP_PASSWORD = "dbjl ziyk rpbb cojk";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Email is required.");
            request.getRequestDispatcher("forgotPassword.jsp").forward(request, response);
            return;
        }

        try {
            HttpSession session = request.getSession();

            // Generate OTP only once
            String otp = String.format("%06d",
                    ThreadLocalRandom.current().nextInt(1000000));

            session.setAttribute("otp", otp);
            session.setAttribute("email", email);
            session.setAttribute("otpTime", System.currentTimeMillis());

            System.out.println("Generated OTP: " + otp);

            sendEmail(email, otp);

            response.sendRedirect("verifyotp.jsp");

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getClass().getName());
            System.out.println("MESSAGE: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendEmail(String to, String otp) throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");
        Session mailSession = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
                    }
                });

        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("OTP for Password Reset");
        message.setText("Your OTP is: " + otp + "\nValid for 5 minutes.");

        Transport.send(message);
    }
}
