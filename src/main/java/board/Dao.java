package board;

import javax.servlet.ServletContext;

import common.Connect;

public class Dao extends Connect{

	public Dao(ServletContext application) {
		super(application);
	}
	
	public int select(para) {
		
		int totalPage = 0;
		
		String query = "SELECT COUNT(*) FROM board";
		
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
}
