package ES14.ProjetoES;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.ListAutoNumber;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GUI {

	private JFrame janela;
	private XSSFSheet sheet;
	private String[] headers;
	private String[][] data;
	private File selectedFile;
	private TableModel table;
	private JPanel painelAux;
	private Algoritmo alg;
	private JDialog janelaRegras;
	private String ferramentaSelecionada;
	private JTextField treshold;
	private JComboBox<String> metricas;
	private JComboBox<String> operador;
	private JTextArea regras;
	private JPanel secondPanel;
	private JPanel thirdPanel;
	private JPanel mainPanel;
	private ArrayList<Regra> listaRegras;
	private ArrayList<JComboBox<String>> combos;

	/* verifica que não é a primeira vez a adicionar o painel dos metodos */
	private boolean aux = false;

	public GUI() {
		janela = new JFrame("DetetorDefeitos3000");
		janela.pack();
		janela.setSize(1200, 900);
		janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
		addFrameContent();

	}

	private void addFrameContent() {

		final JPanel ferramentas = new JPanel(new BorderLayout());

		JPanel botoes = new JPanel(new FlowLayout());

		JButton importar = new JButton("Importar");

		importar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser("C:\\Users\\fnpm\\OneDrive\\ISCTE\\3ºAno\\1º Semestre\\ES");
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
				if (aux) {
					ferramentas.remove(painelAux);
				}

				alg = new Algoritmo(sheet);

				if (ferramentaSelecionada.equals("PMD") || ferramentaSelecionada.equals("iPlasma")) {
					alg.runAlgoritmo(ferramentaSelecionada, null);
				} else {
					alg.runAlgoritmo(ferramentaSelecionada, listaRegras);
				}

				String[] header = { "MethodID" };
				String[] headerIndicadores = { "Indicador", "Valor" };

				painelAux = new JPanel(new GridLayout(1, 2));
				JPanel painelMetodos = new JPanel(new FlowLayout());
				JPanel painelResultados = new JPanel(new FlowLayout());

				JTable tabelaMethodID = new JTable(arrayToMatrix(alg.getMethods()), header);
				JTable tabelaIndicadores = new JTable(mapToMatrix(alg.getIndicadores()), headerIndicadores);

				JScrollPane scroll = new JScrollPane(tabelaMethodID);
				JScrollPane scroll1 = new JScrollPane(tabelaIndicadores);

				painelResultados.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Indicadores de Qualidade", TitledBorder.CENTER, TitledBorder.TOP));
				painelMetodos.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Métodos com Defeitos", TitledBorder.CENTER, TitledBorder.TOP));

				painelMetodos.add(scroll);
				painelResultados.add(scroll1);

				painelAux.add(painelMetodos);
				painelAux.add(painelResultados);
				ferramentas.add(painelAux, BorderLayout.CENTER);

				janela.setVisible(true);

				aux = true;
			}

		});

		JButton escolherRegra = new JButton("Escolher Regra");

		escolherRegra.addActionListener(new ActionListener() {
			JDialog ruleDialog = new JDialog(janela, "Escolher Regra");
			ButtonGroup g1 = new ButtonGroup();
			JRadioButton pmd = new JRadioButton("PMD");
			JRadioButton iPlasma = new JRadioButton("iPlasma");
			JRadioButton longMethod = new JRadioButton("Definir regra - LongMethod");
			JRadioButton featureEnvy = new JRadioButton("Definir regra - FeatureEnvy");
			JButton confirmarRegra = new JButton("OK");
			JPanel aux_confirmar = new JPanel(new FlowLayout());
			JLabel erro = new JLabel();
			JDialog erroDialog = new JDialog();

			@Override
			public void actionPerformed(ActionEvent e) {

				ruleDialog.pack();
				ruleDialog.setSize(200, 350);
				ruleDialog.setLocation(janela.getWidth() / 2 - 100, janela.getHeight() / 2 - 125);
				ruleDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ruleDialog.setLayout(new GridLayout(5, 1));

				aux_confirmar.add(confirmarRegra);

				g1.add(pmd);
				g1.add(iPlasma);
				g1.add(longMethod);
				g1.add(featureEnvy);

				ruleDialog.add(pmd);
				ruleDialog.add(iPlasma);
				ruleDialog.add(longMethod);
				ruleDialog.add(featureEnvy);
				ruleDialog.add(aux_confirmar);

				ruleDialog.setVisible(true);

				erroDialog.add(erro);
				erroDialog.setSize(250, 150);
				erroDialog.setLocation(janela.getWidth() / 2 - 100, janela.getHeight() / 2 - 125);

				confirmarRegra.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						if (pmd.isSelected()) {
							ferramentaSelecionada = pmd.getText();
							ruleDialog.dispose();
						} else if (iPlasma.isSelected()) {
							ferramentaSelecionada = iPlasma.getText();
							ruleDialog.dispose();
						} else if (longMethod.isSelected()) {
							ferramentaSelecionada = "LongMethod";
							setPopUp(longMethod.getText());
							ruleDialog.dispose();
						} else if (featureEnvy.isSelected()) {
							ferramentaSelecionada = "FeatureEnvy";
							setPopUp(featureEnvy.getText());
							ruleDialog.dispose();
						}
						g1.clearSelection();

					}
				});
			}
		});

		janela.setLayout(new BorderLayout());

		botoes.add(escolherRegra);
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

	private String[][] arrayToMatrix(ArrayList<Integer> aux) {
		int l = 0;
		String[][] aux1 = new String[aux.size()][1];

		for (Integer i : aux) {
			aux1[l][0] = i.toString();
			l++;
		}
		return aux1;
	}

	private String[][] mapToMatrix(HashMap<String, Integer> aux) {
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

	// Definicao de Regras
	private void setPopUp(String aux) {
		listaRegras = new ArrayList<Regra>();
		regras = new JTextArea();
		combos = new ArrayList<JComboBox<String>>();

		janelaRegras = new JDialog(janela, aux);
		janelaRegras.pack();
		janelaRegras.setSize(800, 600);
		janelaRegras.setLocation(janela.getWidth() / 2 - 400, janela.getHeight() / 2 - 300);
		janelaRegras.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JButton addTreshold = new JButton("+");
		JButton reset = new JButton("Reset");
		JButton confirmar_tresholds = new JButton("Confirmar");
		JButton checkR = new JButton("OK");

		mainPanel = new JPanel(new BorderLayout());
		secondPanel = new JPanel(new BorderLayout());
		GridBagLayout layout = new GridBagLayout();
		JPanel tresholds = new JPanel(layout);
		JPanel checkRule = new JPanel(new FlowLayout());
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JPanel button_aux = new JPanel(new FlowLayout());

		String[] longMethod = { "LOC", "CYCLO" };
		String[] featureEnvy = { "ATFD", "LAA" };
		String[] operadores = { "<", ">", "<=", ">=" };

		metricas = new JComboBox<String>();
		operador = new JComboBox<String>(operadores);

		thirdPanel = new JPanel(new FlowLayout());

		if (aux.equals("Definir regra - LongMethod")) {
			for (String string : longMethod) {
				metricas.addItem(string);
			}
		} else if (aux.equals("Definir regra - FeatureEnvy")) {
			for (String string : featureEnvy) {
				metricas.addItem(string);
			}
		}

		treshold = new JTextField();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.ipadx = 30;
		gbc.ipady = 5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		tresholds.add(metricas, gbc);

		gbc.ipadx = 30;
		gbc.ipady = 5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		tresholds.add(operador, gbc);

		gbc.ipadx = 50;
		gbc.ipady = 10;
		gbc.gridx = 2;
		gbc.gridy = 0;
		tresholds.add(treshold, gbc);

		addTreshold.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				regras.setVisible(true);
				regras.setEditable(false);
				regras.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Tresholds Definidas", TitledBorder.CENTER, TitledBorder.TOP));

				regras.setText(regras.getText() + metricas.getSelectedItem().toString() + " "
						+ operador.getSelectedItem().toString() + " " + treshold.getText() + "\n");
				secondPanel.add(regras, BorderLayout.CENTER);

				listaRegras.add(new Regra(metricas.getSelectedItem().toString(), operador.getSelectedItem().toString(),
						Integer.parseInt(treshold.getText()), null));

				janelaRegras.setVisible(true);

			}
		});

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				regras.setText(null);
				regras.setVisible(false);
				listaRegras.clear();
				thirdPanel.removeAll();
			}
		});

		confirmar_tresholds.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				thirdPanel.removeAll();

				int l = listaRegras.size() * 2 - 1;
				String[] temp = new String[listaRegras.size()];
				String[] opLogico = { "OR", "AND" };
				int i = 0;
				for (Regra r : listaRegras) {
					temp[i] = r.getMetrica() + r.getOp() + r.getValor();
					i++;
				}

				JComboBox<String> combo = new JComboBox<String>(temp);
				JComboBox<String> comboOperadores = new JComboBox<String>(opLogico);
				for (int t = 0; t < l; t++) {
					if (t % 2 == 0) {
						combos.add(combo);
						thirdPanel.add(new JComboBox<String>(temp));
					} else {
						combos.add(comboOperadores);
						thirdPanel.add(new JComboBox<String>(opLogico));
					}
				}

				thirdPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Regra Definida", TitledBorder.CENTER, TitledBorder.TOP));
				mainPanel.add(thirdPanel, BorderLayout.CENTER);
				janelaRegras.setVisible(true);
			}
		});

		checkR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Regra> aux = new ArrayList<Regra>();

				for (int i = 0; i < combos.size(); i += 2) {
					for (Regra r : listaRegras) {
						if (i != combos.size() - 1) {
							if (r.checkRule(combos.get(i).getSelectedItem().toString())) {
								r.setOpLogico(combos.get(i + 1).getSelectedItem().toString());
								aux.add(r);
							}
						} else {
							aux.add(r);
						}
					}
				}

				listaRegras = aux;
				janelaRegras.dispose();

			}
		});

		tresholds.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Definição de Tresholds", TitledBorder.CENTER, TitledBorder.TOP));

		button_aux.add(addTreshold);
		button_aux.add(reset);
		button_aux.add(confirmar_tresholds);
		buttonPanel.add(button_aux, BorderLayout.WEST);
		secondPanel.add(tresholds, BorderLayout.WEST);
		secondPanel.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

		checkRule.add(checkR);
		mainPanel.add(checkRule, BorderLayout.SOUTH);
		mainPanel.add(secondPanel, BorderLayout.NORTH);
		janelaRegras.add(mainPanel);

		janelaRegras.setVisible(true);

	}

}
