package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Day;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.schedule.ArrangementService;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Resource(name = "periodService")
	private PeriodService periodService;

	@Override
	public String insert(ArrangementPo arrangementPo) {
		if (arrangementMapper.insert(arrangementPo) == 1) {
			return arrangementPo.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public List<ArrangementPo> selectAll() {
		return arrangementMapper.selectAll();
	}

	@Override
	public ArrangementPo selectById(Integer arrangementId) {
		return arrangementMapper.selectByPrimaryKey(arrangementId);
	}

	@Override
	public String updateById(ArrangementPo arrangementPo) {
		if (arrangementMapper.updateByPrimaryKey(arrangementPo) == 1) {
			return arrangementPo.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public String deleteById(String arrangementId) {
		if (arrangementMapper.deleteByPrimaryKey(arrangementId) == 1) {
			return arrangementId;
		} else {
			return null;
		}
	}

	@Override
	public Integer selectCount(ArrangementPo arrangementPo) {
		return arrangementMapper.selectCount(arrangementPo);
	}

	@Override
	public List<ArrangementPo> selectByPeriodId(Integer periodId) {
		ArrangementPo queryArrangementPo = new ArrangementPo();
		queryArrangementPo.setPeriodId(periodId);
		return arrangementMapper.select(queryArrangementPo);
	}

	@Override
	public List<ArrangementPo> selectByRelationId(Integer relationId) {
		ArrangementPo queryArrangementPo = new ArrangementPo();
		queryArrangementPo.setRelationId(relationId);
		return arrangementMapper.select(queryArrangementPo);
	}

	@Override
	public List<Day> gnrDayList(Schedule schedule, Tclass tclass) {
		List<Day> dayList = new ArrayList<Day>();
		for (int i = 0; i < schedule.days; i++) {
			dayList.add(gnrDay(schedule, tclass, i + 1));
		}
		return dayList;
	}

	private Day gnrDay(Schedule schedule, Tclass tclass, Integer dayOfWeek) {
		Integer periodPerDay = schedule.forenoon + schedule.afternoon + schedule.evening;
		List<Arrangement> arrangementList = new ArrayList<Arrangement>();
		for (int i = 0; i < periodPerDay; i++) {
			Integer orderOfDay = i + 1;
			PeriodPo queryPeriodPo = new PeriodPo();
			queryPeriodPo.setOrderOfDay(orderOfDay);
			queryPeriodPo.setDayOfWeek(dayOfWeek);
			PeriodPo periodPo = periodService.select(queryPeriodPo).get(0);

		}
		return null;
	}
}
