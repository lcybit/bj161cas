package com.jefflee.mapper.information;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Period;
import com.jefflee.util.MyMapper;

@Repository("periodMapper")
public interface PeriodMapper extends MyMapper<Period> {

	// TODO order -> period
	@Select("select * from info_period where day_of_week <= #{daysPerWeek} and order_of_day <= #{periodsPerDay}")
	@Results({ @Result(id = true, column = "period_id", property = "periodId"),
			@Result(column = "period_no", property = "periodNo"),
			@Result(column = "day_of_week", property = "dayOfWeek"),
			@Result(column = "order_of_day", property = "orderOfDay") })
	public List<Period> selectListByScope(@Param("daysPerWeek") Integer daysPerWeek,
			@Param("periodsPerDay") Integer periodsPerDay);

}
