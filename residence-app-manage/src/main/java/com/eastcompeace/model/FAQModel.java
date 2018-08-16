package com.eastcompeace.model;
/**
 * 常见问题信息s
 * @author caiyaonan
 *
 */
public class FAQModel {
    /** 问题id */
    private int id;
    /** 问题 */
	private String questions;
	/** 回答 */
	private String answers;
	/** 是否发布标志 0:未发布 1:发布 */
	private int issueFlag;
	/** 问答排序 */
	private String orderNo;
	/** 创建时间 */
	private String createTime;
	/** 修改时间 */
	private String updateTime;
	
	
	
 



    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }



    public String getQuestions() {
        return questions;
    }



    public void setQuestions(String questions) {
        this.questions = questions;
    }



    public String getAnswers() {
        return answers;
    }



    public void setAnswers(String answers) {
        this.answers = answers;
    }







    public int getIssueFlag() {
        return issueFlag;
    }



    public void setIssueFlag(int issueFlag) {
        this.issueFlag = issueFlag;
    }



    public String getOrderNo() {
        return orderNo;
    }



    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }



    public String getCreateTime() {
        return createTime;
    }



    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    public String getUpdateTime() {
        return updateTime;
    }



    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }



    @Override
    public String toString() {
        return "FAQModel [id=" + id + ", questions=" + questions + ", answers=" + answers + ", issueFlag=" + issueFlag + ", orderNo=" + orderNo + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
    }



    
	
	
}
