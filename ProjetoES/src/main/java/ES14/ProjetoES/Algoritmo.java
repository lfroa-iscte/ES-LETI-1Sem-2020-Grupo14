package ES14.ProjetoES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Algoritmo {

	private static int isLong = 8;
	private static int iPlasma = 9;
	private static int pmd = 10;
	
	private ArrayList<Integer> methods;
	private HashMap<String, Integer> indicadores;

	private Sheet sheet;

	public Algoritmo(Sheet sheet, String ferramenta) {
		this.sheet = sheet;
		methods=new ArrayList<>();
		indicadores=new HashMap<>();
		int ferramentaN=-1;
		
		if(ferramenta.equals("iPlasma"))
			ferramentaN=0;
		else if(ferramenta.equals("PMD"))
				ferramentaN = 1;
		
		
		retMethods(ferramentaN);
		checkDci(ferramentaN);
		checkDii(ferramentaN);
		checkAdci(ferramentaN);
		checkAdii(ferramentaN);
	}
	
	private void retMethods(int ferramenta) {
		
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				if(ferramenta==0) {
					Boolean value = row.getCell(iPlasma).getBooleanCellValue();
					if(value)
						methods.add(row.getRowNum());
				}
				if(ferramenta==1) {
					Boolean value = row.getCell(pmd).getBooleanCellValue();
					if(value)
						methods.add(row.getRowNum());
				}
			}
		}
	}

	private void checkDci(int ferramenta) {
		int i=0;
		indicadores.put("DCI", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();
				
				
				if (ferramenta==0 && value == true && value1 == true)
					indicadores.put("DCI", i++);

				if (ferramenta==1 && value == true && value2 == true)
					indicadores.put("DCI", i++);
			}
		}
	}

	private void checkDii(int ferramenta) {
		int i=0;
		indicadores.put("DII", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();
				
		
				if (ferramenta==0 && value == false && value1 == true)
					indicadores.put("DII", i++);

				if (ferramenta==1 && value == false && value2 == true)
					indicadores.put("DII", i++);
			}
		}
	}

	private void checkAdci(int ferramenta) {
		int i=0;
		indicadores.put("ADCI", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (ferramenta==0 && value == false && value1 == false)
					indicadores.put("ADCI", i++);

				if (ferramenta==1 && value == false && value2 == false)
					indicadores.put("ADCI", i++);
			}
		}
	}
	
	private void checkAdii(int ferramenta) {
		int i=0;
		indicadores.put("ADII", 0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (ferramenta==0 && value == true && value1 == false)
					indicadores.put("ADII", i++);

				if (ferramenta==1 && value == true && value2 == false)
					indicadores.put("ADII", i++);
			}
		}
	}
	
	public ArrayList<Integer> getMethods(){
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
			Algoritmo alg=new Algoritmo(sheet, "PMD");
			
			HashMap<String, Integer> temp= alg.getIndicadores();
			for(String i: temp.keySet())
		    	System.out.println(i+ "->" + temp.get(i));
		    
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
