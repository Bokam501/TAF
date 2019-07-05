/**
 * 
 */
package com.hcl.atf.taf.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author silambarasur
 *
 */
@Entity
@Table(name="userActivityTracker")
public class UserActivityTracker implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer userActivityId;
	private UserList user;
	private Date userLoginTime;
	private Date userLogoutTime;
	private String reason;
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userActivityId", unique = true, nullable = false)
	public Integer getUserActivityId() {
		return userActivityId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="userId")
	public UserList getUser() {
		return user;
	}
	
	@Column(name="userLoginTime")
	public Date getUserLoginTime() {
		return userLoginTime;
	}
	
	@Column(name="userLogoutTime")
	public Date getUserLogoutTime() {
		return userLogoutTime;
	}
	
	@Column(name="reason")
	public String getReason() {
		return reason;
	}
	
	public void setUserActivityId(Integer userActivityId) {
		this.userActivityId = userActivityId;
	}
	public void setUser(UserList user) {
		this.user = user;
	}
	public void setUserLoginTime(Date userLoginTime) {
		this.userLoginTime = userLoginTime;
	}
	public void setUserLogoutTime(Date userLogoutTime) {
		this.userLogoutTime = userLogoutTime;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
}
