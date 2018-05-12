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

		Integer periodId1 = this.arrangement.getPeriodId();
		Integer courseId1 = this.arrangement.getCourseId();
		Integer courseType = this.arrangement.getCourse().getType();
		Integer tclassId1 = this.arrangement.getTclassId();
		Integer teacherId1 = this.arrangement.getTeacherId();

		Integer periodId2 = arrangement.getPeriodId();
		Integer courseId2 = arrangement.getCourseId();
		Integer tclassId2 = arrangement.getTclassId();
		Integer teacherId2 = arrangement.getTeacherId();

		if (periodId1 != periodId2) {
			return false;
		}
		if (Objects.equals(courseId1, courseId2)) {
			// TODO type 23 24 25
			if (courseType == 4) {
				return false;
			}
		}
		if (Objects.equals(tclassId1, tclassId2)) {
			return true;
		}
		if (Objects.equals(teacherId1, teacherId2)) {
			return true;
		}
		return false;
	}

}
