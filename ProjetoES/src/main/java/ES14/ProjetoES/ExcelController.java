package ES14.ProjetoES;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelController {

	private XSSFSheet sheet;
	private String[] headers;
	private String[][] data;

	/**
	 * Lê um ficheiro Excel localizado em <i>path</i>.
	 * 
	 * @param path Localização do ficheiro Excel.
	 * @throws Exception
	 * 
	 * @author Francisco Mendes
	 */
	public void readExcel(String path) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
		sheet = workbook.getSheetAt(0);

		DataFormatter dataFormatter = new DataFormatter();
		headers = new String[sheet.getRow(0).getLastCellNum()];
		data = new String[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				if (cell.getRowIndex() != 0) {
					data[row.getRowNum() - 1][cell.getColumnIndex()] = cellValue;
				} else {
					headers[cell.getColumnIndex()] = cellValue;
				}
			}
		}
	}

	public XSSFSheet getSheet() {
		return sheet;
	}

	public String[] getHeaders() {
		return headers;
	}

	public String[][] getData() {
		return data;
	}
}
