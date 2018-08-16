package com.eastcompeace.model;

import org.apache.ibatis.type.Alias;

/**
 * 用户实体类
 * 
 * @author caobo
 * 
 */
@Alias(value = "User")
public class UserModel {
	// 与数据表一一映射的实体字段
	private int userId;
	private String userCode;
	private String userName;
	private String userSex;
	private String userPassword;
	private String userIdcard;
	private String userDeptcode;
	private String pwdvaildTime;
	private String isFrozen;
	private String isSmsVerify;
	private String userBirthdate;
	private String userMobile;
	private String userEmail;
	private String createTime;
	private String lastloginTime;
	private String lastloginIp;
	private String pwdinitial;
	private String pwdmodifydate; // PWD_MODIFY_DATE
	private String areaUuid;
	private String disuuid; // 用户区域
	private String userDivisionsId; // 用户能操作的区域

	// 虚拟显示字段
	private String userSexVal;
	private String isFrozenVal;
	private String createtimeStart;
	private String createtimeEnd;

	private String roleLevel;
	private String roleId;
	private String roleName;
	private String disUuid;
	private String disName;
	private String commUuid;
	private String commName;
	

	// getter and setter

	public int getUserId() {
		return userId;
	}

	public String getUserCode() {
		return this.userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return this.userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserIdcard() {
		return this.userIdcard;
	}

	public void setUserIdcard(String userIdcard) {
		this.userIdcard = userIdcard;
	}

	public String getUserDeptcode() {
		return this.userDeptcode;
	}

	public void setUserDeptcode(String userDeptcode) {
		this.userDeptcode = userDeptcode;
	}

	public String getPwdvaildTime() {
		return this.pwdvaildTime;
	}

	public void setPwdvaildTime(String pwdvaildTime) {
		this.pwdvaildTime = pwdvaildTime;
	}

	public String getIsFrozen() {
		return this.isFrozen;
	}

	public void setIsFrozen(String isFrozen) {
		this.isFrozen = isFrozen;
	}

	public String getIsSmsVerify() {
		return isSmsVerify;
	}

	public void setIsSmsVerify(String isSmsVerify) {
		this.isSmsVerify = isSmsVerify;
	}

	public String getUserBirthdate() {
		return this.userBirthdate;
	}

	public void setUserBirthdate(String userBirthdate) {
		this.userBirthdate = userBirthdate;
	}

	public String getUserMobile() {
		return this.userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastloginTime() {
		return this.lastloginTime;
	}

	public void setLastloginTime(String lastloginTime) {
		this.lastloginTime = lastloginTime;
	}

	public String getLastloginIp() {
		return this.lastloginIp;
	}

	public void setLastloginIp(String lastloginIp) {
		this.lastloginIp = lastloginIp;
	}

	public String getPwdinitial() {
		return pwdinitial;
	}

	public void setPwdinitial(String pwdinitial) {
		this.pwdinitial = pwdinitial;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserSexVal() {
		return this.userSexVal;
	}

	public void setUserSexVal(String userSexVal) {
		this.userSexVal = userSexVal;
	}

	public String getIsFrozenVal() {
		return this.isFrozenVal;
	}

	public void setIsFrozenVal(String isFrozenVal) {
		this.isFrozenVal = isFrozenVal;
	}

	public String getCreatetimeStart() {
		return createtimeStart;
	}

	public void setCreatetimeStart(String createtimeStart) {
		this.createtimeStart = createtimeStart;
	}

	public String getCreatetimeEnd() {
		return createtimeEnd;
	}

	public void setCreatetimeEnd(String createtimeEnd) {
		this.createtimeEnd = createtimeEnd;
	}

	public String getPwdmodifydate() {
		return pwdmodifydate;
	}

	public void setPwdmodifydate(String pwdmodifydate) {
		this.pwdmodifydate = pwdmodifydate;
	}

	public String getAreaUuid() {
		return areaUuid;
	}

	public void setAreaUuid(String areaUuid) {
		this.areaUuid = areaUuid;
	}

	public String getDisuuid() {
		return disuuid;
	}

	public void setDisuuid(String disuuid) {
		this.disuuid = disuuid;
	}
 
	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDisUuid() {
		return disUuid;
	}

	public void setDisUuid(String disUuid) {
		this.disUuid = disUuid;
	}

	public String getDisName() {
		return disName;
	}

	public void setDisName(String disName) {
		this.disName = disName;
	}

	public String getCommUuid() {
		return commUuid;
	}

	public void setCommUuid(String commUuid) {
		this.commUuid = commUuid;
	}

	public String getCommName() {
		return commName;
	}

	public void setCommName(String commName) {
		this.commName = commName;
	}

	public String getUserDivisionsId() {
		return userDivisionsId;
	}

	public void setUserDivisionsId(String userDivisionsId) {
		this.userDivisionsId = userDivisionsId;
	}
	
}