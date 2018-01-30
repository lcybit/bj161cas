package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.entity.information.Room;

import tk.mybatis.mapper.common.Mapper;

@Repository("roomMapper")
public interface RoomMapper extends Mapper<Room> {

}
