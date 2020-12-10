package ES14.ProjetoES;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * Esta classe  lê o excel e guarda a informação deste
 * 
 * @author Francisco Mendes
 * @version 1.0
 * @since 24/10/2020
 */

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
	
	/**
	 * 
	 * Devolve a sheet do excel
	 * 
	 * @return XSSFSheet
	 * 
	 *  @author Francisco Mendes
	 */
	public XSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * 
	 * Devolve os header do excel num vetor
	 * 
	 * @return String[]
	 * 
	 *  @author Francisco Mendes
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * 
	 * Devolve os dados do excel numa matriz
	 * 
	 * @return String[][]
	 * 
	 *  @author Francisco Mendes
	 */
	public String[][] getData() {
		return data;
	}
}
