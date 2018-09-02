package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM_A1 database table.
 * 
 */
@Entity
@Table(name="FS_FORM_A1")
@NamedQuery(name="FsFormA1.findAll", query="SELECT f FROM FsFormA1 f")
public class FsFormA1 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FS_FORM_A1_SEQ", sequenceName="FS_FORM_A1_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="FS_FORM_A1_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String data1;

	private String data2;
	
	private String data3;

	private long formid;

	private String recid;
	
	private long auditid;

	public FsFormA1() {
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
		return data3;
	}

	public void setData3(String data3) {
		this.data3 = data3;
	}

	public long getFormid() {
		return this.formid;
	}

	public void setFormid(long formid) {
		this.formid = formid;
	}

	public String getRecid() {
		return this.recid;
	}

	public void setRecid(String recid) {
		this.recid = recid;
	}

	public long getAuditid() {
		return auditid;
	}

	public void setAuditid(long auditid) {
		this.auditid = auditid;
	}

	
	
}