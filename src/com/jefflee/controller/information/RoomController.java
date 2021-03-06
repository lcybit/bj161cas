package com.jefflee.controller.information;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jefflee.entity.information.Room;
import com.jefflee.service.information.RoomService;

@RestController
@RequestMapping(value = "/room")
public class RoomController {

	@Resource(name = "roomService")
	RoomService roomService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, String> create(@RequestBody Room room) {
		Map<String, String> result = new HashMap<String, String>();
		Integer roomId = roomService.insert(room);
		if (roomId != null) {
			result.put("roomId", roomId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Room> listAll() {
		return roomService.selectList();
	}

	@RequestMapping(value = "/find/{roomId}", method = RequestMethod.GET)
	public Room findById(@PathVariable("roomId") Integer roomId) {
		return roomService.selectById(roomId);
	}

	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public Map<String, String> modify(@RequestBody Room room) {
		Map<String, String> result = new HashMap<String, String>();
		Integer roomId = roomService.updateById(room);
		if (roomId != null) {
			result.put("roomId", roomId.toString());
		}
		return result;
	}

	@RequestMapping(value = "/delete/{roomId}", method = RequestMethod.DELETE)
	public Map<String, String> delete(@PathVariable("roomId") Integer roomId) {
		Map<String, String> result = new HashMap<String, String>();
		roomId = roomService.deleteById(roomId);
		if (roomId != null) {
			result.put("roomId", roomId.toString());
		}
		return result;
	}
}
