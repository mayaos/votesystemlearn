package com.eastcompeace.enums;

import java.util.LinkedHashMap;

/**
 * 
 * @FileName ArticleTypeEnum.java
 * @Date   2018年5月30日下午1:53:43
 * @author wzh
 *
 * 文章类别枚举
 *
 */
public enum ArticleTypeEnum{
    TPXW_TYPE("1", "tpxw"), 		//图片新闻
    GDDT_TYPE("3", "gddt"), 		//工作动态
    TSZS_TYPE("5", "tszs"), 		//平安栏
    MTJJ_TYPE("6", "mtjj"), 		//社会聚焦
    ZCFG_TYPE("7", "zcfg"), 		//政策法规
    DCYJ_TYPE("10", "dcyj"), 		//工作交流
    TXWH_TYPE("11", "txwh");		//文化园
    
    private String typeCode;
    private String typeValue;
    private static final LinkedHashMap<String, String> map;
    
    static {
        map = new LinkedHashMap<>();
        for (ArticleTypeEnum articleTypeEnum : ArticleTypeEnum.values()) {
            map.put(articleTypeEnum.getTypeCode(), articleTypeEnum.getTypeValue());
        }
    }

    private ArticleTypeEnum(String typeCode, String typeValue) {
        this.typeCode = typeCode;
        this.typeValue = typeValue;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeValue() {
        return typeValue;
    }
    
    public static LinkedHashMap<String, String> getMap() {
        return map;
    }
    
    public static ArticleTypeEnum getByTypeCode(String typeCode) {
        for (ArticleTypeEnum articleTypeEnum : ArticleTypeEnum.values()) {
            if (articleTypeEnum.getTypeCode().equals(typeCode)) {
                return articleTypeEnum;
            }
        }
        return null;
    }
}
