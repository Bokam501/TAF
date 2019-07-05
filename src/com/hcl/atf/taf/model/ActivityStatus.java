package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity_status_master")
public class ActivityStatus {


private Integer activityStatusId;
private String activityStatusName;
private String activityStatusDescription;
private DimensionMaster dimension;
private StatusCategory statusCategory;
private Integer activeStatus;

private Set<ActivitySecondaryStatusMaster> activitySecondaryStatus = new HashSet<ActivitySecondaryStatusMaster>(0);



@Id
@GeneratedValue(strategy = IDENTITY)
@Column(name = "activityStatusId", unique = true, nullable = false)
public Integer getActivityStatusId() {
	return activityStatusId;
}
public void setActivityStatusId(Integer activityStatusId) {
	this.activityStatusId = activityStatusId;
}
@Column(name = "activityStatusName")
public String getActivityStatusName() {
	return activityStatusName;
}
public void setActivityStatusName(String activityStatusName) {
	this.activityStatusName = activityStatusName;
}
@Column(name = "activityStatusDescription")
public String getActivityStatusDescription() {
	return activityStatusDescription;
}
public void setActivityStatusDescription(String activityStatusDescription) {
	this.activityStatusDescription = activityStatusDescription;
}


@ManyToMany(fetch = FetchType.LAZY, mappedBy = "activityStatus",cascade=CascadeType.ALL)
public Set<ActivitySecondaryStatusMaster> getActivitySecondaryStatus() {
	return activitySecondaryStatus;
}
public void setActivitySecondaryStatus(
		Set<ActivitySecondaryStatusMaster> activitySecondaryStatus) {
	this.activitySecondaryStatus = activitySecondaryStatus;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "dimensionId")
public DimensionMaster getDimension() {
	return dimension;
}
public void setDimension(DimensionMaster dimension) {
	this.dimension = dimension;
}

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "statusCategoryId")
public StatusCategory getStatusCategory() {
	return statusCategory;
}
public void setStatusCategory(StatusCategory statusCategory) {
	this.statusCategory = statusCategory;
}

@Column(name = "activeStatus")
public Integer getActiveStatus() {
	return activeStatus;
}
public void setActiveStatus(Integer activeStatus) {
	this.activeStatus = activeStatus;
}

@Override
public boolean equals(Object o) {
	
		ActivityStatus activityStatus = (ActivityStatus) o;
		if (this.activityStatusId == activityStatus.getActivityStatusId()){
			return true;
		}else{
			return false;
		}
	
	
}

@Override
public int hashCode(){
    return (int) activityStatusId;
 }

}
