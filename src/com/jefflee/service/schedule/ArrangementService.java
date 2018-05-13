package com.jefflee.service.schedule;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.view.ScheduleView;

public interface ArrangementService {

	Integer insert(Arrangement arrangement);

	List<Arrangement> selectList();

	Arrangement selectById(Integer arrangementId);

	Integer updateById(Arrangement arrangement);

	Integer deleteById(Integer arrangementId);

	void deleteByScheduleId(Integer scheduleId);

	void gnrArrangementList(Integer scheduleId);

	Integer insertList(List<Arrangement> arrangementList);

	void initial(Integer scheduleId);

	ScheduleView gnrScheduleView(Integer scheduleId) throws Exception;

	void doAdjust(Integer adjustmentId);

	void undoAdjust(Integer adjustmentId);

	Map<String, Map<String, Integer>> getBackgroundMap(Integer scheduleId);

	void saveAdjustment(Integer scheduleId);

	void resetList(Integer scheduleId);

	void copyListByScheduleId(Integer srcScheduleId, Integer destScheduleId);

	void gnrEmptyArrangementList(Integer scheduleId);

	void exportExcelView(HttpServletResponse response, Integer scheduleId) throws Exception;

}
