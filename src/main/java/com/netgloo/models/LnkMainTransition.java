package com.netgloo.models;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_MAIN_TRANSITION database table.
 * 
 */
@Entity
@Table(name="LNK_MAIN_TRANSITION")
@NamedQuery(name="LnkMainTransition.findAll", query="SELECT l FROM LnkMainTransition l")
public class LnkMainTransition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_MAIN_TRANSITION_SEQ", sequenceName="LNK_MAIN_TRANSITION_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_MAIN_TRANSITION_SEQ")
	@Column(name = "id", unique = true, nullable = false)	
	private long id;

	private long stepid;

	private long wid;
	
	private long mid;
	
	private long levelid;
	
	private long parentid;
	
	private long pstep;

	//bi-directional many-to-one association to MainAuditRegistration
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="mid", insertable=false, updatable=false)
	private MainAuditRegistration mainAuditRegistration;

	public LnkMainTransition() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getLevelid() {
		return levelid;
	}

	public void setLevelid(long levelid) {
		this.levelid = levelid;
	}

	public long getMid() {
		return mid;
	}

	public void setMid(long mid) {
		this.mid = mid;
	}

	public long getStepid() {
		return this.stepid;
	}

	public void setStepid(long stepid) {
		this.stepid = stepid;
	}

	public long getWid() {
		return this.wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public MainAuditRegistration getMainAuditRegistration() {
		return this.mainAuditRegistration;
	}

	public void setMainAuditRegistration(MainAuditRegistration mainAuditRegistration) {
		this.mainAuditRegistration = mainAuditRegistration;
	}

	public long getParentid() {
		return parentid;
	}

	public void setParentid(long parentid) {
		this.parentid = parentid;
	}

	public long getPstep() {
		return pstep;
	}

	public void setPstep(long pstep) {
		this.pstep = pstep;
	}

	
}