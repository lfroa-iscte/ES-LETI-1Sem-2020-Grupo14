package ES14.ProjetoES;

import org.junit.jupiter.api.Test;

/**
 * Classe de testes para a classe GUI.
 * 
 * @see GUI
 * 
 * @author Francisco Mendes
 * @author Tomás Santos
 */

public class GUITest {

	/**
	 * Método que testa a abertura de algumas janelas principais da GUI.
	 * 
	 * @throws Exception
	 * @see GUI
	 * 
	 * @author Francisco Mendes
	 * @author Tomás Santos
	 */

	@Test
	void test() throws Exception {
		App app = new App();
		app.main(null);
		ExcelController excel = new ExcelController();
		excel.readExcel("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx");
		GUI g = new GUI();
		g.open();
		g.showErrorDialog("JUnit test", 200, 150);
		g.setPopUp("Definir regra - LongMethod");
		g.setPopUp("Definir regra - FeatureEnvy");
		g.setExcelFrame("Defeitos.xlsx", excel.getData(), excel.getHeaders());

	}

}
