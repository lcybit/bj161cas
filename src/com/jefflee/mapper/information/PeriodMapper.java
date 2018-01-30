package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Period;

import tk.mybatis.mapper.common.Mapper;

@Repository("periodMapper")
public interface PeriodMapper extends Mapper<Period> {

}
