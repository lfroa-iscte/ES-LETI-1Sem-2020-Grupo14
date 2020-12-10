package ES14.ProjetoES;

/**
 * <h1>App<h1>
 * <p>
 * A classe App implementa uma aplicação para avaliação da qualidade de deteção de defeitos (code smells)
 * de desenho em projetos de software, simplesmente invocando uma instância da classe GUI.
 * <p>
 * 
 * @author Francisco Mendes
 * @author Tomás Santos
 * @author Lucas Oliveira
 * @version 1.0
 *
 */

public class App {
	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.open();
	}
}
