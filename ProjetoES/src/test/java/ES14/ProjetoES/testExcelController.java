package ES14.ProjetoES;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



public class testExcelController{

	private static ExcelController excel;

//	public ExcelControllerTest() {
//		super();
//	}

	@BeforeAll
	static public void setUpBeforeClass() throws Exception {
		excel = new ExcelController();
		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");
		System.out.println("OLA");
	}
//
//	@AfterAll
//	public void finalize() {
//		System.out.println("ADEUS");
//	}

	@Test
	public void testReadExcel() throws Exception {
//		 Verificar se está a ler corretamente posições aleatórias do ficheiro excel
//
//		excel = new ExcelController();
//		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");

		String[][] data = excel.getData();
		String aux = "15";
		String aux1 = "0.428571";
		String aux2 = "TRUE";
		String aux3 = "SphericalLens";
		assertEquals(aux, data[28][6]);
		assertEquals(aux1, data[28][7]);
		assertEquals(aux2, data[43][8]);
		assertEquals(aux3, data[121][2]);
	}

	@Test
	public void testHeaders() throws Exception {

//		excel = new ExcelController();
//		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");

		String[] headers = excel.getHeaders();
		String aux = "is_long_method";
		String aux1 = "method";

		assertEquals(aux, headers[8]);
		assertEquals(aux1, headers[3]);

	}

//	public void testSheet() throws Exception {
//
//		excel = new ExcelController();
//		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");
//
//		XSSFWorkbook workbook = new XSSFWorkbook(new File("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx"));
//		XSSFSheet sheet = workbook.getSheetAt(0);
//
//		assertThat(sheet).isEqualToComparingFieldByField(excel.getSheet());
//		assertEquals(sheet, excel.getSheet());
//		assertThat(sheet).isEqualToComparingFieldByField(excel.getSheet());
//	}

}
