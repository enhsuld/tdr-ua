package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM7 database table.
 * 
 */
@Entity
@Table(name="FS_FORM7")
@NamedQuery(name="FsForm7.findAll", query="SELECT f FROM FsForm7 f")
public class FsForm7 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String accountcode;

	private String balanceind;

	private BigDecimal c1;

	private BigDecimal c2;

	private BigDecimal planid;

	public FsForm7() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountcode() {
		return this.accountcode;
	}

	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}

	public String getBalanceind() {
		return this.balanceind;
	}

	public void setBalanceind(String balanceind) {
		this.balanceind = balanceind;
	}

	public BigDecimal getC1() {
		return this.c1;
	}

	public void setC1(BigDecimal c1) {
		this.c1 = c1;
	}

	public BigDecimal getC2() {
		return this.c2;
	}

	public void setC2(BigDecimal c2) {
		this.c2 = c2;
	}

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

}