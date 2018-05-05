package com.jefflee.util;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
	@InsertProvider(type = MyProvider.class, method = "dynamicSQL")
	int insertListOnDuplicateKeyUpdate(List<T> recordList);
}
