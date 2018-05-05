package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Room;
import com.jefflee.util.MyMapper;

@Repository("roomMapper")
public interface RoomMapper extends MyMapper<Room> {

}
