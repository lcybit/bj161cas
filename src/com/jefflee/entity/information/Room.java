package com.jefflee.entity.information;

import com.jefflee.po.information.RoomPo;

public class Room {
	public Integer roomId;
	public String roomNo;

	public Room() {
	}

	public Room(Integer roomId) {
		this.roomId = roomId;
	}

	public Room(RoomPo roomPo) {
		roomId = roomPo.getRoomId();
		roomNo = roomPo.getRoomNo();
	}

	public RoomPo toPo() {
		RoomPo roomPo = new RoomPo();
		roomPo.setRoomId(roomId);
		roomPo.setRoomNo(roomNo);
		return roomPo;
	}
}
