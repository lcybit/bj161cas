package com.jefflee.service.schedule.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.entity.schedule.Grade;
import com.jefflee.entity.schedule.Plan;
import com.jefflee.mapper.schedule.PlanMapper;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.GradeService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.util.Cache;
import com.jefflee.util.PlanPredicate;
import com.jefflee.view.CoursePlanView;
import com.jefflee.view.SchedulePlanView;

@Service("planService")
public class PlanServiceImpl implements PlanService {

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
	@Resource(name = "cache")
	private Cache cache;

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
		List<Teacher> destTeacherList = teacherService.selectListByScheduleId(destScheduleId);
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

	// TODO 待完善 初始化planlist
	@Override
	public SchedulePlanView gnrSchedulePlanView(Integer gradeId, Integer scheduleId) {

		SchedulePlanView schedulePlanView = new SchedulePlanView();

		// 获取tclasslist
		// 根据gradeid获取相应的year ，再根据year从tclass表中获取该年级的多个班级
		Grade grade = gradeService.selectById(gradeId);
		List<Tclass> tclassList = tclassService.selectListByYear(grade.getYear());
		schedulePlanView.setTclassList(tclassList);

		// 获取courselist
		List<Course> courseList = courseService.selectListByGradeId(gradeId);

		// 根据scheduleId获得planList
		List<Plan> planList = selectDetailListByScheduleId(scheduleId);

		// 根据courselist 、tclasslist 生成具有部分属性的coursePlanViewMap
		Map<String, CoursePlanView> coursePlanViewMap = new LinkedHashMap<String, CoursePlanView>();

		for (Course course : courseList) {
			CoursePlanView coursePlanView = new CoursePlanView();
			Map<String, Plan> paneMap = new LinkedHashMap<String, Plan>();

			Integer courseId = course.getCourseId();
			coursePlanView.setCourse(course);

			for (Tclass tclass : tclassList) {
				Integer tclassId = tclass.getTclassId();
				paneMap.put(tclassId.toString(), new Plan());

				// schd_plan表里没有一条scheduleId的数据时，插入x*y条数据到该表,这样 planlist 只有两种情况
				// 1. null 2. planlist.size()=x*y
				Plan queryPlan = new Plan();
				queryPlan.setCourseId(courseId);
				queryPlan.setTclassId(tclassId);
				if (ListUtils.select(planList, new PlanPredicate(queryPlan)).isEmpty()) {
					Plan plan = new Plan();
					plan.setScheduleId(scheduleId);
					plan.setCourseId(courseId);
					// TODO type
					// 默认教室，体育课（courseid=10）roomid=9，其他的课程roomid=tclassid;
					if (courseId == 10) {
						plan.setRoomId(9);
					} else {
						plan.setRoomId(tclassId);
					}
					plan.setTclassId(tclassId);
					plan.setPeriodNum(0);// 默认值0
					insert(plan);
				}
			}
			coursePlanView.setPaneMap(paneMap);
			coursePlanViewMap.put(courseId.toString(), coursePlanView);
		}

		// 定位 planList中的plan元素
		// 即将用planList中的每一个plan的plan.getPeriodNum()、plan.getTeacher()填充coursePlanViewMap，使其完善
		for (Plan plan : planList) {
			Integer courseId = plan.getCourseId();
			Integer tclassId = plan.getTclassId();
			CoursePlanView coursePlanView = coursePlanViewMap.get(courseId.toString());

			// 判断coursePlanView是否存在，即 coursePlanViewMap是否包含courseId的元素
			if (coursePlanView != null) {
				coursePlanView.setPeriodNum(plan.getPeriodNum());
				coursePlanView.getPaneMap().put(tclassId.toString(), plan);
			}
		}

		schedulePlanView.setCoursePlanViewMap(coursePlanViewMap);
		return schedulePlanView;
	}

}
