package ES14.ProjetoES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Algoritmo {

	private static int isLong = 8;
	private static int iPlasma = 9;
	private static int pmd = 10;

	private List<Indicador> indicadoresPlasma;
	private List<Indicador> indicadoresPmd;

	private Sheet sheet;

	public Algoritmo(Sheet sheet) {
		this.sheet = sheet;
		indicadoresPlasma = new ArrayList<>();
		indicadoresPmd = new ArrayList<>();
		checkDci();
		checkDii();
		checkAdci();
		checkAdii();
	}

	private void checkDci() {

		Iterator<Row> rowIterator = sheet.iterator();
		int c = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Indicador add1=new Indicador();
				Indicador add2=new Indicador();
				indicadoresPlasma.add(add1);
				indicadoresPmd.add(add2);
				
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (value == true && value1 == true)
					indicadoresPlasma.get(c).setDci(1);

				if (value == true && value2 == true)
					indicadoresPmd.get(c).setDci(1);
				c++;
			}
		}
	}

	private void checkDii() {

		Iterator<Row> rowIterator = sheet.iterator();
		int c = 0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (value == false && value1 == true)
					indicadoresPlasma.get(c).setDii(1);

				if (value == false && value2 == true)
					indicadoresPmd.get(c).setDii(1);
				c++;
			}
		}
	}

	private void checkAdci() {

		Iterator<Row> rowIterator = sheet.iterator();
		int c=0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (value == false && value1 == false)
					indicadoresPlasma.get(c).setAdci(1);

				if (value == false && value2 == false)
					indicadoresPmd.get(c).setAdci(1);
				c++;
			}
		}
	}

	private void checkAdii() {

		Iterator<Row> rowIterator = sheet.iterator();
		int c=0;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			if (row.getRowNum() != 0) {
				Boolean value = row.getCell(isLong).getBooleanCellValue();
				Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
				Boolean value2 = row.getCell(pmd).getBooleanCellValue();

				if (value == true && value1 == false)
					indicadoresPlasma.get(c).setAdii(1);

				if (value == true && value2 == false)
					indicadoresPmd.get(c).setAdii(1);
				c++;
			}
		}
	}

	public List<Indicador> getIndicadoresPlasma() {
		return indicadoresPlasma;
	}

	public List<Indicador> getIndicadoresPmd() {
		return indicadoresPmd;
	}

	public static void main(String[] args) {
		String path = new String("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx");
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			Sheet sheet = workbook.getSheetAt(0);

			Algoritmo alg = new Algoritmo(sheet);
			for(int i=0; i<alg.getIndicadoresPlasma().size(); i++) {
				System.out.println("Method ID: "+(i+1)  + " | iPlasma -> " + alg.getIndicadoresPlasma().get(i).getDii() + " " + "PMD -> " + alg.getIndicadoresPmd().get(i).getDii() );
			}
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
