package board;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import common.Connect;

public class Dao extends Connect{

	public Dao(ServletContext application) {
		super(application);
	}
	
	public int select(Map<String, Object> map) {
		
		int totalPage = 0;
		
		String query = "SELECT COUNT(*) FROM board";
		if(map.get("searchIn") != null) {
			query += " WHERE " + map.get("searchSe") + " "
					+ " LIKE '%" + map.get("searchIn") + "%'";
		}
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			rs.next();
			
			totalPage = rs.getInt(1);
			
		}
		catch (Exception e) {
			System.out.println("오류발생");
			e.printStackTrace();
		}
		
		return totalPage;
	}
	
	public List<Dto> seList(Map<String, Object> map) {
		
		List<Dto> dts = new Vector<Dto>();
		
		String query = "SELECT * FROM board";
		// 검색어가 있는 경우에 where 절을 추가한다.
		if(map.get("searchIn") != null) {
			query += " WHERE " + map.get("searchSe") + " "
					+ " LIKE '%" + map.get("searchIn") + "%' ";
		}
		
		query += " ORDER BY num DESC ";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				
				Dto dto = new Dto();
				
				dto.setNum(rs.getString("num"));
				dto.setId(rs.getString("id"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				dts.add(dto);
			}
		}
		catch (Exception e) {
			System.out.println("오류 발생");
			e.printStackTrace();
		}
		
		return dts;
	}
	
	public void updateVisitCount(String num) {
		// visitcount 컬럼은 number 타입이므로 덧셈이 가능하다.
		String query = "UPDATE board SET "
					 + " visitcount=visitcount+1 "
					 + " WHERE num=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeQuery();
		}
		catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외발생");
			e.printStackTrace();
		}
	}
	
	public Dto selectView(String num) {
		Dto dto = new Dto();
		
		//join을 이용해서 member테이블의 name 컬럼까지 가져온다.
		String query = "SELECT B.*, M.name "
					 + " FROM member M INNER JOIN board B "
					 + " ON M.id=B.id "
					 + " WHERE num=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();
			//일련번호는 중복되지 않으므로 if문에서 처리하면 된다.
			
			if(rs.next()) { // ResultSet에서 커서를 이동시켜 레코드를 읽은 후
				// DTO 객체에 레코드의 내용을 추가한다.
				dto.setNum(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate")); //날짜타입이므로 getDate()사용함
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString(6));
				dto.setName(rs.getString("name"));
			}
		}
		catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외발생");
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public int insertW(Dto dto) {
		// 입력결과 확인용 변수
		int result2 = 0;
		
		try {
			// 인파라미터가 있는 쿼리문 작성(동적쿼리문)
			String query = " INSERT INTO board( "
						 + " num, title, content, id, visitcount) "
						 + " VALUES ( "
						 + " seq_board_num.NEXTVAL, ?, ?, ?, 0)";
			
			// 동적쿼리문 실행을 위한 prepared객체 생성
			psmt = con.prepareStatement(query);
			// 순서대로 인파라미터 설정
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());
			//쿼리문 실행 : 입력에 성공한다면 1이 반환된다 실패시 0 반환
			result2 = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		
		return result2;
	}
	
	public List<Dto> selectListPage(Map<String, Object> map) {
		List<Dto> dts = new Vector<Dto>();
		
		// 3개의 서브 쿼리문을 통한 페이지 처리
		String query = " SELECT * FROM ( "
					 + "   SELECT Tb.*, ROWNUM rNum FROM ( "
					 + "      SELECT * FROM board ";
		
		// 검색 조건 추가 (검색어가 있는 경우에만 where절이 추가됨)
		if(map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField")
					+ " LIKE '%" + map.get("searchWord") + "%' ";
		}
		
		query += "       ORDER BY num DESC "
				+ "     ) Tb"
				+ " ) "
				+ " WHERE rNum BETWEEN ? AND ?";
		/* JSP에서 계산된 게시물의 구간을 인파라미터로 처리함 */
		
		try {
			// 쿼리 실행을 위한 prepared객체 생성
			psmt = con.prepareStatement(query);
			// 인파라미터 설정 : 구간을 위한 start, end를 설정함
			psmt.setString(1, map.get("start").toString());
			psmt.setString(2, map.get("end").toString());
			// 쿼리문 실행
			rs = psmt.executeQuery();
			
			// select한 게시물의 갯수만큼 반복함
			while (rs.next()) {
				// 한 행(게시물 하나)의 데이터를 DTO에 저장
				Dto dto = new Dto();
				
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				//반환할 결과 목록에 게시물 추가
				dts.add(dto);
			}
		}
		catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생");
			e.printStackTrace();
		}
		
		return dts;
	}
}
