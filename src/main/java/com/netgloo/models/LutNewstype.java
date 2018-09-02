package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the LUT_NEWSTYPE database table.
 * 
 */
@Entity
@Table(name="LUT_NEWSTYPE")
@NamedQuery(name="LutNewstype.findAll", query="SELECT l FROM LutNewstype l")
public class LutNewstype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_NEWSTYPE_SEQ", sequenceName="LUT_NEWSTYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_NEWSTYPE_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String newstypename;

	//bi-directional many-to-one association to LnkNewstype
	@OneToMany(mappedBy="lutNewstype")
	private List<LnkNewstype> lnkNewstypes;

	public LutNewstype() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNewstypename() {
		return this.newstypename;
	}

	public void setNewstypename(String newstypename) {
		this.newstypename = newstypename;
	}

	public List<LnkNewstype> getLnkNewstypes() {
		return this.lnkNewstypes;
	}

	public void setLnkNewstypes(List<LnkNewstype> lnkNewstypes) {
		this.lnkNewstypes = lnkNewstypes;
	}

	public LnkNewstype addLnkNewstype(LnkNewstype lnkNewstype) {
		getLnkNewstypes().add(lnkNewstype);
		lnkNewstype.setLutNewstype(this);

		return lnkNewstype;
	}

	public LnkNewstype removeLnkNewstype(LnkNewstype lnkNewstype) {
		getLnkNewstypes().remove(lnkNewstype);
		lnkNewstype.setLutNewstype(null);

		return lnkNewstype;
	}

}