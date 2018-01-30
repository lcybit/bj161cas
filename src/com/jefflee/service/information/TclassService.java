package com.jefflee.service.information;

import java.util.List;

import com.jefflee.dto.information.TclassDto;

public interface TclassService {

	public Integer create(TclassDto tclassDto);

	public List<TclassDto> listAll();

	public TclassDto findById(Integer tclassId);

	public Integer modify(TclassDto tclassDto);

	public Integer delete(Integer tclassId);

}
