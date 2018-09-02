package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_NEWSTYPE database table.
 * 
 */
@Entity
@Table(name="LNK_NEWSTYPE")
@NamedQuery(name="LnkNewstype.findAll", query="SELECT l FROM LnkNewstype l")
public class LnkNewstype implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_NEWSTYPE_SEQ", sequenceName="LNK_NEWSTYPE_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_NEWSTYPE_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	private long newsid;
	
	private long newstypeid;



	//bi-directional many-to-one association to New
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="newsid",insertable=false,updatable=false)
	private LutNews lutNews;
	
	//bi-directional many-to-one association to LutNewstype
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="newstypeid",insertable=false,updatable=false)
	private LutNewstype lutNewstype;

	public LnkNewstype() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LutNewstype getLutNewstype() {
		return this.lutNewstype;
	}

	public void setLutNewstype(LutNewstype lutNewstype) {
		this.lutNewstype = lutNewstype;
	}

	public LutNews getLutNews() {
		return this.lutNews;
	}

	public void setLutNews(LutNews lutNews) {
		this.lutNews = lutNews;
	}

	public long getNewsid() {
		return newsid;
	}

	public void setNewsid(long newsid) {
		this.newsid = newsid;
	}

	public long getNewstypeid() {
		return newstypeid;
	}

	public void setNewstypeid(long newstypeid) {
		this.newstypeid = newstypeid;
	}
	
	

}