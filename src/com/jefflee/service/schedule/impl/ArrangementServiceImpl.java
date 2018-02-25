package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.view.ArrangementView;
import com.jefflee.view.DayView;
import com.jefflee.view.PeriodView;
import com.jefflee.view.PlanView;
import com.jefflee.view.WeekView;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
			weekViewList.add(gnrWeekView(scheduleArrangementList, schedulePlanList, courseList, tclass, daysPerWeek,
					periodsPerDay));
		}

		return weekViewList;
	}

	@Override
	public List<WeekView> gnrWeekViewListByTclassTeacher(Schedule schedule, List<Tclass> tclassList,
			List<Teacher> teacherList) {
		List<WeekView> weekViewList = new ArrayList<WeekView>();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(schedule.scheduleId);
		List<Plan> schedulePlanList = planService.selectPlanListByScheduleId(schedule.scheduleId);
		Integer daysPerWeek = schedule.days;
		Integer periodsPerDay = schedule.forenoon + schedule.afternoon + schedule.evening;

		for (Teacher teacher : teacherList) {
			weekViewList.add(gnrWeekView(scheduleArrangementList, schedulePlanList, tclassList, teacher, daysPerWeek,
					periodsPerDay));
		}

		return weekViewList;
	}

	private WeekView gnrWeekView(List<Arrangement> scheduleArrangementList, List<Plan> schedulePlanList,
			List<Course> courseList, Tclass tclass, Integer daysPerWeek, Integer periodsPerDay) {
		WeekView weekView = new WeekView();

		weekView.setType("tclass");
		weekView.setTypeId(tclass.tclassId);
		List<PlanView> planViewList = gnrPlanViewList(scheduleArrangementList, schedulePlanList, courseList, tclass);
		weekView.setPlanViewList(planViewList);
		List<DayView> dayViewList = gnrDayViewList(scheduleArrangementList, tclass, daysPerWeek, periodsPerDay);
		weekView.setDayViewList(dayViewList);

		return weekView;
	}

	private WeekView gnrWeekView(List<Arrangement> scheduleArrangementList, List<Plan> schedulePlanList,
			List<Tclass> tclassList, Teacher teacher, Integer daysPerWeek, Integer periodsPerDay) {
		WeekView weekView = new WeekView();

		weekView.setType("teacher");
		weekView.setTypeId(teacher.teacherId);
		List<PlanView> planViewList = gnrPlanViewList(scheduleArrangementList, schedulePlanList, tclassList, teacher);
		weekView.setPlanViewList(planViewList);
		List<DayView> dayViewList = gnrDayViewList(scheduleArrangementList, teacher, daysPerWeek, periodsPerDay);
		weekView.setDayViewList(dayViewList);

		return weekView;
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
			List<Tclass> tclassList, Teacher teacher) {
		List<PlanView> planViewList = new ArrayList<PlanView>();

		for (Plan plan : schedulePlanList) {
			if (plan.teacher.teacherId == teacher.teacherId) {
				planViewList.add(gnrPlanView(scheduleArrangementList, plan, teacher));
			}
		}
		return planViewList;
	}

	private PlanView gnrPlanView(List<Arrangement> arrangementList, Plan plan, Tclass tclass) {
		PlanView planView = new PlanView();

		planView.setPlan(plan);
		List<Arrangement> arrangedList = getByArranged(
				getByTclass(getByCourse(arrangementList, plan.course.courseId), tclass.tclassId), 1);
		planView.setArrangedNum(arrangedList.size());
		return planView;
	}

	private PlanView gnrPlanView(List<Arrangement> arrangementList, Plan plan, Teacher teacher) {
		PlanView planView = new PlanView();

		planView.setPlan(plan);
		List<Arrangement> arrangedList = getByArranged(
				getByCourse(getByTeacher(getByTclass(arrangementList, plan.tclass.tclassId), teacher.teacherId),
						plan.course.courseId),
				1);
		planView.setArrangedNum(arrangedList.size());
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

	private List<DayView> gnrDayViewList(List<Arrangement> scheduleArrangementList, Teacher teacher,
			Integer daysPerWeek, Integer periodsPerDay) {
		List<DayView> dayViewList = new ArrayList<DayView>();

		for (int i = 0; i < daysPerWeek; i++) {
			dayViewList.add(gnrDayView(scheduleArrangementList, teacher, i + 1, periodsPerDay));
		}

		return dayViewList;
	}

	private DayView gnrDayView(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer dayOfWeek,
			Integer periodsPerDay) {
		DayView dayView = new DayView();

		dayView.setPeriodViewList(gnrPeriodViewList(scheduleArrangementList, tclass, dayOfWeek, periodsPerDay));

		return dayView;
	}

	private DayView gnrDayView(List<Arrangement> scheduleArrangementList, Teacher teacher, Integer dayOfWeek,
			Integer periodsPerDay) {
		DayView dayView = new DayView();

		dayView.setPeriodViewList(gnrPeriodViewList(scheduleArrangementList, teacher, dayOfWeek, periodsPerDay));

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

	private List<PeriodView> gnrPeriodViewList(List<Arrangement> scheduleArrangementList, Teacher teacher,
			Integer dayOfWeek, Integer periodsPerDay) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();

		for (int i = 0; i < periodsPerDay; i++) {
			periodViewList.add(gnrPeriodView(scheduleArrangementList, teacher, dayOfWeek, i + 1));
		}

		return periodViewList;
	}

	private PeriodView gnrPeriodView(List<Arrangement> scheduleArrangementList, Tclass tclass, Integer dayOfWeek,
			Integer orderOfDay) {
		PeriodView periodView = new PeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		Integer periodId = periodPo.getPeriodId();
		Integer tclassId = tclass.tclassId;
		String periodViewId = concatenateViewId("period", periodId, "tclass", tclassId);
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

	private PeriodView gnrPeriodView(List<Arrangement> scheduleArrangementList, Teacher teacher, Integer dayOfWeek,
			Integer orderOfDay) {
		PeriodView periodView = new PeriodView();

		PeriodPo periodPo = periodService.selectByOrder(dayOfWeek, orderOfDay);
		Integer periodId = periodPo.getPeriodId();
		Integer teacherId = teacher.teacherId;
		String periodViewId = concatenateViewId("period", periodId, "teacher", teacherId);
		periodView.setPeriodViewId(periodViewId);
		List<Arrangement> periodViewArrangementList = getByArranged(
				getByPeriod(getByTeacher(scheduleArrangementList, teacherId), periodId), 1);
		if (!periodViewArrangementList.isEmpty()) {
			periodView.setArrangement(periodViewArrangementList.get(0));
		} else {
			Arrangement arrangement = new Arrangement();
			arrangement.period.periodId = periodId;
			arrangement.teacher.teacherId = teacherId;
			periodView.setArrangement(arrangement);
		}
		return periodView;
	}

	/* Display Block End */

	/* Conflict Block Start */
	@Override
	public List<Map<String, String>> gnrConflictList(Integer scheduleId, String type, Integer typeId) {
		List<Map<String, String>> conflictList = new ArrayList<Map<String, String>>();

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> arrangedArrangementList = getByArranged(scheduleArrangementList, 1);
		List<Arrangement> typeArrangementList = new ArrayList<Arrangement>();
		if ("course".equals(type)) {
			typeArrangementList = getByCourse(scheduleArrangementList, typeId);
			for (Arrangement typeArrangement : typeArrangementList) {
				Map<String, String> conflict = new HashMap<String, String>();
				Integer periodId = typeArrangement.period.periodId;
				Integer tclassId = typeArrangement.tclass.tclassId;
				conflict.put("periodViewId", concatenateViewId("period", periodId, "tclass", tclassId));
				conflict.put("conflictValue", "0");
				for (Arrangement arrangedArrangement : arrangedArrangementList) {
					if (typeArrangement.period.periodId == arrangedArrangement.period.periodId
							&& typeArrangement.tclass.tclassId == arrangedArrangement.tclass.tclassId) {
						continue;
					}
					if (isConflictive(typeArrangement, arrangedArrangement)) {
						conflict.put("conflictValue", "1");
						break;
					}
				}
				conflictList.add(conflict);
			}
		} else if ("tclass".equals(type)) {
			typeArrangementList = getByTclass(scheduleArrangementList, typeId);
			for (Arrangement typeArrangement : typeArrangementList) {
				Map<String, String> conflict = new HashMap<String, String>();
				Integer periodId = typeArrangement.period.periodId;
				Integer teacherId = typeArrangement.teacher.teacherId;
				conflict.put("periodViewId", concatenateViewId("period", periodId, "teacher", teacherId));
				conflict.put("conflictValue", "0");
				for (Arrangement arrangedArrangement : arrangedArrangementList) {
					if (typeArrangement.period.periodId == arrangedArrangement.period.periodId
							&& typeArrangement.teacher.teacherId == arrangedArrangement.teacher.teacherId) {
						continue;
					}
					if (isConflictive(typeArrangement, arrangedArrangement)) {
						conflict.put("conflictValue", "1");
						break;
					}
				}
				conflictList.add(conflict);
			}
		}
		return conflictList;
	}

	private boolean isConflictive(Arrangement first, Arrangement second) {
		if (first.period.periodId != second.period.periodId) {
			return false;
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

	/* Conflict Block End */

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

	/* Select Block End */

	/* Arrange Block Start */

	@Override
	public void gnrArrangementList(Integer scheduleId) {
		ArrangementPo arrangementPo = new ArrangementPo();
		arrangementPo.setArranged(0);
		Example example = new Example(ArrangementPo.class);
		arrangementMapper.updateByExampleSelective(arrangementPo, example);

		List<Arrangement> scheduleArrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		List<Arrangement> unarrangedArrangementList = getByArranged(scheduleArrangementList, 0);
		List<PlanPo> schedulePlanPoList = planService.selectByScheduleId(scheduleId);

		while (!unarrangedArrangementList.isEmpty()) {
			for (int priority = 4; priority > 0; priority--) {
				List<Arrangement> priorityArrangementList = getByPriority(unarrangedArrangementList, priority);
				if (!priorityArrangementList.isEmpty()) {
					Arrangement chosenArrangement = chooseRandomly(priorityArrangementList);
					Integer courseId = chosenArrangement.course.courseId;
					Integer tclassId = chosenArrangement.tclass.tclassId;
					Integer arrangedNum = getByCourse(getByTclass(getByArranged(scheduleArrangementList, 1), tclassId),
							courseId).size();
					Integer periodNum = planService
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
	}

	/* Arrange Block End */

	/* Tool Block Start */

	private <T> T chooseRandomly(List<T> tList) {
		Random random = new Random();
		T t = tList.get(random.nextInt(tList.size()));
		return t;
	}

	private String concatenateViewId(String firstName, Integer firstId, String secondName, Integer secondId) {
		return firstName + "-" + firstId + "-" + secondName + "-" + secondId;
	}

	private List<Integer> splitViewId(String viewId) {
		List<Integer> idList = new ArrayList<Integer>();
		String[] viewIdArray = viewId.split("-");
		idList.add(0, Integer.parseInt(viewIdArray[1]));
		idList.add(1, Integer.parseInt(viewIdArray[3]));
		return idList;
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

	private void updateArrangement(ArrangementView arrangementView, Integer arranged, Integer priority) {
		ArrangementPo updateArrangementPo = new ArrangementPo();
		if (arranged >= 0) {
			updateArrangementPo.setArranged(arranged);
		}
		if (priority >= 0) {
			updateArrangementPo.setPriority(priority);
		}
		Example example = new Example(ArrangementPo.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("scheduleId", arrangementView.getScheduleId());
		criteria.andEqualTo("periodId", arrangementView.getPeriodId());
		if (arrangementView.getType().equals("tclass")) {
			criteria.andEqualTo("courseId", arrangementView.getCourseId());
			criteria.andEqualTo("tclassId", arrangementView.getTclassId());
		} else if (arrangementView.getType().equals("teacher")) {
			criteria.andEqualTo("tclassId", arrangementView.getTclassId());
			criteria.andEqualTo("teacherId", arrangementView.getTeacherId());
		}
		arrangementMapper.updateByExampleSelective(updateArrangementPo, example);
	}

	private void updateArrangement(Arrangement arrangement) {
		ArrangementPo arrangementPo = arrangement.toPo();
		arrangementMapper.updateByPrimaryKeySelective(arrangementPo);
	}

	@Override
	public Arrangement selectArrangementById(Integer arrangementId) {
		return arrangementMapper.selectEntityById(arrangementId);
	}

	@Override
	public void arrange(ArrangementView arrangementView) {
		updateArrangement(arrangementView, 1, -1);
		return;
	}

	@Override
	public void cancel(ArrangementView arrangementView) {
		updateArrangement(arrangementView, 0, -1);
		return;
	}

}
