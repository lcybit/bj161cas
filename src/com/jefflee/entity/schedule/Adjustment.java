package com.jefflee.entity.schedule;

public class Adjustment {
	private Integer adjustmentId;
	private Integer scheduleId;
	private Integer startWeek;
	private Integer duration;
	private Integer type;
	private String position;
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

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

}
