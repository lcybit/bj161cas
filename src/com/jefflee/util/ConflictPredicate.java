package com.jefflee.util;

import java.util.Objects;

import org.apache.commons.collections4.Predicate;

import com.jefflee.entity.schedule.Arrangement;

public class ConflictPredicate implements Predicate<Arrangement> {

	private Arrangement arrangement;

	public ConflictPredicate(Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	// TODO 待优化
	@Override
	public boolean evaluate(Arrangement arrangement) {

		if (this.arrangement.getArrangementId() != null && (arrangement.getArrangementId() == null
				|| !Objects.equals(this.arrangement.getArrangementId(), arrangement.getArrangementId()))) {
			return false;
		}
		if (this.arrangement.getPeriod().getPeriodId() != null && (arrangement.getPeriod().getPeriodId() == null
				|| this.arrangement.getPeriod().getPeriodId() != arrangement.getPeriod().getPeriodId())) {
			return false;
		}
		if (this.arrangement.getCourse().getCourseId() != null && (arrangement.getCourse().getCourseId() == null
				|| this.arrangement.getCourse().getCourseId() != arrangement.getCourse().getCourseId())) {
			return false;
		}
		if (this.arrangement.getRoom().getRoomId() != null && (arrangement.getRoom().getRoomId() == null
				|| this.arrangement.getRoom().getRoomId() != arrangement.getRoom().getRoomId())) {
			return false;
		}
		if (this.arrangement.getTclass().getTclassId() != null && (arrangement.getTclass().getTclassId() == null
				|| this.arrangement.getTclass().getTclassId() != arrangement.getTclass().getTclassId())) {
			return false;
		}
		if (this.arrangement.getTeacher().getTeacherId() != null && (arrangement.getTeacher().getTeacherId() == null
				|| this.arrangement.getTeacher().getTeacherId() != arrangement.getTeacher().getTeacherId())) {
			return false;
		}
		if (this.arrangement.getArranged() != null
				&& (arrangement.getArranged() == null || this.arrangement.getArranged() != arrangement.getArranged())) {
			return false;
		}
		if (this.arrangement.getPriority() != null
				&& (arrangement.getPriority() == null || this.arrangement.getPriority() != arrangement.getPriority())) {
			return false;
		}
		return true;
	}

}
