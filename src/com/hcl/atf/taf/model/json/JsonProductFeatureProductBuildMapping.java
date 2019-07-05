/**
 * 
 */
package com.hcl.atf.taf.model.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.model.ProductFeatureProductBuildMapping;
import com.hcl.atf.taf.model.ProductMaster;

/**
 * @author silambarasur
 * 
 */
public class JsonProductFeatureProductBuildMapping {

	@JsonProperty
	private Integer featureId;
	@JsonProperty
	private Integer buildId;
	@JsonProperty
	private Integer productId;
	
	@JsonProperty
	private Integer isMapped;

	public JsonProductFeatureProductBuildMapping() {

	}

	public JsonProductFeatureProductBuildMapping(ProductFeatureProductBuildMapping productFeatureProductBuildMapping) {
			this.featureId = productFeatureProductBuildMapping.getFeatureId();
		
			this.buildId = productFeatureProductBuildMapping.getBuildId();
		
		if(productFeatureProductBuildMapping.getProduct() != null) {
			this.productId = productFeatureProductBuildMapping.getProduct().getProductId();
		}
		
		if (productFeatureProductBuildMapping.getIsMapped()  != null && productFeatureProductBuildMapping.getIsMapped() >0) {
			this.isMapped = productFeatureProductBuildMapping.getIsMapped();
		} else {
			this.isMapped=0;
		}

	}
	
	@JsonIgnore
	public ProductFeatureProductBuildMapping getProductFeatureProductBuildMapping() {
		ProductFeatureProductBuildMapping productFeatureProductBuildMapping = new ProductFeatureProductBuildMapping();
		if(this.buildId != null) {
			productFeatureProductBuildMapping.setBuildId(buildId);
		}
		if(this.featureId !=null) {
			productFeatureProductBuildMapping.setFeatureId(featureId);
		}
		if(this.productId != null) {
			ProductMaster product = new ProductMaster();
			product.setProductId(productId);
			productFeatureProductBuildMapping.setProduct(product);
		}
		if(this.isMapped != null) {
			productFeatureProductBuildMapping.setIsMapped(isMapped);
		} else {
			productFeatureProductBuildMapping.setIsMapped(0);
		}
		return productFeatureProductBuildMapping;
	}
	

	public Integer getFeatureId() {
		return featureId;
	}

	public Integer getBuildId() {
		return buildId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setFeatureId(Integer featureId) {
		this.featureId = featureId;
	}

	public void setBuildId(Integer buildId) {
		this.buildId = buildId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getIsMapped() {
		return isMapped;
	}

	public void setIsMapped(Integer isMapped) {
		this.isMapped = isMapped;
	}

}
