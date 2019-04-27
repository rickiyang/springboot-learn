package com.rickiyang.learn.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Author yangyue
 * @Date Created in 下午1:08 2018/6/14
 * @Modified by:
 * @Description:
 **/
public class EmailAuthenticator extends Authenticator {

	private String username;

	private String password;

	public EmailAuthenticator(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

}
