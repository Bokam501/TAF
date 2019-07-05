package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.dto.CompetencySummaryDTO;

public class JsonCompetencySummary {

	@JsonProperty
	private Integer dimensionId;
	@JsonProperty
	private String competencyName;	
	@JsonProperty
	private String description;	
	@JsonProperty
	private Integer status;	
	@JsonProperty
	private Integer ownerId;
	@JsonProperty
	private String ownerName;
	@JsonProperty
	private String createdOn;
	@JsonProperty
	private Integer lastModifiedById;
	@JsonProperty
	private String lastModifiedByName;
	@JsonProperty
	private String lastModifiedDate;
	@JsonProperty
	private Integer numberOfProducts;	
	@JsonProperty
	private Integer numberOfTeamMembers;	
	@JsonProperty
	private Integer numberOfProductsWithTeamMembers;
	@JsonProperty
	private Integer numberOfProductsWithoutTeamMembers;
	@JsonProperty
	private Integer numberOfAllocatedMembers;
	@JsonProperty
	private Integer numberOfUnallocatedMembers;
	
	
	public JsonCompetencySummary(){
		this.dimensionId = 0;
		this.competencyName = "";	
		this.description = "";	
		this.status = 0;	
		this.ownerId = 0;
		this.ownerName = "";
		this.createdOn = "";
		this.lastModifiedById = 0;
		this.lastModifiedByName = "";
		this.lastModifiedDate = "";
		this.numberOfProducts = 0;	
		this.numberOfTeamMembers = 0;	
		this.numberOfProductsWithTeamMembers = 0;
		this.numberOfProductsWithoutTeamMembers = 0;
		this.numberOfAllocatedMembers = 0;
		this.numberOfUnallocatedMembers = 0;
	}
	
	public JsonCompetencySummary(CompetencySummaryDTO competencySummaryDTO){
		if(competencySummaryDTO.getDimensionMaster() != null){
			this.dimensionId = competencySummaryDTO.getDimensionMaster().getDimensionId();
			this.competencyName = competencySummaryDTO.getDimensionMaster().getName();
			this.description = competencySummaryDTO.getDimensionMaster().getDescription();
			this.status = competencySummaryDTO.getDimensionMaster().getStatus();
			if(competencySummaryDTO.getDimensionMaster().getOwner() != null){
				this.ownerId = competencySummaryDTO.getDimensionMaster().getOwner().getUserId();
				this.ownerName = competencySummaryDTO.getDimensionMaster().getOwner().getUserDisplayName();
			}
			if(competencySummaryDTO.getDimensionMaster().getCreatedDate() != null){
				this.createdOn = DateUtility.dateToStringWithSeconds1(competencySummaryDTO.getDimensionMaster().getCreatedDate());
			}
			if(competencySummaryDTO.getDimensionMaster().getModifiedBy() != null){
				this.lastModifiedById = competencySummaryDTO.getDimensionMaster().getModifiedBy().getUserId();
				this.lastModifiedByName = competencySummaryDTO.getDimensionMaster().getModifiedBy().getUserDisplayName();
			}
			if(competencySummaryDTO.getDimensionMaster().getModifiedDate() != null){
				this.lastModifiedDate = DateUtility.dateToStringWithSeconds1(competencySummaryDTO.getDimensionMaster().getModifiedDate());
			}
		}
		
		this.numberOfProducts = competencySummaryDTO.getNumberOfProducts();
		this.numberOfTeamMembers = competencySummaryDTO.getNumberOfTeamMembers();
		this.numberOfProductsWithTeamMembers = competencySummaryDTO.getNumberOfProductsWithTeamMembers();
		this.numberOfProductsWithoutTeamMembers = competencySummaryDTO.getNumberOfProductsWithoutTeamMembers();
		this.numberOfAllocatedMembers = competencySummaryDTO.getNumberOfAllocatedMembers();
		this.numberOfUnallocatedMembers = competencySummaryDTO.getNumberOfUnallocatedMembers();
	}

	public Integer getDimensionId() {
		return dimensionId;
	}

	public void setDimensionId(Integer dimensionId) {
		this.dimensionId = dimensionId;
	}

	public String getCompetencyName() {
		return competencyName;
	}

	public void setCompetencyName(String competencyName) {
		this.competencyName = competencyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

	public Integer getLastModifiedById() {
		return lastModifiedById;
	}

	public void setLastModifiedById(Integer lastModifiedById) {
		this.lastModifiedById = lastModifiedById;
	}

	public String getLastModifiedByName() {
		return lastModifiedByName;
	}

	public void setLastModifiedByName(String lastModifiedByName) {
		this.lastModifiedByName = lastModifiedByName;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Integer getNumberOfProducts() {
		return numberOfProducts;
	}

	public void setNumberOfProducts(Integer numberOfProducts) {
		this.numberOfProducts = numberOfProducts;
	}

	public Integer getNumberOfTeamMembers() {
		return numberOfTeamMembers;
	}

	public void setNumberOfTeamMembers(Integer numberOfTeamMembers) {
		this.numberOfTeamMembers = numberOfTeamMembers;
	}

	public Integer getNumberOfProductsWithTeamMembers() {
		return numberOfProductsWithTeamMembers;
	}

	public void setNumberOfProductsWithTeamMembers(
			Integer numberOfProductsWithTeamMembers) {
		this.numberOfProductsWithTeamMembers = numberOfProductsWithTeamMembers;
	}

	public Integer getNumberOfProductsWithoutTeamMembers() {
		return numberOfProductsWithoutTeamMembers;
	}

	public void setNumberOfProductsWithoutTeamMembers(
			Integer numberOfProductsWithoutTeamMembers) {
		this.numberOfProductsWithoutTeamMembers = numberOfProductsWithoutTeamMembers;
	}

	public Integer getNumberOfAllocatedMembers() {
		return numberOfAllocatedMembers;
	}

	public void setNumberOfAllocatedMembers(Integer numberOfAllocatedMembers) {
		this.numberOfAllocatedMembers = numberOfAllocatedMembers;
	}

	public Integer getNumberOfUnallocatedMembers() {
		return numberOfUnallocatedMembers;
	}

	public void setNumberOfUnallocatedMembers(Integer numberOfUnallocatedMembers) {
		this.numberOfUnallocatedMembers = numberOfUnallocatedMembers;
	}
	
	
}
