package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.netgloo.models.LnkMainAttach;
import com.netgloo.models.MainAuditRegistration;

import com.netgloo.models.LutAuditWork;

import java.math.BigDecimal;


/**
 * The persistent class for the FIN_FORM_B1 database table.
 * 
 */
@Entity
@Table(name="FIN_FORM_B1")
@NamedQuery(name="FinFormB1.findAll", query="SELECT f FROM FinFormB1 f")
public class FinFormB1 implements Serializable {
	private static final long serialVersionUID = 1L;

	/*@Id
	@SequenceGenerator(name="FIN_FORM_B1_SEQ", sequenceName="FIN_FORM_B1_SEQ",allocationSize=1)
	@GeneratedValue(strategy ="org.hibernate.id.enhanced.SequenceStyleGenerator" ,generator="FIN_FORM_B1_SEQ")*/
	
	@Id
	@SequenceGenerator(name="FIN_FORM_B1_SEQ", sequenceName="FIN_FORM_B1_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FIN_FORM_B1_SEQ")
	
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String cyear;

	private String data1;

	private String data2;

	private String data3;

	private String data4;

	private String data5;

	private long orgcatid;

	private String orgcode;

	private long stepid;

	private long userid;
	
	private long planid;
	
	private long filelnkid;
	
	private long workid;
	
	//bi-directional many-to-one association to LnkMainAttach
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="filelnkid", insertable=false,updatable=false)
	private LnkMainAttach lnkMainAttach;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="planid", insertable=false,updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public FinFormB1() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCyear() {
		return this.cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}

	public String getData1() {
		return this.data1;
	}

	public void setData1(String data1) {
		this.data1 = data1;
	}

	public String getData2() {
		return this.data2;
	}

	public void setData2(String data2) {
		this.data2 = data2;
	}

	public String getData3() {
		return this.data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public String getData4() {
		return this.data4;
	}

	public void setData4(String data4) {
		this.data4 = data4;
	}

	public String getData5() {
		return this.data5;
	}

	public void setData5(String data5) {
		this.data5 = data5;
	}

	public long getOrgcatid() {
		return this.orgcatid;
	}

	public void setOrgcatid(long orgcatid) {
		this.orgcatid = orgcatid;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public long getStepid() {
		return this.stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public LnkMainAttach getLnkMainAttach() {
		return this.lnkMainAttach;
	}

	public void setLnkMainAttach(LnkMainAttach lnkMainAttach) {
		this.lnkMainAttach = lnkMainAttach;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

	public long getPlanid() {
		return planid;
	}

	public void setPlanid(long planid) {
		this.planid = planid;
	}

	public long getFilelnkid() {
		return filelnkid;
	}

	public void setFilelnkid(long filelnkid) {
		this.filelnkid = filelnkid;
	}
	
	public long getWorkid() {
		return workid;
	}

	public void setWorkid(long workid) {
		this.workid = workid;
	}
	
}