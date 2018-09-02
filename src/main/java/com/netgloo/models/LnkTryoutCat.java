package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;


/**
 * The persistent class for the LNK_TRYOUT_CAT database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_CAT")
@NamedQuery(name="LnkTryoutCat.findAll", query="SELECT l FROM LnkTryoutCat l")
public class LnkTryoutCat implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_TRYOUT_CAT_SEQ", sequenceName="LNK_TRYOUT_CAT_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_TRYOUT_CAT_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private long dirid;
	
	private long catid;
	
	private long tryoutid;

	//bi-directional many-to-one association to LutCategory
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="catid",insertable=false,updatable=false)
	private LutCategory lutCategory;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="tryoutid",insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutCat() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getDirid() {
		return this.dirid;
	}

	public void setDirid(long dirid) {
		this.dirid = dirid;
	}

	public LutCategory getLutCategory() {
		return this.lutCategory;
	}

	public void setLutCategory(LutCategory lutCategory) {
		this.lutCategory = lutCategory;
	}

	public LutTryout getLutTryout() {
		return this.lutTryout;
	}

	public void setLutTryout(LutTryout lutTryout) {
		this.lutTryout = lutTryout;
	}

	public long getCatid() {
		return catid;
	}

	public void setCatid(long catid) {
		this.catid = catid;
	}

	public long getTryoutid() {
		return tryoutid;
	}

	public void setTryoutid(long tryoutid) {
		this.tryoutid = tryoutid;
	}

	
}