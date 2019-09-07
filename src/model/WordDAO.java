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

public class WordDAO {
	Connection db=null;
	PreparedStatement ps=null;
	ResultSet rs=null;

	/**
	 * 検索語の件数を返す
	 * @param match likeの文字列
	 * @return 件数
	 */
	public int getCount(String match){
		int count=-1;
		connect();
		try {
			ps=db.prepareStatement("SELECT count(*) FROM words WHERE title like ?");
			ps.setString(1, match);
			rs=ps.executeQuery();
			if(rs.next()){
				count=rs.getInt("count(*)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			disConnect();
		}
		return count;
	}

	/**
	 * 検索語の結果をリストとして返却
	 * @param match likeの文字列
	 * @return リスト(Word)
	 */
	public ArrayList<Word> findAll(String match, User user){
		ArrayList<Word> list=new ArrayList<>();
		connect();
		StringBuilder sb = null;
		try {
			//ログインしているとき
			if (user != null) {
				sb = new StringBuilder();
				sb.append("SELECT words.title, words.body, words.id, sub.rate");
				sb.append(" FROM words");
				sb.append(" LEFT JOIN (SELECT id, rate FROM mywords WHERE id=?) AS sub");
				sb.append(" ON words.id = sub.id");
				sb.append(" WHERE title LIKE ?");
				sb.append(" ORDER BY id;");
				ps.setString(1, user.getUserId());
				ps.setString(2, match);

			//ログインしていないとき
			} else {
				ps = db.prepareStatement("SELECT * FROM words WHERE title like ?;");
				ps.setString(1, match);
			}

			rs=ps.executeQuery();

			while(rs.next()){
				Word word=new Word();
				word.setId(rs.getString("id"));
				word.setTitle(rs.getString("title"));
				word.setBody(rs.getString("body"));
				if(user != null) {
					word.setRate(rs.getInt("rate"));
				}
				list.add(word);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			disConnect();
		}
		return list;
	}

	/**
	 * 検索文字列、ページ、１ページの表示件数を受け取ってリストを返却
	 * @param match likeの文字列
	 * @param page ページ番号
	 * @param limit １ページの表示件数
	 * @return リスト(Word)
	 */
	public ArrayList<Word> findAll(String match,int page,int limit, User user){
		ArrayList<Word> list=new ArrayList<>();
		connect();
		int offset=(page-1)*limit;
		StringBuilder sb = null;
		try {
			//ログインしているとき
			if (user != null) {
				sb = new StringBuilder();
				sb.append("SELECT words.title, words.body, words.id, sub.rate");
				sb.append(" FROM words");
				sb.append(" LEFT JOIN (SELECT id, rate FROM mywords WHERE id=?) AS sub");
				sb.append(" ON words.id = sub.id");
				sb.append(" WHERE title LIKE ? LIMIT ? OFFSET ?");
				sb.append(" ORDER BY id;");
				ps.setString(1, user.getUserId());
				ps.setString(2, match);
				ps.setString(3, match);
				ps.setInt(4, limit);
				ps.setInt(5, offset);
			//ログインしていないとき
			} else {
				ps = db.prepareStatement("SELECT * FROM words WHERE title like ? limit ? offset ?");
				ps.setString(1, match);
				ps.setInt(2, limit);
				ps.setInt(3, offset);
			}
			rs=ps.executeQuery();
			while(rs.next()){
				Word word=new Word();
				word.setId(rs.getString("id"));
				word.setTitle(rs.getString("title"));
				word.setBody(rs.getString("body"));
				list.add(word);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			disConnect();
		}
		return list;
	}

	/**
	 *
	 * @param id
	 * @return
	 */
	public Word findOne(String id) {
		Word word = null;
		connect();
		try {
			ps = db.prepareStatement("SELECT * FROM words WHERE id=?");
			ps.setInt(1, Integer.parseInt(id));
			rs = ps.executeQuery();
			if (rs.next()) {
				word = new Word();
				word.setId(rs.getString("id"));
				word.setTitle(rs.getString("title"));
				word.setBody(rs.getString("body"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disConnect();
		}
		return word;

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
