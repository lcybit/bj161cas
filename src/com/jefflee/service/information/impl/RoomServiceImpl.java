package com.jefflee.service.information.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.dto.information.RoomDto;
import com.jefflee.entity.information.Room;
import com.jefflee.mapper.information.RoomMapper;
import com.jefflee.service.information.RoomService;
import com.jefflee.util.BeanUtil;

import tk.mybatis.mapper.entity.Example;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

	@Resource(name = "roomMapper")
	private RoomMapper roomMapper;

	@Override
	public Integer create(RoomDto roomDto) {
		Room room = new Room();
		BeanUtil.copyProperties(roomDto, room);
		if (roomMapper.insert(room) == 1) {
			return room.getRoomId();
		} else {
			return null;
		}
	}

	@Override
	public List<RoomDto> listAll() {
		Example example = new Example(Room.class);
		example.setOrderByClause("room_no ASC");
		List<Room> roomList = roomMapper.selectByExample(example);
		List<RoomDto> roomDtoList = new ArrayList<RoomDto>();
		for (Room room : roomList) {
			RoomDto roomDto = new RoomDto();
			BeanUtil.copyPropertiesSelective(room, roomDto);
			roomDtoList.add(roomDto);
		}
		return roomDtoList;
	}

	@Override
	public RoomDto findById(Integer roomId) {
		RoomDto roomDto = new RoomDto();
		Room room = roomMapper.selectByPrimaryKey(roomId);
		BeanUtil.copyProperties(room, roomDto);
		return roomDto;
	}

	@Override
	public Integer modify(RoomDto roomDto) {
		Room room = new Room();
		BeanUtil.copyProperties(roomDto, room);
		if (roomMapper.updateByPrimaryKey(room) == 1) {
			return room.getRoomId();
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
