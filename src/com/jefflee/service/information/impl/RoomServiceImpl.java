package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.RoomDto;
import com.jefflee.mapper.information.RoomMapper;
import com.jefflee.po.information.RoomPo;
import com.jefflee.service.information.RoomService;
import com.jefflee.util.BeanUtil;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

	@Resource(name = "roomMapper")
	private RoomMapper roomMapper;

	@Override
	public Integer create(RoomDto roomDto) {
		RoomPo roomPo = new RoomPo();
		BeanUtil.copyProperties(roomDto, roomPo);
		if (roomMapper.insert(roomPo) == 1) {
			return roomPo.getRoomId();
		} else {
			return null;
		}
	}

	@Override
	public List<RoomDto> listAll() {
		List<RoomPo> roomList = roomMapper.selectAll();
		List<RoomDto> roomDtoList = new ArrayList<RoomDto>();
		for (RoomPo roomPo : roomList) {
			RoomDto roomDto = new RoomDto();
			BeanUtil.copyPropertiesSelective(roomPo, roomDto);
			roomDtoList.add(roomDto);
		}
		return roomDtoList;
	}

	@Override
	public RoomDto findById(Integer roomId) {
		RoomDto roomDto = new RoomDto();
		RoomPo roomPo = roomMapper.selectByPrimaryKey(roomId);
		BeanUtil.copyProperties(roomPo, roomDto);
		return roomDto;
	}

	@Override
	public Integer modify(RoomDto roomDto) {
		RoomPo roomPo = new RoomPo();
		BeanUtil.copyProperties(roomDto, roomPo);
		if (roomMapper.updateByPrimaryKey(roomPo) == 1) {
			return roomPo.getRoomId();
		} else {
			return null;
		}
	}

	@Override
	public Integer delete(Integer roomId) {
		if (roomMapper.deleteByPrimaryKey(roomId) == 1) {
			return roomId;
		} else {
			return null;
		}
	}
}
