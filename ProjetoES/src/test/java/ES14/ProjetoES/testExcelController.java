package ES14.ProjetoES;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class testExcelController extends TestCase {

	private static ExcelController excel;

//	@BeforeAll
//	static public void setUpBeforeClass() throws Exception {
//		excel = new ExcelController();
//		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");
//	}

	@Test
	public void testReadExcel() throws Exception {
		excel = new ExcelController();
		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");

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
		excel = new ExcelController();
		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");

		String[] headers = excel.getHeaders();
		String aux = "is_long_method";
		String aux1 = "method";

		assertEquals(aux, headers[8]);
		assertEquals(aux1, headers[3]);
	}
}
