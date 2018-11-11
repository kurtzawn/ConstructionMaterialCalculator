import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class ReadExcel {
	
	public static LinkedList<RecordRaw> getRecordFromExcel(String filenameWithSuffix) throws IOException, InvalidFormatException {
		Workbook workbook = WorkbookFactory.create(new File(filenameWithSuffix));
		LinkedList<RecordRaw> res = new LinkedList<>();
		Sheet sheet = workbook.getSheetAt(0);
		DataFormatter dataFormatter = new DataFormatter();
		RecordRaw record;
		for (Row row: sheet) {
			if(row.getRowNum()==0) continue;
			record = new RecordRaw();
			record.room = dataFormatter.formatCellValue(row.getCell(1));
			record.position = dataFormatter.formatCellValue(row.getCell(2));
			record.direction = dataFormatter.formatCellValue(row.getCell(3)); // can be null, need check?
			record.borx = dataFormatter.formatCellValue(row.getCell(4));
			record.name = dataFormatter.formatCellValue(row.getCell(5));
			record.type = dataFormatter.formatCellValue(row.getCell(6));
			if (dataFormatter.formatCellValue(row.getCell(7)).equals("") || row.getCell(7) == null) {
				record.padlen = 0;
			} else {
				record.padlen = Integer.parseInt(dataFormatter.formatCellValue(row.getCell(7)));
			}
			record.size = dataFormatter.formatCellValue(row.getCell(8));
			res.add(record);
		}
		
		workbook.close();
		return res;
	} 
	
}
