package ES14.ProjetoES;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class Algoritmo {

	private static int isLong = 8;
	private static int iPlasma= 9;
	private static int pmd = 10;
	
	private int dciPlasma;
	public int getDciPlasma() {
		return dciPlasma;
	}

	public int getDciPmd() {
		return dciPmd;
	}

	private int dciPmd;
		
	private int diiPlasma;
	private int diiPmd;
		
	private int adciPlasma;
	private int adciPmd;
		
	private int adiiPlasma;
	private int adiiPmd;
	
	private Sheet sheet;
	
	public Algoritmo(Sheet sheet) {
		this.sheet=sheet;
		checkDci();
	}
	
	private void checkDci() {
		
		Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            
            if(row.getRowNum()!=0) {
            Boolean value = row.getCell(isLong).getBooleanCellValue();
            Boolean value1 = row.getCell(iPlasma).getBooleanCellValue();
            Boolean value2 = row.getCell(pmd).getBooleanCellValue();
            
            
            if(value==true && value1==true) 
            	dciPlasma++;
            
            if(value ==true && value2==true)
            	dciPmd++;
            }
        }
	}
	
	public static void main(String[] args) {
		String path = new String("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx");
		try {
			Workbook workbook = WorkbookFactory.create(new File(path));
			Sheet sheet = workbook.getSheetAt(0);
			Algoritmo alg = new Algoritmo(sheet);
			System.out.println("iPlasma -> " + alg.getDciPlasma() + " " + "PMD -> " + alg.getDciPmd());
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
