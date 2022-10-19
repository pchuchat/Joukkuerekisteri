package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite kerho-ohjelmalle
 * @author vesal
 * @version 3.1.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    tietokanta.test.HetuTarkistusTest.class,
    joukkue.test.PelaajaTest.class,
    joukkue.test.PelaajatTest.class,
    joukkue.test.PelipaikkaTest.class,
    joukkue.test.PelipaikatTest.class,
    joukkue.test.JoukkueTest.class
    })
public class AllTests {
 //
}
