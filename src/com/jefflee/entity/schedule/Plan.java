package com.jefflee.entity.schedule;

import com.jefflee.po.schedule.PlanPo;

public class Plan {
	public Integer planId;
	public Relation relation;
	public Integer periodNum;

	public Plan() {
		this.relation = new Relation();
	}

	public Plan(Integer planId) {
		this.planId = planId;
		this.relation = new Relation();
	}

	public Plan(PlanPo planPo) {
		planId = planPo.getPlanId();
		relation = new Relation(planPo.getRelationId());
		periodNum = planPo.getPeriodNum();
	}

	public PlanPo toPo() {
		PlanPo planPo = new PlanPo();
		planPo.setPlanId(planId);
		planPo.setRelationId(relation.relationId);
		planPo.setPeriodNum(periodNum);
		return planPo;
	}
}
