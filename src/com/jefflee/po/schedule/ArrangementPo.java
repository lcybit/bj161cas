package com.jefflee.po.schedule;

import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "schd_arrangement")
public class ArrangementPo {
	@Id
	private String arrangementId;
	private Integer periodId;
	private Integer relationId;
	private Integer arranged;
	private Integer priority;

	public String getArrangementId() {
		return arrangementId;
	}

	public void setArrangementId(String arrangementId) {
		this.arrangementId = arrangementId;
	}

	public Integer getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
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

	public boolean isPeriodConflictive(ArrangementPo arrangementPo) {
		if (periodId == arrangementPo.periodId) {
			return false;
		} else {
			return true;
		}
	}

}