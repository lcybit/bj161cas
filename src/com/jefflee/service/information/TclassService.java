package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Tclass;
import com.jefflee.po.information.TclassPo;

public interface TclassService {

	public Integer insert(TclassPo tclassPo);

	public List<TclassPo> selectAll();

	public TclassPo selectById(Integer tclassId);

	public Integer updateById(TclassPo tclassPo);

	public Integer deleteById(Integer tclassId);

	public String gnrName(Tclass tclass);

	public String gnrShortName(Tclass tclass);
}
