package com.hcl.atf.taf.model.json;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hcl.atf.taf.controller.utilities.DateUtility;
import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.ProductUserRole;
import com.hcl.atf.taf.model.UserList;
import com.hcl.atf.taf.model.UserRoleMaster;
public class JsonProductUserRole implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1168854323761952715L;
	private static final Log log = LogFactory
			.getLog(JsonProductUserRole.class);
	@JsonProperty
	private int productUserRoleId;
	@JsonProperty	
	private Integer status;
	@JsonProperty
	private String name;
	@JsonProperty
	private String description;
	
	@JsonProperty
	private String createdDate;
	@JsonProperty
	private String  modifiedDate;
	
	@JsonProperty
	private  int productId;
	
	@JsonProperty
	private  String productName;
	
	@JsonProperty
	private  int userId;
	
	@JsonProperty
	private  String userName;
	
	@JsonProperty
	private  int roleId;
	
	@JsonProperty
	private  String roleName;
	
	@JsonProperty
	private  String loginId;
	
	
	public JsonProductUserRole() {
	}

	public JsonProductUserRole(ProductUserRole productUserRole) {
		this.productUserRoleId=productUserRole.getProductUserRoleId();
		
		this.name=productUserRole.getName();
		this.description=productUserRole.getDescription();		
		if(productUserRole.getCreatedDate()!=null)			
			this.createdDate=DateUtility.dateToStringWithSeconds1(productUserRole.getCreatedDate());
		if(productUserRole.getModifiedDate()!=null)		
			this.modifiedDate=DateUtility.dateformatWithOutTime(productUserRole.getModifiedDate());	
		
		UserList userList = productUserRole.getUser();
		if(userList != null){
			this.userId=productUserRole.getUser().getUserId();
			this.userName=productUserRole.getUser().getFirstName();
			this.loginId = productUserRole.getUser().getLoginId();
		}else{
			this.userId=0;
			this.userName=null;
			this.loginId = null;
			
		}		
		UserRoleMaster userRoleMaster= productUserRole.getRole();
		if(userRoleMaster != null){
			this.roleId=userRoleMaster.getUserRoleId();
			this.roleName=userRoleMaster.getRoleName();
		}else{
			this.roleId=0;
			this.roleName=null;
		}
		if(productUserRole.getStatus() != null){
			this.status = productUserRole.getStatus();
		}
		
		this.productId=productUserRole.getProduct().getProductId();
		this.productName=productUserRole.getProduct().getProductName();		
	}
	
	
	public int getProductUserRoleId() {
		return productUserRoleId;
	}

	public void setProductUserRoleId(int productUserRoleId) {
		this.productUserRoleId = productUserRoleId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@JsonIgnore
	public ProductUserRole getProductUserRoleList(){
		
		ProductUserRole productUserRole = new ProductUserRole();
		
		productUserRole.setProductUserRoleId(productUserRoleId);
		if(this.status != null ){			
			productUserRole.setStatus(status);			
		}else{
			productUserRole.setStatus(0);	
		}
		productUserRole.setName(name);
		productUserRole.setDescription(description);		
		
		
		if(this.createdDate == null || this.createdDate.trim().isEmpty()) {
			productUserRole.setCreatedDate(DateUtility.getCurrentTime());
		}else{
			log.info("this.createdDate "+this.createdDate);
			productUserRole.setCreatedDate(DateUtility.toFormatDate(this.createdDate));
						
		}
		productUserRole.setModifiedDate(DateUtility.getCurrentTime());
		
		ProductMaster product = new ProductMaster();
		product.setProductId(productId);
		product.setProductName(productName);
		productUserRole.setProduct(product);
		
		
		UserList userList = new UserList();
		userList.setUserId(userId);
		productUserRole.setUser(userList);		
		
		productUserRole.getRole();
		
		UserRoleMaster role=new UserRoleMaster();		
		role.setUserRoleId(roleId);
		productUserRole.setRole(role);	
		return productUserRole;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

}
