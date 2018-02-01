package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.view.DayView;

public interface ArrangementService {

	public String insert(ArrangementPo arrangementPo);

	public List<ArrangementPo> selectAll();

	public ArrangementPo selectById(Integer arrangementId);

	public String updateById(ArrangementPo arrangementPo);

	public String deleteById(String arrangementId);

	public Integer selectCount(ArrangementPo queryArrangementPo);

	public List<ArrangementPo> selectByPeriodId(Integer periodId);

	public List<ArrangementPo> selectByRelationId(Integer relationId);

	public List<DayView> gnrDayViewList(Schedule schedule, Tclass tclass);

}
