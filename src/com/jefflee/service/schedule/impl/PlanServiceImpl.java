package com.jefflee.service.schedule.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.schedule.Grade;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.GradeService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.util.Cache;
import com.jefflee.view.CoursePlanView;
import com.jefflee.view.SchedulePlanView;

@Service("planService")
public class PlanServiceImpl implements PlanService {

	@Resource(name = "cache")
	private Cache cache;

	@Resource(name = "planMapper")
	private PlanMapper planMapper;
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

	@Override
	public Integer insert(Plan plan) {
		if (planMapper.insert(plan) == 1) {
			return plan.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public List<Plan> selectList() {
		return planMapper.selectAll();
	}

	public Plan selectById(Integer planId) {
		return planMapper.selectDetailById(planId);
	}

	@Override
	public Integer updateById(Plan plan) {
		if (planMapper.updateByPrimaryKey(plan) == 1) {
			return plan.getPlanId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer planId) {
		if (planMapper.deleteByPrimaryKey(planId) == 1) {
			return planId;
		} else {
			return null;
		}
	}

	// TODO
	@Override
	public List<Plan> selectListByScheduleId(Integer scheduleId) {
		Plan plan = new Plan();
		plan.setScheduleId(scheduleId);
		return planMapper.select(plan);
	}

	public List<Plan> getPlanById(Integer courseId, Integer scheduleId) {
		return planMapper.selectListByCourseIdAndScheduleId(courseId, scheduleId);
	}

	@Override
	public List<Plan> selectDetailListByScheduleId(Integer scheduleId) {
		return planMapper.selectDetailListByScheduleId(scheduleId);
	}

	@Override
	public void copyListByScheduleId(Integer srcScheduleId, Integer destScheduleId) {
		List<Plan> srcPlanList = selectDetailListByScheduleId(srcScheduleId);
		List<Plan> destPlanList = selectDetailListByScheduleId(destScheduleId);
		Grade destGrade = scheduleService.selectById(destScheduleId).getGrade();
		List<Teacher> destTeacherList = teacherService.selectListByGradeId(destGrade.getGradeId());
		Map<Integer, Teacher> destTeacherMap = new HashMap<Integer, Teacher>();
		for (Teacher teacher : destTeacherList) {
			destTeacherMap.put(teacher.getTeacherId(), teacher);
		}
		for (Plan destPlan : destPlanList) {
			Integer destCourseId = destPlan.getCourseId();
			Tclass destTclass = destPlan.getTclass();
			String destTclassNo = destTclass == null ? "0" : destTclass.getTclassNo();
			for (Plan srcPlan : srcPlanList) {
				Integer srcCourseId = srcPlan.getCourseId();
				Tclass srcTclass = srcPlan.getTclass();
				String srcTclassNo = srcTclass == null ? "0" : srcTclass.getTclassNo();
				Integer srcTeacherId = srcPlan.getTeacherId();
				if (destCourseId.equals(srcCourseId) && destTclassNo.equals(srcTclassNo)) {
					if (destTeacherMap.containsKey(srcTeacherId)) {
						destPlan.setTeacherId(srcTeacherId);
						planMapper.updateByPrimaryKeySelective(destPlan);
						srcPlanList.remove(srcPlan);
						break;
					}
				}
			}
		}
	}

	@Override
	public void gnrEmptyPlanList(Integer scheduleId) {
		Schedule schedule = scheduleService.selectById(scheduleId);
		Grade grade = schedule.getGrade();
		List<Tclass> tclassList = tclassService.selectListByYearAndLevel(grade.getYear(), grade.getLevel());
		List<Course> courseList = courseService.selectListByGradeId(grade.getGradeId());
		List<Plan> planList = new ArrayList<Plan>();
		for (Course course : courseList) {
			// TODO type 23 24 25
			Integer courseId = course.getCourseId();
			if (courseId != 23 && courseId != 24 && courseId != 25) {
				for (Tclass tclass : tclassList) {
					Integer tclassId = tclass.getTclassId();
					Plan plan = new Plan();
					plan.setScheduleId(scheduleId);
					plan.setCourseId(courseId);
					plan.setTclassId(tclassId);
					plan.setTeacherId(0);
					plan.setRoomId(0);
					plan.setPeriodNum(0);
					planList.add(plan);
				}
			} else {

			}
		}
		planMapper.insertList(planList);
	}

	// TODO 待完善 初始化planlist
	@Override
	public SchedulePlanView gnrSchedulePlanView(Integer scheduleId) {
		Schedule schedule = scheduleService.selectById(scheduleId);
		SchedulePlanView schedulePlanView = new SchedulePlanView();

		schedulePlanView.setCoursePlanViewMap(new LinkedHashMap<Integer, CoursePlanView>());

		Grade grade = schedule.getGrade();
		List<Tclass> tclassList = tclassService.selectListByYearAndLevel(grade.getYear(), grade.getLevel());
		schedulePlanView.setTclassList(tclassList);

		Map<Integer, CoursePlanView> coursePlanViewMap = schedulePlanView.getCoursePlanViewMap();
		List<Course> courseList = courseService.selectListByGradeId(grade.getGradeId());
		for (Course course : courseList) {
			coursePlanViewMap.put(course.getCourseId(), gnrCoursePlanView(course, tclassList));
		}

		List<Plan> planList = selectDetailListByScheduleId(scheduleId);

		for (Plan plan : planList) {
			Integer courseId = plan.getCourseId();
			Integer tclassId = plan.getTclassId();
			Integer periodNum = plan.getPeriodNum();
			CoursePlanView coursePlanView = coursePlanViewMap.get(courseId);
			if (coursePlanView != null) {
				if (coursePlanView.getPeriodNum() < periodNum) {
					coursePlanView.setPeriodNum(periodNum);
				}
				String coursePlanViewId = "c-" + courseId + "-s-" + tclassId;
				coursePlanView.getPaneMap().get(coursePlanViewId).add(plan);
			}
		}

		schedulePlanView.setCoursePlanViewMap(coursePlanViewMap);
		return schedulePlanView;
	}

	private CoursePlanView gnrCoursePlanView(Course course, List<Tclass> tclassList) {
		CoursePlanView coursePlanView = new CoursePlanView();
		coursePlanView.setCourse(course);
		Integer courseId = course.getCourseId();
		// TODO type 23 24 25
		if (courseId != 23 && courseId != 24 && courseId != 25) {
			coursePlanView.setPaneMap(gnrPaneMap(courseId, tclassList));
		} else {
			coursePlanView.setPaneMap(gnrPaneMap(courseId, null));
		}
		coursePlanView.setPeriodNum(0);
		return coursePlanView;
	}

	private Map<String, List<Plan>> gnrPaneMap(Integer courseId, List<Tclass> tclassList) {
		Map<String, List<Plan>> paneMap = new LinkedHashMap<String, List<Plan>>();
		if (tclassList == null) {
			paneMap.put("c-" + courseId + "-s-0", new ArrayList<Plan>());
		} else {
			for (Tclass tclass : tclassList) {
				Integer tclassId = tclass.getTclassId();
				paneMap.put("c-" + courseId + "-s-" + tclassId, new ArrayList<Plan>());
			}
		}
		return paneMap;
	}

}
