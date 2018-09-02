package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LutConfirmationType;
import com.netgloo.models.LutTryout;


/**
 * The persistent class for the LNK_TRYOUT_CONF_TYPE database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_CONF_TYPE")
@NamedQuery(name="LnkTryoutConfType.findAll", query="SELECT l FROM LnkTryoutConfType l")
public class LnkTryoutConfType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long typeid;
	
	private long tryid;

	//bi-directional many-to-one association to LutConfirmationType
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="typeid",nullable = false,insertable=false,updatable=false)
	private LutConfirmationType lutConfirmationType;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="tryid",nullable = false,insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutConfType() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutConfirmationType getLutConfirmationType() {
		return this.lutConfirmationType;
	}

	public void setLutConfirmationType(LutConfirmationType lutConfirmationType) {
		this.lutConfirmationType = lutConfirmationType;
	}

	public LutTryout getLutTryout() {
		return this.lutTryout;
	}

	public void setLutTryout(LutTryout lutTryout) {
		this.lutTryout = lutTryout;
	}

	public long getTypeid() {
		return typeid;
	}

	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}

	public long getTryid() {
		return tryid;
	}

	public void setTryid(long tryid) {
		this.tryid = tryid;
	}

}