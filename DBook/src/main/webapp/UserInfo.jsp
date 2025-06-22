<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="jakarta.servlet.http.Cookie" %>
<%@ page import="com.dbook.service.GetUsername" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>User Information</title>
	<link rel="stylesheet" href="style.css">
	<script>
			window.onload = async function(){
					const response = await fetch("/DBook/CheckLogin",{
						method: 'GET',
						credentials: 'include' //sends cookies
					})
					
					const data = await response.json();
					
					if(!data.logged){
						window.location.href = "/DBook";
					}
				}
	</script>
</head>
<body>
	<div class="information">
		<h1 class="headers" id="mainHeader"><b>DBook</b></h1>
		<h2 class="headers" id="header"><b>User information:</b></h2>
		
		<% String username = null; %>

		<%
			// Get all cookies from request and search for uid
			Cookie[] cookies = request.getCookies();
			String uid = null;
			if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("UID".equals(cookie.getName())) {
					uid = cookie.getValue();
					break;
					}
				}
				// Fetch username from backend service
				username = GetUsername.getUsernameByUID(uid);
			}
						%>
		<p id="UID">UID: <%= uid != null ? uid : "Not found" %></p>
		<p id="username">Username: <% out.println(username); %></p>
		<button class="goBack" id="backBtn"><b>Go Back</b></button>
	</div>
</body>
<script>
	document.getElementById("backBtn").addEventListener("click", () => {
		window.location.href = "/DBook";
	});
</script>
</html>