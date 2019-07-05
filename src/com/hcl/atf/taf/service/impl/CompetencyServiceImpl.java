package com.hcl.atf.taf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcl.atf.taf.dao.CompetencyDAO;
import com.hcl.atf.taf.dao.DimensionDAO;
import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.CompetencySummaryDTO;
import com.hcl.atf.taf.service.CompetencyService;

@Service
public class CompetencyServiceImpl implements CompetencyService {

	@Autowired
	private CompetencyDAO competencyDAO;
	
	@Autowired
	private DimensionDAO dimensionDAO;

	@Override
	@Transactional
	public CompetencySummaryDTO getCompetencySummary(Integer competencyId) {
		
		CompetencySummaryDTO competencySummaryDTO = new CompetencySummaryDTO();
		
		int totalMembers = 0;
		int allocatedMembers = 0;
		int unallocatedMembers = 0;
		int totalProducts = 0;
		int productsWithMembers = 0;
		int productsWithoutMembers = 0;
		
		totalMembers = competencyDAO.getTotalRecordsForCompetencyMemberPagination(competencyId, 2, CompetencyMember.class);
		allocatedMembers = competencyDAO.getNumberOfAllocatedMembersForCompetency(competencyId, 2, ProductTeamResources.class);
		unallocatedMembers = totalMembers - allocatedMembers;
		
		totalProducts = competencyDAO.getTotalRecordsForDimensionProductPagination(competencyId, 2, DimensionProduct.class);
		productsWithMembers = competencyDAO.getNumberOfProductsWithMembersInCompetency(competencyId, 2, ProductTeamResources.class);
		productsWithoutMembers = totalProducts - productsWithMembers;
		
		competencySummaryDTO.setDimensionMaster(dimensionDAO.getDimensionById(competencyId));		
		competencySummaryDTO.setNumberOfTeamMembers(totalMembers);
		competencySummaryDTO.setNumberOfProducts(totalProducts);
		competencySummaryDTO.setNumberOfAllocatedMembers(allocatedMembers);
		competencySummaryDTO.setNumberOfUnallocatedMembers(unallocatedMembers);
		competencySummaryDTO.setNumberOfProductsWithTeamMembers(productsWithMembers);
		competencySummaryDTO.setNumberOfProductsWithoutTeamMembers(productsWithoutMembers);
		
		return competencySummaryDTO;
	}
	
	@Override
	@Transactional
	public CompetencyMember getMemberMappedStatusByUserId(Integer userId, Integer status, Integer referenceId, Date startDate, Date endDate) {
		return competencyDAO.getMemberMappedStatusByUserId(userId, status, referenceId, startDate, endDate);
	}
	
	@Override
	@Transactional
	public void addCompetencyMember(CompetencyMember competencyMember) {
		competencyDAO.addCompetencyMember(competencyMember);
	}
	
	@Override
	@Transactional
	public List<CompetencyMember> getCompetencyMembers(Integer competencyId, Integer status, Integer jtStartIndex, Integer jtPageSize) {
		return competencyDAO.getCompetencyMembers(competencyId, status, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getTotalRecordsForCompetencyMemberPagination(Integer competencyId, Integer status, Class<CompetencyMember> className) {
		return competencyDAO.getTotalRecordsForCompetencyMemberPagination(competencyId, status, className);
	}

	@Override
	@Transactional
	public List<UserList> getMembersToAddWithCompetency() {
		return competencyDAO.getMembersToAddWithCompetency();
	}

	@Override
	@Transactional
	public void updateCompetencyMember(CompetencyMember competencyMember) {
		competencyDAO.updateCompetencyMember(competencyMember);
	}

	@Override
	@Transactional
	public List<ProductTeamResources> getCompetencyProductTeamResourcesList(Integer productId, Integer competencyId, Integer jtStartIndex, Integer jtPageSize) {
		return competencyDAO.getCompetencyProductTeamResourcesList(productId, competencyId, jtStartIndex, jtPageSize);
	}

	@Override
	@Transactional
	public Integer getTotalRecordsForComptencyProductTeam(Integer productId, Integer competencyId) {
		return competencyDAO.getTotalRecordsForComptencyProductTeam(productId, competencyId);
	}

	@Override
	@Transactional
	public List<UserList> getMembersToMapWithCompetencyProduct(Integer competencyId) {
		return competencyDAO.getMembersToMapWithCompetencyProduct(competencyId);
	}

	@Override
	@Transactional
	public List<CompetencyMember> getCompetencyMemberMappedByUserId(Integer userId, Integer status, Integer referenceId) {
		return competencyDAO.getCompetencyMemberMappedByUserId(userId, status, referenceId);
	}
	
	@Override
	@Transactional
	public List<Integer> getCompetencyIdByProductId(Integer productId){
		return competencyDAO.getCompetencyIdByProductId(productId);
	}
}
