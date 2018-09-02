package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the SUB_ADVICES database table.
 * 
 */
@Entity
@Table(name="SUB_ADVICES")
@NamedQuery(name="SubAdvice.findAll", query="SELECT s FROM SubAdvice s")
public class SubAdvice implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private BigDecimal auditorid;

	private String description;

	private String givendate;

	private BigDecimal isimplemented;

	private BigDecimal orgid;

	private BigDecimal planid;

	public SubAdvice() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAuditorid() {
		return this.auditorid;
	}

	public void setAuditorid(BigDecimal auditorid) {
		this.auditorid = auditorid;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGivendate() {
		return this.givendate;
	}

	public void setGivendate(String givendate) {
		this.givendate = givendate;
	}

	public BigDecimal getIsimplemented() {
		return this.isimplemented;
	}

	public void setIsimplemented(BigDecimal isimplemented) {
		this.isimplemented = isimplemented;
	}

	public BigDecimal getOrgid() {
		return this.orgid;
	}

	public void setOrgid(BigDecimal orgid) {
		this.orgid = orgid;
	}

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

}