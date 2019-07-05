package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "custom_field_values")
public class CustomFieldValues implements Cloneable {

	private Integer id;
	private CustomFieldConfigMaster customFieldId;
	private Integer entityInstanceId;
	private String fieldValue;
	private Integer frequencyOrder;
	private Integer frequencyMonth;
	private Integer frequencyYear;
	private UserList createdBy;
	private Date createdOn;
	private UserList modifiedBy;
	private Date modifiedOn;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customFieldId")
	public CustomFieldConfigMaster getCustomFieldId() {
		return customFieldId;
	}
	public void setCustomFieldId(CustomFieldConfigMaster customFieldId) {
		this.customFieldId = customFieldId;
	}
	
	@Column(name = "entityInstanceId")
	public Integer getEntityInstanceId() {
		return entityInstanceId;
	}
	public void setEntityInstanceId(Integer entityInstanceId) {
		this.entityInstanceId = entityInstanceId;
	}

	@Column(name = "fieldValue")
	public String getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	@Column(name = "frequencyOrder")
	public Integer getFrequencyOrder() {
		return frequencyOrder;
	}
	public void setFrequencyOrder(Integer frequencyOrder) {
		this.frequencyOrder = frequencyOrder;
	}
	
	@Column(name = "frequencyMonth")
	public Integer getFrequencyMonth() {
		return frequencyMonth;
	}
	public void setFrequencyMonth(Integer frequencyMonth) {
		this.frequencyMonth = frequencyMonth;
	}
	
	@Column(name = "frequencyYear")
	public Integer getFrequencyYear() {
		return frequencyYear;
	}
	public void setFrequencyYear(Integer frequencyYear) {
		this.frequencyYear = frequencyYear;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "createdBy")
	public UserList getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(UserList createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "createdOn")
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modifiedBy")
	public UserList getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(UserList modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "modifiedOn")
	public Date getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	@Override
    public Object clone() {
        try {
            return super.clone();
        }
        catch (Exception e) {
        }
        return null;
    }
}
