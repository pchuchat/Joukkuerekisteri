package joukkue.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import joukkue.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2021.03.24 09:22:30 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class PelipaikkaTest {


  // Generated by ComTest BEGIN
  /** testRekisteroi33 */
  @Test
  public void testRekisteroi33() {    // Pelipaikka: 33
    Pelipaikka puolustaja1 = new Pelipaikka(); 
    assertEquals("From: Pelipaikka line: 35", 0, puolustaja1.getTunnusNro()); 
    puolustaja1.rekisteroi(); 
    Pelipaikka puolustaja2 = new Pelipaikka(); 
    puolustaja2.rekisteroi(); 
    int n1 = puolustaja1.getTunnusNro(); 
    int n2 = puolustaja2.getTunnusNro(); 
    assertEquals("From: Pelipaikka line: 41", n2-1, n1); 
  } // Generated by ComTest END
}