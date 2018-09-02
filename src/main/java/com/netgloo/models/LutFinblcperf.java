package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_FINBLCPERF database table.
 * 
 */
@Entity
@Table(name="LUT_FINBLCPERF")
@NamedQuery(name="LutFinblcperf.findAll", query="SELECT l FROM LutFinblcperf l")
public class LutFinblcperf implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private String acc;

	private String code;

	private String name;

	public LutFinblcperf() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAcc() {
		return this.acc;
	}

	public void setAcc(String acc) {
		this.acc = acc;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}