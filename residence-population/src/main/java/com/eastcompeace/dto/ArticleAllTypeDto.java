package com.eastcompeace.dto;

import java.util.ArrayList;
import java.util.List;

public class ArticleAllTypeDto {
	private List<ArticleTypeDto> typeList = new ArrayList<>();	//文章类别实体类集合

	public List<ArticleTypeDto> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ArticleTypeDto> typeList) {
		this.typeList = typeList;
	}
}
