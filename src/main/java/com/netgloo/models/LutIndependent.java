package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_INDEPENDENT database table.
 * 
 */
@Entity
@Table(name="LUT_INDEPENDENT")
@NamedQuery(name="LutIndependent.findAll", query="SELECT l FROM LutIndependent l")
public class LutIndependent implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String address;

	private String licexpiredate;

	private String licnum;

	private String name;

	private String phone;

	private String reg;

	private String web;
	
	private boolean isactive;

	public LutIndependent() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLicexpiredate() {
		return this.licexpiredate;
	}

	public void setLicexpiredate(String licexpiredate) {
		this.licexpiredate = licexpiredate;
	}

	public String getLicnum() {
		return this.licnum;
	}

	public void setLicnum(String licnum) {
		this.licnum = licnum;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReg() {
		return this.reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	public String getWeb() {
		return this.web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public boolean getIsactive() {
		return isactive;
	}

	public void setIsactive(boolean isactive) {
		this.isactive = isactive;
	}	

}