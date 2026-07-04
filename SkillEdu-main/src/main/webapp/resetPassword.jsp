<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reset Password</title>
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(to bottom right, #ffffff, #ffe0b2);
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }
        form {
            background: #fff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.2);
            text-align: center;
            width: 350px;
        }
        input {
            width: 90%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 8px;
        }
        button {
            background: #ff9800;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
        }
        button:hover {
            background: #e65100;
        }
    </style>
</head>
<body>
<form action="ResetPasswordServlet" method="post">
    <h2>Reset Password</h2>

    <input type="password" name="newPassword" placeholder="New Password" required>
    <input type="password" name="confirmPassword" placeholder="Confirm Password" required>

    <button type="submit">Update</button>

    <div style="color:red;">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "" %>
    </div>
</form>

</body>
</html>
