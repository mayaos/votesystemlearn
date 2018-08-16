package com.eastcompeace.base;

/**
 * 常量定义
 * 
 * @author caobo
 */
public class Constant {
	/**浙江省代码*/
	public static final String PROVINCE = "33";
	
	/**
	 * 登录页面地址
	 */
	public static final String LOGIN = "modules/common/login/login.html";
	/**
	 * 首页页面地址
	 */
	public static final String PORTAL = "modules/common/portal/portal.html";

	/**
	 * 密码默认有效期为3个月
	 */
	public static final int PWD_MODIFY_DATE = 3;
	/**
	 * 用户登录后，在 session 中存储时采用的 id
	 */
	public static final String SESSION_USER = "USER_INFO";
	/**
	 * 用户登录后，在 session 中存储时采用的 id
	 */
	public static final String SESSION_ROLEID = "USER_ROLEID";

	/**
	 * 用户登录时，在 session 中存储的验证码ID
	 */
	public static final String SESSION_CHECK_CODE = "CheckCode";
	/**
	 * 用户找回密码时，在 session 中存储的验证码ID
	 */
	public static final String SESSION_CHECK_CODE2 = "CheckCode2";
	
	public static final String SESSION_CHECK_CODE3 = "updatecheckcode";


	/**
	 * menu是否为子节点，YES = 是；NO==否
	 */
	public static final String MENU_ISLEAF = "YES";

	/**
	 * 用户登录后，在session中存储用户菜单权限信息的id 实际对象是 List<MenuModel>
	 */
	public static final String SESSION_POWER = "USER_POWER_INFO";

	/**文件路径常量*/
	public static final String DECISION_FILES = "decision/";
	public static final String LOGO_IMG = "linkLogo/";
	public static final String COMM_NOTICE = "commNotice/";
	

	/**
	 * excel 03-07
	 */
    public static final String OFFICE_EXCEL_2003_POSTFIX = "xls";
    /**
	 * excel 2010
	 */
    public static final String OFFICE_EXCEL_2010_POSTFIX = "xlsx";

    public static final String EMPTY = "";
    public static final String POINT = ".";
    
    /**决策事项类型：1-文案*/
    public static final int DICISION_OBJ_TYPE_F = 1;
    /**决策事项类型：2-候选人*/
    public static final int DICISION_OBJ_TYPE_C = 2;
    /**决策事项类型：3-物业企业*/
    public static final int DICISION_OBJ_TYPE_P = 3;
    /**决策事项类型：4-其他*/
    public static final int DICISION_OBJ_TYPE_O = 4;
    
    /**导入业主信息文件日志*/
    public static final String IMPORT_LOG = "import-person-log.log";
    
    /** 不动产中心接口身份类型*/
    public static final int IDS_ID = 1;        //身份证 
    public static final int IDS_HOUSEHOLE = 2; //户籍本
    public static final int IDS_JOB = 3;       //工作证
    public static final int IDS_OFFICER = 4;   //军官证
    public static final int IDS_CIVILIAN = 5;  //文职证
    public static final int IDS_PASSPORT = 6;  //护照
    public static final int IDS_PROVE = 7;     //证明
    public static final int IDS_HMT = 8;       //港澳台身份证
    public static final int IDS_LEGAL = 9;     //法人代码证书
    public static final int IDS_LICENSE = 10;  //营业执照
    public static final int IDS_BUSINESS = 11;	//商业登记证书
    public static final int IDS_POLICE = 12;	//警官证
    public static final int IDS_SOLDIERS = 13;	//士兵证
    public static final int IDS_RETIRED = 14;	//军官退休证
    public static final int IDS_LAW_FIRM = 15;	//律师事务所执业许可证
    public static final int IDS_ORGANIZATION = 16;	//组织机构代码
    public static final int IDS_BEAD_EDGE = 17;	//珠边证
    public static final int IDS_RETIRE = 18;	//离休证
    public static final int IDS_HK_AND_MACAO = 19;	//港澳来往内地通行证
    public static final int IDS_FISHERMEN = 20;	//渔民证
    public static final int IDS_BORDER = 21;	//边防证
    public static final int IDS_OTHER = 22;	//其他

    
    /** 房屋用途表*/
    public static final int HOUSE_TYPE_1 =11;	//成套住宅
    public static final int HOUSE_TYPE_2 =12;	//非成套住宅
    public static final int HOUSE_TYPE_3 =13;	//集体宿舍
    public static final int HOUSE_TYPE_4 =14;	//经济适用住房
    public static final int HOUSE_TYPE_5 =16;	//别墅
    public static final int HOUSE_TYPE_6 =17;	//车库
    public static final int HOUSE_TYPE_7 =18;	//单车房
    public static final int HOUSE_TYPE_8 =19;	//高档公寓
    public static final int HOUSE_TYPE_9 =21;	//工业
    public static final int HOUSE_TYPE_10 =22;	//公用设施
    
    public static final int OWNER_ROWS = 10;   //调用不动产产权状态批量值10（不动产中心限制）

}
