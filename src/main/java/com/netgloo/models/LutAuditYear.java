package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LUT_AUDIT_YEAR database table.
 * 
 */
@Entity
@Table(name="LUT_AUDIT_YEAR")
@NamedQuery(name="LutAuditYear.findAll", query="SELECT l FROM LutAuditYear l")
public class LutAuditYear implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String audityear;

	private boolean isactive;

	public LutAuditYear() {
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

	public boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}

}