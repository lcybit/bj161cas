package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Period;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.schedule.Adjustment;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Group;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.AdjustmentService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.GroupService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.util.ArrangementPredicate;
import com.jefflee.util.Cache;
import com.jefflee.util.ConflictPredicate;
import com.jefflee.util.PlanPredicate;
import com.jefflee.view.DayView;
import com.jefflee.view.PeriodView;
import com.jefflee.view.PlanView;
import com.jefflee.view.ScheduleView;
import com.jefflee.view.TitleView;
import com.jefflee.view.WeekView;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service("arrangementService")
public class ArrangementServiceImpl implements ArrangementService {

	@Resource(name = "arrangementMapper")
	private ArrangementMapper arrangementMapper;
	@Resource(name = "cache")
	private Cache cache;

	@Resource(name = "periodService")
	private PeriodService periodService;
	@Resource(name = "courseService")
	private CourseService courseService;
	@Resource(name = "tclassService")
	private TclassService tclassService;
	@Resource(name = "teacherService")
	private TeacherService teacherService;

	@Resource(name = "groupService")
	private GroupService groupService;
	@Resource(name = "planService")
	private PlanService planService;
	@Resource(name = "adjustmentService")
	private AdjustmentService adjustmentService;

	/* Initial Block Start */

	@Override
	public void initial(Integer scheduleId) {
		cache.initial(scheduleId);
		List<Arrangement> scheduleArrangementList = cache.getScheduleArrangementList();
		List<Adjustment> scheduleAdjustmentList = adjustmentService.selectTempListByScheduleId(scheduleId);
		mergeAdjustmentList(scheduleArrangementList, scheduleAdjustmentList);
	}

	/* Initial Block End */

	/* CRUD Block Start */

	@Override
	public Integer insert(ArrangementPo arrangementPo) {
		if (arrangementMapper.insert(arrangementPo) == 1) {
			return arrangementPo.getArrangementId();
		} else {
			return null;
		}
	}

	// 测试 插入arrangement表的数据
	public List<Arrangement> selectListByScheduleId(Integer scheduleId) {
		List<Arrangement> arrangementList = new ArrayList<Arrangement>();
		arrangementList = arrangementMapper.selectEntityListByScheduleId(scheduleId);
		return arrangementList;
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
	public void deleteByScheduleId(Integer scheduleId) {
		Example example = new Example(ArrangementPo.class);
		example.createCriteria().andEqualTo("scheduleId", scheduleId);
		arrangementMapper.deleteByExample(example);
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
	public ScheduleView gnrScheduleView(Schedule schedule) {
		ScheduleView scheduleView = gnrEmptyScheduleView(schedule);

		List<Arrangement> scheduleArrangementList = cache.getScheduleArrangementList();
		List<Plan> schedulePlanList = cache.getSchedulePlanList();
		Map<String, Integer> conflictMap = cache.getBackgroundMap().get("conflicting");
		conflictMap.clear();

		Integer daysPerWeek = schedule.getDays();
		Integer periodsPerDay = schedule.getForenoon() + schedule.getAfternoon() + schedule.getEvening();
		List<Period> periodList = periodService.selectPeriodListByScope(daysPerWeek, periodsPerDay);

		Map<String, WeekView> tclassWeekViewMap = scheduleView.getTclassWeekViewMap();
		Map<String, WeekView> teacherWeekViewMap = scheduleView.getTeacherWeekViewMap();

		WeekView tclassWeekView = null;
		String tclassWeekViewId = "";
		WeekView teacherWeekView = null;
		String teacherWeekViewId = "";

		PlanView tclassPlanView = null;
		String tclassPlanViewId = "";
		PlanView teacherPlanView = null;
		String teacherPlanViewId = "";

		PeriodView tclassPeriodView = null;
		PeriodView teacherPeriodView = null;

		TitleView titleView = null;

		// 新建WeekView
		for (Plan plan : schedulePlanList) {
			Course course = plan.getCourse();
			Tclass tclass = plan.getTclass();
			Teacher teacher = plan.getTeacher();

			if (tclass.getTclassId() != null) {
				tclassWeekViewId = "s-" + tclass.getTclassId();
				// 若不存在该班级课表，新建之
				if (!tclassWeekViewMap.containsKey(tclassWeekViewId)) {
					tclassWeekView = gnrEmptyWeekView(schedule.getGroup(), periodList);
					tclassWeekViewMap.put(tclassWeekViewId, tclassWeekView);
					titleView = tclassWeekView.getTitleView();
					titleView.setTclassName(tclassService.gnrName(tclass));
					// 必要时添加
					for (DayView dayView : tclassWeekView.getDayViewList()) {
						for (PeriodView periodView : dayView.getArrangedPeriodViewList()) {
							periodView.setPeriodViewId(periodView.getPeriodViewId() + "-" + tclassWeekViewId);
						}
					}
				}

				if (teacher.getTeacherId() != null) {
					Integer courseId = course.getCourseId();
					teacherWeekViewId = "t-" + teacher.getTeacherId() + "-c-" + courseId;
					// 若不存在该教师课程课表，新建之
					// TODO type
					if (courseId != 11 && courseId != 23 && courseId != 24 && courseId != 25) {
						if (!teacherWeekViewMap.containsKey(teacherWeekViewId)) {
							teacherWeekView = gnrEmptyWeekView(schedule.getGroup(), periodList);
							teacherWeekViewMap.put(teacherWeekViewId, teacherWeekView);
							titleView = teacherWeekView.getTitleView();
							titleView.setCourseName(course.getShortName());
							titleView.setGradeName(groupService.gnrGradeName(schedule.getGroup()));
							titleView.setTeacherName(teacher.getName());
							// 必要时添加
							for (DayView dayView : teacherWeekView.getDayViewList()) {
								for (PeriodView periodView : dayView.getArrangedPeriodViewList()) {
									periodView.setPeriodViewId(periodView.getPeriodViewId() + "-" + teacherWeekViewId);
								}
							}
						}
					}
				}
			}

		}

		for (Plan plan : schedulePlanList) {
			Course course = plan.getCourse();
			Tclass tclass = plan.getTclass();
			Teacher teacher = plan.getTeacher();

			if (tclass.getTclassId() != null) {
				tclassWeekViewId = "s-" + tclass.getTclassId();
				tclassPlanViewId = "c-" + course.getCourseId();
				Integer courseId = course.getCourseId();
				// TODO type
				// if (course.getType() == 0) {
				if (courseId != 23 && courseId != 24 && courseId != 25) {
					tclassWeekView = tclassWeekViewMap.get(tclassWeekViewId);
					if (courseId == 11) {
						tclassWeekView.getTitleView().setTeacherName(plan.getTeacher().getName());
					}
					tclassWeekView.getPlanViewMap().put(tclassPlanViewId, gnrEmptyPlanView(plan));
				}
			}

			if (teacher.getTeacherId() != null) {
				teacherWeekViewId = "t-" + teacher.getTeacherId();
				Integer courseId = course.getCourseId();
				// TODO type
				// if (course.getType() == 0) {
				if (courseId != 11 && courseId != 23 && courseId != 24 && courseId != 25) {
					teacherWeekViewId = teacherWeekViewId + "-c-" + courseId;
					teacherPlanViewId = "s-" + tclass.getTclassId();
					teacherWeekView = teacherWeekViewMap.get(teacherWeekViewId);
					teacherWeekView.getPlanViewMap().put(teacherPlanViewId, gnrEmptyPlanView(plan));
				} else {
					teacherPlanViewId = "c-" + courseId;
					Set<String> existTeacherWeekViewIdList = teacherWeekViewMap.keySet();
					for (String existTeacherWeekViewId : existTeacherWeekViewIdList) {
						if (existTeacherWeekViewId.startsWith(teacherWeekViewId + "-")) {
							teacherWeekView = teacherWeekViewMap.get(existTeacherWeekViewId);
							teacherWeekView.getPlanViewMap().put(teacherPlanViewId, gnrEmptyPlanView(plan));
						}
					}

				}
			}
		}

		for (Arrangement arrangement : scheduleArrangementList) {
			if (arrangement.getArranged() == 1) {
				Period period = arrangement.getPeriod();
				Course course = arrangement.getCourse();
				Tclass tclass = arrangement.getTclass();
				Teacher teacher = arrangement.getTeacher();

				if (tclass.getTclassId() != null) {
					tclassWeekViewId = "s-" + tclass.getTclassId();
					tclassPlanViewId = "c-" + course.getCourseId();
					Integer courseId = course.getCourseId();
					// TODO type
					// if (course.getType() == 0) {
					if (courseId != 23 && courseId != 24 && courseId != 25) {
						tclassWeekView = tclassWeekViewMap.get(tclassWeekViewId);
						tclassPeriodView = tclassWeekView.getDayViewList().get(period.getDayOfWeek() - 1)
								.getArrangedPeriodViewList().get(period.getOrderOfDay() - 1);

						// TODO type
						if (tclassPeriodView.getArrangementList().isEmpty() || (courseId != 19 && courseId != 20)) {
							if (!tclassPeriodView.getArrangementList().isEmpty()) {
								conflictMap.put(tclassPeriodView.getPeriodViewId(), 0);
							}
							tclassPeriodView.getArrangementList().add(arrangement);
							tclassPeriodView.getJumpViewIdList()
									.add("t-" + teacher.getTeacherId() + "-c-" + course.getCourseId());

							tclassPlanView = tclassWeekView.getPlanViewMap().get(tclassPlanViewId);
							tclassPlanView.setArrangedNum(tclassPlanView.getArrangedNum() + 1);
						}
					}
				}

				if (teacher.getTeacherId() != null) {
					teacherWeekViewId = "t-" + teacher.getTeacherId();
					Integer courseId = course.getCourseId();
					// TODO type
					// if (course.getType() == 0) {
					if (courseId != 11 && courseId != 23 && courseId != 24 && courseId != 25) {
						teacherWeekViewId = teacherWeekViewId + "-c-" + courseId;
						teacherWeekView = teacherWeekViewMap.get(teacherWeekViewId);
						teacherPeriodView = teacherWeekView.getDayViewList().get(period.getDayOfWeek() - 1)
								.getArrangedPeriodViewList().get(period.getOrderOfDay() - 1);

						if (!teacherPeriodView.getArrangementList().isEmpty()) {
							conflictMap.put(teacherPeriodView.getPeriodViewId(), 0);
						}
						teacherPeriodView.getArrangementList().add(arrangement);
						teacherPeriodView.getJumpViewIdList().add("s-" + tclass.getTclassId());

						teacherPlanViewId = "s-" + tclass.getTclassId();
						teacherPlanView = teacherWeekView.getPlanViewMap().get(teacherPlanViewId);
						teacherPlanView.setArrangedNum(teacherPlanView.getArrangedNum() + 1);
					} else {
						teacherPlanViewId = "c-" + courseId;
						Set<String> existTeacherWeekViewIdList = teacherWeekViewMap.keySet();
						for (String existTeacherWeekViewId : existTeacherWeekViewIdList) {
							if (existTeacherWeekViewId.startsWith(teacherWeekViewId + "-")) {
								teacherWeekView = teacherWeekViewMap.get(existTeacherWeekViewId);
								teacherPeriodView = teacherWeekView.getDayViewList().get(period.getDayOfWeek() - 1)
										.getArrangedPeriodViewList().get(period.getOrderOfDay() - 1);
								if (!teacherPeriodView.getArrangementList().isEmpty()) {
									conflictMap.put(teacherPeriodView.getPeriodViewId(), 0);
								}
								teacherPeriodView.getArrangementList().add(arrangement);
								teacherPeriodView.getJumpViewIdList().add("s-" + tclass.getTclassId());

								teacherPlanView = teacherWeekView.getPlanViewMap().get(teacherPlanViewId);
								teacherPlanView.setArrangedNum(teacherPlanView.getArrangedNum() + 1);
							}
						}
					}
				}
			}
		}
		return scheduleView;
	}

	private ScheduleView gnrEmptyScheduleView(Schedule schedule) {
		ScheduleView scheduleView = new ScheduleView();
		scheduleView.setSchedule(schedule);
		scheduleView.setTclassWeekViewMap(gnrEmptyWeekViewMap());
		scheduleView.setTeacherWeekViewMap(gnrEmptyWeekViewMap());
		return scheduleView;

	}

	private Map<String, WeekView> gnrEmptyWeekViewMap() {
		return new LinkedHashMap<String, WeekView>();
	}

	private WeekView gnrEmptyWeekView(Group group, List<Period> periodList) {
		WeekView weekView = new WeekView();
		weekView.setTitleView(gnrEmptyTitleView(group));
		weekView.setPlanViewMap(gnrEmptyPlanViewMap());
		weekView.setDayViewList(gnrEmptyDayViewList(periodList));
		return weekView;
	}

	private TitleView gnrEmptyTitleView(Group group) {
		TitleView titleView = new TitleView();
		titleView.setStartDate(group.getStartDate());
		return titleView;
	}

	private Map<String, PlanView> gnrEmptyPlanViewMap() {
		return new LinkedHashMap<String, PlanView>();
	}

	private PlanView gnrEmptyPlanView(Plan plan) {
		PlanView planView = new PlanView();
		planView.setPlan(plan);
		planView.setJumpViewId("t-" + plan.getTeacher().getTeacherId() + "-c-" + plan.getCourse().getCourseId());
		planView.setArrangedNum(0);
		planView.setHighestNum(0);
		return planView;
	}

	private List<DayView> gnrEmptyDayViewList(List<Period> periodList) {
		List<DayView> dayViewList = new ArrayList<DayView>();
		Map<Integer, List<Period>> dayPeriodListMap = new HashMap<Integer, List<Period>>();
		for (Period period : periodList) {
			if (!dayPeriodListMap.containsKey(period.getDayOfWeek() - 1)) {
				dayPeriodListMap.put(period.getDayOfWeek() - 1, new ArrayList<Period>());
			}
			dayPeriodListMap.get(period.getDayOfWeek() - 1).add(period.getOrderOfDay() - 1, period);
		}
		for (int i = 0; i < dayPeriodListMap.size(); i++) {
			dayViewList.add(i, gnrEmptyDayView(dayPeriodListMap.get(i)));
		}
		return dayViewList;
	}

	private DayView gnrEmptyDayView(List<Period> dayPeriodList) {
		DayView dayView = new DayView();
		dayView.setArrangedPeriodViewList(gnrEmptyPeriodViewList(dayPeriodList));
		dayView.setHighestPeriodViewList(gnrEmptyPeriodViewList(dayPeriodList));
		return dayView;
	}

	private List<PeriodView> gnrEmptyPeriodViewList(List<Period> dayPeriodList) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();
		for (Period period : dayPeriodList) {
			periodViewList.add(period.getOrderOfDay() - 1, gnrEmptyPeriodView(period));
		}
		return periodViewList;
	}

	private PeriodView gnrEmptyPeriodView(Period period) {
		PeriodView periodView = new PeriodView();
		List<Arrangement> arrangementList = new ArrayList<Arrangement>();
		List<String> jumpViewIdList = new ArrayList<String>();
		periodView.setArrangementList(arrangementList);
		periodView.setJumpViewIdList(jumpViewIdList);
		periodView.setPeriodViewId("p-" + period.getPeriodId());
		return periodView;
	}

	/* Display Block End */

	/* Conflict Block Start */

	private boolean isConflictive(Arrangement first, Arrangement second) {
		// TODO type
		if (first.getPeriod().getPeriodId() != second.getPeriod().getPeriodId()) {
			return false;
		}
		if (first.getCourse().getType() == 1 && second.getCourse().getType() == 1) {
			if (first.getCourse().getCourseId() != 11
					&& first.getCourse().getCourseId() == second.getCourse().getCourseId()) {
				return false;
			}
		}
		if (first.getRoom().getRoomId() == second.getRoom().getRoomId()) {
			return true;
		}
		if (first.getTclass().getTclassId() == second.getTclass().getTclassId()) {
			return true;
		}
		if (first.getTeacher().getTeacherId() == second.getTeacher().getTeacherId()) {
			return true;
		}
		return false;
	}

	/* Conflict Block End */

	/* Arrange Block Start */

	@Override
	public void gnrArrangementList(Integer scheduleId) {
		List<Arrangement> scheduleArrangementList = cache.getScheduleArrangementList();
		List<Plan> schedulePlanList = cache.getSchedulePlanList();

		Arrangement queryUnselected = new Arrangement();
		queryUnselected.setArranged(0);
		Predicate<Arrangement> unselectedPredicate = new ArrangementPredicate(queryUnselected);
		List<Arrangement> unselectedArrangementList = ListUtils.select(scheduleArrangementList, unselectedPredicate);

		Arrangement queryArranged = new Arrangement();
		queryArranged.setArranged(1);
		Predicate<Arrangement> arrangedPredicate = new ArrangementPredicate(queryArranged);
		List<Arrangement> arrangedArrangementList = ListUtils.select(scheduleArrangementList, arrangedPredicate);

		Integer courseId;
		Integer tclassId;
		Integer arrangedNum;
		Integer periodNum;

		Arrangement queryArrangement = new Arrangement();
		Arrangement querySameCourseArrangement = new Arrangement();
		Plan queryPlan = new Plan();

		// TODO 重新调整排课逻辑
		while (!unselectedArrangementList.isEmpty()) {
			Arrangement selectedArrangement = chooseRandomly(unselectedArrangementList);
			courseId = selectedArrangement.getCourse().getCourseId();
			// TODO type
			if (courseId == 23 || courseId == 24 || courseId == 25) {
				selectedArrangement.setArranged(-1);
				unselectedArrangementList.remove(selectedArrangement);
				continue;
			}
			tclassId = selectedArrangement.getTclass().getTclassId();

			// 若需要添加的课程冲突，不添加
			ConflictPredicate conflictPredicate = new ConflictPredicate(selectedArrangement);
			List<Arrangement> arrangedConflictList = ListUtils.select(arrangedArrangementList, conflictPredicate);
			if (!arrangedConflictList.isEmpty()) {
				selectedArrangement.setArranged(-1);
				unselectedArrangementList.remove(selectedArrangement);
				continue;
			}

			// 若已安排数量超过上限，不添加
			queryArrangement.getCourse().setCourseId(courseId);
			queryArrangement.getTclass().setTclassId(tclassId);
			arrangedNum = ListUtils.select(arrangedArrangementList, new ArrangementPredicate(queryArrangement)).size();
			queryPlan.getCourse().setCourseId(courseId);
			queryPlan.getTclass().setTclassId(tclassId);
			periodNum = ListUtils.select(schedulePlanList, new PlanPredicate(queryPlan)).iterator().next()
					.getPeriodNum();
			if (periodNum - arrangedNum <= 0) {
				selectedArrangement.setArranged(-1);
				unselectedArrangementList.remove(selectedArrangement);
				continue;
			}

			// 同课时同课程同班级安排一起添加
			querySameCourseArrangement.getPeriod().setPeriodId(selectedArrangement.getPeriod().getPeriodId());
			querySameCourseArrangement.getCourse().setCourseId(courseId);
			querySameCourseArrangement.getTclass().setTclassId(tclassId);
			List<Arrangement> sameCourseArrangementList = ListUtils.select(unselectedArrangementList,
					new ArrangementPredicate(querySameCourseArrangement));
			for (Arrangement arrangement : sameCourseArrangementList) {
				arrangement.setArranged(1);
				arrangedArrangementList.add(arrangement);
				unselectedArrangementList.remove(arrangement);
				updateArrangement(arrangement);
			}

			// 移除冲突的课程（注意和前一部分的顺序）
			List<Arrangement> unselectedConflictList = ListUtils.select(unselectedArrangementList, conflictPredicate);
			for (Arrangement arrangement : unselectedConflictList) {
				arrangement.setArranged(-1);
				unselectedArrangementList.remove(arrangement);
			}
		}
		fastUpdateArrangement(scheduleArrangementList);
		return;
	}

	private void clearArrangementList(Integer scheduleId) {
		ArrangementPo arrangementPo = new ArrangementPo();
		arrangementPo.setArranged(0);
		arrangementPo.setPriority(2);
		Example example = new Example(ArrangementPo.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("scheduleId", scheduleId);
		arrangementMapper.updateByExampleSelective(arrangementPo, example);
	}

	@Override
	public void resetArrangementList(Integer scheduleId) {
		clearArrangementList(scheduleId);
		cache.initial(scheduleId);
	}
	/* Arrange Block End */

	/* Adjust Block Start */

	@Override
	public void doAdjust(Integer adjustmentId) {
		List<Arrangement> arrangementList = cache.getScheduleArrangementList();
		Adjustment adjustment = adjustmentService.selectAdjustmentById(adjustmentId);
		adjust(arrangementList, adjustment);
	}

	@Override
	public void undoAdjust(Integer adjustmentId) {
		List<Arrangement> arrangementList = cache.getScheduleArrangementList();
		Adjustment adjustment = adjustmentService.selectAdjustmentById(adjustmentId);
		reverseAdjust(arrangementList, adjustment);
	}

	private void mergeAdjustmentList(List<Arrangement> arrangementList, List<Adjustment> adjustmentList) {
		for (Adjustment adjustment : adjustmentList) {
			adjust(arrangementList, adjustment);
		}
	}

	@Override
	public void saveAdjustment(Integer scheduleId) {
		List<Adjustment> scheduleAdjustmentList = adjustmentService.selectTempListByScheduleId(scheduleId);
		for (Adjustment adjustment : scheduleAdjustmentList) {
			saveAdjust(adjustment);
			adjustmentService.deletePoById(adjustment.getAdjustmentId());
		}
	}

	private void saveAdjust(Adjustment adjustment) {
		String position = adjustment.getPosition();
		String additionalInfo = adjustment.getAdditionalInfo();
		List<Map<String, Integer>> idPairList = new ArrayList<Map<String, Integer>>();
		Integer type = adjustment.getType();

		switch (type) {
		case 0:
			String[] positions = position.split("\\|");
			String[] additionalInfos = additionalInfo.split("\\|");
			saveSwap(positions, additionalInfos);
			break;
		case 1:
			idPairList = parseIdPairList(position, additionalInfo);
			for (Map<String, Integer> idPair : idPairList) {
				saveAdd(idPair);
			}
			break;
		case 2:
			idPairList = parseIdPairList(position, additionalInfo);
			for (Map<String, Integer> idPair : idPairList) {
				saveRemove(idPair);
			}
			break;
		default:
			break;
		}
	}

	private void saveSwap(String[] positions, String[] additionalInfos) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				List<Map<String, Integer>> idPairList = parseIdPairList(positions[i], additionalInfos[j]);
				if (i == j) {
					for (Map<String, Integer> idPair : idPairList) {
						saveRemove(idPair);
					}
				} else {
					for (Map<String, Integer> idPair : idPairList) {
						saveAdd(idPair);
					}
				}
			}
		}
	}

	private void saveAdd(Map<String, Integer> idPair) {
		ArrangementPo addedArrangementPo = new ArrangementPo();
		addedArrangementPo.setArranged(1);
		arrangementMapper.updateByExampleSelective(addedArrangementPo, gnrArrangementPoExample(idPair));
	}

	private void saveRemove(Map<String, Integer> idPair) {
		ArrangementPo cancelledArrangementPo = new ArrangementPo();
		cancelledArrangementPo.setArranged(-1);
		arrangementMapper.updateByExampleSelective(cancelledArrangementPo, gnrArrangementPoExample(idPair));
	}

	private Example gnrArrangementPoExample(Map<String, Integer> idPair) {
		Example example = new Example(ArrangementPo.class);
		Criteria criteria = example.createCriteria();

		for (String key : idPair.keySet()) {
			Integer value = idPair.get(key);
			if (value == null) {
				continue;
			}
			switch (key) {
			case "c":
				criteria.andEqualTo("courseId", value);
				break;
			case "p":
				criteria.andEqualTo("periodId", value);
				break;
			case "r":
				criteria.andEqualTo("roomId", value);
				break;
			case "s":
				criteria.andEqualTo("tclassId", value);
				break;
			case "t":
				criteria.andEqualTo("teacherId", value);
				break;
			default:
				break;
			}
		}
		return example;
	}

	private void adjust(List<Arrangement> arrangementList, Adjustment adjustment) {
		String position = adjustment.getPosition();
		String additionalInfo = adjustment.getAdditionalInfo();
		Integer type = adjustment.getType();

		switch (type) {
		case 0:
			String[] positions = position.split("\\|");
			String[] additionalInfos = additionalInfo.split("\\|");
			swap(arrangementList, positions, additionalInfos, -1);
			break;
		case 1:
			remove(arrangementList, position, additionalInfo);
			break;
		case 2:
			add(arrangementList, position, additionalInfo);
			break;
		default:
			break;
		}
	}

	private void reverseAdjust(List<Arrangement> arrangementList, Adjustment adjustment) {
		String position = adjustment.getPosition();
		String additionalInfo = adjustment.getAdditionalInfo();
		Integer type = adjustment.getType();

		switch (type) {
		case 0:
			String[] positions = position.split("\\|");
			String[] additionalInfos = additionalInfo.split("\\|");
			swap(arrangementList, positions, additionalInfos, 1);
			break;
		case 1:
			add(arrangementList, position, additionalInfo);
			break;
		case 2:
			remove(arrangementList, position, additionalInfo);
			break;
		default:
			break;
		}
	}

	private void swap(List<Arrangement> arrangementList, String[] positions, String[] additionalInfos,
			Integer isReverse) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				List<Map<String, Integer>> idPairList = parseIdPairList(positions[i], additionalInfos[j]);
				if (i == j) {
					for (Map<String, Integer> idPair : idPairList) {
						setArrangedByIdPair(arrangementList, idPair, isReverse);
					}
				} else {
					for (Map<String, Integer> idPair : idPairList) {
						setArrangedByIdPair(arrangementList, idPair, -isReverse);
					}
				}
			}
		}
	}

	private void add(List<Arrangement> arrangementList, String position, String additionalInfo) {
		List<Map<String, Integer>> idPairList = parseIdPairList(position, additionalInfo);
		for (Map<String, Integer> idPair : idPairList) {
			setArrangedByIdPair(arrangementList, idPair, -1);
		}
	}

	private void remove(List<Arrangement> arrangementList, String position, String additionalInfo) {
		List<Map<String, Integer>> idPairList = parseIdPairList(position, additionalInfo);
		for (Map<String, Integer> idPair : idPairList) {
			setArrangedByIdPair(arrangementList, idPair, 1);
		}
	}

	private void setArrangedByIdPair(List<Arrangement> arrangementList, Map<String, Integer> idPair, Integer arranged) {
		Collection<Arrangement> arrangementCollection = CollectionUtils.select(arrangementList,
				gnrArrangementPredicate(idPair));
		for (Arrangement arrangement : arrangementCollection) {
			if (arranged != null) {
				arrangement.setArranged(arranged);
			}
		}
	}

	private Predicate<Arrangement> gnrArrangementPredicate(Map<String, Integer> idPair) {
		Arrangement arrangement = new Arrangement();

		for (String key : idPair.keySet()) {
			Integer value = idPair.get(key);
			switch (key) {
			case "c":
				arrangement.getCourse().setCourseId(value);
				break;
			case "p":
				arrangement.getPeriod().setPeriodId(value);
				break;
			case "r":
				arrangement.getRoom().setRoomId(value);
				break;
			case "s":
				arrangement.getTclass().setTclassId(value);
				break;
			case "t":
				arrangement.getTeacher().setTeacherId(value);
				break;
			default:
				break;
			}
		}
		return new ArrangementPredicate(arrangement);
	}

	/* Adjust Block End */

	/* Tool Block Start */

	private <T> T chooseRandomly(List<T> tList) {
		Random random = new Random();
		T t = tList.get(random.nextInt(tList.size()));
		return t;
	}

	private List<Map<String, Integer>> parseIdPairList(String position, String additionalInfo) {
		List<Map<String, Integer>> idPairList = new ArrayList<Map<String, Integer>>();

		String[] positionArray = position.split("-");
		String[] additionalInfosArray = additionalInfo.split(",");
		for (String additionalInfos : additionalInfosArray) {
			Map<String, Integer> idPair = new HashMap<String, Integer>();
			String[] additionalInfoArray = additionalInfos.split("-");
			for (int i = 0; i < positionArray.length;) {
				idPair.put(positionArray[i++], Integer.parseInt(positionArray[i++]));
			}
			for (int i = 0; i < additionalInfoArray.length;) {
				idPair.put(additionalInfoArray[i++], Integer.parseInt(additionalInfoArray[i++]));
			}
			idPairList.add(idPair);
		}
		return idPairList;
	}
	/* Tool Block End */

	private void updateArrangement(Arrangement arrangement) {
		ArrangementPo arrangementPo = arrangement.toPo();
		arrangementMapper.updateByPrimaryKeySelective(arrangementPo);
		return;
	}

	private void fastUpdateArrangement(List<Arrangement> arrangementList) {
		List<ArrangementPo> arrangementPoList = new ArrayList<ArrangementPo>();
		for (Arrangement arrangement : arrangementList) {
			arrangementPoList.add(arrangement.toPo());
		}
		arrangementMapper.insertListOnDuplicateKeyUpdate(arrangementPoList);
		return;
	}

	@Override
	public Arrangement selectArrangementById(Integer arrangementId) {
		return arrangementMapper.selectEntityById(arrangementId);
	}

	@Override
	public Map<String, Map<String, Integer>> getBackgroundMap(Integer scheduleId) {
		Map<String, Integer> adjustedMap = cache.getBackgroundMap().get("adjusted");
		adjustedMap.clear();
		List<Adjustment> adjustmentList = adjustmentService.selectTempListByScheduleId(scheduleId);
		Integer type = null;
		String[] positions = null;
		for (Adjustment adjustment : adjustmentList) {
			type = adjustment.getType();
			positions = adjustment.getPosition().split("\\|");
			switch (type) {
			case 0:
				adjustedMap.put(positions[0], 0);
				adjustedMap.put(positions[1], 0);
			case 1:
			case 2:
				adjustedMap.put(positions[0], 0);
				break;
			default:
				break;
			}
		}
		return cache.getBackgroundMap();
	}

}
