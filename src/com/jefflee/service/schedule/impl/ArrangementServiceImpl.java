package com.jefflee.service.schedule.impl;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Period;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.schedule.Adjustment;
import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Grade;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ArrangementMapper;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.AdjustmentService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.GradeService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.util.ArrangementPredicate;
import com.jefflee.util.Cache;
import com.jefflee.util.ConflictPredicate;
import com.jefflee.util.ListUtil;
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

	@Resource(name = "gradeService")
	private GradeService gradeService;
	@Resource(name = "scheduleService")
	private ScheduleService scheduleService;
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
	public Integer insert(Arrangement arrangement) {
		if (arrangementMapper.insert(arrangement) == 1) {
			return arrangement.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public List<Arrangement> selectList() {
		return arrangementMapper.selectAll();
	}

	@Override
	public Integer updateById(Arrangement arrangement) {
		if (arrangementMapper.updateByPrimaryKey(arrangement) == 1) {
			return arrangement.getArrangementId();
		} else {
			return null;
		}
	}

	@Override
	public void deleteByScheduleId(Integer scheduleId) {
		Arrangement arrangement = new Arrangement();
		arrangement.setScheduleId(scheduleId);
		arrangementMapper.delete(arrangement);
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
	public Integer insertList(List<Arrangement> arrangementList) {
		return arrangementMapper.insertList(arrangementList);
	}

	/* CRUD Block End */

	/* Display Block Start */

	@Override
	public ScheduleView gnrScheduleView(Integer scheduleId) throws Exception {
		Schedule schedule = scheduleService.selectById(scheduleId);
		ScheduleView scheduleView = new ScheduleView();
		scheduleView.setSchedule(schedule);
		scheduleView.setTclassWeekViewMap(new LinkedHashMap<String, WeekView>());
		scheduleView.setTeacherWeekViewMap(new LinkedHashMap<String, WeekView>());

		List<Arrangement> scheduleArrangementList = cache.getScheduleArrangementList();
		List<Plan> schedulePlanList = cache.getSchedulePlanList();
		Map<String, Integer> conflictingMap = cache.getBackgroundMap().get("conflicting");
		conflictingMap.clear();

		Integer daysPerWeek = schedule.getDays();
		Integer periodsPerDay = schedule.getForenoon() + schedule.getAfternoon() + schedule.getEvening();
		List<Period> periodList = periodService.selectListByRange(daysPerWeek, periodsPerDay);

		Map<String, WeekView> tclassWeekViewMap = scheduleView.getTclassWeekViewMap();
		Map<String, WeekView> teacherWeekViewMap = scheduleView.getTeacherWeekViewMap();

		// 新建WeekView
		for (Plan plan : schedulePlanList) {
			Integer courseType = plan.getCourse().getType();
			Integer tclassId = plan.getTclassId();
			Integer teacherId = plan.getTeacherId();

			TitleView titleView = null;

			if (tclassId != 0 && courseType != 4) {
				Tclass tclass = plan.getTclass();
				String tclassWeekViewId = "s-" + tclassId;
				WeekView tclassWeekView = null;
				// 若不存在该班级课表，新建之
				if (!tclassWeekViewMap.containsKey(tclassWeekViewId)) {
					tclassWeekView = gnrWeekView(schedule, periodList);
					tclassWeekViewMap.put(tclassWeekViewId, tclassWeekView);
					titleView = tclassWeekView.getTitleView();
					titleView.setTclassName(tclass.getName());
					// 必要时添加
					for (DayView dayView : tclassWeekView.getDayViewList()) {
						for (PeriodView periodView : dayView.getPeriodViewList()) {
							periodView.setPeriodViewId(periodView.getPeriodViewId() + "-" + tclassWeekViewId);
						}
					}
				}
			}

			if (teacherId != 0 && courseType != 3) {
				Course course = plan.getCourse();
				Integer courseId = course.getCourseId();
				Teacher teacher = plan.getTeacher();
				String teacherWeekViewId = "t-" + teacherId + "-c-" + courseId;
				WeekView teacherWeekView = null;
				// 若不存在该教师课程课表，新建之
				// TODO type 11 23 24 25
				if (courseType != 1 && courseType != 4) {
					if (!teacherWeekViewMap.containsKey(teacherWeekViewId)) {
						teacherWeekView = gnrWeekView(schedule, periodList);
						teacherWeekViewMap.put(teacherWeekViewId, teacherWeekView);
						titleView = teacherWeekView.getTitleView();
						titleView.setCourseName(course.getShortName());
						titleView.setGradeName(schedule.getGrade().getName());
						titleView.setTeacherName(teacher.getName());
						// 必要时添加
						for (DayView dayView : teacherWeekView.getDayViewList()) {
							for (PeriodView periodView : dayView.getPeriodViewList()) {
								periodView.setPeriodViewId(periodView.getPeriodViewId() + "-" + teacherWeekViewId);
							}
						}
					}
				}
			}
		}

		for (Plan plan : schedulePlanList) {
			Integer courseId = plan.getCourseId();
			Integer courseType = plan.getCourse().getType();
			Integer tclassId = plan.getTclassId();
			Integer teacherId = plan.getTeacherId();

			if (tclassId != 0 && courseType != 4) {
				String tclassWeekViewId = "s-" + tclassId;
				String tclassPlanViewId = "c-" + courseId;
				WeekView tclassWeekView = null;
				// TODO type 23 24 25
				// if (course.getType() == 0) {
				if (courseType != 4) {
					tclassWeekView = tclassWeekViewMap.get(tclassWeekViewId);
					// TODO type 11
					if (courseType == 1) {
						tclassWeekView.getTitleView().setTeacherName(plan.getTeacher().getName());
					}
					tclassWeekView.getPlanViewMap().put(tclassPlanViewId, gnrPlanView(plan));
				}
			}

			if (teacherId != 0 && courseType != 3) {
				String teacherWeekViewId = "t-" + teacherId;
				String teacherPlanViewId = "";
				WeekView teacherWeekView = null;
				// TODO type 11 23 24 25
				if (courseType != 1 && courseType != 4) {
					teacherWeekViewId = teacherWeekViewId + "-c-" + courseId;
					teacherPlanViewId = "s-" + tclassId;
					teacherWeekView = teacherWeekViewMap.get(teacherWeekViewId);
					teacherWeekView.getPlanViewMap().put(teacherPlanViewId, gnrPlanView(plan));
				} else if (courseType == 4) {
					teacherPlanViewId = "c-" + courseId;
					Set<String> existTeacherWeekViewIdList = teacherWeekViewMap.keySet();
					for (String existTeacherWeekViewId : existTeacherWeekViewIdList) {
						if (existTeacherWeekViewId.startsWith(teacherWeekViewId + "-")) {
							teacherWeekView = teacherWeekViewMap.get(existTeacherWeekViewId);
							teacherWeekView.getPlanViewMap().put(teacherPlanViewId, gnrPlanView(plan));
						}
					}

				}
			}
		}

		for (Arrangement arrangement : scheduleArrangementList) {
			Integer courseId = arrangement.getCourseId();
			Integer courseType = arrangement.getCourse().getType();
			Integer tclassId = arrangement.getTclassId();
			Integer teacherId = arrangement.getTeacherId();

			if (arrangement.getArranged() == 1) {
				Period period = arrangement.getPeriod();

				if (tclassId != 0 && courseType != 4) {
					String tclassWeekViewId = "s-" + tclassId;
					String tclassPlanViewId = "c-" + courseId;
					WeekView tclassWeekView = null;
					PeriodView tclassPeriodView = null;
					PlanView tclassPlanView = null;
					// TODO type 23 24 25
					if (courseType != 4) {
						tclassWeekView = tclassWeekViewMap.get(tclassWeekViewId);
						tclassPeriodView = tclassWeekView.getDayViewList().get(period.getDayOfWeek() - 1)
								.getPeriodViewList().get(period.getOrderOfDay() - 1);

						// TODO type 19 20
						if (tclassPeriodView.getArrangementList().isEmpty() || (courseType != 2)) {
							if (!tclassPeriodView.getArrangementList().isEmpty()) {
								conflictingMap.put(tclassPeriodView.getPeriodViewId(), 0);
							}
							tclassPeriodView.getArrangementList().add(arrangement);
							tclassPeriodView.getJumpViewIdList().add("t-" + teacherId + "-c-" + courseId);

							tclassPlanView = tclassWeekView.getPlanViewMap().get(tclassPlanViewId);
							tclassPlanView.setArrangedNum(tclassPlanView.getArrangedNum() + 1);
						}
					}
				}

				if (teacherId != 0 && courseType != 3) {
					String teacherWeekViewId = "t-" + teacherId;
					String teacherPlanViewId = "";
					WeekView teacherWeekView = null;
					PeriodView teacherPeriodView = null;
					PlanView teacherPlanView = null;
					// TODO type 11 23 24 25
					if (courseType != 1 && courseType != 4) {
						teacherWeekViewId = teacherWeekViewId + "-c-" + courseId;
						teacherWeekView = teacherWeekViewMap.get(teacherWeekViewId);
						teacherPeriodView = teacherWeekView.getDayViewList().get(period.getDayOfWeek() - 1)
								.getPeriodViewList().get(period.getOrderOfDay() - 1);

						if (!teacherPeriodView.getArrangementList().isEmpty()) {
							conflictingMap.put(teacherPeriodView.getPeriodViewId(), 0);
						}
						teacherPeriodView.getArrangementList().add(arrangement);
						teacherPeriodView.getJumpViewIdList().add("s-" + tclassId);

						teacherPlanViewId = "s-" + tclassId;
						teacherPlanView = teacherWeekView.getPlanViewMap().get(teacherPlanViewId);
						teacherPlanView.setArrangedNum(teacherPlanView.getArrangedNum() + 1);
					} else if (courseType == 4) {
						teacherPlanViewId = "c-" + courseId;
						Set<String> existTeacherWeekViewIdList = teacherWeekViewMap.keySet();
						for (String existTeacherWeekViewId : existTeacherWeekViewIdList) {
							if (existTeacherWeekViewId.startsWith(teacherWeekViewId + "-")) {
								teacherWeekView = teacherWeekViewMap.get(existTeacherWeekViewId);
								teacherPeriodView = teacherWeekView.getDayViewList().get(period.getDayOfWeek() - 1)
										.getPeriodViewList().get(period.getOrderOfDay() - 1);
								if (!teacherPeriodView.getArrangementList().isEmpty()) {
									conflictingMap.put(teacherPeriodView.getPeriodViewId(), 0);
								}
								teacherPeriodView.getArrangementList().add(arrangement);
								teacherPeriodView.getJumpViewIdList().add("s-" + tclassId);

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

	private WeekView gnrWeekView(Schedule schedule, List<Period> periodList) throws Exception {
		WeekView weekView = new WeekView();
		weekView.setTitleView(gnrTitleView(schedule));
		weekView.setPeriodNameList(gnrPeriodNameList(schedule));
		weekView.setPlanViewMap(new LinkedHashMap<String, PlanView>());
		weekView.setDayViewList(gnrDayViewList(periodList));
		return weekView;
	}

	private List<String> gnrPeriodNameList(Schedule schedule) {
		List<String> periodNameList = new ArrayList<String>();
		Integer periodsPerDay = schedule.getForenoon() + schedule.getAfternoon() + schedule.getEvening();
		for (int i = 0; i < periodsPerDay; i++) {
			if (i == 0) {
				periodNameList.add("早");
			} else {
				periodNameList.add(String.valueOf(i));
			}
		}
		return periodNameList;
	}

	private TitleView gnrTitleView(Schedule schedule) throws Exception {
		TitleView titleView = new TitleView();
		Grade grade = schedule.getGrade();
		String startDate = grade.getStartDate();
		Integer startWeek = schedule.getStartWeek();

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = dateFormat.parse(startDate);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 7 * (startWeek - 1));

		titleView.setStartDate(dateFormat.format(calendar.getTime()));
		return titleView;
	}

	private PlanView gnrPlanView(Plan plan) {
		PlanView planView = new PlanView();
		planView.setPlan(plan);
		planView.setArrangedNum(0);
		return planView;
	}

	private List<DayView> gnrDayViewList(List<Period> periodList) {
		List<DayView> dayViewList = new ArrayList<DayView>();
		Map<Integer, List<Period>> dayPeriodListMap = new HashMap<Integer, List<Period>>();
		for (Period period : periodList) {
			if (!dayPeriodListMap.containsKey(period.getDayOfWeek() - 1)) {
				dayPeriodListMap.put(period.getDayOfWeek() - 1, new ArrayList<Period>());
			}
			dayPeriodListMap.get(period.getDayOfWeek() - 1).add(period.getOrderOfDay() - 1, period);
		}
		for (int i = 0; i < dayPeriodListMap.size(); i++) {
			dayViewList.add(i, gnrDayView(i, dayPeriodListMap.get(i)));
		}
		return dayViewList;
	}

	private DayView gnrDayView(int day, List<Period> dayPeriodList) {
		DayView dayView = new DayView();
		String dayName = "";
		switch (day) {
		case 0:
			dayName = "一";
			break;
		case 1:
			dayName = "二";
			break;
		case 2:
			dayName = "三";
			break;
		case 3:
			dayName = "四";
			break;
		case 4:
			dayName = "五";
			break;
		case 5:
			dayName = "六";
			break;
		case 6:
			dayName = "日";
			break;
		default:
			dayName = "";
			break;
		}
		dayView.setDayName(dayName);
		dayView.setPeriodViewList(gnrPeriodViewList(dayPeriodList));
		return dayView;
	}

	private List<PeriodView> gnrPeriodViewList(List<Period> dayPeriodList) {
		List<PeriodView> periodViewList = new ArrayList<PeriodView>();
		for (Period period : dayPeriodList) {
			periodViewList.add(period.getOrderOfDay() - 1, gnrPeriodView(period));
		}
		return periodViewList;
	}

	private PeriodView gnrPeriodView(Period period) {
		PeriodView periodView = new PeriodView();
		List<Arrangement> arrangementList = new ArrayList<Arrangement>();
		List<String> jumpViewIdList = new ArrayList<String>();
		periodView.setArrangementList(arrangementList);
		periodView.setJumpViewIdList(jumpViewIdList);
		periodView.setPeriodViewId("p-" + period.getPeriodId());
		return periodView;
	}

	/* Display Block End */

	/* Arrange Block Start */

	@Override
	public void gnrEmptyArrangementList(Integer scheduleId) {
		Schedule schedule = scheduleService.selectById(scheduleId);
		Integer daysPerWeek = schedule.getDays();
		Integer periodsPerDay = schedule.getForenoon() + schedule.getAfternoon() + schedule.getEvening();
		List<Period> periodList = periodService.selectListByRange(daysPerWeek, periodsPerDay);
		List<Plan> planList = planService.selectListByScheduleId(scheduleId);
		deleteByScheduleId(scheduleId);

		List<Arrangement> arrangementList = new ArrayList<Arrangement>();

		for (Plan plan : planList) {
			for (Period period : periodList) {
				Arrangement arrangement = new Arrangement();
				arrangement.setScheduleId(scheduleId);
				arrangement.setPeriodId(period.getPeriodId());
				arrangement.setCourseId(plan.getCourseId());
				arrangement.setRoomId(plan.getRoomId());
				arrangement.setTclassId(plan.getTclassId());
				arrangement.setTeacherId(plan.getTeacherId());
				arrangement.setArranged(0);
				arrangement.setPriority(2);
				arrangementList.add(arrangement);
			}
		}
		insertList(arrangementList);
	}

	@Override
	public void gnrArrangementList(Integer scheduleId) {
		saveAdjustment(scheduleId);
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

		// TODO 重新调整排课逻辑
		while (!unselectedArrangementList.isEmpty()) {
			Arrangement selectedArrangement = ListUtil.randomlyChoose(unselectedArrangementList);
			Integer courseId = selectedArrangement.getCourseId();
			Integer courseType = selectedArrangement.getCourse().getType();
			// TODO type 23 24 25
			if (courseType == 4) {
				selectedArrangement.setArranged(-1);
				unselectedArrangementList.remove(selectedArrangement);
				continue;
			}
			Integer tclassId = selectedArrangement.getTclassId();

			// 若需要添加的课程冲突，不添加
			ConflictPredicate conflictPredicate = new ConflictPredicate(selectedArrangement);
			List<Arrangement> arrangedConflictList = ListUtils.select(arrangedArrangementList, conflictPredicate);
			if (!arrangedConflictList.isEmpty()) {
				selectedArrangement.setArranged(-1);
				unselectedArrangementList.remove(selectedArrangement);
				continue;
			}

			// 若已安排数量超过上限，不添加
			Arrangement queryArrangement = new Arrangement();
			queryArrangement.setCourseId(courseId);
			queryArrangement.setTclassId(tclassId);
			Integer arrangedNum = ListUtils.select(arrangedArrangementList, new ArrangementPredicate(queryArrangement))
					.size();
			Plan queryPlan = new Plan();
			queryPlan.setCourseId(courseId);
			queryPlan.setTclassId(tclassId);
			Integer periodNum = ListUtils.select(schedulePlanList, new PlanPredicate(queryPlan)).iterator().next()
					.getPeriodNum();
			if (periodNum - arrangedNum <= 0) {
				selectedArrangement.setArranged(-1);
				unselectedArrangementList.remove(selectedArrangement);
				continue;
			}

			// 同课时同课程同班级安排一起添加
			Arrangement querySameCourseArrangement = new Arrangement();
			querySameCourseArrangement.setPeriodId(selectedArrangement.getPeriod().getPeriodId());
			querySameCourseArrangement.setCourseId(courseId);
			querySameCourseArrangement.setTclassId(tclassId);
			List<Arrangement> sameCourseArrangementList = ListUtils.select(unselectedArrangementList,
					new ArrangementPredicate(querySameCourseArrangement));
			for (Arrangement arrangement : sameCourseArrangementList) {
				arrangement.setArranged(1);
				arrangedArrangementList.add(arrangement);
				unselectedArrangementList.remove(arrangement);
				update(arrangement);
			}

			// 移除冲突的课程（注意和前一部分的顺序）
			List<Arrangement> unselectedConflictList = ListUtils.select(unselectedArrangementList, conflictPredicate);
			for (Arrangement arrangement : unselectedConflictList) {
				arrangement.setArranged(-1);
				unselectedArrangementList.remove(arrangement);
			}
		}
		fastUpdate(scheduleArrangementList);
		return;
	}

	private void clearList(Integer scheduleId) {
		Arrangement arrangement = new Arrangement();
		arrangement.setArranged(0);
		arrangement.setPriority(2);
		Example example = new Example(Arrangement.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("scheduleId", scheduleId);
		arrangementMapper.updateByExampleSelective(arrangement, example);
	}

	@Override
	public void resetList(Integer scheduleId) {
		saveAdjustment(scheduleId);
		clearList(scheduleId);
		cache.initial(scheduleId);
	}
	/* Arrange Block End */

	/* Adjust Block Start */

	@Override
	public void doAdjust(Integer adjustmentId) {
		Adjustment adjustment = adjustmentService.selectById(adjustmentId);
		adjustInCache(adjustment);
	}

	@Override
	public void undoAdjust(Integer adjustmentId) {
		Adjustment adjustment = adjustmentService.selectById(adjustmentId);
		reverseAdjustInCache(adjustment);
	}

	private void mergeAdjustmentList(List<Arrangement> arrangementList, List<Adjustment> adjustmentList) {
		for (Adjustment adjustment : adjustmentList) {
			adjustInCache(adjustment);
		}
	}

	@Override
	public void saveAdjustment(Integer scheduleId) {
		List<Adjustment> scheduleAdjustmentList = adjustmentService.selectTempListByScheduleId(scheduleId);
		for (Adjustment adjustment : scheduleAdjustmentList) {
			adjustInDatabase(adjustment);
			adjustmentService.deleteById(adjustment.getAdjustmentId());
		}
		cache.initial(scheduleId);
	}

	private void adjustInDatabase(Adjustment adjustment) {
		String position = adjustment.getPosition();
		String info = adjustment.getInfo();
		Integer type = adjustment.getType();

		switch (type) {
		case 0:
			String[] positions = position.split("\\|");
			String[] infos = info.split("\\|");
			swapInDatabase(positions, infos);
			break;
		case 1:
			addToDatabase(position, info);
			break;
		case 2:
			removeFromDatabase(position, info);
			break;
		default:
			break;
		}
	}

	private void swapInDatabase(String[] positions, String[] infos) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				List<Map<String, Integer>> idPairList = parseIdPairList(positions[i], infos[j]);
				if (i == j) {
					for (Map<String, Integer> idPair : idPairList) {
						setArrangedInDatabase(idPair, 1);
					}
				} else {
					for (Map<String, Integer> idPair : idPairList) {
						setArrangedInDatabase(idPair, -1);
					}
				}
			}
		}
	}

	private void addToDatabase(String position, String info) {
		List<Map<String, Integer>> idPairList = parseIdPairList(position, info);
		for (Map<String, Integer> idPair : idPairList) {
			setArrangedInDatabase(idPair, 1);
		}
	}

	private void removeFromDatabase(String position, String info) {
		List<Map<String, Integer>> idPairList = parseIdPairList(position, info);
		for (Map<String, Integer> idPair : idPairList) {
			setArrangedInDatabase(idPair, -1);
		}
	}

	private void setArrangedInDatabase(Map<String, Integer> idPair, Integer arranged) {
		List<Arrangement> arrangementList = cache.getScheduleArrangementList();
		List<Plan> planList = cache.getSchedulePlanList();
		Arrangement arrangement = getArrangementByIdPair(idPair);
		Arrangement arrangedArrangement = new Arrangement();
		arrangedArrangement.setArranged(arranged);
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();

		Integer courseType = courseService.selectById(arrangement.getCourseId()).getType();
		// TODO type 23 24 25
		if (courseType == 4) {
			conditionArrangementList = getSameGradeArrangementList(arrangementList, planList, arrangement);
		} else {
			conditionArrangementList = ListUtils.select(arrangementList, new ArrangementPredicate(arrangement));
		}
		for (Arrangement conditionArrangement : conditionArrangementList) {
			Example example = gnrArrangementExample(conditionArrangement);
			arrangementMapper.updateByExampleSelective(arrangedArrangement, example);
		}
	}

	private Example gnrArrangementExample(Arrangement arrangement) {
		Example example = new Example(Arrangement.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("arrangementId", arrangement.getArrangementId());
		return example;
	}

	private void adjustInCache(Adjustment adjustment) {
		String position = adjustment.getPosition();
		String info = adjustment.getInfo();
		Integer type = adjustment.getType();

		switch (type) {
		case 0:
			String[] positions = position.split("\\|");
			String[] infos = info.split("\\|");
			swapInCache(positions, infos, -1);
			break;
		case 1:
			removeFromCache(position, info);
			break;
		case 2:
			addToCache(position, info);
			break;
		default:
			break;
		}
	}

	private void reverseAdjustInCache(Adjustment adjustment) {
		String position = adjustment.getPosition();
		String info = adjustment.getInfo();
		Integer type = adjustment.getType();

		switch (type) {
		case 0:
			String[] positions = position.split("\\|");
			String[] infos = info.split("\\|");
			swapInCache(positions, infos, 1);
			break;
		case 1:
			addToCache(position, info);
			break;
		case 2:
			removeFromCache(position, info);
			break;
		default:
			break;
		}
	}

	private void swapInCache(String[] positions, String[] infos, Integer isReverse) {
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				List<Map<String, Integer>> idPairList = parseIdPairList(positions[i], infos[j]);
				if (i == j) {
					for (Map<String, Integer> idPair : idPairList) {
						setArrangedInCache(idPair, isReverse);
					}
				} else {
					for (Map<String, Integer> idPair : idPairList) {
						setArrangedInCache(idPair, -isReverse);
					}
				}
			}
		}
	}

	private void addToCache(String position, String info) {
		List<Map<String, Integer>> idPairList = parseIdPairList(position, info);
		for (Map<String, Integer> idPair : idPairList) {
			setArrangedInCache(idPair, -1);
		}
	}

	private void removeFromCache(String position, String info) {
		List<Map<String, Integer>> idPairList = parseIdPairList(position, info);
		for (Map<String, Integer> idPair : idPairList) {
			setArrangedInCache(idPair, 1);
		}
	}

	private void setArrangedInCache(Map<String, Integer> idPair, Integer arranged) {
		List<Arrangement> arrangementList = cache.getScheduleArrangementList();
		List<Plan> planList = cache.getSchedulePlanList();
		Arrangement arrangement = getArrangementByIdPair(idPair);
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();

		Integer courseType = courseService.selectById(arrangement.getCourseId()).getType();
		// TODO type 23 24 25
		if (courseType == 4) {
			conditionArrangementList = getSameGradeArrangementList(arrangementList, planList, arrangement);
		} else {
			conditionArrangementList = ListUtils.select(arrangementList, new ArrangementPredicate(arrangement));
		}
		for (Arrangement conditionArrangement : conditionArrangementList) {
			conditionArrangement.setArranged(arranged);
		}
	}

	private List<Arrangement> getSameGradeArrangementList(List<Arrangement> arrangementList, List<Plan> planList,
			Arrangement arrangement) {
		Integer mainCourseId = getMainCourseIdByTeacherId(planList, arrangement.getTeacherId());
		Set<Integer> mainCourseTeacherIdSet = getTeacherIdSetByCourseId(planList, mainCourseId);
		return getArrangementListByTeacherIdSet(arrangementList, arrangement, mainCourseTeacherIdSet);
	}

	private List<Arrangement> getArrangementListByTeacherIdSet(List<Arrangement> arrangementList,
			Arrangement arrangement, Set<Integer> teacherIdSet) {
		List<Arrangement> conditionArrangementList = new ArrayList<Arrangement>();
		for (Integer teacherId : teacherIdSet) {
			Arrangement conditionArrangement = new Arrangement();
			conditionArrangement.setPeriodId(arrangement.getPeriodId());
			conditionArrangement.setCourseId(arrangement.getCourseId());
			conditionArrangement.setTeacherId(teacherId);
			conditionArrangementList
					.addAll(ListUtils.select(arrangementList, new ArrangementPredicate(conditionArrangement)));
		}
		return conditionArrangementList;
	}

	private Set<Integer> getTeacherIdSetByCourseId(List<Plan> planList, Integer courseId) {
		Plan plan = new Plan();
		plan.setCourseId(courseId);
		List<Plan> mainCoursePlanList = ListUtils.select(planList, new PlanPredicate(plan));

		Set<Integer> teacherIdSet = new HashSet<Integer>();
		for (Plan mainCoursPlan : mainCoursePlanList) {
			teacherIdSet.add(mainCoursPlan.getTeacherId());
		}
		return teacherIdSet;
	}

	// 获取该教师主要负责课程
	private Integer getMainCourseIdByTeacherId(List<Plan> planList, Integer teacherId) {
		Plan teacherPlan = new Plan();
		teacherPlan.setTeacherId(teacherId);
		List<Plan> teacherPlanList = ListUtils.select(planList, new PlanPredicate(teacherPlan));
		Integer courseId = null;
		for (Plan plan : teacherPlanList) {
			Integer courseType = plan.getCourse().getType();
			// TODO type 16 17 18 19 20 23 24 25
			if (courseType == 0) {
				return plan.getCourseId();
			}
			if (courseType != 4) {
				courseId = plan.getCourseId();
			}
		}
		return courseId;
	}

	private Arrangement getArrangementByIdPair(Map<String, Integer> idPair) {
		Arrangement arrangement = new Arrangement();

		for (String key : idPair.keySet()) {
			Integer value = idPair.get(key);
			switch (key) {
			case "c":
				arrangement.setCourseId(value);
				break;
			case "p":
				arrangement.setPeriodId(value);
				break;
			case "r":
				arrangement.setRoomId(value);
				break;
			case "s":
				arrangement.setTclassId(value);
				break;
			case "t":
				arrangement.setTeacherId(value);
				break;
			default:
				break;
			}
		}
		return arrangement;
	}

	/* Adjust Block End */

	/* Tool Block Start */

	private List<Map<String, Integer>> parseIdPairList(String position, String info) {
		List<Map<String, Integer>> idPairList = new ArrayList<Map<String, Integer>>();

		String[] positionArray = position.split("-");
		String[] infosArray = info.split(",");
		for (String infos : infosArray) {
			Map<String, Integer> idPair = new HashMap<String, Integer>();
			String[] infoArray = infos.split("-");
			for (int i = 0; i < positionArray.length;) {
				idPair.put(positionArray[i++], Integer.parseInt(positionArray[i++]));
			}
			for (int i = 0; i < infoArray.length;) {
				idPair.put(infoArray[i++], Integer.parseInt(infoArray[i++]));
			}
			idPairList.add(idPair);
		}
		return idPairList;
	}
	/* Tool Block End */

	private void update(Arrangement arrangement) {
		arrangementMapper.updateByPrimaryKeySelective(arrangement);
		return;
	}

	private void fastUpdate(List<Arrangement> arrangementList) {
		arrangementMapper.insertListOnDuplicateKeyUpdate(arrangementList);
		return;
	}

	@Override
	public Arrangement selectById(Integer arrangementId) {
		return arrangementMapper.selectDetailById(arrangementId);
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

	@Override
	public void copyListByScheduleId(Integer srcScheduleId, Integer destScheduleId) {
		List<Arrangement> srcArrangementList = arrangementMapper.selectDetailListByScheduleId(srcScheduleId);
		List<Arrangement> destArrangementList = cache.getScheduleArrangementList();
		for (Arrangement destArrangement : destArrangementList) {
			Integer destCourseId = destArrangement.getCourseId();
			Tclass destTclass = destArrangement.getTclass();
			Integer destTclassNo = destTclass == null ? 0 : destTclass.getTclassNo();
			Integer destPeriodId = destArrangement.getPeriodId();
			for (Arrangement srcArrangement : srcArrangementList) {
				Integer srcCourseId = srcArrangement.getCourseId();
				Tclass srcTclass = srcArrangement.getTclass();
				Integer srcTclassNo = srcTclass == null ? 0 : srcTclass.getTclassNo();
				Integer srcPeriodId = srcArrangement.getPeriodId();
				Integer srcArranged = srcArrangement.getArranged();
				if (Objects.equals(destCourseId, srcCourseId) && Objects.equals(destTclassNo, srcTclassNo)
						&& Objects.equals(destPeriodId, srcPeriodId)) {
					destArrangement.setArranged(srcArranged);
					srcArrangementList.remove(srcArrangement);
					break;
				}
			}
		}
		if (!destArrangementList.isEmpty()) {
			fastUpdate(destArrangementList);
		}
	}

	@Override
	public void exportExcelView(HttpServletResponse response, Integer scheduleId) throws Exception {
		ScheduleView scheduleView = gnrScheduleView(scheduleId);
		Schedule schedule = scheduleView.getSchedule();
		List<WeekView> tclassWeekViewList = new ArrayList<WeekView>(scheduleView.getTclassWeekViewMap().values());
		List<WeekView> teacherWeekViewList = new ArrayList<WeekView>(scheduleView.getTeacherWeekViewMap().values());

		Workbook workbook = new XSSFWorkbook();
		Sheet tclassSheet = workbook.createSheet("班级课表");
		Sheet teacherSheet = workbook.createSheet("教师课表");
		Row tclassRow = null;
		Row teacherRow = null;
		Cell tclassCell = null;
		Cell teacherCell = null;

		// 用于计算单课表大小以及合并单元格
		int daysPerWeek = schedule.getDays();
		int forenoon = schedule.getForenoon();
		int afternoon = schedule.getAfternoon();
		int evening = schedule.getEvening();
		int periodsPerDay = forenoon + afternoon + evening;
		// 单课表行数
		int weekViewRows = daysPerWeek + 3;
		// 单课表列数
		int weekViewCols = periodsPerDay + 1;

		// 每行展示多少课表
		int weekViewColNum = 3;
		// 课表间上下间隔
		int blankIntervalRows = 2;
		// 课表间左右间隔
		int blankIntervalCols = 2;
		// 班级课表数量
		int tclassNum = tclassWeekViewList.size();
		// 教师课表数量
		int teacherNum = teacherWeekViewList.size();

		// 多少行班级课表
		int tclassWeekViewRowNum = tclassNum % weekViewColNum == 0 ? tclassNum / weekViewColNum
				: tclassNum / weekViewColNum + 1;
		// 多少列教师课表
		int teacherWeekViewRowNum = teacherNum % weekViewColNum == 0 ? teacherNum / weekViewColNum
				: teacherNum / weekViewColNum + 1;

		// 单课表行数（含间隔）
		int blockRows = weekViewRows + blankIntervalRows;
		// 单课表列数（含间隔）
		int blockCols = weekViewCols + blankIntervalCols;

		// 班级课表总行数
		int tclassScheduleViewRows = blockRows * tclassWeekViewRowNum - blankIntervalRows;
		// 教师课表总行数
		int teacherScheduleViewRows = blockRows * teacherWeekViewRowNum - blankIntervalRows;
		// 课表总列数
		int scheduleViewCols = blockCols * weekViewColNum - blankIntervalCols;

		WeekView tclassWeekView = new WeekView();
		WeekView teacherWeekView = new WeekView();
		CellRangeAddress region = null;

		int blockRowIdx = -1;
		int blockColIdx = -1;
		int rowIdx = -1;
		int colIdx = -1;
		int weekViewIdx = -1;
		List<Arrangement> periodArrangementList = null;

		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setShrinkToFit(true);

		tclassSheet.setDefaultColumnWidth(2);
		tclassSheet.setDefaultRowHeightInPoints(16);
		teacherSheet.setDefaultColumnWidth(2);
		teacherSheet.setDefaultRowHeightInPoints(16);

		for (int i = 0; i < tclassScheduleViewRows; i++) {
			blockRowIdx = i / blockRows;
			rowIdx = i % blockRows;
			tclassRow = tclassSheet.createRow(i);
			if (rowIdx - weekViewRows >= 0 && rowIdx - weekViewRows <= blankIntervalRows) {
				continue;
			}
			for (int j = 0; j < scheduleViewCols; j++) {
				blockColIdx = j / blockCols;
				colIdx = j % blockCols;
				weekViewIdx = blockRowIdx * weekViewColNum + blockColIdx;
				if (weekViewIdx >= tclassNum) {
					break;
				}
				tclassWeekView = tclassWeekViewList.get(weekViewIdx);
				tclassCell = tclassRow.createCell(j);
				if (colIdx - weekViewCols >= 0 && colIdx - weekViewCols <= blankIntervalCols) {
					continue;
				}
				tclassCell.setCellStyle(style);
				if (rowIdx == 0) {
					if (colIdx == 0) {
						region = new CellRangeAddress(i, i, j, j + 2);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue(tclassWeekView.getTitleView().getTclassName());
						continue;
					}
					if (colIdx == 3) {
						region = new CellRangeAddress(i, i, j, j + 2);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue(tclassWeekView.getTitleView().getTeacherName());
						continue;
					}
					if (colIdx == 6) {
						region = new CellRangeAddress(i, i, j, j + 2);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue(tclassWeekView.getTitleView().getStartDate());
						continue;
					}
				}
				if (rowIdx == 1) {
					if (colIdx == 1) {
						region = new CellRangeAddress(i, i, j, j + forenoon - 1);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue("上午");
						continue;
					}
					if (colIdx == 1 + forenoon) {
						region = new CellRangeAddress(i, i, j, j + afternoon - 1);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue("下午");
						continue;
					}
					if (colIdx == 1 + forenoon + afternoon) {
						region = new CellRangeAddress(i, i, j, j + evening - 1);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue("傍晚");
						continue;
					}
				}
				if (rowIdx == 2) {
					if (colIdx == 0) {
						tclassCell.setCellValue("周");
						continue;
					}
					if (colIdx == 1) {
						tclassCell.setCellValue("早");
						continue;
					}
					if (colIdx >= 2 && colIdx <= periodsPerDay) {
						tclassCell.setCellValue(String.valueOf(colIdx - 1));
						continue;
					}
				}
				if (rowIdx >= 3 && rowIdx <= 2 + daysPerWeek) {
					DayView dayView = tclassWeekView.getDayViewList().get(rowIdx - 3);
					if (colIdx == 0) {
						tclassCell.setCellValue(dayView.getDayName());
						continue;
					}
					if (colIdx >= 1 && colIdx <= periodsPerDay) {
						periodArrangementList = dayView.getPeriodViewList().get(colIdx - 1).getArrangementList();
						if (!periodArrangementList.isEmpty()) {
							for (Arrangement arrangement : periodArrangementList) {
								tclassCell.setCellValue(
										tclassCell.getStringCellValue() + arrangement.getCourse().getShortName());
							}
						}
						continue;
					}
				}
			}
		}
		for (int i = 0; i < teacherScheduleViewRows; i++) {
			blockRowIdx = i / blockRows;
			rowIdx = i % blockRows;
			teacherRow = teacherSheet.createRow(i);
			if (rowIdx - weekViewRows >= 0 && rowIdx - weekViewRows <= blankIntervalRows) {
				continue;
			}
			for (int j = 0; j < scheduleViewCols; j++) {
				blockColIdx = j / blockCols;
				colIdx = j % blockCols;
				weekViewIdx = blockRowIdx * weekViewColNum + blockColIdx;
				if (weekViewIdx >= teacherNum) {
					break;
				}
				teacherWeekView = teacherWeekViewList.get(weekViewIdx);
				teacherCell = teacherRow.createCell(j);
				if (colIdx - weekViewCols >= 0 && colIdx - weekViewCols <= blankIntervalCols) {
					continue;
				}
				teacherCell.setCellStyle(style);
				if (rowIdx == 0) {
					if (colIdx == 0) {
						teacherCell.setCellValue(teacherWeekView.getTitleView().getCourseName());
						continue;
					}
					if (colIdx == 1) {
						region = new CellRangeAddress(i, i, j, j + 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue(teacherWeekView.getTitleView().getGradeName());
					}
					if (colIdx == 3) {
						region = new CellRangeAddress(i, i, j, j + 2);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue(teacherWeekView.getTitleView().getTeacherName());
						continue;
					}
					if (colIdx == 6) {
						region = new CellRangeAddress(i, i, j, j + 2);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue(teacherWeekView.getTitleView().getStartDate());
						continue;
					}
				}
				if (rowIdx == 1) {
					if (colIdx == 1) {
						region = new CellRangeAddress(i, i, j, j + forenoon - 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue("上午");
						continue;
					}
					if (colIdx == 1 + forenoon) {
						region = new CellRangeAddress(i, i, j, j + afternoon - 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue("下午");
						continue;
					}
					if (colIdx == 1 + forenoon + afternoon) {
						region = new CellRangeAddress(i, i, j, j + evening - 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue("晚上");
						continue;
					}
				}
				if (rowIdx == 2) {
					if (colIdx == 0) {
						teacherCell.setCellValue("周");
						continue;
					}
					if (colIdx == 1) {
						teacherCell.setCellValue("早");
						continue;
					}
					if (colIdx >= 2 && colIdx <= periodsPerDay) {
						teacherCell.setCellValue(String.valueOf(colIdx - 1));
						continue;
					}
				}
				if (rowIdx >= 3 && rowIdx <= 2 + daysPerWeek) {
					DayView dayView = teacherWeekView.getDayViewList().get(rowIdx - 3);
					if (colIdx == 0) {
						teacherCell.setCellValue(dayView.getDayName());
						continue;
					}
					if (colIdx >= 1 && colIdx <= periodsPerDay) {
						periodArrangementList = dayView.getPeriodViewList().get(colIdx - 1).getArrangementList();
						if (!periodArrangementList.isEmpty()) {
							// TODO type 11 23 24 25
							for (Arrangement arrangement : periodArrangementList) {
								Integer courseType = arrangement.getCourse().getType();
								if (courseType == 1 || courseType == 4) {
									teacherCell.setCellValue(
											teacherCell.getStringCellValue() + arrangement.getCourse().getShortName());
								} else {
									teacherCell.setCellValue(
											teacherCell.getStringCellValue() + arrangement.getTclass().getShortName());
								}
							}
						}
						continue;
					}
				}
			}
		}

		// 设置打印参数
		tclassSheet.setMargin(Sheet.TopMargin, 0.85);
		tclassSheet.setMargin(Sheet.BottomMargin, 0.85);
		tclassSheet.setHorizontallyCenter(true);
		teacherSheet.setMargin(Sheet.TopMargin, 0.85);
		teacherSheet.setMargin(Sheet.BottomMargin, 0.85);
		teacherSheet.setHorizontallyCenter(true);

		Grade grade = scheduleView.getSchedule().getGrade();
		String gradeName = grade.getName();
		String startDate = grade.getStartDate();
		String fileName = gradeName + startDate + "课表.xlsx";
		fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.close();
		workbook.close();
	}

}
