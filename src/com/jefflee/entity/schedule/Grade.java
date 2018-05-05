package com.jefflee.entity.schedule;

import java.util.Calendar;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "schd_grade")
public class Grade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gradeId;
	private Integer year;
	private Integer semester;
	private Integer level;
	@OrderBy
	private String startDate;

	@Transient
	private String name;

	public Integer getGradeId() {
		return gradeId;
	}

	public void setGradeId(Integer gradeId) {
		this.gradeId = gradeId;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getName() {
		StringBuilder name = new StringBuilder();
		switch (this.level) {
		case 0:
			name.append("初");
			break;
		case 1:
			name.append("高");
			break;
		default:
			break;
		}
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		if (month < Calendar.AUGUST) {
			year -= 1;
		}
		switch (year - this.year) {
		case 0:
			name.append("一");
			break;
		case 1:
			name.append("二");
			break;
		case 2:
			name.append("三");
			break;
		default:
			name.insert(0, "级");
			name.insert(0, this.year);
			name.append("中");
			break;
		}
		return name.toString();
	}

}
