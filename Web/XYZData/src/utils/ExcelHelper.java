package utils;

import entity.InfoData;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

//    public HSSFSheet export(HSSFWorkbook wb, String fileName, String[] title,
//                            ArrayList<InfoData> content) {
//        // int cellNum = 5; //设置列数
//        /**
//         * 建立表格设置。
//         */
//        HSSFSheet sheet = null;
//        try {
//            sheet = wb.createSheet(fileName);
//
//            int columnSize = title.length;
//            HSSFRow row_one = sheet.createRow(0);
//            for (int i = 0; i < columnSize; i++) {
//                HSSFCell cell = row_one.createCell(i);
//                cell.setCellValue(title[i]);
//            }
//            int curRow = 1;
//            int endRow = 1;
//            int temp = curRow;
//            for (int i = 0; i < content.size(); i++) {
//                temp = curRow;
//                InfoData shiti = content.get(i);
//                int xixiangNum = content.get(i).getXixiangList().size();
//                endRow += xixiangNum;
//                for (; curRow < endRow; curRow++) {
//                    // 设置需要合并的单元格部分
//                    for (int k = 0; k < 7; k++) {
//                        CellRangeAddress cra = new CellRangeAddress(curRow,
//                                endRow - 1, k, k);
//                        sheet.addMergedRegion(cra);
//                    }
//                    HSSFRow row = sheet.createRow(curRow);
//                    if (curRow == temp) {
//                        row.createCell(0).setCellValue(shiti.getShitiNum());
//                        row.createCell(1).setCellValue(shiti.getShitiName());
//                        row.createCell(2)
//                                .setCellValue(shiti.getShitiProvince());
//                        row.createCell(3).setCellValue(shiti.getShitiCity());
//                        row.createCell(4).setCellValue(shiti.getShitiPerson());
//                        row.createCell(5).setCellValue(shiti.getShitiResult());
//                        row.createCell(6).setCellValue(shiti.getShitiTime());
//                    }
//                    // 开始添加细分
//                    XixiangModal xixiang = shiti.getXixiangList().get(
//                            curRow - temp);
//                    row.createCell(7).setCellValue(xixiang.getXixiangName());
//                    row.createCell(8).setCellValue(xixiang.getXixiangMember());
//                    row.createCell(9).setCellValue(xixiang.getXixiangResult());
//                    row.createCell(10).setCellValue(xixiang.getXixiangTime());
//                    row.createCell(11).setCellValue(xixiang.getXixiangReason());
//
//                }
//                curRow = endRow;
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return sheet;
//    } // end of export

    public static void createExcel(List<InfoData> list) throws SQLException {
        // 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("sku表");
        // 添加表头行
        HSSFRow hssfRow = sheet.createRow(0);
        // 设置单元格格式居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // 添加表头内容
        HSSFCell headCell = hssfRow.createCell(0);
        headCell.setCellValue("time");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(1);
        headCell.setCellValue("x");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(2);
        headCell.setCellValue("y");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(3);
        headCell.setCellValue("z");
        headCell.setCellStyle(cellStyle);


        // 添加数据内容
        for (int i = 0; i < list.size(); i++) {
            hssfRow = sheet.createRow((int) i + 1);
            InfoData student = list.get(i);

            // 创建单元格，并设置值
            HSSFCell cell = hssfRow.createCell(0);
            cell.setCellValue(student.getTime());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(1);
            cell.setCellValue(student.getX());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(2);
            cell.setCellValue(student.getY());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(3);
            cell.setCellValue(student.getZ());
            cell.setCellStyle(cellStyle);

        }
        // 保存Excel文件
        try {
            OutputStream outputStream = new FileOutputStream("/Users/zhangqiangsheng/apache-tomcat-7.0.107/temp/info.xls");
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}