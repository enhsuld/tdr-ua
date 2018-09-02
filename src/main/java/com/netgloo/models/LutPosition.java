package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the LUT_POSITION database table.
 * 
 */
@Entity
@Table(name="LUT_POSITION")
@NamedQuery(name="LutPosition.findAll", query="SELECT l FROM LutPosition l")
public class LutPosition implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private Boolean isactive;

	private long orderid;

	private String positionname;
	
	private Boolean isstate;

	//bi-directional many-to-one association to LnkComment
	@OneToMany(mappedBy="lutPosition")
	private List<LnkComment> lnkComments;

	public LutPosition() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Boolean getIsactive() {
		return this.isactive;
	}

	public void setIsactive(Boolean isactive) {
		this.isactive = isactive;
	}

	public long getOrderid() {
		return this.orderid;
	}

	public void setOrderid(long orderid) {
		this.orderid = orderid;
	}

	public String getPositionname() {
		return this.positionname;
	}

	public void setPositionname(String positionname) {
		this.positionname = positionname;
	}

	public Boolean getIsstate() {
		return isstate;
	}

	public void setIsstate(Boolean isstate) {
		this.isstate = isstate;
	}

	public List<LnkComment> getLnkComments() {
		return this.lnkComments;
	}

	public void setLnkComments(List<LnkComment> lnkComments) {
		this.lnkComments = lnkComments;
	}

	public LnkComment addLnkComment(LnkComment lnkComment) {
		getLnkComments().add(lnkComment);
		lnkComment.setLutPosition(this);

		return lnkComment;
	}

	public LnkComment removeLnkComment(LnkComment lnkComment) {
		getLnkComments().remove(lnkComment);
		lnkComment.setLutPosition(null);

		return lnkComment;
	}

}