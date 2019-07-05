package com.hcl.atf.taf.service;

import java.util.Date;
import java.util.List;

import com.hcl.atf.taf.model.CompetencyMember;
import com.hcl.atf.taf.model.ProductTeamResources;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.dto.CompetencySummaryDTO;

public interface CompetencyService {

	CompetencySummaryDTO getCompetencySummary(Integer competencyId);
	
	CompetencyMember getMemberMappedStatusByUserId(Integer userId, Integer status, Integer referenceId, Date startDate, Date endDate);
	void addCompetencyMember(CompetencyMember competencyMember);
	List<CompetencyMember> getCompetencyMembers(Integer competencyId, Integer status, Integer jtStartIndex, Integer jtPageSize);
	Integer getTotalRecordsForCompetencyMemberPagination(Integer competencyId, Integer status, Class<CompetencyMember> className);
	List<UserList> getMembersToAddWithCompetency();
	List<CompetencyMember> getCompetencyMemberMappedByUserId(Integer userId, Integer status, Integer referenceId);
	void updateCompetencyMember(CompetencyMember competencyMember);
	
	List<ProductTeamResources> getCompetencyProductTeamResourcesList(Integer productId, Integer competencyId, Integer jtStartIndex, Integer jtPageSize);
	Integer getTotalRecordsForComptencyProductTeam(Integer productId, Integer competencyId);
	List<UserList> getMembersToMapWithCompetencyProduct(Integer competencyId);
	
	List<Integer> getCompetencyIdByProductId(Integer productId);

}
