package ES14.ProjetoES;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.event.*;


public class GUI {

	private JFrame janela;
	
	
	public GUI() {
		janela = new JFrame("DetetorDefeitos3000");
		janela.pack();
		janela.setSize(1200, 900);
		janela.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		addFrameContent();

	}

	private void addFrameContent() {
		
		JPanel ferramentas = new JPanel(new FlowLayout());  //alterar
		
		//paineis terão que que ser criados apenas aquando da execução do algoritmo
		JPanel pmd = new JPanel(new BorderLayout());
		JPanel iPlasma = new JPanel();
		
		JPanel botoes = new JPanel(new FlowLayout());

		JButton importar = new JButton("Importar Excel");
		
		/*
		importar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser(".");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnValue = chooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {

					File selectedFile = chooser.getSelectedFile();
					imagemText.setText(selectedFile.getName());
					imagemPath = selectedFile.getAbsolutePath();
				}

			}
		});
		*/
		
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
}
