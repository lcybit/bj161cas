package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.view.CourseView;
import com.jefflee.view.DayView;
import com.jefflee.view.PeriodView;
import com.jefflee.view.TclassPeriodView;
import com.jefflee.view.WeekView;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Resource(name = "periodService")
	private PeriodService periodService;

	@Resource(name = "planService")
	private PlanService planService;

	/* CRUD Block Start */

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

	/* CRUD Block End */

	/* Display Block Start */

	@Override
	public List<WeekView> gnrWeekViewList(Schedule schedule, List<Course> courseList, List<Tclass> tclassList) {
		List<WeekView> weekViewList = new ArrayList<WeekView>();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(schedule.scheduleId);
		List<PlanPo> schedulePlanPoList = planService.selectByScheduleId(schedule.scheduleId);
		Integer daysPerWeek = schedule.days;
		Integer periodsPerDay = schedule.forenoon + schedule.afternoon + schedule.evening;

		for (Tclass tclass : tclassList) {
			weekViewList.add(gnrWeekView(scheduleArrangementList, schedulePlanPoList, courseList, tclass, daysPerWeek,
					periodsPerDay));
		}

		return weekViewList;
	}

	private WeekView gnrWeekView(List<Arrangement> scheduleArrangementList, List<PlanPo> schedulePlanPoList,
			List<Course> courseList, Tclass tclass, Integer daysPerWeek, Integer periodsPerDay) {
		WeekView weekView = new WeekView();

		weekView.setType("tclass");
		weekView.setTypeId(tclass.tclassId);
		List<CourseView> courseViewList = gnrCourseViewList(scheduleArrangementList, schedulePlanPoList, courseList,
				tclass);
		weekView.setCourseViewList(courseViewList);
		List<DayView> dayViewList = gnrDayViewList(scheduleArrangementList, tclass, daysPerWeek, periodsPerDay);
		weekView.setDayViewList(dayViewList);

		return weekView;
	}

	private List<CourseView> gnrCourseViewList(List<Arrangement> scheduleArrangementList,
			List<PlanPo> schedulePlanPoList, List<Course> courseList, Tclass tclass) {
		List<CourseView> courseViewList = new ArrayList<CourseView>();

		for (Course course : courseList) {
			courseViewList.add(gnrCourseView(scheduleArrangementList, schedulePlanPoList, course, tclass));
		}

		return courseViewList;
	}

	public CourseView gnrCourseView(List<Arrangement> scheduleArrangementList, List<PlanPo> schedulePlanPoList,
			Course course, Tclass tclass) {
		CourseView courseView = new CourseView();

		courseView.setCourse(course);
		for (PlanPo planPo : schedulePlanPoList) {
			if (planPo.getCourseId() == course.courseId && planPo.getTclassId() == tclass.tclassId) {
				courseView.setPeriodNum(planPo.getPeriodNum());
				break;
			}
		}
		Integer arrangedNum = 0;
		for (Arrangement arrangement : scheduleArrangementList) {
			if (arrangement.course.courseId == course.courseId && arrangement.tclass.tclassId == tclass.tclassId) {
				arrangedNum++;
			}
		}
		courseView.setArrangedNum(arrangedNum);

		return courseView;
	}

	private List<DayView> gnrDayViewList(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer daysPerWeek,
			Integer periodsPerDay) {
		List<DayView> dayViewList = new ArrayList<DayView>();

		for (int i = 0; i < daysPerWeek; i++) {
			dayViewList.add(gnrDayView(scheduleArrangementList, tclass, i + 1, periodsPerDay));
		}

		return dayViewList;
	}

	private DayView gnrDayView(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer dayOfWeek,
			Integer periodsPerDay) {
		DayView dayView = new DayView();

		dayView.setPeriodViewList(gnrPeriodViewList(scheduleArrangementList, tclass, dayOfWeek, periodsPerDay));

		return dayView;
	}

	private List<PeriodView> gnrPeriodViewList(List<Arrangement> scheduleArrangementList, Tclass tclass,
			Integer dayOfWeek, Integer periodsPerDay) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();

		for (int i = 0; i < periodsPerDay; i++) {
			periodViewList.add(gnrPeriodView(scheduleArrangementList, tclass, dayOfWeek, i + 1));
		}

		return periodViewList;
	}

	private PeriodView gnrPeriodView(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer dayOfWeek,
			Integer orderOfDay) {
		TclassPeriodView tclassPeriodView = new TclassPeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		tclassPeriodView.setPeriodId(periodPo.getPeriodId());
		tclassPeriodView.setDayOfWeek(dayOfWeek);
		tclassPeriodView.setOrderOfDay(orderOfDay);
		tclassPeriodView.setTclassId(tclass.tclassId);
		tclassPeriodView.setArrangement(selectArrangedArrangementByPeriodTclass(scheduleArrangementList,
				periodPo.getPeriodId(), tclass.tclassId));

		return (PeriodView) tclassPeriodView;
	}

	private Arrangement selectArrangedArrangementByPeriodTclass(List<Arrangement> scheduleArrangementList,
			Integer periodId, Integer tclassId) {

		for (Arrangement arrangement : scheduleArrangementList) {
			if (arrangement.period.periodId == periodId && arrangement.tclass.tclassId == tclassId
					&& arrangement.arranged == 1) {
				return arrangement;
			}
		}
		return new Arrangement();
	}

	/* Display Block End */

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

	@Override
	public Map<String, Object> gnrConflictionLists(Integer scheduleId, Integer courseId) {
		List<Arrangement> arrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.course.courseId == courseId) {

			}
		}
		return null;
	}

}
