package com.jefflee.po.information;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Table(name = "info_tclass")
public class TclassPo {
	@Id
	// TODO 定好顺序
	@OrderBy
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer tclassId;
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
