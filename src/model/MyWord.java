package model;

import java.io.Serializable;

public class MyWord implements Serializable {
	private String id;
	private String title;
	private String body;
	private String rate;
	private User user;

	public MyWord() {
	}

	public MyWord(String id, User user, String title, String body) {
		super();
		this.id = id;
		this.user = user;
		this.title = title;
		this.body = body;
	}

	public MyWord(Word word ,User user) {
		this.setId(word.getId());
		this.setTitle(word.getTitle());
		this.setBody(word.getBody());
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getUserName() {
		return this.user.getEmail();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}


}
