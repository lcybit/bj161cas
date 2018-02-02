package com.jefflee.mapper.information;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Period;
import com.jefflee.po.information.PeriodPo;

import tk.mybatis.mapper.common.Mapper;

@Repository("periodMapper")
public interface PeriodMapper extends Mapper<PeriodPo> {

	@Select("select * from info_period where period_id = #{periodId}")
	@Results({ @Result(id = true, column = "period_id", property = "periodId"),
			@Result(column = "period_no", property = "periodNo") })
	public Period selectEntityById(Integer periodId);

}
