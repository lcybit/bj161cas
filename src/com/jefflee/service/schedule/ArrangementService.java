package com.jefflee.service.schedule;

import java.util.List;
import java.util.Map;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.view.ArrangementView;
import com.jefflee.view.WeekView;

public interface ArrangementService {

	public Integer insert(ArrangementPo arrangementPo);

	public List<ArrangementPo> selectAll();

	public ArrangementPo selectById(Integer arrangementId);

	public Integer updateById(ArrangementPo arrangementPo);

	public Integer deleteById(Integer arrangementId);

	public Integer selectCount(ArrangementPo queryArrangementPo);

	public List<ArrangementPo> selectByPeriodId(Integer periodId);

	public Arrangement selectArrangementById(Integer arrangementId);

	public Map<String, String> gnrConflictList(ArrangementView arrangementView);

	public Map<String, String> gnrPriorityList(ArrangementView arrangementView);

	public List<WeekView> gnrWeekViewListByCourseTclass(Schedule schedule, List<Course> courseList,
			List<Tclass> tclassList);

	public void gnrArrangementList(Integer scheduleId);

	public List<WeekView> gnrWeekViewListByTclassTeacher(Schedule schedule, List<Tclass> tclassList,
			List<Teacher> teacherList);

	public void setArranged(ArrangementView arrangementView);

	public void setPriority(ArrangementView arrangementView);

	public Integer insertList(List<ArrangementPo> arrangementPoList);

}
