package member;

import javax.servlet.ServletContext;

import common.Connect;

public class LoginDAO extends Connect{
	
	public LoginDAO(ServletContext application) {
		super(application);
	}
	
	public LoginDTO getLoginDTO(String lid, String lpass) {
		
		LoginDTO dtol = new LoginDTO();
		
		String query = "SELECT * FROM member WHERE id=? AND pass=?";
		
		try {
			psmt = con.prepareStatement(query);
			
			psmt.setString(1, lid);
			psmt.setString(2, lpass);
			
			rs = psmt.executeQuery();
			
			if(rs.next()) {
				dtol.setId(rs.getString("id"));
				dtol.setPass(rs.getString("pass"));
				dtol.setName(rs.getString(3));
				dtol.setRegidate(rs.getString(4));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return dtol;
	}
	
}
