<%@page import="board.Dto"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="board.Dao"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./commons/header.jsp" %>
<%
	Dao dao = new Dao(application);
	Dto dto2 = new Dto();

	Map<String, Object> param = new HashMap<String, Object>();
	String searchIn = request.getParameter("searchIn");
	String searchSe = request.getParameter("searchSe");
	
	if(searchIn != null) {
		// Map 컬렉션에 파라미터 값을 추가한다.
		param.put("searchSe", searchSe); // 검색필드명(title, content 등)
		param.put("searchIn", searchIn); // 검색어
		
	}

	int totalCount = dao.select(param);
	
	int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
	int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));
	// 전체 페이지 수를 계산한다.
	int totalPage = (int)Math.ceil((double)totalCount / pageSize);

	/*
		목록에 첫 진입시에는 페이지 관련 파라미터가 없으므로 무조건 1page로 지정한다.
		만약 pageNum이 있다면 파라미터를 받아와서 정수로 변경한 후 페이지수로 지정한다.
	*/
	int pageNum = 1;
	String pageTemp = request.getParameter("pageNum");
	if(pageTemp != null && !pageTemp.equals(""))
		pageNum = Integer.parseInt(pageTemp);

	//게시물의 구간을 계산한다.
	int start = (pageNum - 1) * pageSize + 1; // 구간의 시작
	int end = pageNum * pageSize; // Map컬렉션에 저장 후 DAO로 전달함.

	param.put("start", start);
	param.put("end", end);
	
	List<Dto> Lists = dao.selectListPage(param);
	
	dao.close();
%>
<body>
<div class="container">
    <!-- Top영역 -->
    <%@ include file="./commons/top.jsp" %>
    <!-- Body영역 -->
    <div class="row">
        <!-- Left메뉴영역 -->
        <%@ include file="./commons/left.jsp" %>
        <!-- Contents영역 -->
        <div class="col-9 pt-3">
            <h3>게시판 목록 - <small>자유게시판</small></h3>
            <!-- 검색 -->
            <div class="row">
                <form action="">
                    <div class="input-group ms-auto" style="width: 400px;">
                        <select name="searchSe" class="form-control">
                            <option value="">제목</option>
                            <option value="">내용</option>
                            <option value="">작성자</option>
                        </select>
                        <input type="text" class="form-control" placeholder="Search" name="searchIn"
                        	style="width: 200px;">
                        <button class="btn btn-success" type="submit">
                            <i class="bi-search" style="font-size: 1rem; color: white;"></i>
                        </button>
                    </div>
                </form>
            </div>
            <!-- 게시판 리스트 -->
            <div class="row mt-3 mx-1">
                <table class="table table-bordered table-hover table-striped">
                <thead>
                    <tr class="text-center">
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>작성일</th>
                        <th>조회수</th>
                    </tr>
                </thead>
                <tbody>
                	<%
		            if(Lists.isEmpty()) {
		            %>
		            	
			            <tr>
			            	<td colspan="6">
			            		게시물이 없다면 나와야 할 것
			            	</td>
			            </tr>
		            
		            <%
		            }
		            else {
		            	int count = 0;
		            	int countNum = 0;
		            	
		            	for(Dto dto : Lists) {
		            		
		            		count = totalCount - (((pageNum - 1) * pageSize) + countNum++);
		            %>     
		            
		            		<tr align="center">
								<td><%= count %></td>
								<td align="left">
									<a href="viewT.jsp?num=<%= dto.getNum() %>"><%= dto.getTitle() %></a>
								</td>
								<td align="center"><%= dto.getId() %></td>
								<td align="center"><%= dto.getPostdate() %></td>
								<td align="center"><%= dto.getVisitcount() %></td>
							</tr>       
					<%
		            	}
		            }
					%>        
                    
                </tbody>
                </table>
            </div>
            <!-- 각종버튼 -->
            <div class="row">
            <%
            if(session.getAttribute("id") != null) {
            %>
                <div class="col d-flex justify-content-end">
                    <button type="button" class="btn btn-primary" onclick="location.href='writeT.jsp';">글쓰기</button>
                </div>
            <%
            }
            %>
            </div>
            <!-- 페이지 번호 -->
            <div class="row mt-3">
                <div class="col">
                    <ul class="pagination justify-content-center">
                        <li class="page-item"><a class="page-link" href="#">
                            <i class='bi bi-skip-backward-fill'></i>
                        </a></li>
                        <li class="page-item"><a class="page-link" href="#">
                            <i class='bi bi-skip-start-fill'></i>
                        </a></li>
                        <li class="page-item active"><a class="page-link" href="#">1</a></li>
                        <li class="page-item"><a class="page-link" href="#">2</a></li>
                        <li class="page-item"><a class="page-link" href="#">3</a></li>
                        <li class="page-item"><a class="page-link" href="#">
                            <i class='bi bi-skip-end-fill'></i>
                        </a></li>
                        <li class="page-item"><a class="page-link" href="#">
                            <i class='bi bi-skip-forward-fill'></i>
                        </a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
    <!-- Copyright영역 -->
    <%@ include file="./commons/copyright.jsp" %>
</div>
</body>
</html>