package com.jefflee.util;

import java.util.Objects;//出错是因为jdk版本低

import org.apache.commons.collections4.Predicate;

import com.jefflee.entity.schedule.Arrangement;

public class ArrangementPredicate implements Predicate<Arrangement> {

	private Arrangement arrangement;

	public ArrangementPredicate(Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	// TODO 待优化
	@Override
	public boolean evaluate(Arrangement arrangement) {
		Integer arrangementId1 = this.arrangement.getArrangementId();
		Integer periodId1 = this.arrangement.getPeriodId();
		Integer courseId1 = this.arrangement.getCourseId();
		Integer roomId1 = this.arrangement.getRoomId();
		Integer tclassId1 = this.arrangement.getTclassId();
		Integer teacherId1 = this.arrangement.getTeacherId();
		Integer arranged1 = this.arrangement.getArranged();
		Integer priority1 = this.arrangement.getPriority();

		Integer arrangementId2 = arrangement.getArrangementId();
		Integer periodId2 = arrangement.getPeriodId();
		Integer courseId2 = arrangement.getCourseId();
		Integer roomId2 = arrangement.getRoomId();
		Integer tclassId2 = arrangement.getTclassId();
		Integer teacherId2 = arrangement.getTeacherId();
		Integer arranged2 = arrangement.getArranged();
		Integer priority2 = arrangement.getPriority();

		if (arrangementId1 != null && !Objects.equals(arrangementId1, arrangementId2)) {
			return false;
		}
		if (periodId1 != null && !Objects.equals(periodId1, periodId2)) {
			return false;
		}
		if (courseId1 != null && !Objects.equals(courseId1, courseId2)) {
			return false;
		}
		if (roomId1 != null && !Objects.equals(roomId1, roomId2)) {
			return false;
		}
		if (tclassId1 != null && !Objects.equals(tclassId1, tclassId2)) {
			return false;
		}
		if (teacherId1 != null && !Objects.equals(teacherId1, teacherId2)) {
			return false;
		}
		if (arranged1 != null && !Objects.equals(arranged1, arranged2)) {
			return false;
		}
		if (priority1 != null && !Objects.equals(priority1, priority2)) {
			return false;
		}
		return true;
	}

}
