package com.hcl.atf.taf.mongodb.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hcl.atf.taf.model.ProductBuild;

@Document(collection = "builds")
public class ISEProductBuildMongo {
	@Id
	private String id;
	private String build;
	private String release;
	private Date created_date;
	private Integer productId;
	private String productName;
	
	private String build_type;//ISE
	private String wt_distribution;//ISE
	private String updated_by;	//ISE
	private Date last_updated_date;//ISE
	
	public ISEProductBuildMongo(){
		
	}
	
	public ISEProductBuildMongo(Integer buildId, Integer versionId, String build_type, String wt_distribution, Date createdDate, 
			Date modifiedDate, String updated_by, String buildNo, String buildname, String buildDescription,
		Date buildDate, String status, Integer productVersionId){
		this.id = buildId.toString();
		this.build = buildname.toString();
		this.release = versionId.toString();
		this.build_type = build_type;
		this.wt_distribution = wt_distribution;
		this.created_date = createdDate;
		this.last_updated_date = modifiedDate;
		this.updated_by = updated_by;
		this. productName=productName;
		this. productId=productId;
		
	}	
	
public ISEProductBuildMongo(ProductBuild productBuild) {
		this.id= productBuild.getProductBuildId()+"";
		this.build = productBuild.getBuildname().toString();
		if(productBuild.getProductVersion() != null){
			this.release = productBuild.getProductVersion().getProductVersionListId().toString();
		}
		
		this.build_type = productBuild.getBuildType().getStageName();
		this.created_date = productBuild.getCreatedDate();
		this.last_updated_date = productBuild.getModifiedDate();
		
		this.productId=productBuild.getProductVersion().getProductMaster().getProductId();
		this.productName=productBuild.getProductVersion().getProductMaster().getProductName();
	}
	

	public String getBuild_type() {
		return build_type;
	}

	public void setBuild_type(String build_type) {
		this.build_type = build_type;
	}

	public String getWt_distribution() {
		return wt_distribution;
	}

	public void setWt_distribution(String wt_distribution) {
		this.wt_distribution = wt_distribution;
	}

	public String getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}


	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getBuild() {
		return build;
	}

	public void setBuild(String build) {
		this.build = build;
	}

	public String getRelease() {
		return release;
	}

	public void setRelease(String release) {
		this.release = release;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getLast_updated_date() {
		return last_updated_date;
	}

	public void setLast_updated_date(Date last_updated_date) {
		this.last_updated_date = last_updated_date;
	}
	
	
}
