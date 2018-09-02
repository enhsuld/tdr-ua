package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LutConfirmationSource;
import com.netgloo.models.LutTryout;


/**
 * The persistent class for the LNK_TRYOUT_CONF_SOURCE database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_CONF_SOURCE")
@NamedQuery(name="LnkTryoutConfSource.findAll", query="SELECT l FROM LnkTryoutConfSource l")
public class LnkTryoutConfSource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long sourceid;
	
	private long tryid;
	
	private long typeid;

	//bi-directional many-to-one association to LutConfirmationSource
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="sourceid",nullable = false,insertable=false,updatable=false)
	private LutConfirmationSource lutConfirmationSource;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="tryid",nullable = false,insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutConfSource() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutConfirmationSource getLutConfirmationSource() {
		return this.lutConfirmationSource;
	}

	public void setLutConfirmationSource(LutConfirmationSource lutConfirmationSource) {
		this.lutConfirmationSource = lutConfirmationSource;
	}

	public LutTryout getLutTryout() {
		return this.lutTryout;
	}

	public void setLutTryout(LutTryout lutTryout) {
		this.lutTryout = lutTryout;
	}

	public long getSourceid() {
		return sourceid;
	}

	public void setSourceid(long sourceid) {
		this.sourceid = sourceid;
	}

	public long getTryid() {
		return tryid;
	}

	public void setTryid(long tryid) {
		this.tryid = tryid;
	}

	public long getTypeid() {
		return typeid;
	}

	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}
	

}