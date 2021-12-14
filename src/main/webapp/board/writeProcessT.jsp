<%@page import="util.JSFunction"%>
<%@page import="board.Dao"%>
<%@page import="board.Dto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./IsLoggedIn.jsp" %>
<%
	String title2 = request.getParameter("title2");
	String content2 = request.getParameter("content2");
	
	Dto dto2 = new Dto();
	dto2.setTitle(title2);
	dto2.setContent(content2);
	dto2.setId(session.getAttribute("id").toString());
	
	Dao dao = new Dao(application);
	
	int iResult = dao.insertW(dto2);
	
	dao.close();
	
	if(iResult == 1) {
		response.sendRedirect("listT.jsp");
	}
	else {
		JSFunction.alertBack("글쓰기에 실패하였습니다.", out);
	}
	
%>

