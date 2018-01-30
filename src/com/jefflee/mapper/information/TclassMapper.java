package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Tclass;

import tk.mybatis.mapper.common.Mapper;

@Repository("tclassMapper")
public interface TclassMapper extends Mapper<Tclass> {

}
