package com.jefflee.service.information;

import java.util.List;

import com.jefflee.dto.information.PeriodDto;

public interface PeriodService {

	public Integer create(PeriodDto periodDto);

	public List<PeriodDto> listAll();

	public PeriodDto findById(Integer periodId);

	public Integer modify(PeriodDto periodDto);

	public Integer delete(Integer periodId);

}
