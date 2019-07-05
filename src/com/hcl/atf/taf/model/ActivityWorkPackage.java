/* Project Name: iLCM
 * Description : Model class for pm_work_request table which has below fields
 *workRequestId,workRequestName,description,buildId,wrPlannedStartDate,wrPlannedEndDate
 *wrActualStartDate,wrActualEndDate,workReqStatusId,workReqPriorityId,workReqOwnerId
 *workReqTag,workReqType,workReqRemark
 *
 *the 

 * Date:06/01/2016
 */

package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hcl.ilcm.workflow.model.WorkflowStatus;



@Entity
@Table(name = "activity_work_package")
public class ActivityWorkPackage {
	
	private Integer activityWorkPackageId;
	private String activityWorkPackageName;
	private String description;
	private ProductBuild productBuild;
	private  Date baselineStartDate; 
	private  Date baselineEndDate;
	private  Date plannedStartDate; 
	private  Date plannedEndDate;
	private  Date actualStartDate;
	private  Date actualEndDate;
	private StatusCategory statusCategory;
	private  ExecutionPriority priority;
	private DimensionMaster competency;
	private UserList owner;
	private String activityWorkPackageTag;
	private String activityWorkPackageType;
	private String remark;
	private  Date createdDate;
	private  Date modifiedDate;
	private  UserList createdBy;
	private  UserList modifiedBy;
	private Integer isActive;
	
	private WorkflowStatus workflowStatus;
    private Integer totalEffort;
    private Float percentageCompletion;
    private ProductMaster productMaster;
    
    
	
	public ActivityWorkPackage(){
		
	}
	public ActivityWorkPackage(Integer productBuildId, String newWorkpackageName,ActivityWorkPackage activityWorkPackage) {

		this.activityWorkPackageName=newWorkpackageName;
		
		ProductBuild productBuild = new ProductBuild();
		productBuild.setProductBuildId(productBuildId);
		this.productBuild=productBuild;
		
		this.description=activityWorkPackage.description;

		this.baselineStartDate = activityWorkPackage.getBaselineStartDate();
		this.baselineEndDate = activityWorkPackage.getBaselineStartDate();
		this.plannedStartDate=activityWorkPackage.getPlannedStartDate();
		this.plannedEndDate=activityWorkPackage.getPlannedEndDate();
		this.actualStartDate=activityWorkPackage.getActualStartDate();
		this.actualEndDate=activityWorkPackage.getActualEndDate();
		this.statusCategory=activityWorkPackage.getStatusCategory();
			
		this.priority=activityWorkPackage.getPriority();
		this.competency=activityWorkPackage.getCompetency();
		this.owner=activityWorkPackage.getOwner();
		this.activityWorkPackageTag=activityWorkPackage.getActivityWorkPackageTag();
		this.activityWorkPackageType=activityWorkPackage.getActivityWorkPackageType();
		this.remark=activityWorkPackage.getRemark();
	}

	private Set<Activity> activity = new HashSet<Activity>(0);
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "activityWorkPackageId",unique = true, nullable = false)
	public Integer getActivityWorkPackageId() {
		return activityWorkPackageId;
	}
	public void setActivityWorkPackageId(Integer activityWorkPackageId) {
		this.activityWorkPackageId = activityWorkPackageId;
	}
	

	
	@Column(name = "activityWorkPackageName")
	public String getActivityWorkPackageName() {
		return activityWorkPackageName;
	}
	public void setActivityWorkPackageName(String activityWorkPackageName) {
		this.activityWorkPackageName = activityWorkPackageName;
	}
	
	@Column(name = "activityWorkPackageTag")
	public String getActivityWorkPackageTag() {
		return activityWorkPackageTag;
	}
	public void setActivityWorkPackageTag(String activityWorkPackageTag) {
		this.activityWorkPackageTag = activityWorkPackageTag;
	}
	
	@Column(name = "description")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn(name = "productBuild")
	public ProductBuild getProductBuild() {
		return productBuild;
	}
	public void setProductBuild(ProductBuild productbuild) {
		this.productBuild = productbuild;
	}
	@Column(name = "baselineStartDate")
	public Date getBaselineStartDate() {
		return baselineStartDate;
	}
	public void setBaselineStartDate(Date baselineStartDate) {
		this.baselineStartDate = baselineStartDate;
	}
	@Column(name = "baselineEndDate")
	public Date getBaselineEndDate() {
		return baselineEndDate;
	}
	public void setBaselineEndDate(Date baselineEndDate) {
		this.baselineEndDate = baselineEndDate;
	}
	@Column(name = "plannedStartDate")
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	@Column(name = "plannedEndDate")
	public Date getPlannedEndDate() {
		return plannedEndDate;
	}
	public void setPlannedEndDate(Date plannedEndDate) {
		this.plannedEndDate = plannedEndDate;
	}
	
	@Column(name = "actualStartDate")
	public Date getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	@Column(name = "actualEndDate")
	public Date getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "statusCategoryId")
	public StatusCategory getStatusCategory() {
		return statusCategory;
	}

	public void setStatusCategory(StatusCategory statusCategory) {
		this.statusCategory = statusCategory;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "priority")
	public ExecutionPriority getPriority() {
		return priority;
	}
	
	public void setPriority(ExecutionPriority priority) {
		this.priority = priority;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competency")
	public DimensionMaster getCompetency() {
		return competency;
	}
	public void setCompetency(DimensionMaster competency) {
		this.competency = competency;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "owner")
	public UserList getOwner() {
		return owner;
	}
	public void setOwner(UserList owner) {
		this.owner = owner;
	}
	@Column(name = "activityWorkPackageType")
	public String getActivityWorkPackageType() {
		return activityWorkPackageType;
	}
	public void setActivityWorkPackageType(String activityWorkPackageType) {
		this.activityWorkPackageType = activityWorkPackageType;
	}
	
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String Remark) {
		this.remark = Remark;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createdDate")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifiedDate")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	
	@ManyToOne (fetch = FetchType.LAZY)
	@JoinColumn (name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "activityWorkPackage")
	public Set<Activity> getActivity() {
		return activity;
	}
	public void setActivity(Set<Activity> activity) {
		this.activity = activity;
	}
	
	@Override
	public boolean equals(Object o) {
		ActivityWorkPackage activityWp = (ActivityWorkPackage) o;
		if (this.activityWorkPackageId == activityWp.getActivityWorkPackageId()){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
	    return (int) activityWorkPackageId;
	 }
	@Column(name = "isActive")
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "workflowStatusId")
	public WorkflowStatus getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(WorkflowStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Column(name = "totalEfforts")
	public Integer getTotalEffort() {
		return totalEffort;
	}

	public void setTotalEffort(Integer totalEffort) {
		this.totalEffort = totalEffort;
	}
	
	@Column(name = "percentageCompletion")
	public Float getPercentageCompletion() {
		return percentageCompletion;
	}
	public void setPercentageCompletion(Float percentageCompletion) {
		this.percentageCompletion = percentageCompletion;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "productId")
	public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
	@Override
	public String toString() {
		return activityWorkPackageName;
		
	}
	
}
