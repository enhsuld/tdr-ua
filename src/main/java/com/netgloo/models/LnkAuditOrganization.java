package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_AUDIT_ORGANIZATION database table.
 * 
 */
@Entity
@Table(name="LNK_AUDIT_ORGANIZATION")
@NamedQuery(name="LnkAuditOrganization.findAll", query="SELECT l FROM LnkAuditOrganization l")
public class LnkAuditOrganization implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private long auditorgid;

	private int ayear;

	private String orgcode;

	private String orgname;

	private int orgtype;

	private long quataid;
	
	private long reasonid;
	
	private long preid;
	
	private boolean distribution;
	

	//bi-directional many-to-one association to PreAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="preid",insertable=false,updatable=false)
	private PreAuditRegistration preAuditRegistration;

	public LnkAuditOrganization() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAuditorgid() {
		return this.auditorgid;
	}

	public void setAuditorgid(long auditorgid) {
		this.auditorgid = auditorgid;
	}

	public int getAyear() {
		return this.ayear;
	}

	public void setAyear(int ayear) {
		this.ayear = ayear;
	}

	public String getOrgcode() {
		return this.orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
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

	public long getQuataid() {
		return this.quataid;
	}

	public void setQuataid(long quataid) {
		this.quataid = quataid;
	}
		
	public long getReasonid() {
		return reasonid;
	}

	public void setReasonid(long reasonid) {
		this.reasonid = reasonid;
	}
	
	public long getPreid() {
		return preid;
	}

	public void setPreid(long preid) {
		this.preid = preid;
	}
	
	public boolean isDistribution() {
		return distribution;
	}

	public void setDistribution(boolean distribution) {
		this.distribution = distribution;
	}

	public PreAuditRegistration getPreAuditRegistration() {
		return this.preAuditRegistration;
	}

	public void setPreAuditRegistration(PreAuditRegistration preAuditRegistration) {
		this.preAuditRegistration = preAuditRegistration;
	}

}