package com.jefflee.entity.schedule;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "schd_relation")
public class Relation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer relationId;
	private Integer scheduleId;
	private Integer courseId;
	private Integer roomId;
	private Integer tclassId;
	private Integer teacherId;

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public Integer getTclassId() {
		return tclassId;
	}

	public void setTclassId(Integer tclassId) {
		this.tclassId = tclassId;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public boolean isConflictive(Relation relation) {
		if (isSame(relation)) {
			return false;
		} else if (courseId == relation.courseId) {
			return true;
		} else if (roomId == relation.roomId) {
			return true;
		} else if (tclassId == relation.tclassId) {
			return true;
		} else if (teacherId == relation.teacherId) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isSame(Relation relation) {
		if (courseId != relation.courseId) {
			return false;
		} else if (roomId != relation.roomId) {
			return false;
		} else if (tclassId == relation.tclassId) {
			return false;
		} else if (teacherId == relation.teacherId) {
			return false;
		} else {
			return true;
		}
	}

}
