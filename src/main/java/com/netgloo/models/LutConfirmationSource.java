package com.netgloo.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the LUT_CONFIRMATION_SOURCE database table.
 * 
 */
@Entity
@Table(name="LUT_CONFIRMATION_SOURCE")
@NamedQuery(name="LutConfirmationSource.findAll", query="SELECT l FROM LutConfirmationSource l")
public class LutConfirmationSource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LUT_CONFIRMATION_SOURCE_SEQ", sequenceName="LUT_CONFIRMATION_SOURCE_SEQ", allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LUT_CONFIRMATION_SOURCE_SEQ")
	private long id;

	private String name;

	@Column(name="\"TYPE\"")
	private String type;
	
	//bi-directional many-to-one association to LnkTryoutConfSource
	@OneToMany(mappedBy="lutConfirmationSource")
	private List<LnkTryoutConfSource> lnkTryoutConfSources;

	public LutConfirmationSource() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<LnkTryoutConfSource> getLnkTryoutConfSources() {
		return this.lnkTryoutConfSources;
	}

	public void setLnkTryoutConfSources(List<LnkTryoutConfSource> lnkTryoutConfSources) {
		this.lnkTryoutConfSources = lnkTryoutConfSources;
	}

	public LnkTryoutConfSource addLnkTryoutConfSource(LnkTryoutConfSource lnkTryoutConfSource) {
		getLnkTryoutConfSources().add(lnkTryoutConfSource);
		lnkTryoutConfSource.setLutConfirmationSource(this);

		return lnkTryoutConfSource;
	}

	public LnkTryoutConfSource removeLnkTryoutConfSource(LnkTryoutConfSource lnkTryoutConfSource) {
		getLnkTryoutConfSources().remove(lnkTryoutConfSource);
		lnkTryoutConfSource.setLutConfirmationSource(null);

		return lnkTryoutConfSource;
	}

}