package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM17 database table.
 * 
 */
@Entity
@Table(name="FS_FORM17")
@NamedQuery(name="FsForm17.findAll", query="SELECT f FROM FsForm17 f")
public class FsForm17 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private BigDecimal planid;

	public FsForm17() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

}