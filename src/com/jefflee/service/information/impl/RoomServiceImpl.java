package com.jefflee.service.information.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jefflee.entity.information.Room;
import com.jefflee.mapper.information.RoomMapper;
import com.jefflee.service.information.RoomService;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

	@Resource(name = "roomMapper")
	private RoomMapper roomMapper;

	@Override
	public Integer insert(Room room) {
		if (roomMapper.insert(room) == 1) {
			return room.getRoomId();
		} else {
			return null;
		}
	}

	@Override
	public List<Room> selectList() {
		return roomMapper.selectAll();
	}

	@Override
	public Room selectById(Integer roomId) {
		return roomMapper.selectByPrimaryKey(roomId);
	}

	@Override
	public Integer updateById(Room room) {
		if (roomMapper.updateByPrimaryKey(room) == 1) {
			return room.getRoomId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer roomId) {
		if (roomMapper.deleteByPrimaryKey(roomId) == 1) {
			return roomId;
		} else {
			return null;
		}
	}

}
