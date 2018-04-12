package com.jefflee.service.schedule.impl;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.jefflee.entity.schedule.Arrangement;
import com.jefflee.entity.schedule.Group;
import com.jefflee.entity.schedule.Schedule;
import com.jefflee.mapper.schedule.ScheduleMapper;
import com.jefflee.po.information.PeriodPo;
import com.jefflee.po.schedule.ArrangementPo;
import com.jefflee.po.schedule.PlanPo;
import com.jefflee.po.schedule.SchedulePo;
import com.jefflee.service.information.CourseService;
import com.jefflee.service.information.PeriodService;
import com.jefflee.service.information.TclassService;
import com.jefflee.service.information.TeacherService;
import com.jefflee.service.schedule.ArrangementService;
import com.jefflee.service.schedule.GroupService;
import com.jefflee.service.schedule.PlanService;
import com.jefflee.service.schedule.ScheduleService;
import com.jefflee.view.ScheduleView;
import com.jefflee.view.WeekView;

@Service("scheduleService")
public class ScheduleServiceImpl implements ScheduleService {

	@Resource(name = "scheduleMapper")
	private ScheduleMapper scheduleMapper;

	@Resource(name = "planService")
	private PlanService planService;
	@Resource(name = "arrangementService")
	private ArrangementService arrangementService;
	@Resource(name = "groupService")
	private GroupService groupService;

	@Resource(name = "periodService")
	private PeriodService periodService;
	@Resource(name = "courseService")
	private CourseService courseService;
	@Resource(name = "tclassService")
	private TclassService tclassService;
	@Resource(name = "teacherService")
	private TeacherService teacherService;

	@Override
	public Integer insert(SchedulePo schedulePo) {
		if (scheduleMapper.insert(schedulePo) == 1) {
			return schedulePo.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public List<SchedulePo> selectAll() {
		return scheduleMapper.selectAll();
	}

	@Override
	public SchedulePo selectById(Integer scheduleId) {
		return scheduleMapper.selectByPrimaryKey(scheduleId);
	}

	@Override
	public Integer updateById(SchedulePo schedulePo) {
		if (scheduleMapper.updateByPrimaryKey(schedulePo) == 1) {
			return schedulePo.getScheduleId();
		} else {
			return null;
		}
	}

	@Override
	public Integer deleteById(Integer scheduleId) {
		if (scheduleMapper.deleteByPrimaryKey(scheduleId) == 1) {
			return scheduleId;
		} else {
			return null;
		}
	}

	@Override
	public ScheduleView gnrScheduleView(Integer scheduleId) {
		return arrangementService.gnrScheduleView(scheduleMapper.selectEntityById(scheduleId));
	}

	@Override
	public void gnrEmptyArrangementList(Integer scheduleId) {
		// TODO 根据课表上午、下午、晚上课时数生成课时列表，非selectAll
		List<PeriodPo> periodPoList = periodService.selectAll();
		List<PlanPo> planPoList = planService.selectAll();
		for (PlanPo planPo : planPoList) {
			for (PeriodPo periodPo : periodPoList) {
				ArrangementPo arrangementPo = new ArrangementPo();
				arrangementPo.setScheduleId(scheduleId);
				arrangementPo.setPeriodId(periodPo.getPeriodId());
				arrangementPo.setCourseId(planPo.getCourseId());
				arrangementPo.setRoomId(planPo.getRoomId());
				arrangementPo.setTclassId(planPo.getTclassId());
				arrangementPo.setTeacherId(planPo.getTeacherId());
				arrangementPo.setArranged(0);
				arrangementPo.setPriority(2);
				arrangementService.insert(arrangementPo);
			}
		}
	}

	@Override
	public void gnrSchedule(Integer scheduleId) {
		arrangementService.gnrArrangementList(scheduleId);
	}

	@Override
	public void gnrScheduleViewExcel(HttpServletResponse response, Integer scheduleId) throws Exception {
		// TODO 周几用汉字表示
		ScheduleView scheduleView = gnrScheduleView(scheduleId);
		Schedule schedule = scheduleView.getSchedule();
		List<WeekView> tclassWeekViewList = new ArrayList<WeekView>(scheduleView.getTclassWeekViewMap().values());
		List<WeekView> teacherWeekViewList = new ArrayList<WeekView>(scheduleView.getTeacherWeekViewMap().values());

		Workbook workbook = new XSSFWorkbook();
		Sheet tclassSheet = workbook.createSheet("班级课表");
		Sheet teacherSheet = workbook.createSheet("教师课表");
		Row tclassRow = null;
		Row teacherRow = null;
		Cell tclassCell = null;
		Cell teacherCell = null;

		// 用于计算单课表大小以及合并单元格
		int daysPerWeek = schedule.getDays();
		int forenoon = schedule.getForenoon();
		int afternoon = schedule.getAfternoon();
		int evening = schedule.getEvening();
		int periodsPerDay = forenoon + afternoon + evening;
		// 单课表行数
		int weekViewRows = daysPerWeek + 3;
		// 单课表列数
		int weekViewCols = periodsPerDay + 1;

		// 每行展示多少课表
		int weekViewColNum = 3;
		// 课表间上下间隔
		int blankIntervalRows = 2;
		// 课表间左右间隔
		int blankIntervalCols = 2;
		// 班级课表数量
		int tclassNum = tclassWeekViewList.size();
		// 教师课表数量
		int teacherNum = teacherWeekViewList.size();

		// 多少行班级课表
		int tclassWeekViewRowNum = tclassNum % weekViewColNum == 0 ? tclassNum / weekViewColNum
				: tclassNum / weekViewColNum + 1;
		// 多少列教师课表
		int teacherWeekViewRowNum = teacherNum % weekViewColNum == 0 ? teacherNum / weekViewColNum
				: teacherNum / weekViewColNum + 1;

		// 单课表行数（含间隔）
		int blockRows = weekViewRows + blankIntervalRows;
		// 单课表列数（含间隔）
		int blockCols = weekViewCols + blankIntervalCols;

		// 班级课表总行数
		int tclassScheduleViewRows = blockRows * tclassWeekViewRowNum - blankIntervalRows;
		// 教师课表总行数
		int teacherScheduleViewRows = blockRows * teacherWeekViewRowNum - blankIntervalRows;
		// 课表总列数
		int scheduleViewCols = blockCols * weekViewColNum - blankIntervalCols;

		WeekView tclassWeekView = new WeekView();
		WeekView teacherWeekView = new WeekView();
		CellRangeAddress region = null;

		int blockRowIdx = -1;
		int blockColIdx = -1;
		int rowIdx = -1;
		int colIdx = -1;
		int weekViewIdx = -1;
		List<Arrangement> periodArrangementList = null;

		Font font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeightInPoints((short) 11);

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setShrinkToFit(true);

		tclassSheet.setDefaultColumnWidth(2);
		tclassSheet.setDefaultRowHeightInPoints(16);
		teacherSheet.setDefaultColumnWidth(2);
		teacherSheet.setDefaultRowHeightInPoints(16);

		for (int i = 0; i < tclassScheduleViewRows; i++) {
			blockRowIdx = i / blockRows;
			rowIdx = i % blockRows;
			tclassRow = tclassSheet.createRow(i);
			if (rowIdx - weekViewRows >= 0 && rowIdx - weekViewRows <= blankIntervalRows) {
				continue;
			}
			for (int j = 0; j < scheduleViewCols; j++) {
				blockColIdx = j / blockCols;
				colIdx = j % blockCols;
				weekViewIdx = blockRowIdx * weekViewColNum + blockColIdx;
				if (weekViewIdx >= tclassNum) {
					break;
				}
				tclassWeekView = tclassWeekViewList.get(weekViewIdx);
				tclassCell = tclassRow.createCell(j);
				if (colIdx - weekViewCols >= 0 && colIdx - weekViewCols <= blankIntervalCols) {
					continue;
				}
				tclassCell.setCellStyle(style);
				if (rowIdx == 0) {
					if (colIdx == 0) {
						region = new CellRangeAddress(i, i, j, j + 2);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue(tclassWeekView.getTitleView().getTclassName());
						continue;
					}
					if (colIdx == 3) {
						region = new CellRangeAddress(i, i, j, j + 2);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue(tclassWeekView.getTitleView().getTeacherName());
						continue;
					}
					if (colIdx == 6) {
						region = new CellRangeAddress(i, i, j, j + 2);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue(tclassWeekView.getTitleView().getStartDate());
						continue;
					}
				}
				if (rowIdx == 1) {
					if (colIdx == 1) {
						region = new CellRangeAddress(i, i, j, j + forenoon - 1);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue("上午");
						continue;
					}
					if (colIdx == 1 + forenoon) {
						region = new CellRangeAddress(i, i, j, j + afternoon - 1);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue("下午");
						continue;
					}
					if (colIdx == 1 + forenoon + afternoon) {
						region = new CellRangeAddress(i, i, j, j + evening - 1);
						tclassSheet.addMergedRegion(region);
						tclassCell.setCellValue("晚上");
						continue;
					}
				}
				if (rowIdx == 2) {
					if (colIdx == 0) {
						tclassCell.setCellValue("周");
						continue;
					}
					if (colIdx == 1) {
						tclassCell.setCellValue("早");
						continue;
					}
					if (colIdx >= 2 && colIdx <= periodsPerDay) {
						tclassCell.setCellValue(String.valueOf(colIdx - 1));
						continue;
					}
				}
				if (rowIdx >= 3 && rowIdx <= 2 + daysPerWeek) {
					if (colIdx == 0) {
						tclassCell.setCellValue(String.valueOf(rowIdx - 2));
						continue;
					}
					if (colIdx >= 1 && colIdx <= periodsPerDay) {
						periodArrangementList = tclassWeekView.getDayViewList().get(rowIdx - 3)
								.getArrangedPeriodViewList().get(colIdx - 1).getArrangementList();
						if (!periodArrangementList.isEmpty()) {
							tclassCell.setCellValue(periodArrangementList.get(0).getCourse().getShortName());
						}
						continue;
					}
				}
			}
		}
		for (int i = 0; i < teacherScheduleViewRows; i++) {
			blockRowIdx = i / blockRows;
			rowIdx = i % blockRows;
			teacherRow = teacherSheet.createRow(i);
			if (rowIdx - weekViewRows >= 0 && rowIdx - weekViewRows <= blankIntervalRows) {
				continue;
			}
			for (int j = 0; j < scheduleViewCols; j++) {
				blockColIdx = j / blockCols;
				colIdx = j % blockCols;
				weekViewIdx = blockRowIdx * weekViewColNum + blockColIdx;
				if (weekViewIdx >= teacherNum) {
					break;
				}
				teacherWeekView = teacherWeekViewList.get(weekViewIdx);
				teacherCell = teacherRow.createCell(j);
				if (colIdx - weekViewCols >= 0 && colIdx - weekViewCols <= blankIntervalCols) {
					continue;
				}
				teacherCell.setCellStyle(style);
				if (rowIdx == 0) {
					if (colIdx == 0) {
						teacherCell.setCellValue(teacherWeekView.getTitleView().getCourseName());
						continue;
					}
					if (colIdx == 1) {
						region = new CellRangeAddress(i, i, j, j + 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue(teacherWeekView.getTitleView().getGradeName());
					}
					if (colIdx == 3) {
						region = new CellRangeAddress(i, i, j, j + 2);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue(teacherWeekView.getTitleView().getTeacherName());
						continue;
					}
					if (colIdx == 6) {
						region = new CellRangeAddress(i, i, j, j + 2);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue(teacherWeekView.getTitleView().getStartDate());
						continue;
					}
				}
				if (rowIdx == 1) {
					if (colIdx == 1) {
						region = new CellRangeAddress(i, i, j, j + forenoon - 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue("上午");
						continue;
					}
					if (colIdx == 1 + forenoon) {
						region = new CellRangeAddress(i, i, j, j + afternoon - 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue("下午");
						continue;
					}
					if (colIdx == 1 + forenoon + afternoon) {
						region = new CellRangeAddress(i, i, j, j + evening - 1);
						teacherSheet.addMergedRegion(region);
						teacherCell.setCellValue("晚上");
						continue;
					}
				}
				if (rowIdx == 2) {
					if (colIdx == 0) {
						teacherCell.setCellValue("周");
						continue;
					}
					if (colIdx == 1) {
						teacherCell.setCellValue("早");
						continue;
					}
					if (colIdx >= 2 && colIdx <= periodsPerDay) {
						teacherCell.setCellValue(String.valueOf(colIdx - 1));
						continue;
					}
				}
				if (rowIdx >= 3 && rowIdx <= 2 + daysPerWeek) {
					if (colIdx == 0) {
						teacherCell.setCellValue(String.valueOf(rowIdx - 2));
						continue;
					}
					if (colIdx >= 1 && colIdx <= periodsPerDay) {
						periodArrangementList = teacherWeekView.getDayViewList().get(rowIdx - 3)
								.getArrangedPeriodViewList().get(colIdx - 1).getArrangementList();
						if (!periodArrangementList.isEmpty()) {
							teacherCell.setCellValue(periodArrangementList.get(0).getTclass().getShortName());
						}
						continue;
					}
				}
			}
		}

		// 设置打印参数
		tclassSheet.setMargin(Sheet.TopMargin, 0.83);
		tclassSheet.setMargin(Sheet.BottomMargin, 0.83);
		tclassSheet.setHorizontallyCenter(true);
		teacherSheet.setMargin(Sheet.TopMargin, 0.83);
		teacherSheet.setMargin(Sheet.BottomMargin, 0.83);
		teacherSheet.setHorizontallyCenter(true);

		Group group = scheduleView.getSchedule().getGroup();
		String gradeName = groupService.gnrGradeName(group);
		String startDate = group.getStartDate();
		// Integer semester = group.getSemester();
		// String semesterName = semester == 0 ? "上学期" : semester == 1 ? "下学期" :
		// "";
		String fileName = gradeName + startDate + "课表.xlsx";
		fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");

		OutputStream outputStream = response.getOutputStream();
		workbook.write(outputStream);
		outputStream.close();
		workbook.close();
	}

	@Override
	public void initial(Integer scheduleId) {
		arrangementService.initial(scheduleId);
		return;
	}

}
