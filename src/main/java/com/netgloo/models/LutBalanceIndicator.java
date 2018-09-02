package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LUT_BALANCE_INDICATOR database table.
 * 
 */
@Entity
@Table(name="LUT_BALANCE_INDICATOR")
@NamedQuery(name="LutBalanceIndicator.findAll", query="SELECT l FROM LutBalanceIndicator l")
public class LutBalanceIndicator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String acccode;

	private BigDecimal indcode;

	private String indname;

	public LutBalanceIndicator() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAcccode() {
		return this.acccode;
	}

	public void setAcccode(String acccode) {
		this.acccode = acccode;
	}

	public BigDecimal getIndcode() {
		return this.indcode;
	}

	public void setIndcode(BigDecimal indcode) {
		this.indcode = indcode;
	}

	public String getIndname() {
		return this.indname;
	}

	public void setIndname(String indname) {
		this.indname = indname;
	}

}