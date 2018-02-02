package com.jefflee.po.information;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Table(name = "info_tclass")
public class TclassPo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tclassId;
	@OrderBy
	private String tclassNo;
	private String name;
	private Integer type;
	private Integer year;
	private Integer level;
	private Integer semester;
	private Integer grade;

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
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

}
