package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the LUT_DECISIONS database table.
 * 
 */
@Entity
@Table(name="LUT_DECISIONS")
@NamedQuery(name="LutDecision.findAll", query="SELECT l FROM LutDecision l")
public class LutDecision implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long decisionid;

	private String decisionname;

	public LutDecision() {
	}

	public long getDecisionid() {
		return this.decisionid;
	}

	public void setDecisionid(long decisionid) {
		this.decisionid = decisionid;
	}

	public String getDecisionname() {
		return this.decisionname;
	}

	public void setDecisionname(String decisionname) {
		this.decisionname = decisionname;
	}

}