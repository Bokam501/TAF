package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dimension_type")
public class DimensionType implements Serializable {

	private static final long serialVersionUID = 1946758225151961650L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer dimensionTypeId;
	
	@Column(name = "name")
	private String name;

	public Integer getDimensionTypeId() {
		return dimensionTypeId;
	}

	public void setDimensionTypeId(Integer dimensionTypeId) {
		this.dimensionTypeId = dimensionTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
