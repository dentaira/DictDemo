package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class UserDAO {
	Connection db=null;
	PreparedStatement ps=null;
	ResultSet rs=null;

	public User findOne(String email, String password) {
		User user = null;
		connect();
		try {
			ps = db.prepareStatement("SELECT * FROM users WHERE email=? AND password=?;");
			ps.setString(1, email);
			ps.setString(2, password);
			rs = ps.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setUserId(rs.getString("id"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return user;

	}

	public void insertOne(User user) {
		connect();
		try {
			ps = db.prepareStatement("INSERT INTO users(email, password) VALUES(?, ?)");
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.executeUpdate();

			// TODO mysql-connector-java-5
//		} catch(MySQLIntegrityConstraintViolationException e) {
			//アドレスが登録済みだった場合の処理
//			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	private void connect() {
		Context context;
		try {
			context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc/Jsp10");
			db = ds.getConnection();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * db切断
	 */
	private void disConnect(){
		try{
		if(rs !=null){rs.close();}
		if(ps !=null){ps.close();}
		if(db !=null){db.close();}
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
}
