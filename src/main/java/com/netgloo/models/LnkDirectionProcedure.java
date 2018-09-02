package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_DIRECTION_PROCEDURE database table.
 * 
 */
@Entity
@Table(name="LNK_DIRECTION_PROCEDURE")
@NamedQuery(name="LnkDirectionProcedure.findAll", query="SELECT l FROM LnkDirectionProcedure l")
public class LnkDirectionProcedure implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@Id
	@SequenceGenerator(name="LNK_DIRECTION_PROCEDURE_SEQ", sequenceName="LNK_DIRECTION_PROCEDURE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_DIRECTION_PROCEDURE_SEQ")
	private long id;

	private long directionid;
	
	private long procedureid;
	
	//bi-directional many-to-one association to LutAuditDir
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="directionid",insertable=false,updatable=false)
	private LutAuditDir lutAuditDir;

	//bi-directional many-to-one association to LutProcedure
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="procedureid",insertable=false,updatable=false)
	private LutProcedure lutProcedure;

	public LnkDirectionProcedure() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutAuditDir getLutAuditDir() {
		return this.lutAuditDir;
	}

	public void setLutAuditDir(LutAuditDir lutAuditDir) {
		this.lutAuditDir = lutAuditDir;
	}

	public LutProcedure getLutProcedure() {
		return this.lutProcedure;
	}

	public void setLutProcedure(LutProcedure lutProcedure) {
		this.lutProcedure = lutProcedure;
	}

	public long getDirectionid() {
		return directionid;
	}

	public void setDirectionid(long directionid) {
		this.directionid = directionid;
	}

	public long getProcedureid() {
		return procedureid;
	}

	public void setProcedureid(long procedureid) {
		this.procedureid = procedureid;
	}
	
	

}