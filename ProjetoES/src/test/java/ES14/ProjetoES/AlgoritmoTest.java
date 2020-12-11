package ES14.ProjetoES;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import junit.framework.TestCase;

public class AlgoritmoTest extends TestCase {

	public AlgoritmoTest() {
		super();
		
	}
	@Test
	public void testPlasma() {
		App app=new App();
		app.main(null);
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new File("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx"));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		Algoritmo alg=new Algoritmo(sheet);
		alg.runAlgoritmo("iPlasma",null);
		String[][] temp= {{"DCI", "140"}, {"DII", "0"}, {"ADCI", "280"}, {"ADII", "0"}};
		String[][] algLista=alg.getIndicadores();
		assertArrayEquals(temp,algLista);
	}
	
	@Test
	public void testPMD() {
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new File("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx"));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		Algoritmo alg=new Algoritmo(sheet);
		alg.runAlgoritmo("PMD",null);
		String[][] temp= {{"DCI", "140"}, {"DII", "18"}, {"ADCI", "262"}, {"ADII", "0"}};
		String[][] algLista=alg.getIndicadores();
		assertArrayEquals(temp,algLista);
	}
	
	@Test
	public void testRegraLong() {
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new File("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx"));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		Algoritmo alg=new Algoritmo(sheet);
		Regra r=new Regra("LOC", ">", 300, "AND");
		Regra r1=new Regra("CYCLO", ">", 10, "OR");
		List<Regra> regras=new ArrayList();
		regras.add(r);
		regras.add(r1);
		alg.runAlgoritmo("LongMethod",regras);
		String[][] temp= {{"DCI", "22"}, {"DII", "0"}, {"ADCI", "280"}, {"ADII", "118"}};
		String[][] algLista=alg.getIndicadores();
		assertArrayEquals(temp,algLista);
	}
	public void testRegraLong1() {
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new File("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx"));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		Algoritmo alg=new Algoritmo(sheet);
		Regra r=new Regra("LOC", ">", 300, "OR");
		Regra r1=new Regra("CYCLO", "<=", 10, null);
		List<Regra> regras=new ArrayList();
		regras.add(r);
		regras.add(r1);
		alg.runAlgoritmo("LongMethod",regras);
		String[][] temp= {{"DCI", "24"}, {"DII", "272"}, {"ADCI", "8"}, {"ADII", "116"}};
		String[][] algLista=alg.getIndicadores();
		assertArrayEquals(temp,algLista);
	}
	

	@Test
	public void testRegraFeatureEnvy() {
		XSSFWorkbook workbook=null;
		try {
			workbook = new XSSFWorkbook(new File("C:\\Users\\lucas\\Documents\\Faculdade 2020\\ES\\Defeitos.xlsx"));
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheetAt(0);
		Algoritmo alg=new Algoritmo(sheet);
		Regra r=new Regra("ATFD", "<", 20, "AND");
		Regra r1=new Regra("LAA", ">=", 0.5, null);
		List<Regra> regras=new ArrayList();
		regras.add(r);
		regras.add(r1);
		alg.runAlgoritmo("FeatureEnvy",regras);
		String[][] temp= {{"DCI", "0"}, {"DII", "184"}, {"ADCI", "122"}, {"ADII", "114"}};
		String[][] algLista=alg.getIndicadores();
		assertArrayEquals(temp,algLista);
	}

}
