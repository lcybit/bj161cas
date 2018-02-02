package com.jefflee.entity.information;

import com.jefflee.po.information.TclassPo;

public class Tclass {
	public Integer tclassId;
	public String tclassNo;
	public String name;
	public Integer type;
	public Integer year;
	public Integer level;

	public Tclass() {
	}

	public Tclass(Integer tclassId) {
		this.tclassId = tclassId;
	}

	public Tclass(TclassPo tclassPo) {
		tclassId = tclassPo.getTclassId();
		tclassNo = tclassPo.getTclassNo();
	}

	public TclassPo toPo() {
		TclassPo tclassPo = new TclassPo();
		tclassPo.setTclassId(tclassId);
		tclassPo.setTclassNo(tclassNo);
		return tclassPo;
	}
}
