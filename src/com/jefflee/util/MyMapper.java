package com.jefflee.util;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;

import tk.mybatis.mapper.common.Mapper;

public interface MyMapper<T> extends Mapper<T> {
	@InsertProvider(type = MyProvider.class, method = "dynamicSQL")
	int insertListOnDuplicateKeyUpdate(List<T> recordList);
}
