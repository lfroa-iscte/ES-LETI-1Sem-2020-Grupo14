package ES14.ProjetoES;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import junit.framework.TestSuite;

/**
 * Classe de teste da classe Algoritmo.
 * 
 * @see Algoritmo
 * 
 * @author Lucas Oliveira
 * @author Tomás Santos
 * @author Francisco Mendes
 */

public class AlgoritmoTest extends TestSuite {

	private static Sheet sheet;

	/**
	 * 
	 * Método para inicializar a sheet do ficheiro excel.
	 * 
	 * @see Algoritmo
	 * 
	 * @author Lucas Oliveira
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new File("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = workbook.getSheetAt(0);

	}

	/**
	 * Método que verifica os resultados da qualidade de deteção de code smells para
	 * a ferramenta <b>iPlasma</b> segundo o algoritmo, comparando os resultados
	 * obtidos pelo mesmo com os resultados reais.
	 * 
	 * @see Algoritmo
	 * 
	 * @author Lucas Oliveira
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	@Test
	void testPlasma() {
		Algoritmo alg = new Algoritmo(sheet);
		alg.runAlgoritmo("iPlasma", null);
		String[][] temp = { { "DCI", "140" }, { "DII", "0" }, { "ADCI", "280" }, { "ADII", "0" } };
		String[][] algLista = alg.getIndicadores();
		assertArrayEquals(temp, algLista);
	}

	/**
	 * Método que verifica os resultados da qualidade de deteção de code smells para
	 * a ferramenta <b>PMD</b> segundo o algoritmo, comparando os resultados obtidos
	 * pelo mesmo com os resultados reais.
	 * 
	 * @see Algoritmo
	 * 
	 * @author Lucas Oliveira
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	@Test
	void testPMD() {
		Algoritmo alg = new Algoritmo(sheet);
		alg.runAlgoritmo("PMD", null);
		String[][] temp = { { "DCI", "140" }, { "DII", "18" }, { "ADCI", "262" }, { "ADII", "0" } };
		String[][] algLista = alg.getIndicadores();
		assertArrayEquals(temp, algLista);
	}

	/**
	 * Método que verifica os resultados da qualidade de deteção para o code smell
	 * <i>long_method</i> para uma regra definida pelo utilizador, composta por
	 * <b>duas</b> métricas, comparando os resultados obtidos pelo mesmo com os
	 * resultados reais.
	 * 
	 * @see Algoritmo
	 * 
	 * @author Lucas Oliveira
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	@Test
	void testRegraLong() {
		Algoritmo alg = new Algoritmo(sheet);
		Regra r = new Regra("LOC", ">", 300, "AND");
		Regra r1 = new Regra("CYCLO", ">", 10, null);
		List<Regra> regras = new ArrayList<Regra>();
		regras.add(r);
		regras.add(r1);
		alg.runAlgoritmo("LongMethod", regras);
		String[][] temp = { { "DCI", "22" }, { "DII", "0" }, { "ADCI", "280" }, { "ADII", "118" } };
		String[][] temp1 = { { "14" }, { "78" }, { "114" }, { "121" }, { "197" }, { "223" }, { "230" }, { "281" },
				{ "308" }, { "315" }, { "317" }, { "318" }, { "322" }, { "323" }, { "324" }, { "335" }, { "348" },
				{ "368" }, { "376" }, { "392" }, { "408" }, { "419" } };
		String[][] algLista = alg.getIndicadores();
		String[][] algMetodos = alg.getMetodos();
		assertArrayEquals(temp, algLista);
		assertArrayEquals(temp1, algMetodos);
	}

	/**
	 * Método que verifica os resultados da qualidade de deteção para o code smell
	 * <i>long_method</i> para uma regra definida pelo utilizador, composta por
	 * <b>três</b> métricas, comparando os resultados obtidos pelo mesmo com os
	 * resultados reais.
	 * 
	 * @see Algoritmo
	 * 
	 * @author Lucas Oliveira
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	@Test
	void testRegraLong1() {
		Algoritmo alg = new Algoritmo(sheet);
		Regra r = new Regra("LOC", ">", 300, "OR");
		Regra r1 = new Regra("CYCLO", "<=", 10, null);
		List<Regra> regras = new ArrayList<Regra>();
		regras.add(r);
		regras.add(r1);
		alg.runAlgoritmo("LongMethod", regras);
		String[][] temp = { { "DCI", "24" }, { "DII", "272" }, { "ADCI", "8" }, { "ADII", "116" } };
		String[][] algLista = alg.getIndicadores();
		assertArrayEquals(temp, algLista);
	}

	/**
	 * Método que verifica os resultados da qualidade de deteção para o code smell
	 * <i>feature_envy</i> para uma regra definida pelo utilizador, comparando os
	 * resultados obtidos pelo mesmo com os resultados reais.
	 * 
	 * @see Algoritmo
	 * 
	 * @author Lucas Oliveira
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	@Test
	void testRegraFeatureEnvy() {
		Algoritmo alg = new Algoritmo(sheet);
		Regra r = new Regra("ATFD", "<", 20, "AND");
		Regra r1 = new Regra("LAA", ">=", 0.5, null);
		List<Regra> regras = new ArrayList<Regra>();
		regras.add(r);
		regras.add(r1);
		alg.runAlgoritmo("FeatureEnvy", regras);
		String[][] temp = { { "DCI", "0" }, { "DII", "184" }, { "ADCI", "122" }, { "ADII", "114" } };
		String[][] algLista = alg.getIndicadores();
		assertArrayEquals(temp, algLista);
	}

}
