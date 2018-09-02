package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LUT_CONFLICT database table.
 * 
 */
@Entity
@Table(name="LUT_CONFLICT")
@NamedQuery(name="LutConflict.findAll", query="SELECT l FROM LutConflict l")
public class LutConflict implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String account;

	private int auditorid;

	private String conflict;
	
	private String confdate;

	private int orgid;

	private String resolve;

	private String total;

	public LutConflict() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getAuditorid() {
		return auditorid;
	}

	public void setAuditorid(int auditorid) {
		this.auditorid = auditorid;
	}

	public String getConflict() {
		return conflict;
	}

	public void setConflict(String conflict) {
		this.conflict = conflict;
	}

	public String getConfdate() {
		return confdate;
	}

	public void setConfdate(String confdate) {
		this.confdate = confdate;
	}

	public int getOrgid() {
		return orgid;
	}

	public void setOrgid(int orgid) {
		this.orgid = orgid;
	}

	public String getResolve() {
		return resolve;
	}

	public void setResolve(String resolve) {
		this.resolve = resolve;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}	

}