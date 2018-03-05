package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.GroupService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.view.ArrangementView;
import com.jefflee.view.DayView;
import com.jefflee.view.PeriodView;
import com.jefflee.view.PlanView;
import com.jefflee.view.TitleView;
import com.jefflee.view.WeekView;

import tk.mybatis.mapper.entity.Example;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;

	@Resource(name = "periodService")
	private PeriodService periodService;
	@Resource(name = "tclassService")
	private TclassService tclassService;

	@Resource(name = "groupService")
	private GroupService groupService;
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

	@Override
	public Integer insertList(List<ArrangementPo> arrangementPoList) {
		return arrangementMapper.insertList(arrangementPoList);
	}

	/* CRUD Block End */

	/* Display Block Start */

	@Override
	public List<WeekView> gnrWeekViewListByCourseTclass(Schedule schedule, List<Course> courseList,
			List<Tclass> tclassList) {
		List<WeekView> weekViewList = new ArrayList<WeekView>();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(schedule.scheduleId);
		List<Plan> schedulePlanList = planService.selectPlanListByScheduleId(schedule.scheduleId);
		Integer daysPerWeek = schedule.days;
		Integer periodsPerDay = schedule.forenoon + schedule.afternoon + schedule.evening;

		for (Tclass tclass : tclassList) {
			weekViewList.add(gnrWeekView(scheduleArrangementList, schedulePlanList, schedule, courseList, tclass,
					daysPerWeek, periodsPerDay));
		}

		return weekViewList;
	}

	@Override
	public List<WeekView> gnrWeekViewListByTclassTeacher(Schedule schedule, List<Tclass> tclassList,
			List<Teacher> teacherList) {
		List<WeekView> weekViewList = new ArrayList<WeekView>();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(schedule.scheduleId);
		List<Plan> schedulePlanList = planService.selectPlanListByScheduleId(schedule.scheduleId);
		List<Course> courseList = new ArrayList<Course>();
		Integer daysPerWeek = schedule.days;
		Integer periodsPerDay = schedule.forenoon + schedule.afternoon + schedule.evening;

		for (Teacher teacher : teacherList) {
			courseList = getCourseListByTeacher(schedulePlanList, teacher);
			for (Course course : courseList) {
				weekViewList.add(gnrWeekView(scheduleArrangementList, schedulePlanList, schedule, tclassList, course,
						teacher, daysPerWeek, periodsPerDay));
			}
		}

		return weekViewList;
	}

	private WeekView gnrWeekView(List<Arrangement> scheduleArrangementList, List<Plan> schedulePlanList,
			Schedule schedule, List<Course> courseList, Tclass tclass, Integer daysPerWeek, Integer periodsPerDay) {
		WeekView weekView = new WeekView();

		TitleView titleView = gnrTitleView(schedulePlanList, schedule, tclass);
		weekView.setTitleView(titleView);
		List<PlanView> planViewList = gnrPlanViewList(scheduleArrangementList, schedulePlanList, courseList, tclass);
		weekView.setPlanViewList(planViewList);
		List<DayView> dayViewList = gnrDayViewList(scheduleArrangementList, tclass, daysPerWeek, periodsPerDay);
		weekView.setDayViewList(dayViewList);

		return weekView;
	}

	private WeekView gnrWeekView(List<Arrangement> scheduleArrangementList, List<Plan> schedulePlanList,
			Schedule schedule, List<Tclass> tclassList, Course course, Teacher teacher, Integer daysPerWeek,
			Integer periodsPerDay) {
		WeekView weekView = new WeekView();

		TitleView titleView = gnrTitleView(schedulePlanList, schedule, course, teacher);
		weekView.setTitleView(titleView);
		List<PlanView> planViewList = gnrPlanViewList(scheduleArrangementList, schedulePlanList, tclassList, course,
				teacher);
		weekView.setPlanViewList(planViewList);
		List<DayView> dayViewList = gnrDayViewList(scheduleArrangementList, course, teacher, daysPerWeek,
				periodsPerDay);
		weekView.setDayViewList(dayViewList);

		return weekView;
	}

	private TitleView gnrTitleView(List<Plan> schedulePlanList, Schedule schedule, Tclass tclass) {
		TitleView titleView = new TitleView();

		titleView.setTclassName(tclassService.gnrName(tclass));
		for (Plan plan : schedulePlanList) {
			if (plan.course.courseId.equals(11) && plan.tclass.tclassId.equals(tclass.tclassId)) {
				titleView.setTeacherName(plan.teacher.name);
				break;
			}
		}
		titleView.setStartDate(schedule.group.startDate.toString());
		return titleView;
	}

	private TitleView gnrTitleView(List<Plan> schedulePlanList, Schedule schedule, Course course, Teacher teacher) {
		TitleView titleView = new TitleView();

		titleView.setCourseGradeName(course.shortName + " " + groupService.gnrGradeName(schedule.group));
		titleView.setTeacherName(teacher.name);
		titleView.setStartDate(schedule.group.startDate.toString());
		return titleView;
	}

	private List<PlanView> gnrPlanViewList(List<Arrangement> scheduleArrangementList, List<Plan> schedulePlanList,
			List<Course> courseList, Tclass tclass) {
		List<PlanView> planViewList = new ArrayList<PlanView>();

		for (Plan plan : schedulePlanList) {
			if (plan.tclass.tclassId == tclass.tclassId) {
				planViewList.add(gnrPlanView(scheduleArrangementList, plan, tclass));
			}
		}
		return planViewList;
	}

	private List<PlanView> gnrPlanViewList(List<Arrangement> scheduleArrangementList, List<Plan> schedulePlanList,
			List<Tclass> tclassList, Course course, Teacher teacher) {
		List<PlanView> planViewList = new ArrayList<PlanView>();

		for (Plan plan : schedulePlanList) {
			if (plan.teacher.teacherId == teacher.teacherId) {
				if (plan.course.type == 1) {
					planViewList.add(gnrPlanView(scheduleArrangementList, plan, course, teacher));
				} else if (plan.course.courseId == course.courseId) {
					planViewList.add(gnrPlanView(scheduleArrangementList, plan, course, teacher));
				}
			}
		}
		return planViewList;
	}

	private PlanView gnrPlanView(List<Arrangement> arrangementList, Plan plan, Tclass tclass) {
		PlanView planView = new PlanView();

		planView.setPlan(plan);
		List<Arrangement> tclassCourseList = getByTclass(getByCourse(arrangementList, plan.course.courseId),
				tclass.tclassId);
		List<Arrangement> arrangedList = getByArranged(tclassCourseList, 1);
		List<Arrangement> highestList = getByPriority(tclassCourseList, 4);
		planView.setArrangedNum(arrangedList.size());
		planView.setHighestNum(highestList.size());
		return planView;
	}

	private PlanView gnrPlanView(List<Arrangement> arrangementList, Plan plan, Course course, Teacher teacher) {
		PlanView planView = new PlanView();

		planView.setPlan(plan);
		List<Arrangement> courseTeacherTclassList = getByCourse(
				getByTeacher(getByTclass(arrangementList, plan.tclass.tclassId), plan.teacher.teacherId),
				plan.course.courseId);
		List<Arrangement> arrangedList = getByArranged(courseTeacherTclassList, 1);
		List<Arrangement> highestList = getByPriority(courseTeacherTclassList, 4);
		planView.setArrangedNum(arrangedList.size());
		planView.setHighestNum(highestList.size());
		return planView;
	}

	private List<DayView> gnrDayViewList(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer daysPerWeek,
			Integer periodsPerDay) {
		List<DayView> dayViewList = new ArrayList<DayView>();

		for (int i = 0; i < daysPerWeek; i++) {
			dayViewList.add(gnrDayView(scheduleArrangementList, tclass, i + 1, periodsPerDay));
		}

		return dayViewList;
	}

	private List<DayView> gnrDayViewList(List<Arrangement> scheduleArrangementList, Course course, Teacher teacher,
			Integer daysPerWeek, Integer periodsPerDay) {
		List<DayView> dayViewList = new ArrayList<DayView>();

		for (int i = 0; i < daysPerWeek; i++) {
			dayViewList.add(gnrDayView(scheduleArrangementList, course, teacher, i + 1, periodsPerDay));
		}

		return dayViewList;
	}

	private DayView gnrDayView(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer dayOfWeek,
			Integer periodsPerDay) {
		DayView dayView = new DayView();

		dayView.setArrangedPeriodViewList(
				gnrArrangedPeriodViewList(scheduleArrangementList, tclass, dayOfWeek, periodsPerDay));
		dayView.setHighestPeriodViewList(
				gnrHighestPeriodViewList(scheduleArrangementList, tclass, dayOfWeek, periodsPerDay));

		return dayView;
	}

	private DayView gnrDayView(List<Arrangement> scheduleArrangementList, Course course, Teacher teacher,
			Integer dayOfWeek, Integer periodsPerDay) {
		DayView dayView = new DayView();

		dayView.setArrangedPeriodViewList(
				gnrArrangedPeriodViewList(scheduleArrangementList, course, teacher, dayOfWeek, periodsPerDay));
		dayView.setHighestPeriodViewList(
				gnrHighestPeriodViewList(scheduleArrangementList, course, teacher, dayOfWeek, periodsPerDay));

		return dayView;
	}

	private List<PeriodView> gnrArrangedPeriodViewList(List<Arrangement> scheduleArrangementList, Tclass tclass,
			Integer dayOfWeek, Integer periodsPerDay) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();

		for (int i = 0; i < periodsPerDay; i++) {
			periodViewList.add(gnrArrangedPeriodView(scheduleArrangementList, tclass, dayOfWeek, i + 1));
		}

		return periodViewList;
	}

	private List<PeriodView> gnrArrangedPeriodViewList(List<Arrangement> scheduleArrangementList, Course course,
			Teacher teacher, Integer dayOfWeek, Integer periodsPerDay) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();

		for (int i = 0; i < periodsPerDay; i++) {
			periodViewList.add(gnrArrangedPeriodView(scheduleArrangementList, course, teacher, dayOfWeek, i + 1));
		}

		return periodViewList;
	}

	private List<PeriodView> gnrHighestPeriodViewList(List<Arrangement> scheduleArrangementList, Tclass tclass,
			Integer dayOfWeek, Integer periodsPerDay) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();

		for (int i = 0; i < periodsPerDay; i++) {
			periodViewList.add(gnrHighestPeriodView(scheduleArrangementList, tclass, dayOfWeek, i + 1));
		}

		return periodViewList;
	}

	private List<PeriodView> gnrHighestPeriodViewList(List<Arrangement> scheduleArrangementList, Course course,
			Teacher teacher, Integer dayOfWeek, Integer periodsPerDay) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();

		for (int i = 0; i < periodsPerDay; i++) {
			periodViewList.add(gnrHighestPeriodView(scheduleArrangementList, course, teacher, dayOfWeek, i + 1));
		}

		return periodViewList;
	}

	private PeriodView gnrArrangedPeriodView(List<Arrangement> scheduleArrangementList, Tclass tclass,
			Integer dayOfWeek, Integer orderOfDay) {
		PeriodView periodView = new PeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		Integer periodId = periodPo.getPeriodId();
		Integer tclassId = tclass.tclassId;
		String periodViewId = "p" + periodId + "s" + tclassId;
		periodView.setPeriodViewId(periodViewId);
		List<Arrangement> periodViewArrangementList = getByArranged(
				getByPeriod(getByTclass(scheduleArrangementList, tclassId), periodId), 1);

		// TODO 未来直接考虑列表情况
		if (!periodViewArrangementList.isEmpty()) {
			periodView.setArrangement(periodViewArrangementList.get(0));
		} else {
			Arrangement arrangement = new Arrangement();
			arrangement.period.periodId = periodId;
			arrangement.tclass.tclassId = tclassId;
			periodView.setArrangement(arrangement);
		}

		return periodView;
	}

	private PeriodView gnrArrangedPeriodView(List<Arrangement> scheduleArrangementList, Course course, Teacher teacher,
			Integer dayOfWeek, Integer orderOfDay) {
		PeriodView periodView = new PeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		Integer periodId = periodPo.getPeriodId();
		Integer courseId = course.courseId;
		Integer teacherId = teacher.teacherId;
		String periodViewId = "p" + periodId + "t" + teacherId + "c" + courseId;
		periodView.setPeriodViewId(periodViewId);
		List<Arrangement> conditionArrangementList = getByArranged(
				getByPeriod(getByTeacher(scheduleArrangementList, teacherId), periodId), 1);
		List<Arrangement> courseConditionArrangementList = getByCourse(conditionArrangementList, courseId);

		// TODO 未来直接考虑列表情况
		if (!courseConditionArrangementList.isEmpty()) {
			periodView.setArrangement(courseConditionArrangementList.get(0));
		} else if (!conditionArrangementList.isEmpty()) {
			for (Arrangement conditionArrangement : conditionArrangementList) {
				if (conditionArrangement.course.type == 1) {
					periodView.setArrangement(conditionArrangement);
					break;
				} else {
					Arrangement arrangement = new Arrangement();
					arrangement.period.periodId = periodId;
					arrangement.teacher.teacherId = teacherId;
					arrangement.course.courseId = courseId;
					periodView.setArrangement(arrangement);
				}
			}
		} else {
			Arrangement arrangement = new Arrangement();
			arrangement.period.periodId = periodId;
			arrangement.teacher.teacherId = teacherId;
			arrangement.course.courseId = courseId;
			periodView.setArrangement(arrangement);
		}
		return periodView;
	}

	private PeriodView gnrHighestPeriodView(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer dayOfWeek,
			Integer orderOfDay) {
		PeriodView periodView = new PeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		Integer periodId = periodPo.getPeriodId();
		Integer tclassId = tclass.tclassId;
		String periodViewId = "p" + periodId + "s" + tclassId;
		periodView.setPeriodViewId(periodViewId);
		List<Arrangement> periodViewArrangementList = getByPriority(
				getByPeriod(getByTclass(scheduleArrangementList, tclassId), periodId), 4);

		// TODO 未来直接考虑列表情况
		if (!periodViewArrangementList.isEmpty()) {
			periodView.setArrangement(periodViewArrangementList.get(0));
		} else {
			Arrangement arrangement = new Arrangement();
			arrangement.period.periodId = periodId;
			arrangement.tclass.tclassId = tclassId;
			periodView.setArrangement(arrangement);
		}

		return periodView;
	}

	private PeriodView gnrHighestPeriodView(List<Arrangement> scheduleArrangementList, Course course, Teacher teacher,
			Integer dayOfWeek, Integer orderOfDay) {
		PeriodView periodView = new PeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		Integer periodId = periodPo.getPeriodId();
		Integer courseId = course.courseId;
		Integer teacherId = teacher.teacherId;
		String periodViewId = "p" + periodId + "t" + teacherId + "c" + courseId;
		periodView.setPeriodViewId(periodViewId);
		List<Arrangement> conditionArrangementList = getByPriority(
				getByPeriod(getByTeacher(scheduleArrangementList, teacherId), periodId), 4);
		List<Arrangement> courseConditionArrangementList = getByCourse(conditionArrangementList, courseId);

		// TODO 未来直接考虑列表情况
		if (!courseConditionArrangementList.isEmpty()) {
			periodView.setArrangement(courseConditionArrangementList.get(0));
		} else if (!conditionArrangementList.isEmpty()) {
			for (Arrangement conditionArrangement : conditionArrangementList) {
				if (conditionArrangement.course.type == 1) {
					periodView.setArrangement(conditionArrangement);
					break;
				} else {
					Arrangement arrangement = new Arrangement();
					arrangement.period.periodId = periodId;
					arrangement.teacher.teacherId = teacherId;
					arrangement.course.courseId = courseId;
					periodView.setArrangement(arrangement);
				}
			}
		} else {
			Arrangement arrangement = new Arrangement();
			arrangement.period.periodId = periodId;
			arrangement.teacher.teacherId = teacherId;
			arrangement.course.courseId = courseId;
			periodView.setArrangement(arrangement);
		}
		return periodView;
	}

	/* Display Block End */

	/* Conflict Block Start */

	@Override
	public Map<String, String> gnrConflictList(ArrangementView arrangementView) {
		Map<String, String> conflictList = new HashMap<String, String>();

		String type = arrangementView.getType();
		Integer scheduleId = arrangementView.getScheduleId();
		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> arrangedArrangementList = getByArranged(scheduleArrangementList, 1);
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();

		Integer periodId = 0;
		Integer courseId = 0;
		Integer tclassId = 0;
		Integer teacherId = 0;
		String key = "";

		if ("tclass".equals(type)) {
			conditionArrangementList = getByCourse(scheduleArrangementList, arrangementView.getCourseId());
			for (Arrangement conditionArrangement : conditionArrangementList) {
				periodId = conditionArrangement.period.periodId;
				tclassId = conditionArrangement.tclass.tclassId;
				key = "p" + periodId + "s" + tclassId;
				if (!conflictList.containsKey(key)) {
					conflictList.put(key, "0");
				}
				for (Arrangement arrangedArrangement : arrangedArrangementList) {
					if (periodId == arrangedArrangement.period.periodId
							&& tclassId == arrangedArrangement.tclass.tclassId) {
						continue;
					}
					if (isConflictive(conditionArrangement, arrangedArrangement)) {
						conflictList.put(key, "1");
						break;
					}
				}
			}
		} else if ("teacher".equals(type)) {
			conditionArrangementList = getByTclass(scheduleArrangementList, arrangementView.getTclassId());
			for (Arrangement conditionArrangement : conditionArrangementList) {
				periodId = conditionArrangement.period.periodId;
				courseId = conditionArrangement.course.courseId;
				teacherId = conditionArrangement.teacher.teacherId;
				key = "p" + periodId + "t" + teacherId + "c" + courseId;
				if (!conflictList.containsKey(key)) {
					conflictList.put(key, "0");
				}
				for (Arrangement arrangedArrangement : arrangedArrangementList) {
					if (periodId == arrangedArrangement.period.periodId
							&& teacherId == arrangedArrangement.teacher.teacherId) {
						continue;
					}
					if (isConflictive(conditionArrangement, arrangedArrangement)) {
						conflictList.put(key, "1");
						break;
					}
				}
			}
		} else if ("special".equals(type)) {
			conditionArrangementList = getByCourse(scheduleArrangementList, arrangementView.getCourseId());
			List<Arrangement> teacherArrangementList = new ArrayList<Arrangement>();
			String specialKey = "";
			for (Arrangement conditionArrangement : conditionArrangementList) {
				periodId = conditionArrangement.period.periodId;
				teacherId = conditionArrangement.teacher.teacherId;
				key = "p" + periodId + "t" + teacherId;
				teacherArrangementList = getByTeacher(getByPeriod(scheduleArrangementList, periodId), teacherId);
				for (Arrangement teacherArrangement : teacherArrangementList) {
					if (teacherArrangement.course.type == 1) {
						continue;
					}
					courseId = teacherArrangement.course.courseId;
					specialKey = key + "c" + courseId;
					if (!conflictList.containsKey(specialKey)) {
						conflictList.put(specialKey, "0");
					}
					for (Arrangement arrangedArrangement : arrangedArrangementList) {
						if (periodId == arrangedArrangement.period.periodId
								&& teacherId == arrangedArrangement.teacher.teacherId) {
							continue;
						}
						if (isConflictive(conditionArrangement, arrangedArrangement)) {
							conflictList.put(specialKey, "1");
							break;
						}
					}
				}
			}
		}
		return conflictList;
	}

	private boolean isConflictive(Arrangement first, Arrangement second) {
		// TODO 适当设置课程类型以求精简判断
		if (first.period.periodId != second.period.periodId) {
			return false;
		}
		if (first.course.type == 1 && second.course.type == 1) {
			if (first.course.courseId != 11 && first.course.courseId == second.course.courseId) {
				return false;
			}
		}
		if (first.room.roomId == second.room.roomId) {
			return true;
		}
		if (first.tclass.tclassId == second.tclass.tclassId) {
			return true;
		}
		if (first.teacher.teacherId == second.teacher.teacherId) {
			return true;
		}
		return false;
	}

	@Override
	public void setArranged(ArrangementView arrangementView) {
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();

		String type = arrangementView.getType();
		Integer scheduleId = arrangementView.getScheduleId();
		Integer periodId = arrangementView.getPeriodId();
		Integer courseId = arrangementView.getCourseId();
		Integer tclassId = arrangementView.getTclassId();
		Integer teacherId = arrangementView.getTeacherId();
		Integer arranged = arrangementView.getArranged();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> periodCourseArrangementList = getByPeriod(getByCourse(scheduleArrangementList, courseId),
				periodId);
		if ("tclass".equals(type)) {
			conditionArrangementList = getByTclass(periodCourseArrangementList, tclassId);
		} else if ("teacher".equals(type)) {
			conditionArrangementList = getByTeacher(getByTclass(periodCourseArrangementList, tclassId), teacherId);
		} else if ("special".equals(type)) {
			conditionArrangementList = getByTeacher(periodCourseArrangementList, teacherId);
		}
		for (Arrangement conditionArrangement : conditionArrangementList) {
			conditionArrangement.arranged = arranged;
			updateArrangement(conditionArrangement);
		}
		return;
	}

	/* Conflict Block End */

	/* Priority Block Start */

	@Override
	public Map<String, String> gnrPriorityList(ArrangementView arrangementView) {
		Map<String, String> priorityList = new HashMap<String, String>();

		String type = arrangementView.getType();
		Integer scheduleId = arrangementView.getScheduleId();
		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();

		Integer periodId = 0;
		Integer courseId = 0;
		Integer tclassId = 0;
		Integer teacherId = 0;
		String key = "";

		if ("tclass".equals(type)) {
			conditionArrangementList = getByCourse(scheduleArrangementList, arrangementView.getCourseId());
			for (Arrangement conditionArrangement : conditionArrangementList) {
				periodId = conditionArrangement.period.periodId;
				tclassId = conditionArrangement.tclass.tclassId;
				key = "p" + periodId + "s" + tclassId;
				if (!priorityList.containsKey(key)) {
					priorityList.put(key, String.valueOf(conditionArrangement.priority));
				}
			}
		} else if ("teacher".equals(type)) {
			conditionArrangementList = getByTclass(scheduleArrangementList, arrangementView.getTclassId());
			for (Arrangement conditionArrangement : conditionArrangementList) {
				periodId = conditionArrangement.period.periodId;
				courseId = conditionArrangement.course.courseId;
				teacherId = conditionArrangement.teacher.teacherId;
				key = "p" + periodId + "t" + teacherId + "c" + courseId;
				if (!priorityList.containsKey(key)) {
					priorityList.put(key, String.valueOf(conditionArrangement.priority));
				}
			}
		} else if ("special".equals(type)) {
			conditionArrangementList = getByCourse(scheduleArrangementList, arrangementView.getCourseId());
			List<Arrangement> teacherArrangementList = new ArrayList<Arrangement>();
			String specialKey = "";
			for (Arrangement conditionArrangement : conditionArrangementList) {
				periodId = conditionArrangement.period.periodId;
				teacherId = conditionArrangement.teacher.teacherId;
				key = "p" + periodId + "t" + teacherId;
				teacherArrangementList = getByTeacher(getByPeriod(scheduleArrangementList, periodId), teacherId);
				for (Arrangement teacherArrangement : teacherArrangementList) {
					if (teacherArrangement.course.type == 1) {
						continue;
					}
					courseId = teacherArrangement.course.courseId;
					specialKey = key + "c" + courseId;
					if (!priorityList.containsKey(specialKey)) {
						priorityList.put(key + "c" + courseId, String.valueOf(conditionArrangement.priority));
					}
				}
			}
		}
		return priorityList;
	}

	@Override
	public void setPriority(ArrangementView arrangementView) {
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();

		String type = arrangementView.getType();
		Integer scheduleId = arrangementView.getScheduleId();
		Integer periodId = arrangementView.getPeriodId();
		Integer courseId = arrangementView.getCourseId();
		Integer tclassId = arrangementView.getTclassId();
		Integer teacherId = arrangementView.getTeacherId();
		Integer priority = arrangementView.getPriority();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> periodCourseArrangementList = getByPeriod(getByCourse(scheduleArrangementList, courseId),
				periodId);
		if ("tclass".equals(type)) {
			conditionArrangementList = getByTclass(periodCourseArrangementList, tclassId);
		} else if ("teacher".equals(type)) {
			conditionArrangementList = getByTeacher(getByTclass(periodCourseArrangementList, tclassId), teacherId);
		} else if ("special".equals(type)) {
			conditionArrangementList = getByTeacher(periodCourseArrangementList, teacherId);
		}
		for (Arrangement conditionArrangement : conditionArrangementList) {
			if (priority == 4) {
				for (Arrangement scheduleArrangement : scheduleArrangementList) {
					if (conditionArrangement.arrangementId == scheduleArrangement.arrangementId) {
						continue;
					} else if (isConflictive(conditionArrangement, scheduleArrangement)) {
						scheduleArrangement.priority = 0;
						updateArrangement(scheduleArrangement);
					}
				}
			}
			conditionArrangement.priority = priority;
			updateArrangement(conditionArrangement);
		}
		return;
	}

	/* Priority Block End */

	/* Select Block Start */

	private List<Arrangement> getByPeriod(List<Arrangement> arrangementList, Integer periodId) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.period.periodId == periodId) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Arrangement> getByCourse(List<Arrangement> arrangementList, Integer courseId) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.course.courseId == courseId) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Arrangement> getByRoom(List<Arrangement> arrangementList, Integer roomId) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.room.roomId == roomId) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Arrangement> getByTclass(List<Arrangement> arrangementList, Integer tclassId) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.tclass.tclassId == tclassId) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Arrangement> getByTeacher(List<Arrangement> arrangementList, Integer teacherId) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.teacher.teacherId == teacherId) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Arrangement> getByArranged(List<Arrangement> arrangementList, Integer arranged) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.arranged == arranged) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Arrangement> getByPriority(List<Arrangement> arrangementList, Integer priority) {
		List<Arrangement> selectedArrangementList = new ArrayList<Arrangement>();
		for (Arrangement arrangement : arrangementList) {
			if (arrangement.priority == priority) {
				selectedArrangementList.add(arrangement);
			}
		}
		return selectedArrangementList;
	}

	private List<Course> getCourseListByTeacher(List<Plan> schedulePlanList, Teacher teacher) {
		Set<Course> courseSet = new HashSet<Course>();
		for (Plan plan : schedulePlanList) {
			if (plan.teacher.teacherId == teacher.teacherId) {
				if (plan.course.type != 1) {
					courseSet.add(plan.course);
				}
			}
		}
		return new ArrayList<Course>(courseSet);
	}

	/* Select Block End */

	/* Arrange Block Start */

	@Override
	public void gnrArrangementList(Integer scheduleId) {
		// 清空已排课
		ArrangementPo arrangementPo = new ArrangementPo();
		arrangementPo.setArranged(0);
		Example example = new Example(ArrangementPo.class);
		arrangementMapper.updateByExampleSelective(arrangementPo, example);

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> unarrangedArrangementList = getByArranged(scheduleArrangementList, 0);
		List<PlanPo> schedulePlanPoList = planService.selectByScheduleId(scheduleId);

		Integer courseId;
		Integer tclassId;
		Integer arrangedNum;
		Integer periodNum;
		List<Arrangement> arrangedArrangementList = new ArrayList<Arrangement>();

		while (!unarrangedArrangementList.isEmpty()) {
			for (int priority = 4; priority > 0; priority--) {
				List<Arrangement> priorityArrangementList = getByPriority(unarrangedArrangementList, priority);
				if (!priorityArrangementList.isEmpty()) {
					Arrangement chosenArrangement = chooseRandomly(priorityArrangementList);
					courseId = chosenArrangement.course.courseId;
					tclassId = chosenArrangement.tclass.tclassId;
					arrangedArrangementList = getByArranged(scheduleArrangementList, 1);
					arrangedNum = getByCourse(getByTclass(getByArranged(scheduleArrangementList, 1), tclassId),
							courseId).size();
					periodNum = planService
							.selectByCourseId(planService.selectByTclassId(schedulePlanPoList, tclassId), courseId)
							.get(0).getPeriodNum();
					if (periodNum - arrangedNum <= 0) {
						chosenArrangement.arranged = -1;
						break;
					}
					for (Arrangement arrangement : unarrangedArrangementList) {
						if (isConflictive(chosenArrangement, arrangement)) {
							arrangement.arranged = -1;
						}
					}
					chosenArrangement.arranged = 1;
					break;
				}
			}
			unarrangedArrangementList = getByArranged(unarrangedArrangementList, 0);
		}
		for (Arrangement arrangement : scheduleArrangementList) {
			updateArrangement(arrangement);
		}
		return;
	}

	/* Arrange Block End */

	/* Tool Block Start */

	private <T> T chooseRandomly(List<T> tList) {
		Random random = new Random();
		T t = tList.get(random.nextInt(tList.size()));
		return t;
	}

	/* Tool Block End */

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

	private void updateArrangement(Arrangement arrangement) {
		ArrangementPo arrangementPo = arrangement.toPo();
		arrangementMapper.updateByPrimaryKeySelective(arrangementPo);
		return;
	}

	@Override
	public Arrangement selectArrangementById(Integer arrangementId) {
		return arrangementMapper.selectEntityById(arrangementId);
	}

}
