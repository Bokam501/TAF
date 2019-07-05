/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.hcl.atf.etb.model.TestCaseList
 *  com.hcl.atf.etb.model.UserList
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.FetchType
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.Table
 *  javax.persistence.Temporal
 *  javax.persistence.TemporalType
 */
package com.hcl.atf.taf.model;

import com.hcl.atf.taf.model.ProductMaster;
import com.hcl.atf.taf.model.TestCaseList;
import com.hcl.atf.taf.model.UserList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="script_generation_details")
public class ScriptGenerationDetails {
    private Integer scriptId;
    private UserList user;
    private ProductMaster productMaster;
    private TestCaseList testCase;
    private Date scriptGeneratedDate;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="scriptId")
    public Integer getScriptId() {
        return this.scriptId;
    }

    public void setScriptId(Integer scriptId) {
        this.scriptId = scriptId;
    }

    @Temporal(value=TemporalType.TIMESTAMP)
    @Column(name="scriptGeneratedDate")
    public Date getScriptGeneratedDate() {
        return this.scriptGeneratedDate;
    }

    public void setScriptGeneratedDate(Date scriptGeneratedDate) {
        this.scriptGeneratedDate = scriptGeneratedDate;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userId")
    public UserList getUser() {
        return this.user;
    }

    public void setUser(UserList user) {
        this.user = user;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="productId")
    public ProductMaster getProductMaster() {
		return productMaster;
	}

	public void setProductMaster(ProductMaster productMaster) {
		this.productMaster = productMaster;
	}
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="testCaseId")
    public TestCaseList getTestCase() {
        return this.testCase;
    }

    public void setTestCase(TestCaseList testCase) {
        this.testCase = testCase;
    }

	
    
}
