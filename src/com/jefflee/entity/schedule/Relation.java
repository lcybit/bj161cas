package com.jefflee.entity.schedule;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Room;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;
import com.jefflee.po.schedule.RelationPo;

public class Relation {
	public Integer relationId;
	public Schedule schedule;
	public Course course;
	public Room room;
	public Tclass tclass;
	public Teacher teacher;

	public Relation() {
		this.schedule = new Schedule();
		this.course = new Course();
		this.room = new Room();
		this.tclass = new Tclass();
		this.teacher = new Teacher();
	}

	public Relation(Integer relationId) {
		this.relationId = relationId;
		this.schedule = new Schedule();
		this.course = new Course();
		this.room = new Room();
		this.tclass = new Tclass();
		this.teacher = new Teacher();
	}

	public Relation(RelationPo relationPo) {
		relationId = relationPo.getRelationId();
		schedule = new Schedule(relationPo.getScheduleId());
		course = new Course(relationPo.getCourseId());
		room = new Room(relationPo.getRoomId());
		tclass = new Tclass(relationPo.getTclassId());
		teacher = new Teacher(relationPo.getTeacherId());
	}

	public RelationPo toPo() {
		RelationPo relationPo = new RelationPo();
		relationPo.setRelationId(relationId);
		relationPo.setScheduleId(schedule.scheduleId);
		relationPo.setCourseId(course.courseId);
		relationPo.setRoomId(room.roomId);
		relationPo.setTclassId(tclass.tclassId);
		relationPo.setTeacherId(teacher.teacherId);
		return relationPo;
	}
}
