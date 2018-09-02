package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FOMR11 database table.
 * 
 */
@Entity
@Table(name="FS_FOMR11")
@NamedQuery(name="FsFomr11.findAll", query="SELECT f FROM FsFomr11 f")
public class FsFomr11 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private BigDecimal planid;

	public FsFomr11() {
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