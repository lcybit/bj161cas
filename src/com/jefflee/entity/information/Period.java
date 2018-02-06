package com.jefflee.entity.information;

import com.jefflee.po.information.PeriodPo;

public class Period {
	public Integer periodId;
	public String periodNo;
	public Integer dayOfWeek;
	public Integer orderOfDay;

	public Period() {
	}

	public Period(Integer periodId) {
		this.periodId = periodId;
	}

	public Period(PeriodPo periodPo) {
		periodId = periodPo.getPeriodId();
		periodNo = periodPo.getPeriodNo();
		dayOfWeek = periodPo.getDayOfWeek();
		orderOfDay = periodPo.getOrderOfDay();

	}

	public PeriodPo toPo() {
		PeriodPo periodPo = new PeriodPo();
		periodPo.setPeriodId(periodId);
		periodPo.setPeriodNo(periodNo);
		periodPo.setDayOfWeek(dayOfWeek);
		periodPo.setOrderOfDay(orderOfDay);
		return periodPo;
	}
}
