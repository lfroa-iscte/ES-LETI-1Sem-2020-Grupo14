package ES14.ProjetoES;

import junit.framework.Test;
import junit.framework.TestSuite;

	/**
	 * Suite que executa várias classes de testes unitários JUnit simultaneamente.
	 * <p>
	 * Todas as classes de testes que estejam incluídas na suite serão executadas de uma vez.
	 * 
	 * @author Tomás Santos
	 */

public class AllTests {
	
	/**
	 * Método que executa a suite com as classes de testes unitários.
	 * 
	 * @return suite
	 * @see testAlgoritmo
	 * @see testExcelController
	 * @see testApp
	 * @see testGUI
	 * 
	 * @author Tomás Santos
	 */

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());

		suite.addTestSuite(testAlgoritmo.class);
		suite.addTest(testApp.suite());
		suite.addTestSuite(testExcelController.class);
		suite.addTestSuite(testGUI.class);

		return suite;
	}

}
