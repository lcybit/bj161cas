package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Period;
import com.jefflee.mapper.information.PeriodMapper;
import com.jefflee.service.information.PeriodService;

@Service("periodService")
public class PeriodServiceImpl implements PeriodService {

	@Resource(name = "periodMapper")
	private PeriodMapper periodMapper;

	@Override
	public Integer insert(Period period) {
		if (periodMapper.insert(period) == 1) {
			return period.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public List<Period> selectList() {
		return periodMapper.selectAll();
	}

	@Override
	public Period selectById(Integer periodId) {
		return periodMapper.selectByPrimaryKey(periodId);
	}

	@Override
	public Integer updateById(Period period) {
		if (periodMapper.updateByPrimaryKey(period) == 1) {
			return period.getPeriodId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer periodId) {
		if (periodMapper.deleteByPrimaryKey(periodId) == 1) {
			return periodId;
		} else {
			return null;
		}
	}

	@Override
	public Period selectByPosition(Integer dayOfWeek, Integer orderOfDay) {
		Period period = new Period();
		period.setDayOfWeek(dayOfWeek);
		period.setOrderOfDay(orderOfDay);
		return periodMapper.selectOne(period);
	}

	@Override
	public List<Period> selectListByRange(Integer daysPerWeek, Integer periodsPerDay) {
		return periodMapper.selectListByScope(daysPerWeek, periodsPerDay);
	}
}
