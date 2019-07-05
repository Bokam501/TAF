package com.hcl.atf.taf.model.json;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonWorkPackageWeeklyResourceReservation implements java.io.Serializable {
	private static final Log log = LogFactory.getLog(JsonWorkPackageWeeklyResourceReservation.class);
	
	@JsonProperty
	private Integer testFactoryResourceReservationId;
	@JsonProperty
	private Integer workPackageId;
	@JsonProperty
	private String workPackageName;
	@JsonProperty
	private Integer resourcePoolId;
	@JsonProperty
	private String resourcePoolName;
	@JsonProperty
	private Integer userId;
	@JsonProperty
	private String userName;
	
	@JsonProperty
	private Integer shiftId;
	@JsonProperty	
	private String shiftName;
	
	@JsonProperty	
	private String userCode;
	
	
	@JsonProperty
	private Integer userRoleId;
	@JsonProperty	
	private String userRoleName;
	
	@JsonProperty
	private Integer userTypeId;
	@JsonProperty	
	private String userTypeName;
	
	@JsonProperty
	private Integer reservationWeek;
	

	@JsonProperty
	private Integer reservationYear;
	
	@JsonProperty
	private Integer totalUsers;
	
	@JsonProperty
	private Float allWeeks;
	
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
	
	
	public JsonWorkPackageWeeklyResourceReservation(){
		this.allWeeks = 0f;
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
	
	
	
	
	public Integer getTestFactoryResourceReservationId() {
		return testFactoryResourceReservationId;
	}




	public void setTestFactoryResourceReservationId(
			Integer testFactoryResourceReservationId) {
		this.testFactoryResourceReservationId = testFactoryResourceReservationId;
	}




	public Integer getWorkPackageId() {
		return workPackageId;
	}




	public void setWorkPackageId(Integer workPackageId) {
		this.workPackageId = workPackageId;
	}




	public String getWorkPackageName() {
		return workPackageName;
	}




	public void setWorkPackageName(String workPackageName) {
		this.workPackageName = workPackageName;
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

	

	public Integer getTotalUsers() {
		return totalUsers;
	}




	public void setTotalUsers(Integer totalUsers) {
		this.totalUsers = totalUsers;
	}


	public Float getAllWeeks() {
		return allWeeks;
	}


	public void setAllWeeks(Float allWeeks) {
		this.allWeeks = allWeeks;
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
















	public Integer getReservationWeek() {
		return reservationWeek;
	}




	public void setReservationWeek(Integer reservationWeek) {
		this.reservationWeek = reservationWeek;
	}




	public Integer getReservationYear() {
		return reservationYear;
	}




	public void setReservationYear(Integer reservationYear) {
		this.reservationYear = reservationYear;
	}




	public String getUserCode() {
		return userCode;
	}




	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
















	public static Comparator<JsonWorkPackageWeeklyResourceReservation> jsonWorkPackageResourceComparator = new Comparator<JsonWorkPackageWeeklyResourceReservation>() {

		public int compare(JsonWorkPackageWeeklyResourceReservation shift1, JsonWorkPackageWeeklyResourceReservation shift2) {
			String shift1Id = shift1.getShiftName().toUpperCase();
			String shift2Id = shift2.getShiftName().toUpperCase();

			return shift1Id.compareTo(shift2Id);
		}
	};
}
