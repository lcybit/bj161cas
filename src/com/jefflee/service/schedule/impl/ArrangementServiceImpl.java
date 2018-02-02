package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Period;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.RelationService;
import com.jefflee.view.ArrangementView;
import com.jefflee.view.DayView;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Resource(name = "periodService")
	private PeriodService periodService;

	@Resource(name = "relationService")
	private RelationService relationService;

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
		dayView.setArrangementViewList(gnrArrangementViewList(schedule, tclass, dayOfWeek, periodPerDay));
		return dayView;
	}

	private List<ArrangementView> gnrArrangementViewList(Schedule schedule, Tclass tclass, Integer dayOfWeek,
			Integer periodPerDay) {
		List<ArrangementView> arrangementViewList = new ArrayList<ArrangementView>();
		for (int i = 0; i < periodPerDay; i++) {
			Integer orderOfDay = i + 1;
			arrangementViewList.add(gnrArrangementView(schedule, tclass, dayOfWeek, orderOfDay));
		}
		return arrangementViewList;
	}

	private ArrangementView gnrArrangementView(Schedule schedule, Tclass tclass, Integer dayOfWeek,
			Integer orderOfDay) {
		List<ArrangementView> arrangementViewList = arrangementMapper
				.gnrArrangementViewListByScheduleId(schedule.scheduleId);
		for (ArrangementView arrangementView : arrangementViewList) {
			Period period = arrangementView.getPeriod();
			if (period.dayOfWeek == dayOfWeek && period.orderOfDay == orderOfDay
					&& arrangementView.getTclass().tclassId == tclass.tclassId && arrangementView.getArranged() == 1) {
				return arrangementView;
			}
		}
		return new ArrangementView();
	}

	@Override
	public void cancelArrangement(Integer arrangementId) {
		ArrangementPo updateArrangementPo = new ArrangementPo();
		updateArrangementPo.setArrangementId(arrangementId);
		updateArrangementPo.setArranged(0);
		arrangementMapper.updateByPrimaryKeySelective(updateArrangementPo);
	}

	@Override
	public void excuteArrangement(Map<String, String> view) {
		Integer arrangementId = null;
		List<ArrangementView> arrangementViewList = arrangementMapper
				.gnrArrangementViewListByScheduleId(Integer.parseInt(view.get("currentScheduleId")));
		for (ArrangementView arrangementView : arrangementViewList) {
			if (view.get("chosenPeriodId").equals(arrangementView.getPeriod().periodId.toString())
					&& view.get("chosenTclassId").equals(arrangementView.getTclass().tclassId.toString())
					&& view.get("chosenCourse").equals(arrangementView.getCourse().courseId.toString())) {
				arrangementId = arrangementView.getArrangementId();
			}
		}
		ArrangementPo updateArrangementPo = arrangementMapper.selectByPrimaryKey(arrangementId);
		updateArrangementPo.setArranged(1);
		arrangementMapper.updateByPrimaryKeySelective(updateArrangementPo);
	}

	@Override
	public ArrangementView gnrArrangementViewById(Integer arrangementId) {
		return arrangementMapper.gnrArrangementViewById(arrangementId);
	}

}
