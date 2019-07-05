package com.hcl.atf.taf.dao;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.DimensionProduct;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;

public interface CompetencyDAO {
	
	CompetencyMember getMemberMappedStatusByUserId(Integer userId, Integer status, Integer referenceId, Date startDate, Date endDate);
	void addCompetencyMember(CompetencyMember competencyMember);
	List<CompetencyMember> getCompetencyMembers(Integer competencyId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	Integer getTotalRecordsForCompetencyMemberPagination(Integer competencyId, Integer status, Class<CompetencyMember> className);
	List<UserList> getMembersToAddWithCompetency();
	List<CompetencyMember> getCompetencyMemberMappedByUserId(Integer userId, Integer status, Integer referenceId);
	void updateCompetencyMember(CompetencyMember competencyMember);
	
	int getTotalRecordsForDimensionProductPagination(Integer competencyId, Integer status, Class<DimensionProduct> className);
	
	List<ProductTeamResources> getCompetencyProductTeamResourcesList(Integer productId, Integer competencyId, Integer jtStartIndex, Integer jtPageSize);
	Integer getTotalRecordsForComptencyProductTeam(Integer productId, Integer competencyId);
	List<UserList> getMembersToMapWithCompetencyProduct(Integer competencyId);

	Integer getNumberOfAllocatedMembersForCompetency(Integer competencyId, Integer status, Class<ProductTeamResources> className);
	Integer getNumberOfProductsWithMembersInCompetency(Integer competencyId, Integer status, Class<ProductTeamResources> className);
	
	List<Integer> getCompetencyIdByProductId(Integer productId);
	
}
