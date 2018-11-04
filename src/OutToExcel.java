import java.io.FileOutputStream;
import java.io.IOException;
//import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class OutToExcel{
	
	public OutToExcel() {}
	
	/*
	 * 
	 */
	public static void outDetailList(List<RecordRaw> recordsRaw, String filename) throws IOException  {
		int n = recordsRaw.size();
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("施工详单");
		Row titlesRow = sheet.createRow(0);
		titlesRow.createCell(0).setCellValue("序号");
		titlesRow.createCell(1).setCellValue("产品名称");
		titlesRow.createCell(2).setCellValue("规格");
		titlesRow.createCell(3).setCellValue("数量");
		titlesRow.createCell(4).setCellValue("位置");
		titlesRow.createCell(5).setCellValue("房间");
		titlesRow.createCell(6).setCellValue("铺贴方向");
		
		Row contentsRow;
		for (int i = 0; i < n; i++) {
			contentsRow = sheet.createRow(i+1);
			RecordRaw curr = recordsRaw.get(i);
			contentsRow.createCell(0).setCellValue(i+1);
			contentsRow.createCell(1).setCellValue(curr.name);
			contentsRow.createCell(2).setCellValue(curr.type);
			contentsRow.createCell(3).setCellValue(curr.quantity);
			contentsRow.createCell(4).setCellValue(curr.position);
			contentsRow.createCell(5).setCellValue(curr.room);
			contentsRow.createCell(6).setCellValue(curr.direction);
		}
		
		OutputStream fileOut = new FileOutputStream(filename + "_施工详单.xls");
		wb.write(fileOut);
		wb.close();
		System.out.println("Success: Construction detail file output successfully");
	}
	
	/*
	 * 
	 */
	public static void outCheckList(List<RecordRaw> recordsRaw, String filename) throws IOException {
		int n = recordsRaw.size();
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("计算书");
		Row titlesRow = sheet.createRow(0);
		
		titlesRow.createCell(0).setCellValue("序号");
		titlesRow.createCell(1).setCellValue("房间");
		titlesRow.createCell(2).setCellValue("位置");
		titlesRow.createCell(3).setCellValue("铺贴方向");
		titlesRow.createCell(4).setCellValue("板或线");
		titlesRow.createCell(5).setCellValue("产品名称");
		titlesRow.createCell(6).setCellValue("规格");
		titlesRow.createCell(7).setCellValue("单片长度");
		titlesRow.createCell(8).setCellValue("尺寸");

		Row contentsRow;
		for (int i = 0; i < n; i++) {
			contentsRow = sheet.createRow(i+1);
			RecordRaw curr = recordsRaw.get(i);
			contentsRow.createCell(0).setCellValue(i+1);
			contentsRow.createCell(1).setCellValue(curr.room);
			contentsRow.createCell(2).setCellValue(curr.position);
			contentsRow.createCell(3).setCellValue(curr.direction);
			contentsRow.createCell(4).setCellValue(curr.borx);
			contentsRow.createCell(5).setCellValue(curr.name);
			contentsRow.createCell(6).setCellValue(curr.type);
			if (curr.padlen == 0) {
				contentsRow.createCell(7).setCellValue("");
			} else {
				contentsRow.createCell(7).setCellValue(curr.padlen);
			}
			contentsRow.createCell(8).setCellValue(curr.size);
		}
		OutputStream fileOut = new FileOutputStream(filename + "_计算书.xls");
		wb.write(fileOut);
		wb.close();
		System.out.println("Success: CheckList file output successfully");
	}
	
	/*
	 * 
	 */
	public static void outSumListBuying(List<RecordSum> recordsSum, RecordSum locker, double allPrices, String filename) throws IOException {
		int n = recordsSum.size();
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("汇总表");
		Row titlesRow = sheet.createRow(0);
		titlesRow.createCell(0).setCellValue("序号");
		titlesRow.createCell(1).setCellValue("产品名称");
		titlesRow.createCell(2).setCellValue("规格");
		titlesRow.createCell(3).setCellValue("数量");
		titlesRow.createCell(4).setCellValue("平方数/米数");
		titlesRow.createCell(5).setCellValue("单价");
		titlesRow.createCell(6).setCellValue("合计");
		titlesRow.createCell(7).setCellValue("备注");
		
		Row contentsRow;
		for (int i = 0; i < n; i++) {
			contentsRow = sheet.createRow(i+1);
			RecordSum curr = recordsSum.get(i);
			contentsRow.createCell(0).setCellValue(i+1);
			contentsRow.createCell(1).setCellValue(curr.name);
			contentsRow.createCell(2).setCellValue(curr.type);
			contentsRow.createCell(3).setCellValue(curr.quantity);
			contentsRow.createCell(4).setCellValue(curr.area);
			contentsRow.createCell(5).setCellValue(curr.pricebuy);
			contentsRow.createCell(6).setCellValue(curr.totalpricebuy);
			contentsRow.createCell(7).setCellValue(curr.demo);
		}
		
		Row lockerRow = sheet.createRow(n+1);
		lockerRow.createCell(0).setCellValue(n+1);
		lockerRow.createCell(1).setCellValue("卡扣");
		lockerRow.createCell(3).setCellValue(locker.quantity);
		lockerRow.createCell(4).setCellValue(locker.quantity);
		lockerRow.createCell(5).setCellValue(locker.pricebuy);
		lockerRow.createCell(6).setCellValue(locker.totalpricebuy);
		
		Row totalpriceRow = sheet.createRow(n+2);
		totalpriceRow.createCell(6).setCellValue(allPrices);
		
		OutputStream fileOut = new FileOutputStream(filename + "_进货汇总表.xls");
		wb.write(fileOut);
		wb.close();
		System.out.println("Success: Buying summarize file output successfully");

	}

	/*
	 * 
	 */
	public static void outSumListSelling(List<RecordSum> recordsSum, RecordSum locker, double allPrices, String filename) throws IOException {
		int n = recordsSum.size();
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("汇总表");
		Row titlesRow = sheet.createRow(0);
		titlesRow.createCell(0).setCellValue("序号");
		titlesRow.createCell(1).setCellValue("产品名称");
		titlesRow.createCell(2).setCellValue("规格");
		titlesRow.createCell(3).setCellValue("数量");
		titlesRow.createCell(4).setCellValue("平方数/米数");
		titlesRow.createCell(5).setCellValue("单价");
		titlesRow.createCell(6).setCellValue("合计");
		titlesRow.createCell(7).setCellValue("备注");
		
		Row contentsRow;
		for (int i = 0; i < n; i++) {
			contentsRow = sheet.createRow(i+1);
			RecordSum curr = recordsSum.get(i);
			contentsRow.createCell(0).setCellValue(i+1);
			contentsRow.createCell(1).setCellValue(curr.name);
			contentsRow.createCell(2).setCellValue(curr.type);
			contentsRow.createCell(3).setCellValue(curr.quantity);
			contentsRow.createCell(4).setCellValue(curr.area);
			contentsRow.createCell(5).setCellValue(curr.pricesell);
			contentsRow.createCell(6).setCellValue(curr.totalpricesell);
			contentsRow.createCell(7).setCellValue(curr.demo);
		}
		
		Row lockerRow = sheet.createRow(n+1);
		lockerRow.createCell(0).setCellValue(n+1);
		lockerRow.createCell(1).setCellValue("卡扣");
		lockerRow.createCell(3).setCellValue(locker.quantity);
		lockerRow.createCell(4).setCellValue(locker.quantity);
		lockerRow.createCell(5).setCellValue(locker.pricesell);
		lockerRow.createCell(6).setCellValue(locker.totalpricesell);
		
		Row totalpriceRow = sheet.createRow(n+2);
		totalpriceRow.createCell(6).setCellValue(allPrices);
		
		OutputStream fileOut = new FileOutputStream(filename + "_报价汇总表.xls");
		wb.write(fileOut);
		wb.close();
		System.out.println("Success: Selling summarize file output successfully");
	}
}
