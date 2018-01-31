package com.jefflee.entity.schedule;

import com.jefflee.entity.information.Course;
import com.jefflee.entity.information.Room;
import com.jefflee.entity.information.Tclass;
import com.jefflee.entity.information.Teacher;

public class Relation {
	public Integer relationId;
	public Schedule schedule;
	public Course course;
	public Room room;
	public Tclass tclass;
	public Teacher teacher;
}
