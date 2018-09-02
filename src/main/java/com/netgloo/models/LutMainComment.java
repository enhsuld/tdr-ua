package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LUT_MAIN_COMMENT database table.
 * 
 */
@Entity
@Table(name="LUT_MAIN_COMMENT")
@NamedQuery(name="LutMainComment.findAll", query="SELECT l FROM LutMainComment l")
public class LutMainComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String answer;

	private String appdate;

	private String offid;

	private int status;

	private String subdate;

	private String text;

	private String title;

	private long userid;

	private String usermail;

	private String username;

	public LutMainComment() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public String getAppdate() {
		return this.appdate;
	}

	public void setAppdate(String appdate) {
		this.appdate = appdate;
	}

	public String getOffid() {
		return this.offid;
	}

	public void setOffid(String offid) {
		this.offid = offid;
	}

	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubdate() {
		return this.subdate;
	}

	public void setSubdate(String subdate) {
		this.subdate = subdate;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getUsermail() {
		return this.usermail;
	}

	public void setUsermail(String usermail) {
		this.usermail = usermail;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}