package com.jefflee.po.schedule;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Table(name = "schd_adjustment")
public class AdjustmentPo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer adjustmentId;
	private Integer scheduleId;
	@OrderBy
	private Integer startWeek;
	private Integer duration;
	private Integer type;
	// TODO 合并后修改数据库
	@Column(name = "first_id")
	private String position;
	@Column(name = "second_id")
	private String additionalInfo;

	public Integer getAdjustmentId() {
		return adjustmentId;
	}

	public void setAdjustmentId(Integer adjustmentId) {
		this.adjustmentId = adjustmentId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getStartWeek() {
		return startWeek;
	}

	public void setStartWeek(Integer startWeek) {
		this.startWeek = startWeek;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String postion) {
		this.position = postion;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}
