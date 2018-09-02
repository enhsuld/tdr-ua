package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;


/**
 * The persistent class for the LNK_NEWS_AFILE database table.
 * 
 */

@Entity
@Table(name="LNK_NEWS_AFILE")
@NamedQuery(name="LnkNewsAfile.findAll", query="SELECT d FROM LnkNewsAfile d")
public class LnkNewsAfile implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="SEQ_LNK_NEWS_AFILES", sequenceName="SEQ_LNK_NEWS_AFILES", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_LNK_NEWS_AFILES")
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	private long id;

	private String afilename;

	private String afileurl;
	
	private long newsid;

	private String afileext;
	
	private Long afilesize;

	//bi-directional many-to-one association to LutUser
	@ManyToOne
	@JsonBackReference
	@JoinColumn(name="newsid",insertable=false,updatable=false)
	private LutNews lutNews;
	
	
	
	public LnkNewsAfile() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAfilename() {
		return this.afilename;
	}

	public void setAfilename(String afilename) {
		this.afilename = afilename;
	}

	public String getAfileurl() {
		return this.afileurl;
	}

	public void setAfileurl(String afileurl) {
		this.afileurl = afileurl;
	}

	public String getAfileext() {
		return afileext;
	}

	public void setAfileext(String afileext) {
		this.afileext = afileext;
	}

	public Long getAfilesize() {
		return afilesize;
	}

	public void setAfilesize(Long afilesize) {
		this.afilesize = afilesize;
	}

	public long getNewsid() {
		return newsid;
	}

	public void setNewsid(long newsid) {
		this.newsid = newsid;
	}

	public void setLutNews(LutNews lutNews) {
		this.lutNews = lutNews;
	}

	public LutNews getLutNews() {
		return this.lutNews;
	}

	

}