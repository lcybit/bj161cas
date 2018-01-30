package com.jefflee.service.information;

import java.util.List;

import com.jefflee.dto.information.RoomDto;

public interface RoomService {

	public Integer create(RoomDto roomDto);

	public List<RoomDto> listAll();

	public RoomDto findById(Integer roomId);

	public Integer modify(RoomDto roomDto);

	public Integer delete(Integer roomId);

}
