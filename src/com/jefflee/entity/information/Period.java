package com.jefflee.entity.information;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Table(name = "info_period")
public class Period {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@OrderBy
	private Integer periodId;
	private String periodNo;
	private Integer dayOfWeek;
	private Integer orderOfDay;

	public Integer getPeriodId() {
		return periodId;
	}

	public void setPeriodId(Integer periodId) {
		this.periodId = periodId;
	}

	public String getPeriodNo() {
		return periodNo;
	}

	public void setPeriodNo(String periodNo) {
		this.periodNo = periodNo;
	}

	public Integer getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(Integer dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public Integer getOrderOfDay() {
		return orderOfDay;
	}

	public void setOrderOfDay(Integer orderOfDay) {
		this.orderOfDay = orderOfDay;
	}
}
