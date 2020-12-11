package ES14.ProjetoES;

import org.junit.jupiter.api.Test;
import junit.framework.TestCase;

	/**
	 * Classe de testes para a classe GUI.
	 * 
	 * @see GUI
	 * 
	 * @author Francisco Mendes
	 */

public class testGUI extends TestCase {

	/**
	 * Método que testa a abertura de algumas janelas principais da GUI.
	 * 
	 * @throws Exception
	 * @see GUI
	 * 
	 * @author Francisco Mendes
	 */
	
	@Test
	public void test() throws Exception {
		ExcelController excel = new ExcelController();
		excel.readExcel("C:\\Users\\tomas\\OneDrive\\Ambiente de Trabalho\\Defeitos.xlsx");
		GUI g = new GUI();
		g.open();
		g.showErrorDialog("JUnit test", 200, 150);
		g.setPopUp("Definir regra - LongMethod");
		g.setPopUp("Definir regra - FeatureEnvy");
		g.setExcelFrame("Defeitos.xlsx", excel.getData(), excel.getHeaders());

	}

}
