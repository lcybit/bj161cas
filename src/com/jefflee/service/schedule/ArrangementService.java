package com.jefflee.service.schedule;

import java.util.List;
import java.util.Map;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.view.TclassPeriodView;
import com.jefflee.view.WeekView;

public interface ArrangementService {

	public Integer insert(ArrangementPo arrangementPo);

	public List<ArrangementPo> selectAll();

	public ArrangementPo selectById(Integer arrangementId);

	public Integer updateById(ArrangementPo arrangementPo);

	public Integer deleteById(Integer arrangementId);

	public Integer selectCount(ArrangementPo queryArrangementPo);

	public List<ArrangementPo> selectByPeriodId(Integer periodId);

	void updateArrangement(TclassPeriodView tclassPeriodView, Integer arranged, Integer prioriry);

	public Arrangement selectArrangementById(Integer arrangementId);

	public List<Map<String, String>> gnrConflictMap(Integer scheduleId, Integer courseId);

	public List<WeekView> gnrWeekViewList(Schedule schedule, List<Course> courseList, List<Tclass> tclassList);

	public void gnrArrangementList(Integer scheduleId);

}
