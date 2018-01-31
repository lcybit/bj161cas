package com.jefflee.po.schedule;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "schd_relation")
public class RelationPo {
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

	public boolean isConflictive(RelationPo relationPo) {
		if (isSame(relationPo)) {
			return false;
		} else if (courseId == relationPo.courseId) {
			return true;
		} else if (roomId == relationPo.roomId) {
			return true;
		} else if (tclassId == relationPo.tclassId) {
			return true;
		} else if (teacherId == relationPo.teacherId) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isSame(RelationPo relationPo) {
		if (courseId != relationPo.courseId) {
			return false;
		} else if (roomId != relationPo.roomId) {
			return false;
		} else if (tclassId == relationPo.tclassId) {
			return false;
		} else if (teacherId == relationPo.teacherId) {
			return false;
		} else {
			return true;
		}
	}

}
