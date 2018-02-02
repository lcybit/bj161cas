package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Period;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.view.DayView;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Resource(name = "periodService")
	private PeriodService periodService;

	@Override
	public Integer insert(ArrangementPo arrangementPo) {
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
	public Integer updateById(ArrangementPo arrangementPo) {
		if (arrangementMapper.updateByPrimaryKey(arrangementPo) == 1) {
			return arrangementPo.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer arrangementId) {
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
		ArrangementPo selectArrangementPo = new ArrangementPo();
		selectArrangementPo.setPeriodId(periodId);
		return arrangementMapper.select(selectArrangementPo);
	}

	@Override
	public List<DayView> gnrDayViewList(Schedule schedule, Tclass tclass) {
		List<DayView> dayViewList = new ArrayList<DayView>();
		for (int i = 0; i < schedule.days; i++) {
			dayViewList.add(gnrDayView(schedule, tclass, i + 1));
		}
		return dayViewList;
	}

	private DayView gnrDayView(Schedule schedule, Tclass tclass, Integer dayOfWeek) {
		Integer periodPerDay = schedule.forenoon + schedule.afternoon + schedule.evening;
		DayView dayView = new DayView();
		dayView.setArrangementList(selectArrangementList(schedule, tclass, dayOfWeek, periodPerDay));
		return dayView;
	}

	private List<Arrangement> selectArrangementList(Schedule schedule, Tclass tclass, Integer dayOfWeek,
			Integer periodPerDay) {
		List<Arrangement> arrangementList = new ArrayList<Arrangement>();
		for (int i = 0; i < periodPerDay; i++) {
			Integer orderOfDay = i + 1;
			Period period = new Period(periodService.selectByOrder(dayOfWeek, orderOfDay));
			arrangementList.add(selectArrangementByPeriodTclass(schedule, period, tclass));
		}
		return arrangementList;
	}

	private Arrangement selectArrangementByPeriodTclass(Schedule schedule, Period period, Tclass tclass) {
		List<Arrangement> arrangementList = arrangementMapper.selectEntityByScheduleId(schedule.scheduleId);
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.period.periodId == period.periodId && arrangement.tclass.tclassId == tclass.tclassId
					&& arrangement.arranged == 1) {
				return arrangement;
			}
		}
		return new Arrangement();
	}

	@Override
	public void cancelArrangement(Integer arrangementId) {
		ArrangementPo updateArrangementPo = new ArrangementPo();
		updateArrangementPo.setArrangementId(arrangementId);
		updateArrangementPo.setArranged(0);
		arrangementMapper.updateByPrimaryKeySelective(updateArrangementPo);
	}

	@Override
	public void excuteArrangement(ArrangementPo arrangementPo) {
		List<ArrangementPo> updateArrangementPoList = arrangementMapper.select(arrangementPo);
		for (ArrangementPo updateArrangementPo : updateArrangementPoList) {
			updateArrangementPo.setArranged(1);
			arrangementMapper.updateByPrimaryKeySelective(updateArrangementPo);
		}
	}

	@Override
	public Arrangement selectArrangementById(Integer arrangementId) {
		return arrangementMapper.selectEntityById(arrangementId);
	}

}
