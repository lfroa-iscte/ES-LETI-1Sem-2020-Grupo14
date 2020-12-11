package ES14.ProjetoES;


import org.junit.jupiter.api.Test;
import junit.framework.TestCase;

	/**
	 *  Classe de teste da classe ExcelController.
	 * 
	 * @see ExcelController
	 * 
	 * @author Francico Mendes
	 */

public class testExcelController extends TestCase {

	private static ExcelController excel;

	/**
	 * Método que verifica a leitura correta de uma tabela de um ficheiro excel, comparando valores de determinadas células da tabela
	 * com os valores lidos para essas posições.
	 * 
	 * @throws Exception
	 * 
	 * @author Francisco Mendes
	 */
	
	@Test
	public void testReadExcel() throws Exception {
		excel = new ExcelController();
		excel.readExcel("C:\\Users\\tomas\\OneDrive\\Ambiente de Trabalho\\Defeitos.xlsx");

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
	
	/**
	 * Método que verifica a leitura correta do nome das colunas (headers) de uma tabela de um ficheiro excel, comparando valores de determinadas colunas
	 * da tabela com os valores lidos para essas posições.
	 * 
	 * @throws Exception
	 * 
	 * @author Francisco Mendes
	 */

	@Test
	public void testHeaders() throws Exception {
		excel = new ExcelController();
		excel.readExcel("C:\\Users\\tomas\\OneDrive\\Ambiente de Trabalho\\Defeitos.xlsx");

		String[] headers = excel.getHeaders();
		String aux = "is_long_method";
		String aux1 = "method";

		assertEquals(aux, headers[8]);
		assertEquals(aux1, headers[3]);
	}
}
