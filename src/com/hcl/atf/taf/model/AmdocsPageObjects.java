package com.hcl.atf.taf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="amdocs_page_objects")
public class AmdocsPageObjects implements Serializable{
	
	private Integer amdocsPageObjectId;
	private String name;
	private String packageName;
	private Date createdOn;
	private UserList userlist;
	private ProductMaster productMaster;
	private Set<AmdocsPageMethods> amdocsPageMethods;
	private TestCaseList testCaseList;
	
    public AmdocsPageObjects() {
		
	}

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="amdocsPageObjectId", unique=true, nullable=false)
	public Integer getAmdocsPageObjectId() {
		return amdocsPageObjectId;
	}

	public void setAmdocsPageObjectId(Integer amdocsPageObjectId) {
		this.amdocsPageObjectId = amdocsPageObjectId;
	}

	@Column(name="name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="packageName")
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="createdBy")
	public UserList getUserlist() {
		return userlist;
	}

	public void setUserlist(UserList userlist) {
		this.userlist = userlist;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}

	@OneToMany(fetch=FetchType.LAZY, mappedBy="amdocsPageObjects", cascade={CascadeType.ALL})
	public Set<AmdocsPageMethods> getAmdocsPageMethods() {
		return amdocsPageMethods;
	}


	public void setAmdocsPageMethods(Set<AmdocsPageMethods> amdocsPageMethods) {
		this.amdocsPageMethods = amdocsPageMethods;
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="testCaseId")
	public TestCaseList getTestCaseList() {
		return testCaseList;
	}

	public void setTestCaseList(TestCaseList testCaseList) {
		this.testCaseList = testCaseList;
	}
    
   
}
