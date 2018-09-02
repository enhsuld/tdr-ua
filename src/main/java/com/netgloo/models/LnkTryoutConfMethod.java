package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.netgloo.models.LutConfirmationMethod;
import com.netgloo.models.LutTryout;


/**
 * The persistent class for the LNK_TRYOUT_CONF_METHOD database table.
 * 
 */
@Entity
@Table(name="LNK_TRYOUT_CONF_METHOD")
@NamedQuery(name="LnkTryoutConfMethod.findAll", query="SELECT l FROM LnkTryoutConfMethod l")
public class LnkTryoutConfMethod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private long methodid;
	
	private long tryid;

	//bi-directional many-to-one association to LutConfirmationMethod
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="methodid",nullable = false,insertable=false,updatable=false)
	private LutConfirmationMethod lutConfirmationMethod;

	//bi-directional many-to-one association to LutTryout
	@ManyToOne
	@JsonManagedReference
	@JoinColumn(name="tryid",nullable = false,insertable=false,updatable=false)
	private LutTryout lutTryout;

	public LnkTryoutConfMethod() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutConfirmationMethod getLutConfirmationMethod() {
		return this.lutConfirmationMethod;
	}

	public void setLutConfirmationMethod(LutConfirmationMethod lutConfirmationMethod) {
		this.lutConfirmationMethod = lutConfirmationMethod;
	}

	public LutTryout getLutTryout() {
		return this.lutTryout;
	}

	public void setLutTryout(LutTryout lutTryout) {
		this.lutTryout = lutTryout;
	}

	public long getMethodid() {
		return methodid;
	}

	public void setMethodid(long methodid) {
		this.methodid = methodid;
	}

	public long getTryid() {
		return tryid;
	}

	public void setTryid(long tryid) {
		this.tryid = tryid;
	}

}