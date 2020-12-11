package ES14.ProjetoES;

import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class testGUI extends TestCase {

	@Test
	public void test() throws Exception {
		ExcelController excel = new ExcelController();
		excel.readExcel("C:\\Users\\fnpm\\Desktop\\Defeitos.xlsx");
		GUI g = new GUI();
		g.open();
		g.showErrorDialog("JUnit test", 200, 150);
		g.setPopUp("Definir regra - LongMethod");
		g.setPopUp("Definir regra - FeatureEnvy");
		g.setExcelFrame("Defeitos.xlsx", excel.getData(), excel.getHeaders());

	}

}
