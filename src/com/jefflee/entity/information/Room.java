package com.jefflee.entity.information;

import com.jefflee.po.information.RoomPo;

public class Room {
	public Integer roomId;
	public String roomNo;
	public String name;
	public Integer type;

	public Room() {
	}

	public Room(Integer roomId) {
		this.roomId = roomId;
	}

	public Room(RoomPo roomPo) {
		roomId = roomPo.getRoomId();
		roomNo = roomPo.getRoomNo();
		name = roomPo.getName();
		type = roomPo.getType();
	}

	public RoomPo toPo() {
		RoomPo roomPo = new RoomPo();
		roomPo.setRoomId(roomId);
		roomPo.setRoomNo(roomNo);
		roomPo.setName(name);
		roomPo.setType(type);
		return roomPo;
	}
}
