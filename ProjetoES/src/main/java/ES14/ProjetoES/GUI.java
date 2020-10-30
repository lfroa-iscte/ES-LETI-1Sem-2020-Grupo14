package ES14.ProjetoES;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

public class GUI {

	private JFrame janela;
	private XSSFSheet sheet;
	private String[] headers;
	private String[][] data;

	public GUI() {
		janela = new JFrame("DetetorDefeitos3000");
		janela.pack();
		janela.setSize(1200, 900);
		janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		addFrameContent();

	}

	private void addFrameContent() {

		JPanel ferramentas = new JPanel(new FlowLayout()); // alterar

		// paineis terão que que ser criados apenas aquando da execução do algoritmo
		JPanel pmd = new JPanel(new BorderLayout());
		JPanel iPlasma = new JPanel();

		JPanel botoes = new JPanel(new FlowLayout());

		JButton importar = new JButton("Importar Excel");

		importar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(
						"C:\\Users\\fnpm\\OneDrive\\Ambiente de Trabalho\\ISCTE\\3ºAno\\1º Semestre\\ES\\Defeitos.xlsx");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selectedFile = chooser.getSelectedFile();
					String excelPath = selectedFile.getAbsolutePath();

					try {
						readExcel(excelPath);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					JFrame janelaExcel = new JFrame(selectedFile.getName());
					janelaExcel.pack();
					janelaExcel.setSize(1200, 900);
					janelaExcel.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

					TableModel table = new DefaultTableModel(data, headers);

					JTable tabela = new JTable(table);
					JScrollPane center = new JScrollPane(tabela);

					janelaExcel.add(center);

					janelaExcel.setVisible(true);

				}

			}
		});

		JButton detetar = new JButton("Detetar Defeitos");

		janela.setLayout(new BorderLayout());
		ferramentas.add(pmd);
		ferramentas.add(iPlasma);

		botoes.add(detetar);
		botoes.add(importar);

		janela.add(ferramentas, BorderLayout.CENTER);
		janela.add(botoes, BorderLayout.SOUTH);

	}

	public void open() {
		janela.setVisible(true);
	}

	public static void main(String[] args) {
		new GUI().open();
	}

	public void readExcel(String path) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
		sheet = workbook.getSheetAt(0);

		DataFormatter dataFormatter = new DataFormatter();
		headers = new String[12];
		data = new String[sheet.getLastRowNum()][12];

		for (Row row : sheet) {
			for (Cell cell : row) {
				String cellValue = dataFormatter.formatCellValue(cell);
				if (cell.getRowIndex() != 0) {
					data[row.getRowNum() - 1][cell.getColumnIndex()] = cellValue;
				} else {
					headers[cell.getColumnIndex()] = cellValue;
				}
			}
		}
	}
}
