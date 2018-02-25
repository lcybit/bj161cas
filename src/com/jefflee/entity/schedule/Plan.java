package com.jefflee.entity.schedule;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Room;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.po.schedule.PlanPo;

public class Plan {
	public Integer planId;
	public Integer scheduleId;
	public Course course;
	public Room room;
	public Tclass tclass;
	public Teacher teacher;
	public Integer periodNum;

	public Plan() {
		this.course = new Course();
		this.room = new Room();
		this.tclass = new Tclass();
		this.teacher = new Teacher();
	}

	public Plan(Integer planId) {
		this.planId = planId;
		this.course = new Course();
		this.room = new Room();
		this.tclass = new Tclass();
		this.teacher = new Teacher();
	}

	public Plan(PlanPo planPo) {
		planId = planPo.getPlanId();
		scheduleId = planPo.getScheduleId();
		course = new Course(planPo.getCourseId());
		room = new Room(planPo.getRoomId());
		tclass = new Tclass(planPo.getTclassId());
		teacher = new Teacher(planPo.getTeacherId());
		periodNum = planPo.getPeriodNum();
	}

	public PlanPo toPo() {
		PlanPo planPo = new PlanPo();
		planPo.setPlanId(planId);
		planPo.setScheduleId(scheduleId);
		planPo.setCourseId(course.courseId);
		planPo.setRoomId(room.roomId);
		planPo.setTclassId(tclass.tclassId);
		planPo.setTeacherId(teacher.teacherId);
		planPo.setPeriodNum(periodNum);
		return planPo;
	}
}
