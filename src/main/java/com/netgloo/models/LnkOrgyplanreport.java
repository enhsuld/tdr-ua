package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LNK_ORGYPLANREPORT database table.
 * 
 */
@Entity
@Table(name="LNK_ORGYPLANREPORT")
@NamedQuery(name="LnkOrgyplanreport.findAll", query="SELECT l FROM LnkOrgyplanreport l")
public class LnkOrgyplanreport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SEQ_LNKORGYPLANREPORT", sequenceName="SEQ_LNKORGYPLANREPORT",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="SEQ_LNKORGYPLANREPORT")
	@Column(name = "id", unique = true, nullable = false)
	private long id;


	private long orgid;
	
	private long year;
	
	private String plan;
	
	private String report;
	
	private long auditresult;
	
	
	//bi-directional many-to-one association to SubAuditOrganization
	@ManyToOne
	@JoinColumn(name="orgid",nullable = false,insertable=false,updatable=false)
	private SubAuditOrganization subAuditOrganization;

	public LnkOrgyplanreport() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
		
	public long getOrgid() {
		return orgid;
	}

	public void setOrgid(long orgid) {
		this.orgid = orgid;
	}

	public String getPlan() {
		return this.plan;
	}

	public void setPlan(String plan) {
		this.plan = plan;
	}

	public String getReport() {
		return this.report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public long getYear() {
		return this.year;
	}

	public void setYear(long year) {
		this.year = year;
	}

	public long getAuditresult() {
		return auditresult;
	}

	public void setAuditresult(long auditresult) {
		this.auditresult = auditresult;
	}

	public SubAuditOrganization getSubAuditOrganization() {
		return this.subAuditOrganization;
	}

	public void setSubAuditOrganization(SubAuditOrganization subAuditOrganization) {
		this.subAuditOrganization = subAuditOrganization;
	}

}