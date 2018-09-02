package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FS_FORM2 database table.
 * 
 */
@Entity
@Table(name="FS_FORM2")
@NamedQuery(name="FsForm2.findAll", query="SELECT f FROM FsForm2 f")
public class FsForm2 implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	private BigDecimal annualamount;

	private BigDecimal ecocode;

	private String econame;

	private BigDecimal month1;

	private BigDecimal month10;

	private BigDecimal month11;

	private BigDecimal month12;

	private BigDecimal month2;

	private BigDecimal month3;

	private BigDecimal month4;

	private BigDecimal month5;

	private BigDecimal month6;

	private BigDecimal month7;

	private BigDecimal month8;

	private BigDecimal month9;

	private BigDecimal planid;

	private BigDecimal progcode;

	public FsForm2() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BigDecimal getAnnualamount() {
		return this.annualamount;
	}

	public void setAnnualamount(BigDecimal annualamount) {
		this.annualamount = annualamount;
	}

	public BigDecimal getEcocode() {
		return this.ecocode;
	}

	public void setEcocode(BigDecimal ecocode) {
		this.ecocode = ecocode;
	}

	public String getEconame() {
		return this.econame;
	}

	public void setEconame(String econame) {
		this.econame = econame;
	}

	public BigDecimal getMonth1() {
		return this.month1;
	}

	public void setMonth1(BigDecimal month1) {
		this.month1 = month1;
	}

	public BigDecimal getMonth10() {
		return this.month10;
	}

	public void setMonth10(BigDecimal month10) {
		this.month10 = month10;
	}

	public BigDecimal getMonth11() {
		return this.month11;
	}

	public void setMonth11(BigDecimal month11) {
		this.month11 = month11;
	}

	public BigDecimal getMonth12() {
		return this.month12;
	}

	public void setMonth12(BigDecimal month12) {
		this.month12 = month12;
	}

	public BigDecimal getMonth2() {
		return this.month2;
	}

	public void setMonth2(BigDecimal month2) {
		this.month2 = month2;
	}

	public BigDecimal getMonth3() {
		return this.month3;
	}

	public void setMonth3(BigDecimal month3) {
		this.month3 = month3;
	}

	public BigDecimal getMonth4() {
		return this.month4;
	}

	public void setMonth4(BigDecimal month4) {
		this.month4 = month4;
	}

	public BigDecimal getMonth5() {
		return this.month5;
	}

	public void setMonth5(BigDecimal month5) {
		this.month5 = month5;
	}

	public BigDecimal getMonth6() {
		return this.month6;
	}

	public void setMonth6(BigDecimal month6) {
		this.month6 = month6;
	}

	public BigDecimal getMonth7() {
		return this.month7;
	}

	public void setMonth7(BigDecimal month7) {
		this.month7 = month7;
	}

	public BigDecimal getMonth8() {
		return this.month8;
	}

	public void setMonth8(BigDecimal month8) {
		this.month8 = month8;
	}

	public BigDecimal getMonth9() {
		return this.month9;
	}

	public void setMonth9(BigDecimal month9) {
		this.month9 = month9;
	}

	public BigDecimal getPlanid() {
		return this.planid;
	}

	public void setPlanid(BigDecimal planid) {
		this.planid = planid;
	}

	public BigDecimal getProgcode() {
		return this.progcode;
	}

	public void setProgcode(BigDecimal progcode) {
		this.progcode = progcode;
	}

}