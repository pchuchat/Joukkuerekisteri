package joukkue.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import joukkue.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2021.03.24 09:00:39 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class PelaajaTest {


  // Generated by ComTest BEGIN
  /** testRekisteroi69 */
  @Test
  public void testRekisteroi69() {    // Pelaaja: 69
    Pelaaja aku1 = new Pelaaja(); 
    assertEquals("From: Pelaaja line: 71", 0, aku1.getTunnusNro()); 
    aku1.rekisteroi(); 
    Pelaaja aku2 = new Pelaaja(); 
    aku2.rekisteroi(); 
    int n1 = aku1.getTunnusNro(); 
    int n2 = aku2.getTunnusNro(); 
    assertEquals("From: Pelaaja line: 77", n2-1, n1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testGetNimi100 */
  @Test
  public void testGetNimi100() {    // Pelaaja: 100
    Pelaaja aku = new Pelaaja(); 
    aku.taytaAkuAnkkaTiedoilla(); 
    { String _l_=aku.getNimi(),_r_="Ankka Aku .*"; if ( !_l_.matches(_r_) ) fail("From: Pelaaja line: 103" + " does not match: ["+ _l_ + "] != [" + _r_ + "]");}; 
  } // Generated by ComTest END
}