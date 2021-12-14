
<%@page import="board.Dto"%>
<%@page import="board.Dao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./commons/header.jsp" %>
<%@ include file="./IsLoggedIn.jsp" %>

<%

String num = request.getParameter("num");

Dao dao = new Dao(application);

Dto dto = dao.selectView(num);

String sessionId = session.getAttribute("id").toString();

if(!sessionId.equals(dto.getId())) {
	JSFunction.alertBack("작성자 본인만 가능합니다", out);
}

dao.close();
%>
<body>
<script>
	function validateForm(form) {
		if(form.title2.value == "") {
			alert("제목을 입력하세요.");
			form.title2.focus();
			return false;
		}
		if(form.content2.value == "") {
			alert("내용을 입력하세요");
			form.content2.focus();
			return false;
		}
	}
</script>

<div class="container">
    <!-- Top영역 -->
    <%@ include file="./commons/top.jsp" %>
    <!-- Body영역 -->
    <div class="row">
        <!-- Left메뉴영역 -->
        <%@ include file="./commons/left.jsp" %>
        <!-- Contents영역 -->
        <div class="col-9 pt-3">
            <h3>게시판 수정 - <small>자유게시판</small></h3>
            
            <form action="EditProcessT.jsp"  onsubmit="return validateForm(this);">
            <input type="hidden" name="num" value="<%= dto.getNum() %>" />
                <table class="table table-bordered">
                <colgroup>
                    <col width="20%"/>
                    <col width="*"/>
                </colgroup>
                <tbody>
                    <tr>
                        <th class="text-center" 
                            style="vertical-align:middle;">제목</th>
                        <td>
                            <input type="text" class="form-control" name="title" value="<%= dto.getTitle() %>" />
                        </td>
                    </tr>
                    <tr>
                        <th class="text-center" 
                            style="vertical-align:middle;">내용</th>
                        <td>
                            <textarea rows="5" class="form-control" name="content"><%= dto.getContent() %></textarea>
                        </td>
                    </tr>
                </tbody>
                </table>

                <!-- 각종버튼 -->
                <div class="row mb-3">
                    <div class="col d-flex justify-content-end">
                        <button type="submit" class="btn btn-primary">작성완료</button>
                        <button type="reset" class="btn btn-dark">다시쓰기</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <!-- Copyright영역 -->
    <%@ include file="./commons/copyright.jsp" %>
</div>
</body>

