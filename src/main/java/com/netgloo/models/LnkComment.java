package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the LNK_COMMENT database table.
 * 
 */
@Entity
@Table(name="LNK_COMMENT")
@NamedQuery(name="LnkComment.findAll", query="SELECT l FROM LnkComment l")
public class LnkComment implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	//private String auditorname;

	private String comdate;

	private String comnote;

	private BigDecimal decisionid;

	private BigDecimal planid;

	//bi-directional many-to-one association to LutFormNote
	@ManyToOne
	@JoinColumn(name="NOTEID")
	private LutFormNote lutFormNote;

	//bi-directional many-to-one association to LutPosition
	@ManyToOne
	@JoinColumn(name="POSITIONID")
	private LutPosition lutPosition;

	//bi-directional many-to-one association to LutUser
	@ManyToOne
	@JoinColumn(name="AUDITORID")
	private LutUser lutUser;

	public LnkComment() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

/*	public String getAuditorname() {
		return this.auditorname;
	}

	public void setAuditorname(String auditorname) {
		this.auditorname = auditorname;
	}*/

	public String getComdate() {
		return this.comdate;
	}

	public void setComdate(String comdate) {
		this.comdate = comdate;
	}

	public String getComnote() {
		return this.comnote;
	}

	public void setComnote(String comnote) {
		this.comnote = comnote;
	}

	public BigDecimal getDecisionid() {
		return this.decisionid;
	}

	public void setDecisionid(BigDecimal decisionid) {
		this.decisionid = decisionid;
	}

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

	public LutFormNote getLutFormNote() {
		return this.lutFormNote;
	}

	public void setLutFormNote(LutFormNote lutFormNote) {
		this.lutFormNote = lutFormNote;
	}

	public LutPosition getLutPosition() {
		return this.lutPosition;
	}

	public void setLutPosition(LutPosition lutPosition) {
		this.lutPosition = lutPosition;
	}

	public LutUser getLutUser() {
		return this.lutUser;
	}

	public void setLutUser(LutUser lutUser) {
		this.lutUser = lutUser;
	}

}