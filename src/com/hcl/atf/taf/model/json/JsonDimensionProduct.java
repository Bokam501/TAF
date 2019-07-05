package com.hcl.atf.taf.model.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.DimensionMaster;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.UserList;

public class JsonDimensionProduct {

	@JsonProperty
	private Integer dimensionProductId;
	@JsonProperty
	private Integer dimensionId;
	@JsonProperty
	private String dimensionName;
	@JsonProperty
	private Integer productId;
	@JsonProperty
	private String productName;
	@JsonProperty
	private String startDate;
	@JsonProperty
	private String endDate;
	@JsonProperty
	private Integer status;
	@JsonProperty
	private String progress;
	@JsonProperty
	private Integer mappedById;
	@JsonProperty
	private String mappedByName;
	@JsonProperty
	private String mappedDate;
	@JsonProperty
	private Integer modifiedById;
	@JsonProperty
	private String modifiedByName;
	@JsonProperty
	private String modifiedDate;
	
	public JsonDimensionProduct(){
		
	}
	
	public JsonDimensionProduct(DimensionProduct dimensionProduct) {
		this.dimensionProductId = dimensionProduct.getDimensionProductId();
		if(dimensionProduct.getDimensionId() != null){
			this.dimensionId = dimensionProduct.getDimensionId().getDimensionId();
			this.dimensionName = dimensionProduct.getDimensionId().getName();
		}else{
			this.dimensionId = 0;
			this.dimensionName = null;
		}
		if(dimensionProduct.getProductId() != null){
			this.productId = dimensionProduct.getProductId().getProductId();
			this.productName = dimensionProduct.getProductId().getProductName();
		}else{
			this.productId = 0;
			this.productName = null;
		}
		if(dimensionProduct.getStartDate() == null){
			dimensionProduct.setStartDate(new Date());
		}
		this.startDate = DateUtility.dateformatWithOutTime(dimensionProduct.getStartDate());
		if(dimensionProduct.getEndDate() == null){
			dimensionProduct.setEndDate(new Date());
		}
		this.endDate = DateUtility.dateformatWithOutTime(dimensionProduct.getEndDate());
		if(dimensionProduct.getStatus() != null){
			this.status = dimensionProduct.getStatus();
		}else{
			this.status = 0;
		}
		Date now = new Date();
		this.progress = "Inprogress";
		if(now.before(dimensionProduct.getStartDate())){
			this.progress = "Not Statred";
		}else if(now.after(dimensionProduct.getStartDate()) && now.before(dimensionProduct.getEndDate())){
			this.progress = "Inprogress";
		}else if(now.after(dimensionProduct.getEndDate())){
			this.progress = "Completed";
		}
		
		if(this.status == 0){
			this.progress = "Inactive";
		}
		
		if(dimensionProduct.getMappedBy() != null){
			this.mappedById = dimensionProduct.getMappedBy().getUserId();
			this.mappedByName = dimensionProduct.getMappedBy().getUserDisplayName();
		}else{
			this.mappedById = 0;
			this.mappedByName = null;
		}
		if(dimensionProduct.getMappedDate() != null){
			this.mappedDate = DateUtility.dateToStringWithSeconds1(dimensionProduct.getMappedDate());
		}else{
			this.mappedDate = DateUtility.dateToStringWithSeconds1(new Date());
		}
		if(dimensionProduct.getModifiedBy() != null){
			this.modifiedById = dimensionProduct.getModifiedBy().getUserId();
			this.modifiedByName = dimensionProduct.getModifiedBy().getUserDisplayName();
		}else{
			this.modifiedById = 0;
			this.modifiedByName = null;
		}
		if(dimensionProduct.getModifiedDate() != null){
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(dimensionProduct.getModifiedDate());
		}else{
			this.modifiedDate = DateUtility.dateToStringWithSeconds1(new Date());
		}
	}

	
	public Integer getDimensionProductId() {
		return dimensionProductId;
	}
	public void setDimensionProductId(Integer competencyProductId) {
		this.dimensionProductId = competencyProductId;
	}
	public Integer getDimensionId() {
		return dimensionId;
	}
	public void setDimensionId(Integer dimensionId) {
		this.dimensionId = dimensionId;
	}
	public String getDimensionName() {
		return dimensionName;
	}
	public void setDimensionName(String dimensionName) {
		this.dimensionName = dimensionName;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getProgress() {
		return progress;
	}
	public void setProgress(String progress) {
		this.progress = progress;
	}
	public Integer getMappedById() {
		return mappedById;
	}
	public void setMappedById(Integer mappedById) {
		this.mappedById = mappedById;
	}
	public String getMappedByName() {
		return mappedByName;
	}
	public void setMappedByName(String mappedByName) {
		this.mappedByName = mappedByName;
	}
	public String getMappedDate() {
		return mappedDate;
	}
	public void setMappedDate(String mappedDate) {
		this.mappedDate = mappedDate;
	}
	public Integer getModifiedById() {
		return modifiedById;
	}
	public void setModifiedById(Integer modifiedById) {
		this.modifiedById = modifiedById;
	}
	public String getModifiedByName() {
		return modifiedByName;
	}
	public void setModifiedByName(String modifiedByName) {
		this.modifiedByName = modifiedByName;
	}
	public String getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
	@JsonIgnore
	public DimensionProduct getDimensionProduct() {
		DimensionProduct dimensionProduct = new DimensionProduct();
		
		dimensionProduct.setDimensionProductId(this.dimensionProductId);
		
		DimensionMaster dimensionMaster = new DimensionMaster();
		if(this.dimensionId != null){
			dimensionMaster.setDimensionId(this.dimensionId);
		}else{
			dimensionMaster.setDimensionId(0);
		}
		dimensionProduct.setDimensionId(dimensionMaster);
		
		ProductMaster productMaster = new ProductMaster();
		if(this.productId != null){
			productMaster.setProductId(this.productId);
		}else{
			productMaster.setProductId(0);
		}
		dimensionProduct.setProductId(productMaster);
		
		if(this.startDate != null && !this.startDate.trim().isEmpty()){
			dimensionProduct.setStartDate(DateUtility.dateformatWithOutTime(this.startDate));
		}else{
			dimensionProduct.setStartDate(new Date());
		}
		
		if(this.endDate != null && !this.endDate.trim().isEmpty()){
			dimensionProduct.setEndDate(DateUtility.dateformatWithOutTime(this.endDate));
		}else{
			dimensionProduct.setEndDate(new Date());
		}
		
		if(this.status != null){
			dimensionProduct.setStatus(this.status);
		}else{
			dimensionProduct.setStatus(0);
		}
		
		UserList mappedUserList = new UserList();
		if(this.mappedById != null){
			mappedUserList.setUserId(this.mappedById);
		}else{
			mappedUserList.setUserId(0);
		}
		dimensionProduct.setMappedBy(mappedUserList);
		
		if(this.mappedDate != null && !this.mappedDate.trim().isEmpty()){
			dimensionProduct.setMappedDate(DateUtility.toFormatDate(this.mappedDate));
		}else{
			dimensionProduct.setMappedDate(new Date());
		}
		
		UserList modifiedUserList = new UserList();
		if(this.modifiedById != null){
			modifiedUserList.setUserId(this.modifiedById);
		}else{
			modifiedUserList.setUserId(0);
		}
		dimensionProduct.setModifiedBy(modifiedUserList);
		
		if(this.modifiedDate != null && !this.modifiedDate.trim().isEmpty()){
			dimensionProduct.setModifiedDate(DateUtility.toFormatDate(this.modifiedDate));
		}else{
			dimensionProduct.setModifiedDate(new Date());
		}
		return dimensionProduct;
	}
	
}
