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

	@Override
	public void delete(Plan plan) {
		planMapper.delete(plan);
	}

	@Override
	public void deleteByScheduleId(Integer scheduleId) {
		Plan plan = new Plan();
		plan.setScheduleId(scheduleId);
		planMapper.delete(plan);
	}

	// TODO
	@Override
	public List<Plan> selectListByScheduleId(Integer scheduleId) {
		Plan plan = new Plan();
		plan.setScheduleId(scheduleId);
		return planMapper.select(plan);
	}

	@Override
	public List<Plan> selectList(Plan plan) {
		return planMapper.select(plan);
	}

	@Override
	public List<Plan> selectDetailListByScheduleId(Integer scheduleId) {
		return planMapper.selectDetailListByScheduleId(scheduleId);
	}

	@Override
	public void copyListByScheduleId(Integer srcScheduleId, Integer destScheduleId) {
		List<Plan> srcPlanList = selectDetailListByScheduleId(srcScheduleId);
		// 清空原Plan列表
		deleteByScheduleId(destScheduleId);

		Grade destGrade = scheduleService.selectById(destScheduleId).getGrade();
		Integer destGradeId = destGrade.getGradeId();

		List<Teacher> destTeacherList = teacherService.selectListByGradeId(destGradeId);
		Map<Integer, Teacher> destTeacherMap = new HashMap<Integer, Teacher>();
		Teacher emptyTeacher = new Teacher();
		emptyTeacher.setTeacherId(0);
		destTeacherMap.put(0, emptyTeacher);
		for (Teacher teacher : destTeacherList) {
			destTeacherMap.put(teacher.getTeacherId(), teacher);
		}

		List<Course> destCourseList = courseService.selectListByGradeId(destGradeId);
		Map<Integer, Course> destCourseMap = new HashMap<Integer, Course>();
		for (Course course : destCourseList) {
			destCourseMap.put(course.getCourseId(), course);
		}

		List<Tclass> destTclassList = tclassService.selectListByGrade(destGrade);
		Map<String, Tclass> destTclassMap = new HashMap<String, Tclass>();
		Tclass emptyTclass = new Tclass();
		emptyTclass.setTclassId(0);
		destTclassMap.put("0", emptyTclass);
		for (Tclass tclass : destTclassList) {
			destTclassMap.put(tclass.getTclassNo(), tclass);
		}

		for (Plan srcPlan : srcPlanList) {
			Integer srcCourseId = srcPlan.getCourseId();
			Integer srcTeacherId = srcPlan.getTeacherId();
			Tclass srcTclass = srcPlan.getTclass();
			String srcTclassNo = srcTclass == null ? "0" : srcTclass.getTclassNo();
			if (destCourseMap.containsKey(srcCourseId) && destTeacherMap.containsKey(srcTeacherId)
					&& destTclassMap.containsKey(srcTclassNo)) {
				Plan destPlan = new Plan();
				destPlan.setScheduleId(destScheduleId);
				destPlan.setCourseId(srcCourseId);
				destPlan.setTeacherId(srcTeacherId);
				destPlan.setTclassId(destTclassMap.get(srcTclassNo).getTclassId());
				destPlan.setRoomId(srcPlan.getRoomId());
				destPlan.setPeriodNum(srcPlan.getPeriodNum());
				planMapper.insert(destPlan);
			}
		}
	}

	@Override
	public void gnrEmptyPlanList(Integer scheduleId) {
		Schedule schedule = scheduleService.selectById(scheduleId);
		Grade grade = schedule.getGrade();
		List<Tclass> tclassList = tclassService.selectListByGrade(grade);
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
		List<Tclass> tclassList = tclassService.selectListByGrade(grade);
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
				coursePlanView.getPaneMap().get(tclassId).add(plan);
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

	private Map<Integer, List<Plan>> gnrPaneMap(Integer courseId, List<Tclass> tclassList) {
		Map<Integer, List<Plan>> paneMap = new LinkedHashMap<Integer, List<Plan>>();
		if (tclassList == null) {
			paneMap.put(0, new ArrayList<Plan>());
		} else {
			for (Tclass tclass : tclassList) {
				Integer tclassId = tclass.getTclassId();
				paneMap.put(tclassId, new ArrayList<Plan>());
			}
		}
		return paneMap;
	}

	@Override
	public void updatePeriodNum(Plan plan) {
		Integer periodNum = plan.getPeriodNum();
		plan.setPeriodNum(null);
		List<Plan> planList = selectList(plan);
		for (Plan queryPlan : planList) {
			queryPlan.setPeriodNum(periodNum);
			planMapper.updateByPrimaryKeySelective(queryPlan);
		}
	}

}
