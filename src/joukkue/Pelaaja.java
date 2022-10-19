/**
 * 
 */
package joukkue;

import java.io.PrintStream;
import java.util.Comparator;


import fi.jyu.mit.ohj2.Mjonot;
import tietokanta.HetuTarkistus;

import static tietokanta.HetuTarkistus.*;

/**
 * Kopioi CRC-kortin sisältö
 * @author jojohyva
 * @version 22.2.2021
 *
 */
public class Pelaaja implements Cloneable {
    
    private int      tunnusNro;
    private String   nimi        = "";
    private String   hetu        = "";
    private String   katisyys    = "";
    private double   paino       = 0;
    private double   pituus      = 0;
    private int      peliNro     = 0;
    private double   kmaksu      = 0;
    private double   maksu       = 0;
    private Double      tehopiste = 0.0;
    
    private static int seuraavaNro = 1;
    
 
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Pelaaja aku = new Pelaaja();
        Pelaaja aku2 = new Pelaaja();
        
        aku.rekisteroi();
        aku2.rekisteroi();
        
        aku.tulosta(System.out);
        aku.taytaAkuAnkkaTiedoilla();
        aku.tulosta(System.out);
        
        
        aku2.tulosta(System.out);
        aku2.taytaAkuAnkkaTiedoilla();
        aku2.tulosta(System.out);
    }
    
    /**
     * @author joehy
     * @version 23.4.2021
     *
     */
    public static class Vertailija implements Comparator<Pelaaja> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Pelaaja pelaaja1, Pelaaja pelaaja2) { 
            return pelaaja1.getAvain(k).compareToIgnoreCase(pelaaja2.getAvain(k)); 
        } 
    } 
    
    /**
     * @param k monennenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + tunnusNro; 
        case 1: return "" + nimi.toUpperCase(); 
        case 2: return "" + hetu; 
        case 3: return "" + katisyys; 
        case 4: return "" + paino; 
        case 5: return "" + pituus; 
        case 6: return "" + peliNro; 
        case 7: return "" + kmaksu; 
        case 8: return "" + maksu; 
        case 9: return "" + tehopiste;
        default: return "Äääliö"; 
        } 
    } 
    
    /**
     * Tulostetaan pelaajan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", tunnusNro) + " " + nimi + " " + hetu);
        out.println("Pelaajan pituus ja paino: " + pituus + " ja " + paino + ".");
        out.println("Pelaajan kätisyys ja pelinumero: " + katisyys + ", " + peliNro);
        out.println("Kausimaksu " + String.format("%4.2f", kmaksu) + " e."+ 
                    " Maksettu " + String.format("%4.2f", maksu) +  " e.");
    }

    /**
     * Tulostetaan nimet ja pisteet
     * @param out tietovirta johon tulostetaan
     */
    public void tulostaPisteporssi(PrintStream out) {
    out.println(nimi + tehopiste);
    }
    
    
    /**
     * Antaa pelaajalle seuraavan rekisterinumeron
     * @return Pelaajan tunnusnumeron
     * @example
     * <pre name="test">
     *   Pelaaja aku1 = new Pelaaja();
     *   aku1.getTunnusNro() === 0;
     *   aku1.rekisteroi();
     *   Pelaaja aku2 = new Pelaaja();
     *   aku2.rekisteroi();
     *   int n1 = aku1.getTunnusNro();
     *   int n2 = aku2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        seuraavaNro++;
        return this.tunnusNro;
    }
    
    
    /**
     * Palauttaa pelaajan tunnusnumeron
     * @return Pelaajan tunnusnumeron
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Palauttaa pelaajan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return pelaaja tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("   2  |  Ankka Aku   | 030201-111C  | 180 | 80 | vasen | 49 | 300 | 150");
     *   pelaaja.toString().startsWith("3|Ankka Aku|030201-111C|180|80|vasen|49|300|150") === true;
     * </pre>  
     */
    @Override
    public String toString() {
        return "" + 
                getTunnusNro() + "|" +
                nimi + "|" +
                hetu + "|" + 
                pituus + "|" + 
                paino + "|" + 
                katisyys + "|" +
                peliNro + "|" +
                kmaksu + "|" + 
                maksu + "|" +
                tehopiste;
    }
    
    /**
     * Antaa k:n kentän sisällön merkkijonona
     * @param k monenenko kentän sisältö palautetaan
     * @return kentän sisältö merkkijonona
     */
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + nimi;
        case 2: return "" + hetu;
        case 3: return "" + katisyys;
        case 4: return "" + paino;
        case 5: return "" + pituus;
        case 6: return "" + peliNro;
        case 7: return "" + kmaksu;
        case 8: return "" + maksu;
        case 9: return "" + tehopiste;
        default: return "Äääliö";
        }
    }

    /**
     * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
     * @param k kuinka monennen kentän arvo asetetaan
     * @param jono jonoa joka asetetaan kentän arvoksi
     * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.aseta(1,"Ankka Aku") === null;
     *   pelaaja.aseta(2,"kissa") =R= "Hetu liian lyhyt"
     *   pelaaja.aseta(2,"030201-1111") === "Tarkistusmerkin kuuluisi olla C"; 
     *   pelaaja.aseta(2,"030201-111C") === null; 
     *   pelaaja.aseta(9,"kissa") === "Liittymisvuosi väärin jono = \"kissa\"";
     *   pelaaja.aseta(9,"1940") === null;
     * </pre>
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
            return null;
        case 1:
            nimi = tjono;
            return null;
        case 2:
            HetuTarkistus hetut = new HetuTarkistus();
            String virhe = hetut.tarkista(tjono);
            if ( virhe != null ) return virhe;
            hetu = tjono;
            return null;
        case 3:
            katisyys = tjono;
            return null;
        case 4:
            paino = Mjonot.erota(sb, '$', paino);
            return null;
        case 5:
            pituus = Mjonot.erota(sb, '$', pituus);
            return null;
        case 6:
            peliNro = Mjonot.erota(sb, '$', peliNro);
            return null;
        case 7:
            kmaksu = Mjonot.erota(sb, '$', kmaksu);
            return null;
        case 8:
            maksu = Mjonot.erota(sb, '$', maksu);
            return null;
        case 9:
            tehopiste = Mjonot.erota(sb, '$', maksu);
            return null;
        default:
            return "ÄÄliö";
        }
    }

    /**
     * Palauttaa k:tta pelaajan kenttää vastaavan kysymyksen
     * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
     * @return k:netta kenttää vastaava kysymys
     */
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "Tunnus nro";
        case 1: return "nimi";
        case 2: return "hetu";
        case 3: return "katisyys";
        case 4: return "paino";
        case 5: return "pituus";
        case 6: return "peli nro";
        case 7: return "kausimaksu";
        case 8: return "maksu";
        case 9: return "tehopisteet";
        default: return "Äääliö";
        }
    }
    
    
    /**
     * Tehdään identtinen kopio pelaajasta
     * @return Object kloonattu pelaaja
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("   3  |  Ankka Aku   | 123");
     *   Pelaaja kopio = pelaaja.clone();
     *   kopio.toString() === pelaaja.toString();
     *   pelaaja.parse("   4  |  Ankka Tupu   | 123");
     *   kopio.toString().equals(pelaaja.toString()) === false;
     * </pre>
     */
    @Override
    public Pelaaja clone() throws CloneNotSupportedException {
        Pelaaja uusi;
        uusi = (Pelaaja) super.clone();
        return uusi;
    }
    
    /**
     * Tutkii onko pelaajan tiedot samat kuin parametrina tuodun pelaajan tiedot
     * @param pelaaja pelaaja johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja1 = new Pelaaja();
     *   pelaaja1.parse("   3  |  Ankka Aku   | 030201-111C");
     *   Pelaaja pelaaja2 = new Pelaaja();
     *   pelaaja2.parse("   3  |  Ankka Aku   | 030201-111C");
     *   Pelaaja pelaaja3 = new Pelaaja();
     *   pelaaja3.parse("   3  |  Ankka Aku   | 030201-115H");
     *   
     *   pelaaja1.equals(pelaaja2) === true;
     *   pelaaja2.equals(pelaaja1) === true;
     *   pelaaja1.equals(pelaaja3) === false;
     *   pelaaja3.equals(pelaaja2) === false;
     * </pre>
     */
    public boolean equals(Pelaaja pelaaja) {
        if ( pelaaja == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(pelaaja.anna(k)) ) return false;
        return true;
    }
    
    @Override
    public boolean equals(Object pelaaja) {
        if ( pelaaja instanceof Pelaaja ) return equals((Pelaaja)pelaaja);
        return false;
    }

    @Override
    public int hashCode() {
        return tunnusNro;
    }

    
    /**
     * Palauttaa pelaajan kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    public int getKenttia() {
        return 10;
    }

    /**
     * Eka kenttä 
     * @return ekan kentän indeksi
     */
    public int ekaKentta() {
        return 1;
    }
    
    
    /**
     * Selvitää pelaajan tiedot | erotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta pelaajan tiedot otetaan
     * 
     * @example
     * <pre name="test">
     *   Pelaaja pelaaja = new Pelaaja();
     *   pelaaja.parse("   2  |  Ankka Aku   | 030201-111C  | 180 | 80 | vasen | 49 | 300 | 150");
     *   pelaaja.getTunnusNro() === 3;
     *   pelaaja.toString().startsWith("3|Ankka Aku|030201-111C|180|80|vasen|49|300|150") === true;
     *
     *   pelaaja.rekisteroi();
     *   int n = pelaaja.getTunnusNro();
     *   pelaaja.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   pelaaja.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   pelaaja.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */

    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        nimi = Mjonot.erota(sb, '|', nimi);
        hetu = Mjonot.erota(sb, '|', hetu);
        pituus = Mjonot.erota(sb, '|', pituus);
        paino = Mjonot.erota(sb, '|', paino);
        katisyys = Mjonot.erota(sb, '|', katisyys);
        peliNro = Mjonot.erota(sb, '|', peliNro);
        kmaksu = Mjonot.erota(sb, '|', kmaksu);
        maksu = Mjonot.erota(sb, '|', maksu);
        tehopiste = Mjonot.erota(sb, '|', tehopiste);
    }
    
    /**
     * Asettaa tunnusnumeron ja varmistaa että seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nro asetettava tunnusnro
     */
    private void setTunnusNro(int nro) {
        tunnusNro = nro;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    /**
     * Palauttaa pelaajan nimen
     * @return Pelaajan nimi
     * @example
     * <pre name="test">
     *   Pelaaja aku = new Pelaaja();
     *   aku.taytaAkuAnkkaTiedoilla();
     *   aku.getNimi() =R= "Ankka Aku .*";
     * </pre>
     */
    public String getNimi() {
        return nimi;
    }
    
    /**
     * @return pelaajan hetu
     */
    public String getHetu() {
        return hetu;
    }

    /**
     * @return pelaajan kätisyys
     */
    public String getKatisyys() {
        return katisyys;
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot pelaajalle.
     */
    public void taytaAkuAnkkaTiedoilla() {
        nimi = "Ankka Aku " + rand(1000, 9999);
        hetu = arvoHetu();
        pituus = 180.0;
        paino = 80.1;
        katisyys = "vasen";
        peliNro = 10;
        kmaksu = 200;
        maksu = 200;
    }
    
}
