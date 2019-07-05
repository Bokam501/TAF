/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcl.atf.taf.model.EntityMaster;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.StatusCategory;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="entity_status")
public class EntityStatus implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8093438943336656276L;
	
	private Integer entityStatusId;
	private String entityStatusName;
	private String entityStatusDescription;
	private StatusCategory statusCategory;
	private Integer activeStatus;
	private EntityMaster entityMaster;
	private ProductMaster levelId; 
	private String level;
	private Integer weightage;
	private Integer entityId;
	private Integer parentStatusId;
	
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entityStatusId",unique = true, nullable = false)
	public Integer getEntityStatusId() {
		return entityStatusId;
	}
	public void setEntityStatusId(Integer entityStatusId) {
		this.entityStatusId = entityStatusId;
	}
	
	@Column(name = "entityStatusName")
	public String getEntityStatusName() {
		return entityStatusName;
	}
	public void setEntityStatusName(String entityStatusName) {
		this.entityStatusName = entityStatusName;
	}
	
	@Column(name = "description")
	public String getEntityStatusDescription() {
		return entityStatusDescription;
	}
	public void setEntityStatusDescription(String entityStatusDescription) {
		this.entityStatusDescription = entityStatusDescription;
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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "entityTypeId")
	public EntityMaster getEntityMaster() {
		return entityMaster;
	}
	public void setEntityMaster(EntityMaster entityMaster) {
		this.entityMaster = entityMaster;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "levelId")
	public ProductMaster getLevelId() {
		return levelId;
	}
	public void setLevelId(ProductMaster levelId) {
		this.levelId = levelId;
	}
	@Column(name = "level")
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	@Column(name = "weightage")
	public Integer getWeightage() {
		return weightage;
	}
	public void setWeightage(Integer weightage) {
		this.weightage = weightage;
	}
	
	@Column(name = "entityId")
	public Integer getEntityId() {
		return entityId;
	}
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}
	
	@Column(name = "parentStatusId")
	public Integer getParentStatusId() {
		return parentStatusId;
	}
	public void setParentStatusId(Integer parentStatusId) {
		this.parentStatusId = parentStatusId;
	}

}
