package ES14.ProjetoES;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());

		suite.addTestSuite(testAlgoritmo.class);
		suite.addTest(testApp.suite());
		suite.addTestSuite(testExcelController.class);
		suite.addTestSuite(testGUI.class);

		return suite;
	}

}
