package ES14.ProjetoES;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GUI {

	private JFrame janela;
	private XSSFSheet sheet;
	private String[] headers;
	private String[][] data;
	private File selectedFile;
	private TableModel table;

	public GUI() {
		janela = new JFrame("DetetorDefeitos3000");
		janela.pack();
		janela.setSize(1200, 900);
		janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		addFrameContent();

	}

	private void addFrameContent() {

		final JPanel ferramentas = new JPanel(new BorderLayout()); // alterar

		JPanel botoes = new JPanel(new FlowLayout());

		String[] flags = { "PMD", "iPlasma", "Regra a definir" };

		final JComboBox flags2 = new JComboBox<String>(flags);

		JButton importar = new JButton("Importar Excel");

		importar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					selectedFile = chooser.getSelectedFile();
					String excelPath = selectedFile.getAbsolutePath();

					try {

						readExcel(excelPath);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					// criar janela Excel
					JFrame janelaExcel = new JFrame(selectedFile.getName());
					janelaExcel.pack();
					janelaExcel.setSize(1200, 900);
					janelaExcel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					table = new DefaultTableModel(data, headers);

					JTable tabela = new JTable(table);
					JScrollPane center = new JScrollPane(tabela);

					janelaExcel.add(center);

					janelaExcel.setVisible(true);
				}

			}
		});

		JButton detetar = new JButton("Detetar Defeitos");

		detetar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String regraSelecionada = (String) flags2.getSelectedItem(); // falta o caso de "regra a definir"

				JPanel painelMetodos = new JPanel(new FlowLayout()); // experimentar com gridLayout
				JPanel painelResultados = new JPanel(new FlowLayout());

				// teste exemplo
				String data[][] = { { "101" }, { "102" }, { "101" } };
				String column[] = { "ID" };
				JTable jt = new JTable(data, column);
				
				String data1[][] = { { "101", "14", "18", "70" } };
				String column1[] = { "ADI", "ADC", "ADCI", "ADII" };
				JTable jt1 = new JTable(data1, column1);
				
				painelMetodos.add(jt);
				painelResultados.add(jt1);

				JPanel painelAux = new JPanel(new GridLayout(1, 2));
				painelAux.add(painelMetodos);
				painelAux.add(painelResultados);
				ferramentas.add(painelAux, BorderLayout.CENTER);
				janela.setVisible(true);
			}

		});

		janela.setLayout(new BorderLayout());

		botoes.add(flags2);
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

	private void readExcel(String path) throws Exception {
		XSSFWorkbook workbook = new XSSFWorkbook(new File(path));
		sheet = workbook.getSheetAt(0);

		DataFormatter dataFormatter = new DataFormatter();
		headers = new String[sheet.getRow(0).getLastCellNum()];
		data = new String[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

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

	// colunas especificas para a tabela na GUI
	private TableModel compileTable(TableModel table) {

		TableModel table1 = new DefaultTableModel(table.getRowCount(), 1);

		for (int i = 0; i < table.getRowCount(); i++) {
			table1.setValueAt(table.getValueAt(i, 0), i, 0);
			// table1.setValueAt(table.getValueAt(i, 10), i, 1);
			// table1.setValueAt(table.getValueAt(i, 11), i, 2);

			if (table1.getValueAt(i, 1) == "TRUE") {
				table1.setValueAt("DETETADO", i, 1);
			} else if (table1.getValueAt(i, 1) == "FALSE") {
				table1.setValueAt("NÃO DETETADO", i, 1);
			}

			if (table1.getValueAt(i, 2) == "TRUE") {
				table1.setValueAt("DETETADO", i, 2);
			} else if (table1.getValueAt(i, 2) == "FALSE") {
				table1.setValueAt("NÃO DETETADO", i, 2);
			}
		}

		return table1;
	}

}
