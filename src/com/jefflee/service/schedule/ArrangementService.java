package com.jefflee.service.schedule;

import java.util.List;

import com.jefflee.dto.schedule.ArrangementDto;

public interface ArrangementService {

	public String create(ArrangementDto arrangementDto);

	public List<ArrangementDto> listAll();

	public ArrangementDto findById(Integer arrangementId);

	public String modify(ArrangementDto arrangementDto);

	public String delete(String arrangementId);

}
