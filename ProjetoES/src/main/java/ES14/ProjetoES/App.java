package ES14.ProjetoES;

/**
 * <p>
 * A classe App implementa uma aplicação para avaliação da qualidade de deteção
 * de defeitos (code smells) de desenho em projetos de software.
 * </p>
 * 
 * @author Francisco Mendes
 * @author Tomás Santos
 * @author Lucas Oliveira
 * @since 2020-10-24
 *
 */

public class App {

	/**
	 * Invoca uma instância da classe GUI
	 * 
	 * @see GUI
	 * 
	 * @author Tomás Santos
	 * @author Francisco Mendes
	 * @author Lucas Oliveira
	 * 
	 */

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.open();
	}
}
