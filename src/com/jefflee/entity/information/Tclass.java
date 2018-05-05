package com.jefflee.entity.information;

import java.util.Calendar;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name = "info_tclass")
public class Tclass {
	@Id
	// TODO 定好顺序
	@OrderBy
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tclassId;
	@OrderBy
	private String tclassNo;
	@Transient
	private String name;
	@Transient
	private String shortName;
	private Integer type;
	private Integer year;
	private Integer level;

	public Integer getTclassId() {
		return tclassId;
	}

	public void setTclassId(Integer tclassId) {
		this.tclassId = tclassId;
	}

	public String getTclassNo() {
		return tclassNo;
	}

	public void setTclassNo(String tclassNo) {
		this.tclassNo = tclassNo;
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
		name.append(this.tclassNo);
		name.append("班");
		return name.toString();
	}

	public String getShortName() {
		StringBuilder name = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		if (month < Calendar.AUGUST) {
			year -= 1;
		}
		switch (year - this.year) {
		case 0:
			name.append("-");
			break;
		case 1:
			name.append("=");
			break;
		case 2:
			name.append("三");
			break;
		default:
			name.append("?");
			break;
		}
		name.append(this.tclassNo);
		return name.toString();
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
}
