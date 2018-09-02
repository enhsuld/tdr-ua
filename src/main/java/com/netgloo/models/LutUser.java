package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_USERS database table.
 * 
 */
@Entity
@Table(name="LUT_USERS")
@NamedQuery(name="LutUser.findAll", query="SELECT l FROM LutUser l")
public class LutUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_USER_SEQ", sequenceName="LUT_USER_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_USER_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String email;

	private String familyname;

	private String givenname;

	private boolean isactive;

	private String mobile;

	private String password;

	private String positionid;
	
	private long departmentid;

	private String roleid;

	private String username;

	private String configid;
	
	private boolean	isstate;
	
	private boolean	ismultiple;
	
	private long utype;
	
	//bi-directional many-to-one association to FinFormB21
	@OneToMany(mappedBy="lutUser")
	private List<FinFormB21> finFormB21s;
	
	//bi-directional many-to-one association to LnkComment
	@OneToMany(mappedBy="lutUser")
	private List<LnkComment> lnkComments;
	
	@OneToMany(mappedBy="lutUser")
	private List<LnkMainUser> lnkMainUsers;

	//bi-directional many-to-one association to LnkCommentMain
	@OneToMany(mappedBy="lutUser")
	private List<LnkCommentMain> lnkCommentMains;

	//bi-directional many-to-one association to LnkUserrole
	@OneToMany(mappedBy="lutUser")
	@OrderBy("roleid asc")
	private List<LnkUserrole> lnkUserroles;

	//bi-directional many-to-one association to LutDepartment
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="departmentid",nullable = false,insertable=false,updatable=false)	
	private LutDepartment lutDepartment;

	public LutUser() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFamilyname() {
		return this.familyname;
	}

	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}

	public String getGivenname() {
		return this.givenname;
	}

	public void setGivenname(String givenname) {
		this.givenname = givenname;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPositionid() {
		return this.positionid;
	}

	public void setPositionid(String positionid) {
		this.positionid = positionid;
	}

	public String getRoleid() {
		return this.roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	/*public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}*/

	public long getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(long departmentid) {
		this.departmentid = departmentid;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public long getUtype() {
		return utype;
	}

	public void setUtype(long utype) {
		this.utype = utype;
	}

	public String getConfigid() {
		return configid;
	}

	public void setConfigid(String configid) {
		this.configid = configid;
	}

	public boolean getIsstate() {
		return isstate;
	}

	public void setIsstate(boolean isstate) {
		this.isstate = isstate;
	}

	public boolean isIsmultiple() {
		return ismultiple;
	}

	public void setIsmultiple(boolean ismultiple) {
		this.ismultiple = ismultiple;
	}

	public List<LnkComment> getLnkComments() {
		return this.lnkComments;
	}

	public void setLnkComments(List<LnkComment> lnkComments) {
		this.lnkComments = lnkComments;
	}
	
	
	public List<LnkMainUser> getLnkMainUsers() {
		return this.lnkMainUsers;
	}

	public void setLnkMainUsers(List<LnkMainUser> lnkMainUsers) {
		this.lnkMainUsers = lnkMainUsers;
	}

	public LnkMainUser addLnkMainUser(LnkMainUser lnkMainUser) {
		getLnkMainUsers().add(lnkMainUser);
		lnkMainUser.setLutUser(this);

		return lnkMainUser;
	}

	public LnkMainUser removeLnkMainUser(LnkMainUser lnkMainUser) {
		getLnkMainUsers().remove(lnkMainUser);
		lnkMainUser.setLutUser(null);

		return lnkMainUser;
	}

	public LnkComment addLnkComment(LnkComment lnkComment) {
		getLnkComments().add(lnkComment);
		lnkComment.setLutUser(this);

		return lnkComment;
	}

	public LnkComment removeLnkComment(LnkComment lnkComment) {
		getLnkComments().remove(lnkComment);
		lnkComment.setLutUser(null);

		return lnkComment;
	}

	public List<LnkCommentMain> getLnkCommentMains() {
		return this.lnkCommentMains;
	}

	public void setLnkCommentMains(List<LnkCommentMain> lnkCommentMains) {
		this.lnkCommentMains = lnkCommentMains;
	}

	public LnkCommentMain addLnkCommentMain(LnkCommentMain lnkCommentMain) {
		getLnkCommentMains().add(lnkCommentMain);
		lnkCommentMain.setLutUser(this);

		return lnkCommentMain;
	}

	public LnkCommentMain removeLnkCommentMain(LnkCommentMain lnkCommentMain) {
		getLnkCommentMains().remove(lnkCommentMain);
		lnkCommentMain.setLutUser(null);

		return lnkCommentMain;
	}

	public List<LnkUserrole> getLnkUserroles() {
		return this.lnkUserroles;
	}

	public void setLnkUserroles(List<LnkUserrole> lnkUserroles) {
		this.lnkUserroles = lnkUserroles;
	}

	public LnkUserrole addLnkUserrole(LnkUserrole lnkUserrole) {
		getLnkUserroles().add(lnkUserrole);
		lnkUserrole.setLutUser(this);

		return lnkUserrole;
	}

	public LnkUserrole removeLnkUserrole(LnkUserrole lnkUserrole) {
		getLnkUserroles().remove(lnkUserrole);
		lnkUserrole.setLutUser(null);

		return lnkUserrole;
	}

	public LutDepartment getLutDepartment() {
		return this.lutDepartment;
	}

	public void setLutDepartment(LutDepartment lutDepartment) {
		this.lutDepartment = lutDepartment;
	}
	
	public List<FinFormB21> getFinFormB21s() {
		return this.finFormB21s;
	}

	public void setFinFormB21s(List<FinFormB21> finFormB21s) {
		this.finFormB21s = finFormB21s;
	}

	public FinFormB21 addFinFormB21(FinFormB21 finFormB21) {
		getFinFormB21s().add(finFormB21);
		finFormB21.setLutUser(this);

		return finFormB21;
	}

	public FinFormB21 removeFinFormB21(FinFormB21 finFormB21) {
		getFinFormB21s().remove(finFormB21);
		finFormB21.setLutUser(null);

		return finFormB21;
	}

}