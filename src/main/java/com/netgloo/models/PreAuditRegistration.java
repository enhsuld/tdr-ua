package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.netgloo.models.LutCategory;

import java.util.List;


/**
 * The persistent class for the PRE_AUDIT_REGISTRATION database table.
 * 
 */
@Entity
@Table(name="PRE_AUDIT_REGISTRATION")
@NamedQuery(name="PreAuditRegistration.findAll", query="SELECT p FROM PreAuditRegistration p")
public class PreAuditRegistration implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String audityear;

	private String orgname;
	
	private String shortcode;
	
	private String qtype;

	private int orgtype;

	private int stepid;
	
	private int total;
	
	private long depid;


	
	//bi-directional many-to-one association to LutCategory
	@ManyToOne
	@JoinColumn(name="orgtype", insertable=false,updatable=false)
	@JsonBackReference
	private LutCategory lutCategory;

		
	//bi-directional many-to-one association to LnkAuditOrganization
	@OneToMany(mappedBy="preAuditRegistration")
	@JsonBackReference
	private List<LnkAuditOrganization> lnkAuditOrganizations;

	public PreAuditRegistration() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAudityear() {
		return this.audityear;
	}

	public void setAudityear(String audityear) {
		this.audityear = audityear;
	}

	public String getOrgname() {
		return this.orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public int getOrgtype() {
		return this.orgtype;
	}

	public void setOrgtype(int orgtype) {
		this.orgtype = orgtype;
	}

	public int getStepid() {
		return this.stepid;
	}

	public void setStepid(int stepid) {
		this.stepid = stepid;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	
	public long getDepid() {
		return depid;
	}

	public void setDepid(long depid) {
		this.depid = depid;
	}

	public String getQtype() {
		return qtype;
	}

	public void setQtype(String qtype) {
		this.qtype = qtype;
	}

	
	public String getShortcode() {
		return shortcode;
	}

	public void setShortcode(String shortcode) {
		this.shortcode = shortcode;
	}

	public List<LnkAuditOrganization> getLnkAuditOrganizations() {
		return this.lnkAuditOrganizations;
	}

	public void setLnkAuditOrganizations(List<LnkAuditOrganization> lnkAuditOrganizations) {
		this.lnkAuditOrganizations = lnkAuditOrganizations;
	}

	public LnkAuditOrganization addLnkAuditOrganization(LnkAuditOrganization lnkAuditOrganization) {
		getLnkAuditOrganizations().add(lnkAuditOrganization);
		lnkAuditOrganization.setPreAuditRegistration(this);

		return lnkAuditOrganization;
	}

	public LnkAuditOrganization removeLnkAuditOrganization(LnkAuditOrganization lnkAuditOrganization) {
		getLnkAuditOrganizations().remove(lnkAuditOrganization);
		lnkAuditOrganization.setPreAuditRegistration(null);

		return lnkAuditOrganization;
	}
	
	public LutCategory getLutCategory() {
		return this.lutCategory;
	}

	public void setLutCategory(LutCategory lutCategory) {
		this.lutCategory = lutCategory;
	}

}