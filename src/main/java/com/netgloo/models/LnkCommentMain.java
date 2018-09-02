package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LNK_COMMENT_MAIN database table.
 * 
 */
@Entity
@Table(name="LNK_COMMENT_MAIN")
@NamedQuery(name="LnkCommentMain.findAll", query="SELECT l FROM LnkCommentMain l")
public class LnkCommentMain implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private int desid;

	private String mcomment;	

	private String username;
	
	private long userid;
	
	private long mainid;
	
	private long preid;
	
	private String cdate;

	//bi-directional many-to-one association to LutUser
	@ManyToOne
	@JoinColumn(name="userid", insertable= false, updatable= false)
	private LutUser lutUser;

	public LnkCommentMain() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getDesid() {
		return this.desid;
	}

	public void setDesid(int desid) {
		this.desid = desid;
	}

	public String getMcomment() {
		return this.mcomment;
	}

	public void setMcomment(String mcomment) {
		this.mcomment = mcomment;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LutUser getLutUser() {
		return this.lutUser;
	}

	public void setLutUser(LutUser lutUser) {
		this.lutUser = lutUser;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getMainid() {
		return mainid;
	}

	public void setMainid(long mainid) {
		this.mainid = mainid;
	}

	public long getPreid() {
		return preid;
	}

	public void setPreid(long preid) {
		this.preid = preid;
	}

	public String getCdate() {
		return cdate;
	}

	public void setCdate(String cdate) {
		this.cdate = cdate;
	}

}