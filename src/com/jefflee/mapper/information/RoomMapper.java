package com.jefflee.mapper.information;

import org.springframework.stereotype.Repository;

import com.jefflee.po.information.RoomPo;

import tk.mybatis.mapper.common.Mapper;

@Repository("roomMapper")
public interface RoomMapper extends Mapper<RoomPo> {

}
