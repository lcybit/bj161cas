package com.jefflee.entity.schedule;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Period;
import com.jefflee.entity.information.Room;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.po.schedule.ArrangementPo;

public class Arrangement {
	public Integer arrangementId;
	public Integer scheduleId;
	public Period period;
	public Course course;
	public Room room;
	public Tclass tclass;
	public Teacher teacher;
	public Integer arranged;
	public Integer priority;

	public Arrangement() {
		this.period = new Period();
		this.course = new Course();
		this.room = new Room();
		this.tclass = new Tclass();
		this.teacher = new Teacher();
	}

	public Arrangement(Integer arrangementId) {
		this.arrangementId = arrangementId;
		this.period = new Period();
		this.course = new Course();
		this.room = new Room();
		this.tclass = new Tclass();
		this.teacher = new Teacher();
	}

	public Arrangement(ArrangementPo arrangementPo) {
		arrangementId = arrangementPo.getArrangementId();
		scheduleId = arrangementPo.getScheduleId();
		period = new Period(arrangementPo.getPeriodId());
		course = new Course(arrangementPo.getCourseId());
		room = new Room(arrangementPo.getRoomId());
		tclass = new Tclass(arrangementPo.getTclassId());
		teacher = new Teacher(arrangementPo.getTeacherId());
		arranged = arrangementPo.getArranged();
		priority = arrangementPo.getPriority();
	}

	public ArrangementPo toPo() {
		ArrangementPo arrangementPo = new ArrangementPo();
		arrangementPo.setArrangementId(arrangementId);
		arrangementPo.setScheduleId(scheduleId);
		arrangementPo.setPeriodId(period.periodId);
		arrangementPo.setCourseId(course.courseId);
		arrangementPo.setRoomId(room.roomId);
		arrangementPo.setTclassId(tclass.tclassId);
		arrangementPo.setTeacherId(teacher.teacherId);
		arrangementPo.setArranged(arranged);
		arrangementPo.setPriority(priority);
		return arrangementPo;
	}
}