package com.jefflee.service.information;

import java.util.List;

import com.jefflee.po.information.RoomPo;

public interface RoomService {

	public Integer insert(RoomPo roomPo);

	public List<RoomPo> selectAll();

	public RoomPo selectById(Integer roomId);

	public Integer updateById(RoomPo roomPo);

	public Integer deleteById(Integer roomId);

}
