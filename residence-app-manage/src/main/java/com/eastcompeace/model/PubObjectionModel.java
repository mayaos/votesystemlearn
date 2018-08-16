package com.eastcompeace.model;
import org.apache.ibatis.type.Alias;

/**
 * 意见回馈信息实体
 * @author 
 *
 */
@Alias(value="PubObjection")
public class PubObjectionModel {
	
	private Integer objectionId; 
	
    private String citizenId;
    
    private String userName;

    private String objectionContent;

    private String objectionTime;

    private String replyContent;

    private String replyTime;

    private String objectionStatus;

    private String objectionPic1;

    private String objectionPic2;

    private String objectionPic3;

    private String objectionPic4;
    
    private String beginDate;
    
    private String endDate;
    
    public Integer getObjectionId() {
		return objectionId;
	}

	public void setObjectionId(Integer objectionId) {
		this.objectionId = objectionId;
	}

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }
    
    

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getObjectionContent() {
        return objectionContent;
    }

    public void setObjectionContent(String objectionContent) {
        this.objectionContent = objectionContent;
    }

    public String getObjectionTime() {
        return objectionTime;
    }

    public void setObjectionTime(String objectionTime) {
        this.objectionTime = objectionTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }


    public String getObjectionStatus() {
		return objectionStatus;
	}

	public void setObjectionStatus(String objectionStatus) {
		this.objectionStatus = objectionStatus;
	}

	public String getObjectionPic1() {
        return objectionPic1;
    }

    public void setObjectionPic1(String objectionPic1) {
        this.objectionPic1 = objectionPic1;
    }

    public String getObjectionPic2() {
        return objectionPic2;
    }

    public void setObjectionPic2(String objectionPic2) {
        this.objectionPic2 = objectionPic2;
    }

    public String getObjectionPic3() {
        return objectionPic3;
    }

    public void setObjectionPic3(String objectionPic3) {
        this.objectionPic3 = objectionPic3;
    }

    public String getObjectionPic4() {
        return objectionPic4;
    }

    public void setObjectionPic4(String objectionPic4) {
        this.objectionPic4 = objectionPic4;
    }

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
    
    
}