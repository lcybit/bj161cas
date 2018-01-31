package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.po.information.PeriodPo;

import tk.mybatis.mapper.common.Mapper;

@Repository("periodMapper")
public interface PeriodMapper extends Mapper<PeriodPo> {

}
