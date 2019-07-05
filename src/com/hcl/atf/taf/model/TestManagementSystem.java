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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
* TestManagementSystem  - Details of test Management system , Version, and connection properties are
* maintained.
*/
@Entity
@Table(name = "test_management_system")
public class TestManagementSystem implements java.io.Serializable {

                
                private Integer testManagementSystemId;
                private String title;
                private String description;
                private String connectionUri;
                private String connectionUserName;
                private String connectionPassword;
                private String connectionProperty1;
                private String connectionProperty2;
                private String connectionProperty3;
                private String connectionProperty4;
                private String connectionProperty5;//Changes for Bug 792 - Adding TestSetFilter by name 
                private ProductMaster  productMaster;
                private String testSystemName;
                private String testSystemVersion;
                private Set<TestManagementSystemMapping> testManagementSystemMappings = new HashSet<TestManagementSystemMapping>(0);
                private SystemType systemtype;
                private Integer isPrimary;
                private ToolIntagrationMaster toolIntagration;
                
             //   private String connectorQualifiedClassName;

                public TestManagementSystem(){
                                
                }

                public TestManagementSystem(String title, String description, String connectionUri, String connectionUserName, String connectionPassword,
                                                String connectionProperty1,String connectionProperty2,String connectionProperty3,String connectionProperty4, String connectionProperty5, ProductMaster productMaster, 
                                                String testSystemName,String testSystemVersion,SystemType systemtype,Set<TestManagementSystemMapping> testManagementSystemMappings){
                                
                                this.title = title;
                                this.description = description;
                                this.connectionUri = connectionUri;
                                this.connectionPassword = connectionPassword;
                                this.connectionProperty1 = connectionProperty1;
                                this.connectionProperty2 = connectionProperty2;
                                this.connectionProperty3 = connectionProperty3;
                                this.connectionProperty4 = connectionProperty4;
                                this.connectionProperty5 = connectionProperty5;
                                
                                this.productMaster = productMaster;
                                this.testSystemName = testSystemName;
                                this.systemtype = systemtype;
                                this.testSystemVersion = testSystemVersion;
                                this.testManagementSystemMappings = testManagementSystemMappings;
                }
                
                
                @Id
                @GeneratedValue(strategy = IDENTITY)
                @Column(name = "testManagementSystemId", unique = true, nullable = false)
                public Integer getTestManagementSystemId() {
                                return testManagementSystemId;
                }

                public void setTestManagementSystemId(Integer testManagementSystemId) {
                                this.testManagementSystemId = testManagementSystemId;
                }


                @Column(name = "title", length = 250)
                public String getTitle() {
                                return title;
                }


                public void setTitle(String title) {
                                this.title = title;
                }


                @Column(name = "description", length = 2000)
                public String getDescription() {
                                return description;
                }


                public void setDescription(String description) {
                                this.description = description;
                }


                @Column(name = "connectionUri", length = 250)
                public String getConnectionUri() {
                                return connectionUri;
                }

                public void setConnectionUri(String connectionUri) {
                                this.connectionUri = connectionUri;
                }

                @Column(name = "connectionUserName", length = 50)
                public String getConnectionUserName() {
                                return connectionUserName;
                }

                public void setConnectionUserName(String connectionUserName) {
                                this.connectionUserName = connectionUserName;
                }

                @Column(name = "connectionPassword", length = 50)
                public String getConnectionPassword() {
                                return connectionPassword;
                }


                public void setConnectionPassword(String connectionPassword) {
                                this.connectionPassword = connectionPassword;
                }

                @Column(name = "connectionProperty1", length = 100)
                public String getConnectionProperty1() {
                                return connectionProperty1;
                }

                public void setConnectionProperty1(String connectionProperty1) {
                                this.connectionProperty1 = connectionProperty1;
                }

                @Column(name = "connectionProperty2", length = 100)
                public String getConnectionProperty2() {
                                return connectionProperty2;
                }

                public void setConnectionProperty2(String connectionProperty2) {
                                this.connectionProperty2 = connectionProperty2;
                }

                @Column(name = "connectionProperty3", length = 100)
                public String getConnectionProperty3() {
                                return connectionProperty3;
                }


                public void setConnectionProperty3(String connectionProperty3) {
                                this.connectionProperty3 = connectionProperty3;
                }

                @Column(name = "connectionProperty4", length = 100)
                public String getConnectionProperty4() {
                                return connectionProperty4;
                }


                public void setConnectionProperty4(String connectionProperty4) {
                                this.connectionProperty4 = connectionProperty4;
                }

               //Changes for Bug 792 - Adding TestSetFilter by name-starts
                @Column(name = "connectionProperty5", length = 100)
                public String getConnectionProperty5() {
                                return connectionProperty5;
                }


                public void setConnectionProperty5(String connectionProperty5) {
                                this.connectionProperty5 = connectionProperty5;
                }
                
                //Changes for Bug 792 - Adding TestSetFilter by name-ENDs
                
                @ManyToOne(fetch = FetchType.LAZY)
                @JoinColumn(name = "productId", nullable = false)
                public ProductMaster getProductMaster() {
                                return productMaster;
                }              
                
                public void setProductMaster(ProductMaster productMaster) {
                                this.productMaster = productMaster;
                }

                @Column(name = "testSystemName", length = 50)
                public String getTestSystemName() {
                                return testSystemName;
                }


                public void setTestSystemName(String testSystemName) {
                                this.testSystemName = testSystemName;
                }

                
                @ManyToOne(fetch = FetchType.LAZY)
                @JoinColumn(name = "systemtype")
                public SystemType getSystemType() {
					return systemtype;
				}

				public void setSystemType(SystemType systemtype) {
					this.systemtype = systemtype;
				}

				@Column(name = "testSystemVersion", length = 50)
                public String getTestSystemVersion() {
                                return testSystemVersion;
                }

                public void setTestSystemVersion(String testSystemVersion) {
                                this.testSystemVersion = testSystemVersion;
                }
                @OneToMany(fetch = FetchType.LAZY, mappedBy="testManagementSystem",cascade=CascadeType.ALL)
            	public Set<TestManagementSystemMapping> getTestManagementSystemMappings() {
            		return testManagementSystemMappings;
            	}

            	public void setTestManagementSystemMappings(
            			Set<TestManagementSystemMapping> testManagementSystemMappings) {
            		this.testManagementSystemMappings = testManagementSystemMappings;
            	}
				 @Column(name = "isPrimary", length = 50)
				public Integer getIsPrimary() {
					return isPrimary;
				}

				public void setIsPrimary(Integer isPrimary) {
					this.isPrimary = isPrimary;
				}

				@ManyToOne(fetch = FetchType.LAZY)
	            @JoinColumn(name = "toolIntagrationId")
				public ToolIntagrationMaster getToolIntagration() {
					return toolIntagration;
				}

				public void setToolIntagration(ToolIntagrationMaster toolIntagration) {
					this.toolIntagration = toolIntagration;
				}

			/*	@Column(name="connectorQualifiedClassName")
				public String getConnectorQualifiedClassName() {
					return connectorQualifiedClassName;
				}

				public void setConnectorQualifiedClassName(String connectorQualifiedClassName) {
					this.connectorQualifiedClassName = connectorQualifiedClassName;
				}*/
            	
            	
                
}
