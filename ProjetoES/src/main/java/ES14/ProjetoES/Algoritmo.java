package ES14.ProjetoES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

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

	private Sheet sheet;

	public Algoritmo(Sheet sheet) {
		this.sheet = sheet;
		methods = new ArrayList<>();
		indicadores = new LinkedHashMap<>();
		indicadores.put("DCI", 0);
		indicadores.put("DII", 0);
		indicadores.put("ADCI", 0);
		indicadores.put("ADII", 0);
	}

	public void runAlgoritmo(String ferramenta, List<Regra> regras) {

		if (ferramenta.equals("PMD") || ferramenta.equals("iPlasma")) {
			retMetodos(ferramenta);
			checkIndicadoresFerramenta(ferramenta);
		} else {
			retMetodosRegra(regras);
			checkIndicadoresRegra(ferramenta);
		}
	}

	private void checkIndicadoresRegra(String ferramenta) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = false;
				if (ferramenta.equals("FeatureEnvy"))
					value = row.getCell(featureEnvy).getBooleanCellValue();
				else
					value = row.getCell(isLong).getBooleanCellValue();

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
	}

	private void retMetodosRegra(List<Regra> regras) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				checkForSmell(row, regras);

			}
		}
	}

	private void checkForSmell(Row row, List<Regra> regras) {
		boolean smell = false;
		for (Regra i : regras) {

			int cell = metrica(i.getMetrica());
			if (regras.indexOf(i) == 0 || regras.get(regras.indexOf(i) - 1).getOpLogico().equals("OR")
					|| (smell && regras.get(regras.indexOf(i) - 1).getOpLogico().equals("AND"))) {
				if (i.getOp().equals(">") && row.getCell(cell).getNumericCellValue() > i.getValor())
					smell = true;

				else if (i.getOp().equals(">=") && row.getCell(cell).getNumericCellValue() >= i.getValor())
					smell = true;

				else if (i.getOp().equals("<") && row.getCell(cell).getNumericCellValue() < i.getValor())
					smell = true;

				else
					smell = false;
			}
		}
		if (smell == true)
			methods.add(row.getRowNum());
	}

	private int metrica(String metrica) {
		if (metrica.equals("LOC"))
			return loc;
		if (metrica.equals("CYCLO"))
			return cyclo;
		if (metrica.equals("ATFD"))
			return atfd;

		return laa;

	}

	private void retMetodos(String ferramenta) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
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
		}
	}

	private void checkIndicadoresFerramenta(String ferramenta) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = false;

				if (ferramenta.equals("iPlasma"))
					value1 = row.getCell(iPlasma).getBooleanCellValue();
				else
					value1 = row.getCell(pmd).getBooleanCellValue();

				if (value && value1)
					indicadores.put("DCI", indicadores.get("DCI") + 1);
				else if (!value && value1)
					indicadores.put("DII", indicadores.get("DII") + 1);
				else if (!value && !value1)
					indicadores.put("ADCI", indicadores.get("ADCI") + 1);
				else if (value && !value1)
					indicadores.put("ADII", indicadores.get("ADII") + 1);
			}
		}
	}

	public ArrayList<Integer> getMethods() {
		return methods;
	}

	public Map<String, Integer> getIndicadores() {
		return indicadores;
	}

	public static void main(String[] args) {
		String path = new String("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx");
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			Sheet sheet = workbook.getSheetAt(0);
			List<Regra> regras = new ArrayList<>();
			Regra um = new Regra("LOC", ">", 300, "AND");
			Regra dois = new Regra("CYCLO", ">", 50, null);
			// Regra tres = new Regra("LOC", ">", 260, null);
			regras.add(um);
			regras.add(dois);
			// regras.add(tres);
			Algoritmo alg = new Algoritmo(sheet);

			alg.runAlgoritmo("LongMethod", regras);

			/*
			 * List<Integer> lista = alg.getMethods(); for (int i : lista) {
			 * System.out.println(i); }
			 */

			Map<String, Integer> temp = alg.getIndicadores();
			for (String i : temp.keySet())
				System.out.println(i + "->" + temp.get(i));

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
