package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LUT_AUDIT_WORK_TYPE database table.
 * 
 */
@Entity
@Table(name="LUT_AUDIT_WORK_TYPE")
@NamedQuery(name="LutAuditWorkType.findAll", query="SELECT l FROM LutAuditWorkType l")
public class LutAuditWorkType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private boolean isactive;

	private String shortname;

	private long typeid;

	private String typename;

	public LutAuditWorkType() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public String getShortname() {
		return this.shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public long getTypeid() {
		return this.typeid;
	}

	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}

	public String getTypename() {
		return this.typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

}