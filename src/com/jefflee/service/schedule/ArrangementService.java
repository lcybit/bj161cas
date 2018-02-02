package com.jefflee.service.schedule;

import java.util.List;
import java.util.Map;

import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.view.ArrangementView;
import com.jefflee.view.DayView;

public interface ArrangementService {

	public Integer insert(ArrangementPo arrangementPo);

	public List<ArrangementPo> selectAll();

	public ArrangementPo selectById(Integer arrangementId);

	public Integer updateById(ArrangementPo arrangementPo);

	public Integer deleteById(Integer arrangementId);

	public Integer selectCount(ArrangementPo queryArrangementPo);

	public List<ArrangementPo> selectByPeriodId(Integer periodId);

	public List<ArrangementPo> selectByRelationId(Integer relationId);

	public List<DayView> gnrDayViewList(Schedule schedule, Tclass tclass);

	void cancelArrangement(Integer arrangementId);

	void excuteArrangement(Map<String, String> arrangementView);

	public ArrangementView gnrArrangementViewById(Integer arrangementId);

}
