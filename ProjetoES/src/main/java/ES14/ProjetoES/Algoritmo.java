package ES14.ProjetoES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Algoritmo {

	private static int loc = 4;
	private static int cycle = 5;
	private static int atfd = 6;
	private static int laa = 7;
	private static int isLong = 8;
	private static int iPlasma = 9;
	private static int pmd = 10;

	private ArrayList<Integer> methods;
	private HashMap<String, Integer> indicadores;

	private Sheet sheet;

	public Algoritmo(Sheet sheet) {
		this.sheet = sheet;
		methods = new ArrayList<>();
		indicadores = new HashMap<>();
		indicadores.put("DCI", 0);
		indicadores.put("DII", 0);
		indicadores.put("ADCI", 0);
		indicadores.put("ADII", 0);
	}

	public void runAlgoritmo(String ferramenta, List<Regra> regras) {
		int ferramentaN = -1;

		if (ferramenta.equals("iPlasma"))
			ferramentaN = 0;
		else if (ferramenta.equals("PMD"))
			ferramentaN = 1;

		if (ferramentaN != -1) {
			retMetodos(ferramentaN);
			checkDci(ferramentaN);
			checkDii(ferramentaN);
			checkAdci(ferramentaN);
			checkAdii(ferramentaN);
		} else {
			retMetodosRegra(regras);
			checkIndicadoresRegra();
		}
	}

	

	private void checkIndicadoresRegra() {
	
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				
				if (value==true && methods.contains(row.getRowNum())) 
					indicadores.put("DCI", indicadores.get("DCI")+1);
				else if(value==false && methods.contains(row.getRowNum()))
						indicadores.put("DII", indicadores.get("DII")+1);
				else if(value==false && !methods.contains(row.getRowNum()))
						indicadores.put("ADCI",indicadores.get("ADCI")+1);
				else if(value==true && !methods.contains(row.getRowNum()))
						indicadores.put("ADII", indicadores.get("ADII")+1);
					
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
					|| (smell == true && regras.get(regras.indexOf(i) - 1).getOpLogico().equals("AND"))) {
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
		if (metrica.equals("CYCLE"))
			return cycle;
		if (metrica.equals("ATFD"))
			return atfd;

		return laa;

	}

	private void retMetodos(int ferramenta) {

		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				if (ferramenta == 0) {
					Boolean value = row.getCell(iPlasma).getBooleanCellValue();
					if (value)
						methods.add(row.getRowNum());
				}
				if (ferramenta == 1) {
					Boolean value = row.getCell(pmd).getBooleanCellValue();
					if (value)
						methods.add(row.getRowNum());
				}
			}
		}
	}
	
	/*private void checkIndicadoresFerramenta(int ferramenta) {
		int i=0;
		
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();
				if
			}
		}
	}*/

	private void checkDci(int ferramenta) {
		int i = 0;
		indicadores.put("DCI", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (ferramenta == 0 && value == true && value1 == true)
					indicadores.put("DCI", i++);

				if (ferramenta == 1 && value == true && value2 == true)
					indicadores.put("DCI", i++);
			}
		}
	}

	private void checkDii(int ferramenta) {
		int i = 0;
		indicadores.put("DII", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (ferramenta == 0 && value == false && value1 == true)
					indicadores.put("DII", i++);

				if (ferramenta == 1 && value == false && value2 == true)
					indicadores.put("DII", i++);
			}
		}
	}

	private void checkAdci(int ferramenta) {
		int i = 0;
		indicadores.put("ADCI", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (ferramenta == 0 && value == false && value1 == false)
					indicadores.put("ADCI", i++);

				if (ferramenta == 1 && value == false && value2 == false)
					indicadores.put("ADCI", i++);
			}
		}
	}

	private void checkAdii(int ferramenta) {
		int i = 0;
		indicadores.put("ADII", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (ferramenta == 0 && value == true && value1 == false)
					indicadores.put("ADII", i++);

				if (ferramenta == 1 && value == true && value2 == false)
					indicadores.put("ADII", i++);
			}
		}
	}

	public ArrayList<Integer> getMethods() {
		return methods;
	}

	public HashMap<String, Integer> getIndicadores() {
		return indicadores;
	}

	public static void main(String[] args) {
		String path = new String("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx");
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			Sheet sheet = workbook.getSheetAt(0);
			List<Regra> regras = new ArrayList<>();
			Regra um = new Regra("LOC", ">", 300, "AND");
			Regra dois = new Regra("CYCLE", ">", 100, null);
			//Regra tres = new Regra("LOC", ">", 260, null);
			regras.add(um);
			regras.add(dois);
			//regras.add(tres);
			Algoritmo alg = new Algoritmo(sheet);
			
			alg.runAlgoritmo("REGRA", regras);

			List<Integer> lista = alg.getMethods();
			for (int i : lista) {
				System.out.println(i);
			}
			
			 /*HashMap<String, Integer> temp = alg.getIndicadores();
			 for (String i : temp.keySet())
			 System.out.println(i + "->" + temp.get(i));*/

		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
