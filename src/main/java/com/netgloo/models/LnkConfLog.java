package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LNK_CONF_LOG database table.
 * 
 */
@Entity
@Table(name="LNK_CONF_LOG")
@NamedQuery(name="LnkConfLog.findAll", query="SELECT l FROM LnkConfLog l")
public class LnkConfLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_CONF_LOG_SEQ", sequenceName="LNK_CONF_LOG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_CONF_LOG_SEQ")
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	private long id;

	private String confdat;

	private String olddata;
	
	private String description;

	private long userid;

	public LnkConfLog() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getConfdat() {
		return this.confdat;
	}

	public void setConfdat(String confdat) {
		this.confdat = confdat;
	}

	public String getOlddata() {
		return this.olddata;
	}

	public void setOlddata(String olddata) {
		this.olddata = olddata;
	}

	public long getUserid() {
		return this.userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}