package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class MyWordDAO {
	Connection db=null;
	PreparedStatement ps=null;
	ResultSet rs=null;

	public void insertOne(MyWord myWord) {
		connect();
		try {
			ps = db.prepareStatement("INSERT INTO mywords VALUES(?,?,?,?,?)");
			ps.setString(1, myWord.getUserName());
			ps.setInt(2, Integer.parseInt(myWord.getId()));
			ps.setString(3, myWord.getTitle());
			ps.setString(4, myWord.getBody());
			ps.setInt(5, 1);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	/**
	 * 検索語の件数を返す
	 * @param match likeの文字列
	 * @return 件数
	 */
	public int getCount(String match) {
		int count = -1;
		connect();
		try {
			ps = db.prepareStatement("SELECT COUNT(*) FROM mywords WHERE title LIKE ?;");
			ps.setString(1, match);
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return count;

	}


	/**
	 * 検索語の結果をリストとして返却
	 * @param match likeの文字列
	 * @return リスト(Word)
	 */
	public ArrayList<MyWord> findAll(User user){
		ArrayList<MyWord> list=new ArrayList<>();
		connect();
		try {
			ps=db.prepareStatement("SELECT * FROM mywords WHERE user=?;");
			ps.setString(1, user.getEmail());
			rs=ps.executeQuery();
			while(rs.next()){
				MyWord myWord=new MyWord();
				myWord.setId(rs.getString("id"));
				myWord.setTitle(rs.getString("title"));
				myWord.setBody(rs.getString("body"));
				myWord.setUser(user);
				list.add(myWord);
			}
		} catch (NullPointerException e) {
			//ユーザー情報がないときの例外をキャッチしたい
			e.printStackTrace();
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			disConnect();
		}
		return list;
	}

	public MyWord findOne(String userName, String id) {
		MyWord myWord = null;
		connect();
		try {
			ps = db.prepareStatement("SELECT * FROM mywords WHERE user=? AND id=?;");
			ps.setString(1, userName);
			ps.setInt(2, Integer.parseInt(id));
			rs = ps.executeQuery();
			if(rs.next()) {
				myWord = new MyWord();
				myWord.setId(rs.getString("id"));
				myWord.setTitle(rs.getString("title"));
				myWord.setBody(rs.getString("body"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return myWord;

	}

	public void updateOne(MyWord myWord) {
		connect();
		try {
			ps = db.prepareStatement("UPDATE mywords SET body=? WHERE id=? AND user=?;");
			ps.setString(1, myWord.getBody());
			ps.setInt(2, Integer.parseInt(myWord.getId()));
			ps.setString(3, myWord.getUserName());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	/**
	 * 削除
	 * @param myWord
	 */
	public void deleteOne(MyWord myWord) {
		connect();
		try {
			ps = db.prepareStatement("DELETE FROM mywords WHERE id=? AND user=?;");
			ps.setInt(1, Integer.parseInt(myWord.getId()));
			ps.setString(2, myWord.getUserName());
//			ps.setString(3, myWord.getBody());
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
	}

	/**
	 *db接続
	 */
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
