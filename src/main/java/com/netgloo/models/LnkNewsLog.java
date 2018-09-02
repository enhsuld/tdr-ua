package com.netgloo.models;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the DATA_MIN_PLAN_3 database table.
 * 
 */
@Entity
@Table(name="LNK_NEWS_LOG")
@NamedQuery(name="LnkNewsLog.findAll", query="SELECT d FROM LnkNewsLog d")
public class LnkNewsLog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="LNK_NEWS_LOG_SEQ", sequenceName="LNK_NEWS_LOG_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="LNK_NEWS_LOG_SEQ")
	@Column(name = "id", unique = true, nullable = false, precision = 22, scale = 0)
	private long id;
	
	private Long newsid;
	private Long userid;

	public LnkNewsLog() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
			
	public void setField(String aFieldName, Object aValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field aField = getClass().getDeclaredField(aFieldName);
		aField.set(this, aValue);
    }

	public Long getNewsid() {
		return newsid;
	}

	public void setNewsid(Long newsid) {
		this.newsid = newsid;
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}


}