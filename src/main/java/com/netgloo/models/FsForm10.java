package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM10 database table.
 * 
 */
@Entity
@Table(name="FS_FORM10")
@NamedQuery(name="FsForm10.findAll", query="SELECT f FROM FsForm10 f")
public class FsForm10 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String accountcode;

	private String balanceind;

	private BigDecimal planid;

	public FsForm10() {
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

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

}