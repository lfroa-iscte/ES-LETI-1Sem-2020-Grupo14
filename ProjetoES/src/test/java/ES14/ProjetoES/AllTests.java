package ES14.ProjetoES;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

/**
 * Suite que executa várias classes de testes unitários JUnit simultaneamente.
 * <p>
 * Todas as classes de testes que estejam incluídas na suite serão executadas de
 * uma vez.
 * 
 * @see AlgoritmoTest
 * @see ExcelControllerTest
 * @see AppTest
 * @see GUITest
 * 
 * @author Francisco Mendes
 * @author Tomás Santos
 */

@RunWith(JUnitPlatform.class)
@SelectClasses({ AlgoritmoTest.class, ExcelControllerTest.class, GUITest.class })
public class AllTests {
}
