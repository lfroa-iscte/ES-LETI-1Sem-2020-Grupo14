package ES14.ProjetoES;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * 
 * Esta classe representa o algoritmo que devolve os métodos defeituosos e
 * indicadores de qualidade, consoante a utilização de uma dada
 * regra/ferramenta.
 * 
 * @author Lucas Oliveira.
 * @version 1.0
 * @since 2020-10-24
 */

public class Algoritmo {

	private static int loc = 4;
	private static int cyclo = 5;
	private static int atfd = 6;
	private static int laa = 7;
	private static int isLong = 8;
	private static int iPlasma = 9;
	private static int pmd = 10;
	private static int featureEnvy = 11;

	private ArrayList<Integer> methods;
	private Map<String, Integer> indicadores;
	private String[][] indicadoresQualidade;
	private String[][] metodos;
	private Sheet sheet;

	/**
	 * O Algoritmo recebe uma sheet do Excel importado na GUI, de forma a poder
	 * iterar sobre ela, obter os métodos defeituosos e os indicadores de qualidade
	 * de uma dada ferramenta/regra.
	 * 
	 * @param sheet Sheet do ficheiro excel.
	 * 
	 * @author Lucas Oliveira.
	 */
	public Algoritmo(Sheet sheet) {
		this.sheet = sheet;
		methods = new ArrayList<>();
		indicadores = new LinkedHashMap<>();
		indicadores.put("DCI", 0);
		indicadores.put("DII", 0);
		indicadores.put("ADCI", 0);
		indicadores.put("ADII", 0);
	}

	/**
	 * Corre o algoritmo dada uma ferramenta/regra e, caso seja uma regra, de forma
	 * a poder correr o algoritmo, é preciso também dar uma lista com as métricas e
	 * respetivas thresholds, e operadores lógicos.
	 * 
	 * @param ferramenta Representa a ferramenta/regra a usar.
	 * @param regras     Representa a lista de métricas, thresholds e op. lógicos.
	 * 
	 * @author Lucas Oliveira.
	 */
	public void runAlgoritmo(String ferramenta, List<Regra> regras) {

		if (ferramenta.equals("PMD") || ferramenta.equals("iPlasma")) {
			retMetodos(ferramenta);
			checkIndicadoresFerramenta(ferramenta);
		} else {
			retMetodosRegra(regras);
			checkIndicadoresRegra(ferramenta);
		}
	}

	/**
	 * Guarda os indicadores de qualidade de uma dada regra numa matriz, tendo em
	 * conta o tipo de regra em questão (FeatureEnvy ou LongMethod). Utilizado
	 * internamente pelo método runAlgoritmo().
	 * 
	 * @param smell Representa o code smell em questão.
	 * @author Lucas Oliveira.
	 */
	private void checkIndicadoresRegra(String smell) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = isSmell(smell, row);
				if (value && methods.contains(row.getRowNum()))
					indicadores.put("DCI", indicadores.get("DCI") + 1);
				else if (!value && methods.contains(row.getRowNum()))
					indicadores.put("DII", indicadores.get("DII") + 1);
				else if (!value && !methods.contains(row.getRowNum()))
					indicadores.put("ADCI", indicadores.get("ADCI") + 1);
				else if (value && !methods.contains(row.getRowNum()))
					indicadores.put("ADII", indicadores.get("ADII") + 1);

			}
		}
		indicadoresQualidade = mapToMatrix(indicadores);
	}

	/**
	 * Devolve o valor Boolean (true ou false) da coluna de um tipo de code
	 * smell(isLongMethod ou FeatureEnvy). Utilizado internamente pelo método
	 * checkIndicadoresRegra().
	 * 
	 * @param ferramenta Representa o tipo de code smell.
	 * @param row        Representa o método em questão.
	 * @return Boolean
	 * @author Lucas Oliveira.
	 */

	private Boolean isSmell(String ferramenta, Row row) {
		Boolean value = false;
		if (ferramenta.equals("FeatureEnvy"))
			value = row.getCell(featureEnvy).getBooleanCellValue();
		else
			value = row.getCell(isLong).getBooleanCellValue();
		return value;
	}

	/**
	 * Guarda numa matriz os métodos defeituosos consoante uma regra, representada
	 * neste caso por uma lista de métricas e respetivas thresholds e op. lógicos.
	 * Utilizado internamente pelo método runAlgoritmo().
	 * 
	 * @param regras Lista de métricas, thresholds e op. lógicos que representam a
	 *               regra.
	 * @author Lucas Oliveira.
	 */
	private void retMetodosRegra(List<Regra> regras) {

		iteradorSheetRegra(regras);
		metodos = arrayToMatrix(methods);
	}

	/**
	 * Itera sobre a Sheet de Excel, verificando para cada linha(método) se existe
	 * code smell, dada uma regra representada por uma lista de métricas, thresholds
	 * e op. lógicos. Utilizado internamente pelo método retMetodosRegra().
	 * 
	 * @param regras Lista de métricas, thresholds e op. lógicos que representam uma
	 *               regra.
	 * @author Lucas Oliveira.
	 */

	private void iteradorSheetRegra(List<Regra> regras) {
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() != 0) {
				checkForSmell(row, regras);
			}
		}
	}

	/**
	 * Dada uma linha do Excel e uma regra, verifica se o método tem code smell.
	 * Utilizado internamente pelo método retMetodosRegra().
	 * 
	 * @param row    Linha do Excel.
	 * @param regras Conjunto de métricas, thresholds e op. lógicos que representam
	 *               a regra.
	 * @author Lucas Oliveira.
	 */

	private void checkForSmell(Row row, List<Regra> regras) {
		boolean smell = false;
		metodosRegra(row, regras, smell);
	}

	/**
	 * 
	 * Adiciona os métodos que cumprem a regra a uma lista. Utilizado internamente
	 * pelo método checkForSmell().
	 * 
	 * @param row    Linha(Método) a analisar.
	 * @param regras Conjunto de métricas, thresholds e op. lógicos que representam
	 *               a regra.
	 * @param smell  Representa um Boolean.
	 * @throws NumberFormatException
	 * @author Lucas Oliveira.
	 */
	private void metodosRegra(Row row, List<Regra> regras, boolean smell) throws NumberFormatException {
		smell = smell(row, regras, smell);
		if (smell == true)
			methods.add(row.getRowNum());
	}

	/**
	 * 
	 * Indica se o método, consoante uma dada regra, tem code smell. Utilizado
	 * internamente pelo método metodosRegra().
	 * 
	 * @param row    Linha(Método) a analisar.
	 * @param regras Conjunto de métricas, thresholds e op. lógicos que representam
	 *               a regra.
	 * @param smell  Indica se o método tem code smell.
	 * @return boolean.
	 * @throws NumberFormatException
	 * @author Lucas Oliveira.
	 */

	private boolean smell(Row row, List<Regra> regras, boolean smell) throws NumberFormatException {
		for (Regra i : regras) {
			int cell = metrica(i.getMetrica());
			if (regras.indexOf(i) == 0 || (!smell && regras.get(regras.indexOf(i) - 1).getOpLogico().equals("OR"))
					|| (smell && regras.get(regras.indexOf(i) - 1).getOpLogico().equals("AND"))) {
				Cell temp = row.getCell(cell);
				DataFormatter dataFormatter = new DataFormatter();
				String t = dataFormatter.formatCellValue(temp);
				double num = Double.parseDouble(t);
				if (i.getOp().equals(">") && num > i.getValor())
					smell = true;
				else if (i.getOp().equals(">=") && num >= i.getValor())
					smell = true;
				else if (i.getOp().equals("<") && num < i.getValor())
					smell = true;
				else if (i.getOp().equals("<=") && num <= i.getValor())
					smell = true;
				else
					smell = false;
			}
		}
		return smell;
	}

	/**
	 * Devolve o número da coluna do Excel correspondente à métrica dada. Utilizado
	 * internamente pelo método checkForSmell().
	 * 
	 * @param metrica Representa o nome da metrica em questão.
	 * @return int Coluna correspondente do excel.
	 * @author Lucas Oliveira.
	 * 
	 */

	private int metrica(String metrica) {
		if (metrica.equals("LOC"))
			return loc;
		if (metrica.equals("CYCLO"))
			return cyclo;
		if (metrica.equals("ATFD"))
			return atfd;

		return laa;

	}

	/**
	 * Guarda numa matriz os métodos defeituosos de uma dada ferramenta. Utilizado
	 * internamente pelo método runAlgoritmo().
	 * 
	 * @param ferramenta Representa a ferramenta utilizada(iPlasma, PMD).
	 * @author Lucas Oliveira.
	 */
	private void retMetodos(String ferramenta) {

		iteradorSheetFerramenta(ferramenta);
		metodos = arrayToMatrix(methods);
	}

	/**
	 * Itera sobre a Sheet de Excel, verificando para cada linha(método) se existe
	 * code smell, dada uma ferramenta. Utilizado internamente pelo método
	 * retMetodosRegra().
	 * 
	 * @param ferramenta Representa a ferramenta utilizada.
	 * @author Lucas Oliveira.
	 */

	private void iteradorSheetFerramenta(String ferramenta) {
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getRowNum() != 0) {
				metodosFerramenta(ferramenta, row);
			}
		}
	}

	/**
	 * Adiciona a uma lista os métodos defeituosos, consoante a ferramenta
	 * utilizada. Utilizado internamente pelo método iteradorSheetFerramenta().
	 * 
	 * @param ferramenta Representa a ferramenta utilizada.
	 * @param row        Representa a linha(método) em questão.
	 * @author Lucas Oliveira.
	 */

	private void metodosFerramenta(String ferramenta, Row row) {
		if (ferramenta.equals("iPlasma")) {
			Boolean value = row.getCell(iPlasma).getBooleanCellValue();
			if (value)
				methods.add(row.getRowNum());
		}
		if (ferramenta.equals("PMD")) {
			Boolean value = row.getCell(pmd).getBooleanCellValue();
			if (value)
				methods.add(row.getRowNum());
		}
	}

	/**
	 * Guarda os indicadores de qualidade de uma dada ferramenta numa matriz
	 * Utilizado internamente pelo método runAlgoritmo().
	 * 
	 * @param ferramenta Representa a ferramenta selecionada.
	 * @author Lucas Oliveira.
	 */
	private void checkIndicadoresFerramenta(String ferramenta) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean valorFerramenta = value1(ferramenta, row);
				if (value && valorFerramenta)
					indicadores.put("DCI", indicadores.get("DCI") + 1);
				else if (!value && valorFerramenta)
					indicadores.put("DII", indicadores.get("DII") + 1);
				else if (!value && !valorFerramenta)
					indicadores.put("ADCI", indicadores.get("ADCI") + 1);
				else if (value && !valorFerramenta)
					indicadores.put("ADII", indicadores.get("ADII") + 1);
			}
		}

		indicadoresQualidade = mapToMatrix(indicadores);
	}

	/**
	 * Indica se existe code smell num dado método, consoante a ferramenta
	 * utilizada. Utilizado internamente pelo método checkIndicadoresFerramenta().
	 * 
	 * @param ferramenta Representa a ferramenta a ser utilizada.
	 * @param row        Representa a linha(método) a ser verificado.
	 * @return Boolean
	 */

	private Boolean value1(String ferramenta, Row row) {
		Boolean value1 = false;
		if (ferramenta.equals("iPlasma"))
			value1 = row.getCell(iPlasma).getBooleanCellValue();
		else
			value1 = row.getCell(pmd).getBooleanCellValue();
		return value1;
	}

	/**
	 * Utilizado externamente de forma a obter os indicadores de qualidade após a
	 * utilização do algoritmo.
	 * 
	 * @return String[][] Matriz de strings com os indicadores de qualidade.
	 * @author Lucas Oliveira
	 */
	public String[][] getIndicadores() {
		return indicadoresQualidade;
	}

	/**
	 * Utilizado externamente de forma a obter os métodos defeituosos após a
	 * utilização do algoritmo.
	 * 
	 * @return String[][] Matriz de strings com os métodos defeituosos.
	 * @author Lucas Oliveira
	 */
	public String[][] getMetodos() {
		return metodos;
	}

	/**
	 * Converte um Map<String, Integer> numa matriz de Strings. Utilizado
	 * internamente pelos métodos checkIndicadoresFerramenta() e
	 * checkIndicadoresRegra().
	 * 
	 * @param aux Map<String, Integer>
	 * @return String[][] Devolve uma matriz de strings.
	 * 
	 * @author Lucas Oliveira
	 * @author Francisco Mendes
	 *
	 */
	private String[][] mapToMatrix(Map<String, Integer> aux) {

		int c = 0;
		int l = 0;
		String[][] aux1 = new String[aux.size()][2];

		for (String i : aux.keySet()) {
			c = 0;
			aux1[l][c] = i;
			c++;
			aux1[l][c] = aux.get(i).toString();
			l++;
		}
		return aux1;
	}

	/**
	 * Converte um ArrayList de inteiros numa matriz de Strings. Utilizado
	 * internamente pelos métodos retMetodos() e retMetodosRegra().
	 * 
	 * @param aux ArrayList de inteiros
	 * @return String[][] Matriz de strings.
	 * 
	 * @author Lucas Oliveira
	 * @author Francisco Mendes
	 */

	private String[][] arrayToMatrix(ArrayList<Integer> aux) {
		int l = 0;
		String[][] aux1 = new String[aux.size()][1];

		for (Integer i : aux) {
			aux1[l][0] = i.toString();
			l++;
		}
		return aux1;
	}

}