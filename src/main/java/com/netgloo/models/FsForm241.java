package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM_241 database table.
 * 
 */
@Entity
@Table(name="FS_FORM_241")
@NamedQuery(name="FsForm241.findAll", query="SELECT f FROM FsForm241 f")
public class FsForm241 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FS_FORM_241_SEQ", sequenceName="FS_FORM_241_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FS_FORM_241_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name="ACC_CODE")
	private String accCode;

	@Column(name="ACC_NAME")
	private String accName;

	private double data1;

	private double data2;

	private double data3;
	
	private double data4;
	
	private double data5;
	
	private double data6;
	
	private double data7;
	
	private long orderid;

	private long planid;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JoinColumn(name="planid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public FsForm241() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getAccCode() {
		return this.accCode;
	}

	public void setAccCode(String accCode) {
		this.accCode = accCode;
	}

	public String getAccName() {
		return this.accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}
	public double getData1() {
		return data1;
	}

	public void setData1(double data1) {
		this.data1 = data1;
	}

	public double getData2() {
		return data2;
	}

	public void setData2(double data2) {
		this.data2 = data2;
	}

	public double getData3() {
		return data3;
	}

	public void setData3(double data3) {
		this.data3 = data3;
	}

	public double getData4() {
		return data4;
	}

	public void setData4(double data4) {
		this.data4 = data4;
	}

	public double getData5() {
		return data5;
	}

	public void setData5(double data5) {
		this.data5 = data5;
	}

	public double getData6() {
		return data6;
	}

	public void setData6(double data6) {
		this.data6 = data6;
	}

	public double getData7() {
		return data7;
	}

	public void setData7(double data7) {
		this.data7 = data7;
	}

	public long getPlanid() {
		return planid;
	}

	public void setPlanid(long planid) {
		this.planid = planid;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}
	
	public long getOrderid() {
		return orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

}