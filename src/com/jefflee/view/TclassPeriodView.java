package com.jefflee.view;

import com.jefflee.entity.schedule.Arrangement;

public class TclassPeriodView extends PeriodView {
	private Integer tclassId;
	private Arrangement arrangement;

	public Arrangement getArrangement() {
		return arrangement;
	}

	public void setArrangement(Arrangement arrangement) {
		this.arrangement = arrangement;
	}

	public Integer getTclassId() {
		return tclassId;
	}

	public void setTclassId(Integer tclassId) {
		this.tclassId = tclassId;
	}
}
