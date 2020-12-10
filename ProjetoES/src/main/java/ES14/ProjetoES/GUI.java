package ES14.ProjetoES;

/**
 * <h1>GUI<h1>
 * <p>
 * A classe GUI implementa uma interface gráfica disponível para um utilizador de modo a que este possa avaliar a qualidade de deteção de code smells
 * em projetos de software, podendo-se basear em duas ferramentas de deteção (iPlasma e PMD), ou então criando ele próprio as suas regras para a avaliação
 * da qualidade dos code smells long_method e feature_envy.
 * <p>
 * 
 * @author Tomás Santos
 * @author Francisco Mendes
 * @version 1.0
 * @since 2020-10-24
 * 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class GUI {

	private JFrame janela;
	private JDialog janelaRegras;
	private String ferramentaSelecionada;
	private JTextField threshold;
	private JComboBox<String> metricas;
	private JComboBox<String> operador;
	private JTextArea regras;
	private JPanel thresholdsPanel;
	private JPanel rulePanel;
	private JPanel mainPanel;
	private ArrayList<Regra> listaRegras;
	private int contadorRegras;
	private ExcelController excel_controller;

	/**
	 * Cria a janela principal e adiciona os conteúdos a esta.
	 * 
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	public GUI() {
		janela = new JFrame("DetetorDefeitos3000");
		janela.pack();
		janela.setSize(1200, 900);
		janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		janela.setExtendedState(JFrame.MAXIMIZED_BOTH);
		addFrameContent();

	}

	/**
	 * Adiciona os conteúdos à janela principal.
	 * 
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */
	private void addFrameContent() {

		excel_controller = new ExcelController();

		final JPanel ferramentas = new JPanel(new BorderLayout());

		JPanel botoes = new JPanel(new FlowLayout());

		JButton importar = new JButton("Importar");

		importar.addActionListener(new ActionListener() {

			File selectedFile;

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					selectedFile = chooser.getSelectedFile();

					try {
						excel_controller.readExcel(selectedFile.getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					setExcelFrame(selectedFile.getName());
				}

			}
		});

		JButton detetar = new JButton("Detetar Defeitos");

		detetar.addActionListener(new ActionListener() {

			/* verifica que não é a primeira vez a adicionar o painel dos metodos */
			boolean aux = false;
			Algoritmo alg;
			JPanel painelAux;

			@Override
			public void actionPerformed(ActionEvent e) {

				if (excel_controller.getData() == null || ferramentaSelecionada == null) {
					showErrorDialog("Ficheiro não importado ou regra não definida !!!", 350, 125);

				} else {

					if (aux) {
						ferramentas.remove(painelAux);
					}

					alg = new Algoritmo(excel_controller.getSheet());

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

					JTable tabelaMethodID = new JTable(alg.getMetodos(), header);
					JTable tabelaIndicadores = new JTable(alg.getIndicadores(), headerIndicadores);
					tabelaIndicadores.setEnabled(false);
					tabelaMethodID.setEnabled(false);

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

	/**
	 * Coloca a janela principal visível.
	 * 
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	public void open() {
		janela.setVisible(true);
	}

	/**
	 * Cria uma janela maximizada com o nome <i>nome</i> com uma tabela com o
	 * conteúdo lido de um ficheiro excel.
	 * 
	 * 
	 * @param nome
	 * 
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 */

	private void setExcelFrame(String nome) {

		JFrame janelaExcel = new JFrame(nome);
		janelaExcel.pack();
		janelaExcel.setSize(1200, 900);
		janelaExcel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		janelaExcel.setExtendedState(JFrame.MAXIMIZED_BOTH);

		JTable tabela = new JTable(excel_controller.getData(), excel_controller.getHeaders());
		tabela.setEnabled(false);

		JScrollPane center = new JScrollPane(tabela);

		janelaExcel.add(center);

		janelaExcel.setVisible(true);
	}

	/**
	 * Cria e mostra um JDialog centrado referente à janela principal de comprimento
	 * <i>width</i> e <i>height</i> com uma mensagem <i>message</i>.
	 * 
	 * @param message Mensagem a mostrar
	 * @param width   Comprimento do JDialog
	 * @param height  Altura do JDialog
	 * 
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 * 
	 */

	private void showErrorDialog(String message, int width, int height) {
		JLabel error_msg = new JLabel(message);
		JDialog error = new JDialog(janela);
		error.add(error_msg);
		error.setSize(width, height);
		error.setLocation(janela.getWidth() / 2 - 100, janela.getHeight() / 2 - 125);
		error.setVisible(true);

	}

	// Definicao de Regras
	private void setPopUp(String aux) {
		contadorRegras = 0;
		listaRegras = new ArrayList<Regra>();
		regras = new JTextArea();

		janelaRegras = new JDialog(janela, aux);
		janelaRegras.pack();
		janelaRegras.setSize(800, 600);
		janelaRegras.setLocation(janela.getWidth() / 2 - 400, janela.getHeight() / 2 - 300);
		janelaRegras.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		JButton addThreshold = new JButton("+");
		JButton reset = new JButton("Reset");
		JButton confirmar_thresholds = new JButton("Confirmar");
		JButton checkR = new JButton("OK");

		mainPanel = new JPanel(new BorderLayout());
		thresholdsPanel = new JPanel(new BorderLayout());
		GridBagLayout layout = new GridBagLayout();
		JPanel thresholds = new JPanel(layout);
		JPanel checkRule = new JPanel(new FlowLayout());
		JPanel buttonPanel = new JPanel(new BorderLayout());
		JPanel button_aux = new JPanel(new FlowLayout());

		String[] longMethod = { "LOC", "CYCLO" };
		String[] featureEnvy = { "ATFD", "LAA" };
		String[] operadores = { ">", "<", ">=", "<=" };

		metricas = new JComboBox<String>();
		operador = new JComboBox<String>(operadores);

		rulePanel = new JPanel(new FlowLayout());

		if (aux.equals("Definir regra - LongMethod")) {
			for (String string : longMethod) {
				metricas.addItem(string);
			}
		} else if (aux.equals("Definir regra - FeatureEnvy")) {
			for (String string : featureEnvy) {
				metricas.addItem(string);
			}
		}

		threshold = new JTextField();

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.ipadx = 30;
		gbc.ipady = 5;
		gbc.gridx = 0;
		gbc.gridy = 0;
		thresholds.add(metricas, gbc);

		gbc.ipadx = 30;
		gbc.ipady = 5;
		gbc.gridx = 1;
		gbc.gridy = 0;
		thresholds.add(operador, gbc);

		gbc.ipadx = 50;
		gbc.ipady = 10;
		gbc.gridx = 2;
		gbc.gridy = 0;
		thresholds.add(threshold, gbc);

		addThreshold.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (contadorRegras < 3) {
					regras.setVisible(true);
					regras.setEditable(false);
					regras.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
							"Thresholds Definidas", TitledBorder.CENTER, TitledBorder.TOP));

					regras.setText(regras.getText() + metricas.getSelectedItem().toString() + " "
							+ operador.getSelectedItem().toString() + " " + threshold.getText() + "\n");
					thresholdsPanel.add(regras, BorderLayout.CENTER);

					listaRegras.add(new Regra(metricas.getSelectedItem().toString(),
							operador.getSelectedItem().toString(), Double.parseDouble(threshold.getText()), null));

					janelaRegras.setVisible(true);

				} else {
					showErrorDialog("  Número máximo de métricas atingido!!", 250, 125);
				}
				contadorRegras++;

			}
		});

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				regras.setText(null);
				regras.setVisible(false);
				listaRegras.clear();
				rulePanel.removeAll();
				contadorRegras = 0;
			}
		});

		confirmar_thresholds.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				rulePanel.removeAll();

				int l = listaRegras.size() * 2 - 1;
				String[] temp = new String[listaRegras.size()];
				String[] opLogico = { "OR", "AND" };
				int i = 0;
				for (Regra r : listaRegras) {
					temp[i] = r.getMetrica() + r.getOp() + r.getValor();
					i++;
				}

				for (int t = 0; t < l; t++) {
					if (t % 2 == 0) {
						rulePanel.add(new JComboBox<String>(temp));
					} else {
						rulePanel.add(new JComboBox<String>(opLogico));
					}
				}

				rulePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
						"Regra Definida", TitledBorder.CENTER, TitledBorder.TOP));
				mainPanel.add(rulePanel, BorderLayout.CENTER);
				janelaRegras.setVisible(true);

			}
		});

		checkR.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Regra> aux = new ArrayList<Regra>();

				Component[] comp = rulePanel.getComponents();

				for (int i = 0; i < comp.length; i += 2) {
					for (Regra r : listaRegras) {
						if (i != comp.length - 1) {
							if (r.checkRule(((JComboBox) comp[i]).getSelectedItem().toString())) {
								aux.add(new Regra(r.getMetrica(), r.getOp(), r.getValor(),
										((JComboBox) comp[i + 1]).getSelectedItem().toString()));
								break;
							}
						} else {
							if (r.checkRule(((JComboBox) comp[i]).getSelectedItem().toString())) {
								aux.add(r);
								break;
							}
						}
					}
				}

				listaRegras = aux;
				janelaRegras.dispose();
			}
		});

		thresholds.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Definição de Thresholds", TitledBorder.CENTER, TitledBorder.TOP));

		button_aux.add(addThreshold);
		button_aux.add(reset);
		button_aux.add(confirmar_thresholds);
		buttonPanel.add(button_aux, BorderLayout.WEST);
		thresholdsPanel.add(thresholds, BorderLayout.WEST);
		thresholdsPanel.add(buttonPanel, BorderLayout.AFTER_LAST_LINE);

		checkRule.add(checkR);
		mainPanel.add(checkRule, BorderLayout.SOUTH);
		mainPanel.add(thresholdsPanel, BorderLayout.NORTH);
		janelaRegras.add(mainPanel);

		janelaRegras.setVisible(true);

	}

}
