package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;

public class Connect {

	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	
	public Connect() {
		try {
			// 오라클 드라이버 로드
			Class.forName("oracle.jdbc.OracleDriver");
			// 커넥션 URL 생성
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String id = "musthave";
			String pwd = "1234";
			
			con = DriverManager.getConnection(url, id, pwd);
			
			System.out.println("DB 연결 성공(기본 생성자)");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connect(ServletContext application) {
		
		// web.xml에 저장된 오라클 접속정보를 얻어온다.
		String driver = application.getInitParameter("OracleDriver");
		String url = application.getInitParameter("OracleURL");
		String id = application.getInitParameter("OracleId");
		String pwd = application.getInitParameter("OraclePwd");
		try {
			//JDBC 드라이버 로드
			Class.forName(driver);
			// DB에 연결
			con = DriverManager.getConnection(url,id,pwd);
			
			System.out.println("DB 연결 성공(인수 생성자 2)");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connect(String driver, String url, String id, String pwd) {
		try {
			//JDBC 드라이버 로드
			Class.forName(driver);
			// DB에 연결
			con = DriverManager.getConnection(url, id, pwd);
			
			System.out.println("DB 연결 성공(인수 생성자 1)");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if(rs != null) rs.close();
			if(stmt != null) stmt.close();
			if(psmt != null) psmt.close();
			if(con != null) con.close();
			
			System.out.println("JDBC 자원 해제");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
