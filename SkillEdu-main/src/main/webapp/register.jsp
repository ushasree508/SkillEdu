<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: "Poppins", sans-serif;
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background-color: violet;
        }
        .form-area {
            width: 30%;
            min-width: 320px;
            padding: 50px;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        form {
            width: 100%;
            background-color: #fff;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.2);
        }
        h2 { text-align: center; margin-bottom: 20px; color: #333; }
        label { font-weight: 500; color: #333; }
        input {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .btn {
            width: 100%;
            padding: 12px;
            border: none;
            color: white;
            font-size: 16px;
            border-radius: 30px;
            background: linear-gradient(45deg, #ff4081, #9c27b0);
            opacity: 0.6;
            cursor: not-allowed;
        }
        .btn:enabled { opacity: 1; cursor: pointer; }
        .note { text-align: center; margin-top: 15px; }
        a { color: #9c27b0; text-decoration: none; }
        .error { color: red; font-size: 13px; margin-bottom: 5px; }
    </style>
</head>
<body>
    <div class="form-area">
        <form id="registerForm" action="RegisterServlet" method="post">
            <h2>Sign Up</h2>

            <label>Full Name</label>
            <input type="text" id="fullname" name="fullname" placeholder="Enter full name" required>
            <div id="nameError" class="error"></div>

            <label>Username</label>
            <input type="text" id="username" name="username" placeholder="Enter username (min 6 chars)" required>
            <div id="userError" class="error"></div>

            <label>Email</label>
            <input type="email" id="email" name="email" placeholder="Enter email" required>
            <div id="emailError" class="error"></div>

            <label>Password</label>
            <input type="password" id="password" name="password" placeholder="Enter password" required>
            <div id="passError" class="error"></div>

            <label>Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm password" required>
            <div id="confirmError" class="error"></div>

            <button type="submit" id="registerBtn" class="btn" disabled>Sign Up</button>

            <div class="note">
                Already registered? <a href="login.jsp">Go to Login</a>
            </div>
            <% if (request.getAttribute("error") != null) { %>
    <div class="error"><%= request.getAttribute("error") %></div>
<% } %>
            
        </form>
    </div>

<script>
    const fullname = document.getElementById('fullname');
    const username = document.getElementById('username');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const confirmPassword = document.getElementById('confirmPassword');
    const registerBtn = document.getElementById('registerBtn');

    const nameError = document.getElementById('nameError');
    const userError = document.getElementById('userError');
    const emailError = document.getElementById('emailError');
    const passError = document.getElementById('passError');
    const confirmError = document.getElementById('confirmError');

    const usernamePattern = /^[A-Za-z0-9]{6,}$/;
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%?&])[A-Za-z\d@$!%?&]{8,15}$/;


    function validateForm() {
        let valid = true;

        if (fullname.value.trim() === "") {
            nameError.textContent = "Full name is required.";
            valid = false;
        } else nameError.textContent = "";

        if (!usernamePattern.test(username.value)) {
            userError.textContent = "Username must be alphanumeric and at least 6 characters.";
            valid = false;
        } else userError.textContent = "";

        if (email.value.trim() === "") {
            emailError.textContent = "Email is required.";
            valid = false;
        } else emailError.textContent = "";

        if (!passwordPattern.test(password.value)) {
            passError.textContent = "Password must be 8–15 chars with uppercase, lowercase, number, and special char.";
            valid = false;
        } else passError.textContent = "";

        if (password.value !== confirmPassword.value) {
            confirmError.textContent = "Passwords do not match.";
            valid = false;
        } else confirmError.textContent = "";

        registerBtn.disabled = !valid;
    }
    

    document.getElementById('registerForm').addEventListener('input', validateForm);
</script>
</body>
</html>