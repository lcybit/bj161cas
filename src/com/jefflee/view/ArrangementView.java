package com.jefflee.view;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Period;
import com.jefflee.entity.information.Room;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;

public class ArrangementView {
	private Integer arrangementId;
	private Period period;
	private Course course;
	private Room room;
	private Tclass tclass;
	private Teacher teacher;
	private Integer arranged;
	private Integer priority;

	public ArrangementView() {
		period = new Period();
		course = new Course();
		room = new Room();
		tclass = new Tclass();
		teacher = new Teacher();
	}

	public Integer getArrangementId() {
		return arrangementId;
	}

	public void setArrangementId(Integer arrangementId) {
		this.arrangementId = arrangementId;
	}

	public Integer getArranged() {
		return arranged;
	}

	public void setArranged(Integer arranged) {
		this.arranged = arranged;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Period getPeriod() {
		return period;
	}

	public void setPeriod(Period period) {
		this.period = period;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Tclass getTclass() {
		return tclass;
	}

	public void setTclass(Tclass tclass) {
		this.tclass = tclass;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

}
