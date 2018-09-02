package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LUT_VALIDATION database table.
 * 
 */
@Entity
@Table(name="LUT_VALIDATION")
@NamedQuery(name="LutValidation.findAll", query="SELECT l FROM LutValidation l")
public class LutValidation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_VALIDATION_SEQ", sequenceName="LUT_VALIDATION_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_VALIDATION_SEQ")
	private long id;

	private long balanceid;

	private String code1;

	private String code2;

	private long isformula1;

	private long isformula2;

	private String position1;

	private String position2;

	private String title1;

	private String title2;

	private long valid;
	
	private long adirid;
	
	private long riskid;
	
	private Long tryoutid;
	private Long factorid;

	public LutValidation() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBalanceid() {
		return this.balanceid;
	}

	public void setBalanceid(long balanceid) {
		this.balanceid = balanceid;
	}

	public String getCode1() {
		return this.code1;
	}

	public void setCode1(String code1) {
		this.code1 = code1;
	}

	public String getCode2() {
		return this.code2;
	}

	public void setCode2(String code2) {
		this.code2 = code2;
	}

	public long getIsformula1() {
		return this.isformula1;
	}

	public void setIsformula1(long isformula1) {
		this.isformula1 = isformula1;
	}

	public long getIsformula2() {
		return this.isformula2;
	}

	public void setIsformula2(long isformula2) {
		this.isformula2 = isformula2;
	}

	public String getPosition1() {
		return this.position1;
	}

	public void setPosition1(String position1) {
		this.position1 = position1;
	}

	public String getPosition2() {
		return this.position2;
	}

	public void setPosition2(String position2) {
		this.position2 = position2;
	}

	public String getTitle1() {
		return this.title1;
	}

	public void setTitle1(String title1) {
		this.title1 = title1;
	}

	public String getTitle2() {
		return this.title2;
	}

	public void setTitle2(String title2) {
		this.title2 = title2;
	}

	public long getValid() {
		return this.valid;
	}

	public void setValid(long valid) {
		this.valid = valid;
	}

	public long getAdirid() {
		return adirid;
	}

	public void setAdirid(long adirid) {
		this.adirid = adirid;
	}

	public long getRiskid() {
		return riskid;
	}

	public void setRiskid(long riskid) {
		this.riskid = riskid;
	}

	public Long getTryoutid() {
		return tryoutid;
	}

	public void setTryoutid(Long tryoutid) {
		this.tryoutid = tryoutid;
	}

	public Long getFactorid() {
		return factorid;
	}

	public void setFactorid(Long factorid) {
		this.factorid = factorid;
	}

}