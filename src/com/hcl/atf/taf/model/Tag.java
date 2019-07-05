package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag implements java.io.Serializable{
	private Integer tagId;
	private String tagName;
	private Set<BDDKeywordsPhrases> bddKeywordsSet;
	
	public Tag(){
		
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "tagId", unique = true, nullable = false)
	 
	public Integer getTagId() {
		return tagId;
	}
	public void setTagId(Integer tagId) {
		this.tagId = tagId;
	}

	
	@Column(name = "tagName")
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	
		
	@ManyToMany(fetch = FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinTable(name = "bdd_keyword_phrases_has_tag", joinColumns = { @JoinColumn(name = "tagId", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "bddKeywordId", nullable = false, updatable = false) })
	public Set<BDDKeywordsPhrases> getBddKeywordsSet() {
		return bddKeywordsSet;
	}

	public void setBddKeywordsSet(Set<BDDKeywordsPhrases> bddKeywordsSet) {
		this.bddKeywordsSet = bddKeywordsSet;
	}

	public boolean equals(Tag tag) {
	
		if (tag == null)
			return false;
		//Tag tag = (Tag) tag;
		if (tag.getTagId().equals(this.tagId)) {
			System.out.println("tag  equals");
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) tagId;
	  }
}
