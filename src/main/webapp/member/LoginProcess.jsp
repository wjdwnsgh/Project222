<%@page import="member.LoginDAO"%>
<%@page import="member.LoginDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String userId = request.getParameter("id");
	String userPw = request.getParameter("passwd");
	
	
	LoginDAO dao = new LoginDAO(application);
	LoginDTO dto = dao.getLoginDTO(userId, userPw);
	dao.close();
	
	if(dto.getId() != null) {
		session.setAttribute("id", dto.getId());
		session.setAttribute("name", dto.getName());
		
		response.sendRedirect("Login.jsp");
	}
	else {

		request.setAttribute("LoginErrMsg", "로그인 오류입니다.");
		request.getRequestDispatcher("Login.jsp").forward(request, response);
	}
%>
</body>
</html>