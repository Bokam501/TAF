package com.hcl.atf.taf.model.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;


public class JsonWorkPackageWeeklyDemandProjection implements java.io.Serializable{
	
	private static final Log log = LogFactory.getLog(JsonWorkPackageWeeklyDemandProjection.class);
	
	@JsonProperty	
	private Integer wpDemandProjectionId;
	@JsonProperty	
	private Integer resourceCount;
	
	@JsonProperty	
	private String workDate;
	@JsonProperty
	private int workPackageId;
	@JsonProperty	
	private String workPackageName;
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	@JsonProperty
	private Integer skillId;
	@JsonProperty	
	private String skillName;
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty	
	private String userRoleName;
	@JsonProperty
	private Long groupDemandId;
	
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty	
	private String userTypeName;
	
	@JsonProperty	
	private Integer skillandRoleBasedresourceCount;
	
	@JsonProperty
	private Integer demandRaisedByUserId;
	@JsonProperty	
	private String demandRaisedByUserName;
	@JsonProperty
	private float reservedResourceCount;
	
	@JsonProperty
	private float reservedResourceCountWk1;

	@JsonProperty
	private float reservedResourceCountWk2;

	@JsonProperty
	private float reservedResourceCountWk3;

	@JsonProperty
	private float reservedResourceCountWk4;

	@JsonProperty
	private float reservedResourceCountWk5;

	@JsonProperty
	private float reservedResourceCountWk6;

	@JsonProperty
	private float reservedResourceCountWk7;

	@JsonProperty
	private float reservedResourceCountWk8;
	@JsonProperty
	private float reservedResourceCountWk9;
	@JsonProperty
	private float reservedResourceCountWk10;
	@JsonProperty
	private float reservedResourceCountWk11;
	@JsonProperty
	private float reservedResourceCountWk12;
	@JsonProperty
	private float reservedResourceCountWk13;
	@JsonProperty
	private float reservedResourceCountWk14;
	@JsonProperty
	private float reservedResourceCountWk15;
	@JsonProperty
	private float reservedResourceCountWk16;
	@JsonProperty
	private float reservedResourceCountWk17;
	@JsonProperty
	private float reservedResourceCountWk18;
	@JsonProperty
	private float reservedResourceCountWk19;
	@JsonProperty
	private float reservedResourceCountWk20;
	@JsonProperty
	private float reservedResourceCountWk21;
	@JsonProperty
	private float reservedResourceCountWk22;
	@JsonProperty
	private float reservedResourceCountWk23;
	@JsonProperty
	private float reservedResourceCountWk24;
	@JsonProperty
	private float reservedResourceCountWk25;
	@JsonProperty
	private float reservedResourceCountWk26;
	@JsonProperty
	private float reservedResourceCountWk27;
	@JsonProperty
	private float reservedResourceCountWk28;
	@JsonProperty
	private float reservedResourceCountWk29;
	@JsonProperty
	private float reservedResourceCountWk30;
	@JsonProperty
	private float reservedResourceCountWk31;
	@JsonProperty
	private float reservedResourceCountWk32;
	@JsonProperty
	private float reservedResourceCountWk33;
	@JsonProperty
	private float reservedResourceCountWk34;
	@JsonProperty
	private float reservedResourceCountWk35;
	@JsonProperty
	private float reservedResourceCountWk36;
	@JsonProperty
	private float reservedResourceCountWk37;
	@JsonProperty
	private float reservedResourceCountWk38;
	@JsonProperty
	private float reservedResourceCountWk39;
	@JsonProperty
	private float reservedResourceCountWk40;
	@JsonProperty
	private float reservedResourceCountWk41;
	@JsonProperty
	private float reservedResourceCountWk42;
	
	@JsonProperty
	private float reservedResourceCountWk43;
	
	@JsonProperty
	private float reservedResourceCountWk44;
	
	@JsonProperty
	private float reservedResourceCountWk45;
	@JsonProperty
	private float reservedResourceCountWk46;
	@JsonProperty
	private float reservedResourceCountWk47;
	@JsonProperty
	private float reservedResourceCountWk48;
	@JsonProperty
	private float reservedResourceCountWk49;
	@JsonProperty
	private float reservedResourceCountWk50;
	
	@JsonProperty
	private float reservedResourceCountWk51;
	@JsonProperty
	private float reservedResourceCountWk52;
	
	@JsonProperty
	private Integer productId;
	@JsonProperty	
	private String productName;
	
	@JsonProperty
	private Integer testFactoryId;
	@JsonProperty	
	private String testFactoryName;
	
	
	@JsonProperty
	private Integer resourcePoolId;
	@JsonProperty	
	private String resourcePoolName;
	
	@JsonProperty	
	private Integer workWeek;
	@JsonProperty	
	private Integer workYear;
	@JsonProperty	
	private Float week1;
	@JsonProperty	
	private Float week2;
	@JsonProperty	
	private Float week3;
	@JsonProperty	
	private Float week4;
	@JsonProperty	
	private Float week5;
	@JsonProperty	
	private Float week6;
	@JsonProperty	
	private Float week7;
	@JsonProperty	
	private Float week8;
	@JsonProperty	
	private Float week9;
	@JsonProperty	
	private Float week10;
	@JsonProperty	
	private Float week11;
	@JsonProperty	
	private Float week12;
	@JsonProperty	
	private Float week13;
	@JsonProperty	
	private Float week14;
	@JsonProperty	
	private Float week15;
	@JsonProperty	
	private Float week16;
	@JsonProperty	
	private Float week17;
	@JsonProperty	
	private Float week18;
	@JsonProperty	
	private Float week19;
	@JsonProperty	
	private Float week20;
	@JsonProperty	
	private Float week21;
	@JsonProperty	
	private Float week22;
	@JsonProperty	
	private Float week23;
	@JsonProperty	
	private Float week24;
	@JsonProperty	
	private Float week25;
	@JsonProperty	
	private Float week26;
	@JsonProperty	
	private Float week27;
	@JsonProperty	
	private Float week28;
	@JsonProperty	
	private Float week29;
	@JsonProperty	
	private Float week30;
	@JsonProperty	
	private Float week31;
	@JsonProperty	
	private Float week32;
	@JsonProperty	
	private Float week33;
	@JsonProperty	
	private Float week34;
	@JsonProperty	
	private Float week35;
	@JsonProperty	
	private Float week36;
	@JsonProperty	
	private Float week37;
	@JsonProperty	
	private Float week38;
	@JsonProperty	
	private Float week39;
	@JsonProperty	
	private Float week40;
	@JsonProperty	
	private Float week41;
	@JsonProperty	
	private Float week42;
	@JsonProperty	
	private Float week43;
	@JsonProperty	
	private Float week44;
	@JsonProperty	
	private Float week45;
	@JsonProperty	
	private Float week46;
	@JsonProperty	
	private Float week47;
	@JsonProperty	
	private Float week48;
	@JsonProperty	
	private Float week49;
	@JsonProperty	
	private Float week50;
	@JsonProperty	
	private Float week51;
	@JsonProperty	
	private Float week52;
	@JsonProperty
	private List<Integer> reservedWeeks;
	@JsonProperty	
	private Integer userId;
	@JsonProperty	
	private String userName;
	@JsonProperty	
	private String userCode;
	
	public JsonWorkPackageWeeklyDemandProjection() {
		this.week1 = 0f;
		this.week2 = 0f;
		this.week3 = 0f;
		this.week4 = 0f;
		this.week5 = 0f;
		this.week6 = 0f;
		this.week7 = 0f;
		this.week8 = 0f;
		this.week9 = 0f;
		this.week10 = 0f;
		this.week11 = 0f;
		this.week12 = 0f;
		this.week13 = 0f;
		this.week14 = 0f;
		this.week15 = 0f;
		this.week16 = 0f;
		this.week17 = 0f;
		this.week18 = 0f;
		this.week19 = 0f;
		this.week20 = 0f;
		this.week21 = 0f;
		this.week22 = 0f;
		this.week23 = 0f;
		this.week24 = 0f;
		this.week25 = 0f;
		this.week26 = 0f;
		this.week27 = 0f;
		this.week28 = 0f;
		this.week29 = 0f;
		this.week30 = 0f;
		this.week31 = 0f;
		this.week32 = 0f;
		this.week33 = 0f;
		this.week34 = 0f;
		this.week35 = 0f;
		this.week36 = 0f;
		this.week37 = 0f;
		this.week38 = 0f;
		this.week39 = 0f;
		this.week40 = 0f;
		this.week41 = 0f;
		this.week42 = 0f;
		this.week43 = 0f;
		this.week44 = 0f;
		this.week45 = 0f;
		this.week46 = 0f;
		this.week47 = 0f;
		this.week48 = 0f;
		this.week49 = 0f;
		this.week50 = 0f;
		this.week51 = 0f;
		this.week52 = 0f;
		this.reservedWeeks = new ArrayList<Integer>();
		
	}



	public Integer getWpDemandProjectionId() {
		return wpDemandProjectionId;
	}



	public void setWpDemandProjectionId(Integer wpDemandProjectionId) {
		this.wpDemandProjectionId = wpDemandProjectionId;
	}



	public Integer getResourceCount() {
		return resourceCount;
	}



	public void setResourceCount(Integer resourceCount) {
		this.resourceCount = resourceCount;
	}



	public String getWorkDate() {
		return workDate;
	}



	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}



	public int getWorkPackageId() {
		return workPackageId;
	}



	public void setWorkPackageId(int workPackageId) {
		this.workPackageId = workPackageId;
	}



	public String getWorkPackageName() {
		return workPackageName;
	}



	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
	}



	public Integer getShiftId() {
		return shiftId;
	}



	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}



	public String getShiftName() {
		return shiftName;
	}



	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}



	public Integer getSkillId() {
		return skillId;
	}



	public void setSkillId(Integer skillId) {
		this.skillId = skillId;
	}



	public String getSkillName() {
		return skillName;
	}



	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}



	public Integer getUserRoleId() {
		return userRoleId;
	}



	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}



	public String getUserRoleName() {
		return userRoleName;
	}



	public void setUserRoleName(String userRoleName) {
		this.userRoleName = userRoleName;
	}



	public Integer getSkillandRoleBasedresourceCount() {
		return skillandRoleBasedresourceCount;
	}



	public void setSkillandRoleBasedresourceCount(
			Integer skillandRoleBasedresourceCount) {
		this.skillandRoleBasedresourceCount = skillandRoleBasedresourceCount;
	}



	public Float getWeek1() {
		return week1;
	}



	public void setWeek1(Float week1) {
		this.week1 = week1;
	}



	public Float getWeek2() {
		return week2;
	}



	public void setWeek2(Float week2) {
		this.week2 = week2;
	}



	public Float getWeek3() {
		return week3;
	}



	public void setWeek3(Float week3) {
		this.week3 = week3;
	}



	public Float getWeek4() {
		return week4;
	}



	public void setWeek4(Float week4) {
		this.week4 = week4;
	}



	public Float getWeek5() {
		return week5;
	}



	public void setWeek5(Float week5) {
		this.week5 = week5;
	}



	public Float getWeek6() {
		return week6;
	}



	public void setWeek6(Float week6) {
		this.week6 = week6;
	}



	public Float getWeek7() {
		return week7;
	}



	public void setWeek7(Float week7) {
		this.week7 = week7;
	}



	public Float getWeek8() {
		return week8;
	}



	public void setWeek8(Float week8) {
		this.week8 = week8;
	}



	public Float getWeek9() {
		return week9;
	}



	public void setWeek9(Float week9) {
		this.week9 = week9;
	}



	public Float getWeek10() {
		return week10;
	}



	public void setWeek10(Float week10) {
		this.week10 = week10;
	}



	public Float getWeek11() {
		return week11;
	}



	public void setWeek11(Float week11) {
		this.week11 = week11;
	}



	public Float getWeek12() {
		return week12;
	}



	public void setWeek12(Float week12) {
		this.week12 = week12;
	}



	public Float getWeek13() {
		return week13;
	}



	public void setWeek13(Float week13) {
		this.week13 = week13;
	}



	public Float getWeek14() {
		return week14;
	}



	public void setWeek14(Float week14) {
		this.week14 = week14;
	}



	public Float getWeek15() {
		return week15;
	}



	public void setWeek15(Float week15) {
		this.week15 = week15;
	}



	public Float getWeek16() {
		return week16;
	}



	public void setWeek16(Float week16) {
		this.week16 = week16;
	}



	public Float getWeek17() {
		return week17;
	}



	public void setWeek17(Float week17) {
		this.week17 = week17;
	}



	public Float getWeek18() {
		return week18;
	}



	public void setWeek18(Float week18) {
		this.week18 = week18;
	}



	public Float getWeek19() {
		return week19;
	}



	public void setWeek19(Float week19) {
		this.week19 = week19;
	}



	public Float getWeek20() {
		return week20;
	}



	public void setWeek20(Float week20) {
		this.week20 = week20;
	}



	public Float getWeek21() {
		return week21;
	}



	public void setWeek21(Float week21) {
		this.week21 = week21;
	}



	public Float getWeek22() {
		return week22;
	}



	public void setWeek22(Float week22) {
		this.week22 = week22;
	}



	public Float getWeek23() {
		return week23;
	}



	public void setWeek23(Float week23) {
		this.week23 = week23;
	}



	public Float getWeek24() {
		return week24;
	}



	public void setWeek24(Float week24) {
		this.week24 = week24;
	}



	public Float getWeek25() {
		return week25;
	}



	public void setWeek25(Float week25) {
		this.week25 = week25;
	}



	public Float getWeek26() {
		return week26;
	}



	public void setWeek26(Float week26) {
		this.week26 = week26;
	}



	public Float getWeek27() {
		return week27;
	}



	public void setWeek27(Float week27) {
		this.week27 = week27;
	}



	public Float getWeek28() {
		return week28;
	}



	public void setWeek28(Float week28) {
		this.week28 = week28;
	}



	public Float getWeek29() {
		return week29;
	}



	public void setWeek29(Float week29) {
		this.week29 = week29;
	}



	public Float getWeek30() {
		return week30;
	}



	public void setWeek30(Float week30) {
		this.week30 = week30;
	}



	public Float getWeek31() {
		return week31;
	}



	public void setWeek31(Float week31) {
		this.week31 = week31;
	}



	public Float getWeek32() {
		return week32;
	}



	public void setWeek32(Float week32) {
		this.week32 = week32;
	}



	public Float getWeek33() {
		return week33;
	}



	public void setWeek33(Float week33) {
		this.week33 = week33;
	}



	public Float getWeek34() {
		return week34;
	}



	public void setWeek34(Float week34) {
		this.week34 = week34;
	}



	public Float getWeek35() {
		return week35;
	}



	public void setWeek35(Float week35) {
		this.week35 = week35;
	}



	public Float getWeek36() {
		return week36;
	}



	public void setWeek36(Float week36) {
		this.week36 = week36;
	}



	public Float getWeek37() {
		return week37;
	}



	public void setWeek37(Float week37) {
		this.week37 = week37;
	}



	public Float getWeek38() {
		return week38;
	}



	public void setWeek38(Float week38) {
		this.week38 = week38;
	}



	public Float getWeek39() {
		return week39;
	}



	public void setWeek39(Float week39) {
		this.week39 = week39;
	}



	public Float getWeek40() {
		return week40;
	}



	public void setWeek40(Float week40) {
		this.week40 = week40;
	}



	public Float getWeek41() {
		return week41;
	}



	public void setWeek41(Float week41) {
		this.week41 = week41;
	}



	public Float getWeek42() {
		return week42;
	}



	public void setWeek42(Float week42) {
		this.week42 = week42;
	}



	public Float getWeek43() {
		return week43;
	}



	public void setWeek43(Float week43) {
		this.week43 = week43;
	}



	public Float getWeek44() {
		return week44;
	}



	public void setWeek44(Float week44) {
		this.week44 = week44;
	}



	public Float getWeek45() {
		return week45;
	}



	public void setWeek45(Float week45) {
		this.week45 = week45;
	}



	public Float getWeek46() {
		return week46;
	}



	public void setWeek46(Float week46) {
		this.week46 = week46;
	}



	public Float getWeek47() {
		return week47;
	}



	public void setWeek47(Float week47) {
		this.week47 = week47;
	}



	public Float getWeek48() {
		return week48;
	}



	public void setWeek48(Float week48) {
		this.week48 = week48;
	}



	public Float getWeek49() {
		return week49;
	}



	public void setWeek49(Float week49) {
		this.week49 = week49;
	}



	public Float getWeek50() {
		return week50;
	}



	public void setWeek50(Float week50) {
		this.week50 = week50;
	}



	public Float getWeek51() {
		return week51;
	}



	public void setWeek51(Float week51) {
		this.week51 = week51;
	}



	public Float getWeek52() {
		return week52;
	}



	public void setWeek52(Float week52) {
		this.week52 = week52;
	}



	public Integer getWorkWeek() {
		return workWeek;
	}



	public void setWorkWeek(Integer workWeek) {
		this.workWeek = workWeek;
	}



	public Integer getWorkYear() {
		return workYear;
	}



	public void setWorkYear(Integer workYear) {
		this.workYear = workYear;
	}



	public Integer getDemandRaisedByUserId() {
		return demandRaisedByUserId;
	}



	public void setDemandRaisedByUserId(Integer demandRaisedByUserId) {
		this.demandRaisedByUserId = demandRaisedByUserId;
	}



	public String getDemandRaisedByUserName() {
		return demandRaisedByUserName;
	}



	public void setDemandRaisedByUserName(String demandRaisedByUserName) {
		this.demandRaisedByUserName = demandRaisedByUserName;
	}



	public float getReservedResourceCountWk1() {
		return reservedResourceCountWk1;
	}



	public void setReservedResourceCountWk1(float reservedResourceCountWk1) {
		this.reservedResourceCountWk1 = reservedResourceCountWk1;
	}



	public float getReservedResourceCountWk2() {
		return reservedResourceCountWk2;
	}



	public void setReservedResourceCountWk2(float reservedResourceCountWk2) {
		this.reservedResourceCountWk2 = reservedResourceCountWk2;
	}



	public float getReservedResourceCountWk3() {
		return reservedResourceCountWk3;
	}



	public void setReservedResourceCountWk3(float reservedResourceCountWk3) {
		this.reservedResourceCountWk3 = reservedResourceCountWk3;
	}



	public float getReservedResourceCountWk4() {
		return reservedResourceCountWk4;
	}



	public void setReservedResourceCountWk4(float reservedResourceCountWk4) {
		this.reservedResourceCountWk4 = reservedResourceCountWk4;
	}



	public float getReservedResourceCountWk5() {
		return reservedResourceCountWk5;
	}



	public void setReservedResourceCountWk5(float reservedResourceCountWk5) {
		this.reservedResourceCountWk5 = reservedResourceCountWk5;
	}



	public float getReservedResourceCountWk6() {
		return reservedResourceCountWk6;
	}



	public void setReservedResourceCountWk6(float reservedResourceCountWk6) {
		this.reservedResourceCountWk6 = reservedResourceCountWk6;
	}



	public float getReservedResourceCountWk7() {
		return reservedResourceCountWk7;
	}



	public void setReservedResourceCountWk7(float reservedResourceCountWk7) {
		this.reservedResourceCountWk7 = reservedResourceCountWk7;
	}



	public float getReservedResourceCountWk8() {
		return reservedResourceCountWk8;
	}



	public void setReservedResourceCountWk8(float reservedResourceCountWk8) {
		this.reservedResourceCountWk8 = reservedResourceCountWk8;
	}



	public float getReservedResourceCountWk9() {
		return reservedResourceCountWk9;
	}



	public void setReservedResourceCountWk9(float reservedResourceCountWk9) {
		this.reservedResourceCountWk9 = reservedResourceCountWk9;
	}



	public float getReservedResourceCountWk10() {
		return reservedResourceCountWk10;
	}



	public void setReservedResourceCountWk10(float reservedResourceCountWk10) {
		this.reservedResourceCountWk10 = reservedResourceCountWk10;
	}



	public float getReservedResourceCountWk11() {
		return reservedResourceCountWk11;
	}



	public void setReservedResourceCountWk11(float reservedResourceCountWk11) {
		this.reservedResourceCountWk11 = reservedResourceCountWk11;
	}



	public float getReservedResourceCountWk12() {
		return reservedResourceCountWk12;
	}



	public void setReservedResourceCountWk12(float reservedResourceCountWk12) {
		this.reservedResourceCountWk12 = reservedResourceCountWk12;
	}



	public float getReservedResourceCountWk13() {
		return reservedResourceCountWk13;
	}



	public void setReservedResourceCountWk13(float reservedResourceCountWk13) {
		this.reservedResourceCountWk13 = reservedResourceCountWk13;
	}



	public float getReservedResourceCountWk14() {
		return reservedResourceCountWk14;
	}



	public void setReservedResourceCountWk14(float reservedResourceCountWk14) {
		this.reservedResourceCountWk14 = reservedResourceCountWk14;
	}



	public float getReservedResourceCountWk15() {
		return reservedResourceCountWk15;
	}



	public void setReservedResourceCountWk15(float reservedResourceCountWk15) {
		this.reservedResourceCountWk15 = reservedResourceCountWk15;
	}



	public float getReservedResourceCountWk16() {
		return reservedResourceCountWk16;
	}



	public void setReservedResourceCountWk16(float reservedResourceCountWk16) {
		this.reservedResourceCountWk16 = reservedResourceCountWk16;
	}



	public float getReservedResourceCountWk17() {
		return reservedResourceCountWk17;
	}



	public void setReservedResourceCountWk17(float reservedResourceCountWk17) {
		this.reservedResourceCountWk17 = reservedResourceCountWk17;
	}



	public float getReservedResourceCountWk18() {
		return reservedResourceCountWk18;
	}



	public void setReservedResourceCountWk18(float reservedResourceCountWk18) {
		this.reservedResourceCountWk18 = reservedResourceCountWk18;
	}



	public float getReservedResourceCountWk19() {
		return reservedResourceCountWk19;
	}



	public void setReservedResourceCountWk19(float reservedResourceCountWk19) {
		this.reservedResourceCountWk19 = reservedResourceCountWk19;
	}



	public float getReservedResourceCountWk20() {
		return reservedResourceCountWk20;
	}



	public void setReservedResourceCountWk20(float reservedResourceCountWk20) {
		this.reservedResourceCountWk20 = reservedResourceCountWk20;
	}



	public float getReservedResourceCountWk21() {
		return reservedResourceCountWk21;
	}



	public void setReservedResourceCountWk21(float reservedResourceCountWk21) {
		this.reservedResourceCountWk21 = reservedResourceCountWk21;
	}



	public float getReservedResourceCountWk22() {
		return reservedResourceCountWk22;
	}



	public void setReservedResourceCountWk22(float reservedResourceCountWk22) {
		this.reservedResourceCountWk22 = reservedResourceCountWk22;
	}



	public float getReservedResourceCountWk23() {
		return reservedResourceCountWk23;
	}



	public void setReservedResourceCountWk23(float reservedResourceCountWk23) {
		this.reservedResourceCountWk23 = reservedResourceCountWk23;
	}



	public float getReservedResourceCountWk24() {
		return reservedResourceCountWk24;
	}



	public void setReservedResourceCountWk24(float reservedResourceCountWk24) {
		this.reservedResourceCountWk24 = reservedResourceCountWk24;
	}



	public float getReservedResourceCountWk25() {
		return reservedResourceCountWk25;
	}



	public void setReservedResourceCountWk25(float reservedResourceCountWk25) {
		this.reservedResourceCountWk25 = reservedResourceCountWk25;
	}


	public float getReservedResourceCountWk26() {
		return reservedResourceCountWk26;
	}



	public void setReservedResourceCountWk26(float reservedResourceCountWk26) {
		this.reservedResourceCountWk26 = reservedResourceCountWk26;
	}



	public float getReservedResourceCountWk27() {
		return reservedResourceCountWk27;
	}



	public void setReservedResourceCountWk27(float reservedResourceCountWk27) {
		this.reservedResourceCountWk27 = reservedResourceCountWk27;
	}



	public float getReservedResourceCountWk28() {
		return reservedResourceCountWk28;
	}



	public void setReservedResourceCountWk28(float reservedResourceCountWk28) {
		this.reservedResourceCountWk28 = reservedResourceCountWk28;
	}



	public float getReservedResourceCountWk29() {
		return reservedResourceCountWk29;
	}



	public void setReservedResourceCountWk29(float reservedResourceCountWk29) {
		this.reservedResourceCountWk29 = reservedResourceCountWk29;
	}



	public float getReservedResourceCountWk30() {
		return reservedResourceCountWk30;
	}



	public void setReservedResourceCountWk30(float reservedResourceCountWk30) {
		this.reservedResourceCountWk30 = reservedResourceCountWk30;
	}



	public float getReservedResourceCountWk31() {
		return reservedResourceCountWk31;
	}



	public void setReservedResourceCountWk31(float reservedResourceCountWk31) {
		this.reservedResourceCountWk31 = reservedResourceCountWk31;
	}



	public float getReservedResourceCountWk32() {
		return reservedResourceCountWk32;
	}



	public void setReservedResourceCountWk32(float reservedResourceCountWk32) {
		this.reservedResourceCountWk32 = reservedResourceCountWk32;
	}



	public float getReservedResourceCountWk33() {
		return reservedResourceCountWk33;
	}



	public void setReservedResourceCountWk33(float reservedResourceCountWk33) {
		this.reservedResourceCountWk33 = reservedResourceCountWk33;
	}



	public float getReservedResourceCountWk34() {
		return reservedResourceCountWk34;
	}



	public void setReservedResourceCountWk34(float reservedResourceCountWk34) {
		this.reservedResourceCountWk34 = reservedResourceCountWk34;
	}



	public float getReservedResourceCountWk35() {
		return reservedResourceCountWk35;
	}



	public void setReservedResourceCountWk35(float reservedResourceCountWk35) {
		this.reservedResourceCountWk35 = reservedResourceCountWk35;
	}



	public float getReservedResourceCountWk36() {
		return reservedResourceCountWk36;
	}



	public void setReservedResourceCountWk36(float reservedResourceCountWk36) {
		this.reservedResourceCountWk36 = reservedResourceCountWk36;
	}



	public float getReservedResourceCountWk37() {
		return reservedResourceCountWk37;
	}



	public void setReservedResourceCountWk37(float reservedResourceCountWk37) {
		this.reservedResourceCountWk37 = reservedResourceCountWk37;
	}



	public float getReservedResourceCountWk38() {
		return reservedResourceCountWk38;
	}



	public void setReservedResourceCountWk38(float reservedResourceCountWk38) {
		this.reservedResourceCountWk38 = reservedResourceCountWk38;
	}



	public float getReservedResourceCountWk39() {
		return reservedResourceCountWk39;
	}



	public void setReservedResourceCountWk39(float reservedResourceCountWk39) {
		this.reservedResourceCountWk39 = reservedResourceCountWk39;
	}



	public float getReservedResourceCountWk40() {
		return reservedResourceCountWk40;
	}



	public void setReservedResourceCountWk40(float reservedResourceCountWk40) {
		this.reservedResourceCountWk40 = reservedResourceCountWk40;
	}



	public float getReservedResourceCountWk41() {
		return reservedResourceCountWk41;
	}



	public void setReservedResourceCountWk41(float reservedResourceCountWk41) {
		this.reservedResourceCountWk41 = reservedResourceCountWk41;
	}



	public float getReservedResourceCountWk42() {
		return reservedResourceCountWk42;
	}



	public void setReservedResourceCountWk42(float reservedResourceCountWk42) {
		this.reservedResourceCountWk42 = reservedResourceCountWk42;
	}



	public float getReservedResourceCountWk43() {
		return reservedResourceCountWk43;
	}



	public void setReservedResourceCountWk43(float reservedResourceCountWk43) {
		this.reservedResourceCountWk43 = reservedResourceCountWk43;
	}



	public float getReservedResourceCountWk44() {
		return reservedResourceCountWk44;
	}



	public void setReservedResourceCountWk44(float reservedResourceCountWk44) {
		this.reservedResourceCountWk44 = reservedResourceCountWk44;
	}



	public float getReservedResourceCountWk45() {
		return reservedResourceCountWk45;
	}



	public void setReservedResourceCountWk45(float reservedResourceCountWk45) {
		this.reservedResourceCountWk45 = reservedResourceCountWk45;
	}



	public float getReservedResourceCountWk46() {
		return reservedResourceCountWk46;
	}



	public void setReservedResourceCountWk46(float reservedResourceCountWk46) {
		this.reservedResourceCountWk46 = reservedResourceCountWk46;
	}



	public float getReservedResourceCountWk47() {
		return reservedResourceCountWk47;
	}



	public void setReservedResourceCountWk47(float reservedResourceCountWk47) {
		this.reservedResourceCountWk47 = reservedResourceCountWk47;
	}



	public float getReservedResourceCountWk48() {
		return reservedResourceCountWk48;
	}



	public void setReservedResourceCountWk48(float reservedResourceCountWk48) {
		this.reservedResourceCountWk48 = reservedResourceCountWk48;
	}



	public float getReservedResourceCountWk49() {
		return reservedResourceCountWk49;
	}



	public void setReservedResourceCountWk49(float reservedResourceCountWk49) {
		this.reservedResourceCountWk49 = reservedResourceCountWk49;
	}



	public float getReservedResourceCountWk50() {
		return reservedResourceCountWk50;
	}



	public void setReservedResourceCountWk50(float reservedResourceCountWk50) {
		this.reservedResourceCountWk50 = reservedResourceCountWk50;
	}



	public float getReservedResourceCountWk51() {
		return reservedResourceCountWk51;
	}



	public void setReservedResourceCountWk51(float reservedResourceCountWk51) {
		this.reservedResourceCountWk51 = reservedResourceCountWk51;
	}



	public float getReservedResourceCountWk52() {
		return reservedResourceCountWk52;
	}



	public void setReservedResourceCountWk52(float reservedResourceCountWk52) {
		this.reservedResourceCountWk52 = reservedResourceCountWk52;
	}



	public float getReservedResourceCount() {
		return reservedResourceCount;
	}



	public void setReservedResourceCount(float reservedResourceCount) {
		this.reservedResourceCount = reservedResourceCount;
	}



	public Long getGroupDemandId() {
		return groupDemandId;
	}



	public void setGroupDemandId(Long groupDemandId) {
		this.groupDemandId = groupDemandId;
	}



	public Integer getProductId() {
		return productId;
	}



	public void setProductId(Integer productId) {
		this.productId = productId;
	}



	public String getProductName() {
		return productName;
	}



	public void setProductName(String productName) {
		this.productName = productName;
	}



	public Integer getTestFactoryId() {
		return testFactoryId;
	}



	public void setTestFactoryId(Integer testFactoryId) {
		this.testFactoryId = testFactoryId;
	}



	public String getTestFactoryName() {
		return testFactoryName;
	}



	public void setTestFactoryName(String testFactoryName) {
		this.testFactoryName = testFactoryName;
	}



	public Integer getUserTypeId() {
		return userTypeId;
	}



	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}



	public String getUserTypeName() {
		return userTypeName;
	}



	public void setUserTypeName(String userTypeName) {
		this.userTypeName = userTypeName;
	}



	public List<Integer> getReservedWeeks() {
		return reservedWeeks;
	}



	public void setReservedWeeks(List<Integer> reservedWeeks) {
		this.reservedWeeks = reservedWeeks;
	}



	public Integer getResourcePoolId() {
		return resourcePoolId;
	}



	public void setResourcePoolId(Integer resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}



	public String getResourcePoolName() {
		return resourcePoolName;
	}



	public void setResourcePoolName(String resourcePoolName) {
		this.resourcePoolName = resourcePoolName;
	}



	public Integer getUserId() {
		return userId;
	}



	public void setUserId(Integer userId) {
		this.userId = userId;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
