package com.hcl.atf.taf.model.dto;

import com.hcl.atf.taf.model.DimensionMaster;

public class CompetencySummaryDTO {
	
	private DimensionMaster dimensionMaster;
	private Integer numberOfProducts;	
	private Integer numberOfTeamMembers;	
	private Integer numberOfProductsWithTeamMembers;
	private Integer numberOfProductsWithoutTeamMembers;
	private Integer numberOfAllocatedMembers;
	private Integer numberOfUnallocatedMembers;
	
	public DimensionMaster getDimensionMaster() {
		return dimensionMaster;
	}
	public void setDimensionMaster(DimensionMaster dimensionMaster) {
		this.dimensionMaster = dimensionMaster;
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
