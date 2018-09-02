package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM3 database table.
 * 
 */
@Entity
@Table(name="FS_FORM3")
@NamedQuery(name="FsForm3.findAll", query="SELECT f FROM FsForm3 f")
public class FsForm3 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String jbankacc;

	private String jcredit;

	private String jdate;

	private String jdebt;

	private BigDecimal jdocid;

	private String jexp;

	private BigDecimal jinquan;

	private String jinvcode;

	private String jinvname;

	private String jmeas;

	private String jorgbank;

	private String jorgname;

	private BigDecimal jregid;

	private String jtotal;

	private String jtrandesc;

	private BigDecimal planid;

	private BigDecimal progcode;

	public FsForm3() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getJbankacc() {
		return this.jbankacc;
	}

	public void setJbankacc(String jbankacc) {
		this.jbankacc = jbankacc;
	}

	public String getJcredit() {
		return this.jcredit;
	}

	public void setJcredit(String jcredit) {
		this.jcredit = jcredit;
	}

	public String getJdate() {
		return this.jdate;
	}

	public void setJdate(String jdate) {
		this.jdate = jdate;
	}

	public String getJdebt() {
		return this.jdebt;
	}

	public void setJdebt(String jdebt) {
		this.jdebt = jdebt;
	}

	public BigDecimal getJdocid() {
		return this.jdocid;
	}

	public void setJdocid(BigDecimal jdocid) {
		this.jdocid = jdocid;
	}

	public String getJexp() {
		return this.jexp;
	}

	public void setJexp(String jexp) {
		this.jexp = jexp;
	}

	public BigDecimal getJinquan() {
		return this.jinquan;
	}

	public void setJinquan(BigDecimal jinquan) {
		this.jinquan = jinquan;
	}

	public String getJinvcode() {
		return this.jinvcode;
	}

	public void setJinvcode(String jinvcode) {
		this.jinvcode = jinvcode;
	}

	public String getJinvname() {
		return this.jinvname;
	}

	public void setJinvname(String jinvname) {
		this.jinvname = jinvname;
	}

	public String getJmeas() {
		return this.jmeas;
	}

	public void setJmeas(String jmeas) {
		this.jmeas = jmeas;
	}

	public String getJorgbank() {
		return this.jorgbank;
	}

	public void setJorgbank(String jorgbank) {
		this.jorgbank = jorgbank;
	}

	public String getJorgname() {
		return this.jorgname;
	}

	public void setJorgname(String jorgname) {
		this.jorgname = jorgname;
	}

	public BigDecimal getJregid() {
		return this.jregid;
	}

	public void setJregid(BigDecimal jregid) {
		this.jregid = jregid;
	}

	public String getJtotal() {
		return this.jtotal;
	}

	public void setJtotal(String jtotal) {
		this.jtotal = jtotal;
	}

	public String getJtrandesc() {
		return this.jtrandesc;
	}

	public void setJtrandesc(String jtrandesc) {
		this.jtrandesc = jtrandesc;
	}

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

	public BigDecimal getProgcode() {
		return this.progcode;
	}

	public void setProgcode(BigDecimal progcode) {
		this.progcode = progcode;
	}

}