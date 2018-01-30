package com.jefflee.entity.information;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "info_room")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer roomId;

	private String roomNo;

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}
}
