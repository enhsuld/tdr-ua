package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LNK_USERROLE database table.
 * 
 */
@Entity
@Table(name="LNK_USERROLE")
@NamedQuery(name="LnkUserrole.findAll", query="SELECT l FROM LnkUserrole l")
public class LnkUserrole implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_USERROLE_SEQ", sequenceName="LNK_USERROLE_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_USERROLE_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	private long roleid;
	
	private long userid;

	//bi-directional many-to-one association to LutRole
	@ManyToOne
	@JoinColumn(name="roleid",insertable=false,updatable=false)
	private LutRole lutRole;

	//bi-directional many-to-one association to LutUser
	@ManyToOne
	@JoinColumn(name="userid",insertable=false,updatable=false)
	private LutUser lutUser;

	public LnkUserrole() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public long getRoleid() {
		return roleid;
	}

	public void setRoleid(long roleid) {
		this.roleid = roleid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public LutRole getLutRole() {
		return this.lutRole;
	}
	
	public void setLutRole(LutRole lutRole) {
		this.lutRole = lutRole;
	}

	public LutUser getLutUser() {
		return this.lutUser;
	}

	public void setLutUser(LutUser lutUser) {
		this.lutUser = lutUser;
	}

}