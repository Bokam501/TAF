package com.hcl.atf.taf.model;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "entitymaster")
public class EntityMaster implements java.io.Serializable {

	private Integer entitymasterid;
	private String entitymastername;
	private String entityDisplayName;
	private String description;
	private Integer isWorkflowCapable;
	private Integer isCustomFieldCapable;

	public EntityMaster() {
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "entitymasterid", unique = true, nullable = false)
	public Integer getEntitymasterid() {
		return entitymasterid;
	}

	public void setEntitymasterid(Integer entitymasterid) {
		this.entitymasterid = entitymasterid;
	}

	@Column(name = "entitymastername", length = 100)
	public String getEntitymastername() {
		return entitymastername;
	}


	public void setEntitymastername(String entitymastername) {
		this.entitymastername = entitymastername;
	}
	
	@Column(name = "entityDisplayName", length = 150)
	public String getEntityDisplayName() {
		return entityDisplayName;
	}

	public void setEntityDisplayName(String entityDisplayName) {
		this.entityDisplayName = entityDisplayName;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}	

	@Column(name = "isWorkflowCapable")
	public Integer getIsWorkflowCapable() {
		return isWorkflowCapable;
	}

	public void setIsWorkflowCapable(Integer isWorkflowCapable) {
		this.isWorkflowCapable = isWorkflowCapable;
	}

	@Column(name = "isCustomFieldCapable")
	public Integer getIsCustomFieldCapable() {
		return isCustomFieldCapable;
	}

	public void setIsCustomFieldCapable(Integer isCustomFieldCapable) {
		this.isCustomFieldCapable = isCustomFieldCapable;
	}
	
	
}
