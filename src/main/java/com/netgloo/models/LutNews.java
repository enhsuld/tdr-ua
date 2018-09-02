package com.netgloo.models;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the NEWS database table.
 * 
 */
@Entity
@Table(name="LUT_NEWS")
@NamedQuery(name="LutNews.findAll", query="SELECT n FROM LutNews n")
public class LutNews implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="NEWS_SEQ", sequenceName="NEWS_SEQ",allocationSize=1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="NEWS_SEQ")
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	private String imgurl;

	private boolean ishighlight;

	private String newsdate;

	@Lob
	private String newstext;

	private String newstitle;
	
	private boolean isfile;
	
	private long showid;
	
	private long userid;
	

	//bi-directional many-to-one association to LnkNewstype
	@OneToMany(mappedBy="lutNews")
	@JsonManagedReference
	@OrderBy("id asc")
	private List<LnkNewstype> lnkNewstypes;
	
	//bi-directional many-to-one association to LnkNewstype
	@OneToMany(mappedBy="lutNews")
	@JsonManagedReference
	@OrderBy("id asc")	
	private List<LnkNewsAfile> lnkNewsAfiles;

	public LutNews() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImgurl() {
		return this.imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public boolean getIshighlight() {
		return this.ishighlight;
	}

	public void setIshighlight(boolean ishighlight) {
		this.ishighlight = ishighlight;
	}

	public boolean isIsfile() {
		return isfile;
	}

	public void setIsfile(boolean isfile) {
		this.isfile = isfile;
	}

	public long getShowid() {
		return showid;
	}

	public void setShowid(long showid) {
		this.showid = showid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getNewsdate() {
		return this.newsdate;
	}

	public void setNewsdate(String newsdate) {
		this.newsdate = newsdate;
	}

	public String getNewstext() {
		return this.newstext;
	}

	public void setNewstext(String newstext) {
		this.newstext = newstext;
	}

	public String getNewstitle() {
		return this.newstitle;
	}

	public void setNewstitle(String newstitle) {
		this.newstitle = newstitle;
	}
	@JsonBackReference
	public List<LnkNewstype> getLnkNewstypes() {
		return this.lnkNewstypes;
	}

	public void setLnkNewstypes(List<LnkNewstype> lnkNewstypes) {
		this.lnkNewstypes = lnkNewstypes;
	}

	public LnkNewstype addLnkNewstype(LnkNewstype lnkNewstype) {
		getLnkNewstypes().add(lnkNewstype);
		lnkNewstype.setLutNews(this);

		return lnkNewstype;
	}

	public LnkNewstype removeLnkNewstype(LnkNewstype lnkNewstype) {
		getLnkNewstypes().remove(lnkNewstype);
		lnkNewstype.setLutNews(null);

		return lnkNewstype;
	}
	
	public List<LnkNewsAfile> getLnkNewsAfiles() {
		return this.lnkNewsAfiles;
	}

	public void setLnkNewsAfiles(List<LnkNewsAfile> lnkNewsAfiles) {
		this.lnkNewsAfiles = lnkNewsAfiles;
	}

	public LnkNewsAfile addLnkNewsAfile(LnkNewsAfile lnkNewsAfile) {
		getLnkNewsAfiles().add(lnkNewsAfile);
		lnkNewsAfile.setLutNews(this);

		return lnkNewsAfile;
	}

	public LnkNewsAfile removeLnkNewsAfile(LnkNewsAfile lnkNewsAfile) {
		getLnkNewsAfiles().remove(lnkNewsAfile);
		lnkNewsAfile.setLutNews(null);

		return lnkNewsAfile;
	}

}