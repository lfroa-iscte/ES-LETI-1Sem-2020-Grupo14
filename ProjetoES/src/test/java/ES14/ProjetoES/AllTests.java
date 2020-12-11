package ES14.ProjetoES;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());

		suite.addTestSuite(AlgoritmoTest.class);
		suite.addTest(AppTest.suite());
		suite.addTestSuite(testExcelController.class);
		
		return suite;
	}

}
