package com.jefflee.mapper.schedule;

import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.jefflee.entity.schedule.Plan;
import com.jefflee.po.schedule.PlanPo;

import tk.mybatis.mapper.common.Mapper;

public interface PlanMapper extends Mapper<PlanPo> {

	@Select("select * from info_plan where plan_id = #{planId}")
	@Results({ @Result(id = true, column = "plan_id", property = "planId"),
			@Result(column = "relation_id", property = "relation", one = @One(select = "com.jefflee.mapper.schedule.RelationMapper.selectEntityById") ),
			@Result(column = "period_num", property = "periodNum") })
	public Plan selectEntityById(Integer planId);

}
