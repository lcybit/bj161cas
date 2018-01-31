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
}
