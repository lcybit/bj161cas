package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Period;

public interface PeriodService {

	Integer insert(Period period);

	List<Period> selectList();

	Period selectById(Integer periodId);

	Integer updateById(Period period);

	Integer deleteById(Integer periodId);

	Period selectByPosition(Integer dayOfWeek, Integer orderOfDay);

	List<Period> selectListByRange(Integer daysPerWeek, Integer periodsPerDay);

}
