package com.jefflee.service.information;

import java.util.List;

import com.jefflee.entity.information.Room;

public interface RoomService {

	Integer insert(Room room);

	List<Room> selectList();

	Room selectById(Integer roomId);

	Integer updateById(Room room);

	Integer deleteById(Integer roomId);

}
