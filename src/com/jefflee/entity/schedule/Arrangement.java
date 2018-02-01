package com.jefflee.entity.schedule;

import com.jefflee.entity.information.Period;
import com.jefflee.po.schedule.ArrangementPo;

public class Arrangement {
	public String arrangementId;
	public Period period;
	public Relation relation;
	public Integer arranged;
	public Integer priority;

	public Arrangement() {
		this.period = new Period();
		this.relation = new Relation();
	}

	public Arrangement(String arrangementId) {
		this.arrangementId = arrangementId;
		this.period = new Period();
		this.relation = new Relation();
	}

	public Arrangement(ArrangementPo arrangementPo) {
		arrangementId = arrangementPo.getArrangementId();
		period = new Period(arrangementPo.getPeriodId());
		relation = new Relation(arrangementPo.getRelationId());
		arranged = arrangementPo.getArranged();
		priority = arrangementPo.getPriority();
	}

	public ArrangementPo toPo() {
		ArrangementPo arrangementPo = new ArrangementPo();
		arrangementPo.setArrangementId(arrangementId);
		arrangementPo.setPeriodId(period.periodId);
		arrangementPo.setRelationId(relation.relationId);
		arrangementPo.setArranged(arranged);
		arrangementPo.setPriority(priority);
		return arrangementPo;
	}
}