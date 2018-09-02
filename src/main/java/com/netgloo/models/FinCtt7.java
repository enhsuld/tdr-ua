package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.netgloo.models.MainAuditRegistration;

import java.math.BigDecimal;


/**
 * The persistent class for the FIN_CTT5 database table.
 * 
 */
@Entity
@Table(name="FIN_CTT7")
@NamedQuery(name="FinCtt7.findAll", query="SELECT f FROM FinCtt7 f")
public class FinCtt7 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FIN_CTT7_SEQ", sequenceName="FIN_CTT7_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FIN_CTT7_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String data1;

	private String data2;

	private String data3;

	private String data4;

	private String data5;

	private String data6;

	private String data7;

	private String data8;
	
	private String data9;

	private String data10;

	private String data11;

	private String data12;

	private String data13;

	private String data14;

	private String data15;

	private String data16;

	private long formid;
	
	private long orgcatid;

	private String orgcode;
	
	private String cyear;

	private long planid;

	private long stepid;
	
	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="planid",nullable = false,insertable=false,updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public FinCtt7() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getData6() {
		return this.data6;
	}

	public void setData6(String data6) {
		this.data6 = data6;
	}

	public String getData7() {
		return this.data7;
	}

	public void setData7(String data7) {
		this.data7 = data7;
	}

	public String getData8() {
		return this.data8;
	}

	public void setData8(String data8) {
		this.data8 = data8;
	}

	public String getData9() {
		return data9;
	}

	public void setData9(String data9) {
		this.data9 = data9;
	}

	public String getData10() {
		return data10;
	}

	public void setData10(String data10) {
		this.data10 = data10;
	}

	public String getData11() {
		return data11;
	}

	public void setData11(String data11) {
		this.data11 = data11;
	}

	public String getData12() {
		return data12;
	}

	public void setData12(String data12) {
		this.data12 = data12;
	}

	public String getData13() {
		return data13;
	}

	public void setData13(String data13) {
		this.data13 = data13;
	}

	public String getData14() {
		return data14;
	}

	public void setData14(String data14) {
		this.data14 = data14;
	}

	public String getData15() {
		return data15;
	}

	public void setData15(String data15) {
		this.data15 = data15;
	}

	public String getData16() {
		return data16;
	}

	public void setData16(String data16) {
		this.data16 = data16;
	}

	public long getFormid() {
		return this.formid;
	}

	public void setFormid(long formid) {
		this.formid = formid;
	}

	public long getOrgcatid() {
		return orgcatid;
	}

	public void setOrgcatid(long orgcatid) {
		this.orgcatid = orgcatid;
	}

	public long getPlanid() {
		return planid;
	}

	public void setPlanid(long planid) {
		this.planid = planid;
	}

	public long getStepid() {
		return stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getCyear() {
		return cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}
}