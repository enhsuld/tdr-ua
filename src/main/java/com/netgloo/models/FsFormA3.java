package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM_A3 database table.
 * 
 */
@Entity
@Table(name="FS_FORM_A3")
@NamedQuery(name="FsFormA3.findAll", query="SELECT f FROM FsFormA3 f")
public class FsFormA3 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FS_FORM_A3_SEQ", sequenceName="FS_FORM_A3_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FS_FORM_A3_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private long auditid;

	private String data1;

	private String data2;

	private String data3;

	private String data4;

	private String data5;

	private long formid;

	private String recnum;

	public FsFormA3() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAuditid() {
		return this.auditid;
	}

	public void setAuditid(long auditid) {
		this.auditid = auditid;
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

	public long getFormid() {
		return this.formid;
	}

	public void setFormid(long formid) {
		this.formid = formid;
	}

	public String getRecnum() {
		return this.recnum;
	}

	public void setRecnum(String recnum) {
		this.recnum = recnum;
	}

}