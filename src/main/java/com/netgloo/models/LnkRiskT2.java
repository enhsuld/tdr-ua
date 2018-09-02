package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LNK_RISK_T2 database table.
 * 
 */
@Entity
@Table(name="LNK_RISK_T2")
@NamedQuery(name="LnkRiskT2.findAll", query="SELECT l FROM LnkRiskT2 l")
public class LnkRiskT2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_RISK_T2_N_SEQ", sequenceName="LNK_RISK_T2_N_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_RISK_T2_N_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private long riskid;
	
	private long t2id;
	
	private long dirid;
	
	private long tryid;
	
	private long rtype;
	
	private long data1;
	
	private long data2;
	
	private long data3;
	
	private long data4;
	
	private boolean data5;
	
	private boolean data6;
	
	private boolean data7;
	
	private String description;
	
	@Transient
	private String criname;
	
	@Transient
	private String dirname;
	
	@Transient
	private String riskname;

	//bi-directional many-to-one association to LnkMainFormT2
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="t2id", insertable=false,updatable=false)
	private LnkMainFormT2 lnkMainFormT2;
	
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="riskid", insertable=false,updatable=false)
	private LutRisk lutRisk;

	
	//bi-directional many-to-one association to LnkRiskTryout
	@OneToMany(mappedBy="lnkRiskT2")
	@JsonBackReference
	private List<LnkRiskTryout> lnkRiskTryouts;
	
	
	public LnkRiskT2() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getRiskid() {
		return this.riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}
	
	public long getTryid() {
		return tryid;
	}

	public void setTryid(long tryid) {
		this.tryid = tryid;
	}

	public long getRtype() {
		return rtype;
	}

	public void setRtype(long rtype) {
		this.rtype = rtype;
	}

	public long getT2id() {
		return t2id;
	}

	public void setT2id(long t2id) {
		this.t2id = t2id;
	}

	public LnkMainFormT2 getLnkMainFormT2() {
		return this.lnkMainFormT2;
	}

	public void setLnkMainFormT2(LnkMainFormT2 lnkMainFormT2) {
		this.lnkMainFormT2 = lnkMainFormT2;
	}
	
	public LutRisk getLutRisk() {
		return this.lutRisk;
	}

	public void setLutRisk(LutRisk lutRisk) {
		this.lutRisk = lutRisk;
	}

	public long getDirid() {
		return dirid;
	}

	public void setDirid(long dirid) {
		this.dirid = dirid;
	}

	public long getData1() {
		return data1;
	}

	public void setData1(long data1) {
		this.data1 = data1;
	}

	public long getData2() {
		return data2;
	}

	public void setData2(long data2) {
		this.data2 = data2;
	}

	public long getData3() {
		return data3;
	}

	public void setData3(long data3) {
		this.data3 = data3;
	}

	public long getData4() {
		return data4;
	}

	public void setData4(long data4) {
		this.data4 = data4;
	}

	public boolean isData5() {
		return data5;
	}

	public void setData5(boolean data5) {
		this.data5 = data5;
	}

	public boolean isData6() {
		return data6;
	}

	public void setData6(boolean data6) {
		this.data6 = data6;
	}

	public boolean isData7() {
		return data7;
	}

	public void setData7(boolean data7) {
		this.data7 = data7;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCriname() {
		return criname;
	}

	public void setCriname(String criname) {
		this.criname = criname;
	}

	public String getDirname() {
		return dirname;
	}

	public void setDirname(String dirname) {
		this.dirname = dirname;
	}

	public String getRiskname() {
		return riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public List<LnkRiskTryout> getLnkRiskTryouts() {
		return lnkRiskTryouts;
	}

	public void setLnkRiskTryouts(List<LnkRiskTryout> lnkRiskTryouts) {
		this.lnkRiskTryouts = lnkRiskTryouts;
	}
	
}