package com.jefflee.util;

import java.util.Objects;

import org.apache.commons.collections4.Predicate;

import com.jefflee.entity.schedule.Plan;

public class PlanPredicate implements Predicate<Plan> {

	private Plan plan;

	public PlanPredicate(Plan plan) {
		this.plan = plan;
	}

	// TODO 待优化
	@Override
	public boolean evaluate(Plan plan) {
		Integer planId1 = this.plan.getPlanId();
		Integer courseId1 = this.plan.getCourse().getCourseId();
		Integer roomId1 = this.plan.getRoom().getRoomId();
		Integer tclassId1 = this.plan.getTclass().getTclassId();
		Integer teacherId1 = this.plan.getTeacher().getTeacherId();
		Integer periodNum1 = this.plan.getPeriodNum();

		Integer planId2 = plan.getPlanId();
		Integer courseId2 = plan.getCourse().getCourseId();
		Integer roomId2 = plan.getRoom().getRoomId();
		Integer tclassId2 = plan.getTclass().getTclassId();
		Integer teacherId2 = plan.getTeacher().getTeacherId();
		Integer periodNum2 = plan.getPeriodNum();

		if (planId1 != null && !Objects.equals(planId1, planId2)) {
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
		if (periodNum1 != null && !Objects.equals(periodNum1, periodNum2)) {
			return false;
		}
		return true;
	}

}
