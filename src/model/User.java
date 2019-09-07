package model;

import java.io.Serializable;

public class User implements Serializable {
	private String userId;
	private String email;
	private String password;

	public User() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public boolean equals(Object object) {
		if(this == object) { return true; }
		if(object == null) { return false; }
		if(!(object instanceof User)) { return false; }

		//idで比較する
		User user = (User) object;
		if(!this.email.equals(user.getEmail())) {
			return false;
		}
		return true;
	}

}
