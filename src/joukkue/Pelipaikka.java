package joukkue;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

import static tietokanta.HetuTarkistus.*;

/**
 * @author jojohyva
 * @version 19.3.2021
 *
 */
public class Pelipaikka implements Cloneable {
    private int tunnusNro;
    private int pelaajaNro;
    private String pelipaikka;
    private int aloitusvuosi;
    private int pelejaPelattu;
    private static int seuraavaNro = 1;
    
    /**
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Pelipaikka pp = new Pelipaikka();
        pp.vastaaPuolustaja(2);
        pp.tulosta(System.out);
    }
    
    /**
     * Antaa pelipaikalle seuraavan rekisterinumeron
     * @return harrastuksen uusi tunnusNro
     * @example
     * <pre name="test">
     *   Pelipaikka puolustaja1 = new Pelipaikka();
     *   puolustaja1.getTunnusNro() === 0;
     *   puolustaja1.rekisteroi();
     *   Pelipaikka puolustaja2 = new Pelipaikka();
     *   puolustaja2.rekisteroi();
     *   int n1 = puolustaja1.getTunnusNro();
     *   int n2 = puolustaja2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.tunnusNro = seuraavaNro;
        seuraavaNro++;
        return this.tunnusNro;
    }
    
    
    /**
     * @return pelipaikan kenttien lukumäärä
     */
    public int getKenttia() {
        return 5;
    }


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    public int ekaKentta() {
        return 2;
    }

    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "jasenNro";
            case 1:
                return "tunnusNro";
            case 2:
                return "pelipaikka";
            case 3:
                return "aloitusvuosi";
            case 4:
                return "pelejä pelattu";
            default:
                return "???";
        }
    }

    
    @Override
    public Pelipaikka clone() throws CloneNotSupportedException {
        Pelipaikka uusi;
        uusi = (Pelipaikka) super.clone();
        return uusi;
    }
    
    
    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Pelipaikka pp = new Pelipaikka();
     *   pp.parse("   2   |  10  |   Hyökkääjä  | 1949 | 22 t ");
     *   pp.anna(0) === "2";   
     *   pp.anna(1) === "10";   
     *   pp.anna(2) === "Hyökkääjä";   
     *   pp.anna(3) === "1949";   
     *   pp.anna(4) === "22";   
     *   
     * </pre>
     */
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + pelaajaNro;
            case 1:
                return "" + tunnusNro;
            case 2:
                return pelipaikka;
            case 3:
                return "" + aloitusvuosi;
            case 4:
                return "" + pelejaPelattu;
            default:
                return "???";
        }
    }

    
    /**
     * asetetaan valitun kentän sisältö
     * @param k minkä kentän sisältö asetetaan
     * @param jono asetettava sisältö merkkijonona
     * @return null jos ok muuten virheteksti
     */
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            pelaajaNro = (Mjonot.erota(sb, '$', pelaajaNro));
            return null;
        case 1:
            tunnusNro = (Mjonot.erota(sb, '$', getTunnusNro()));
            return null;
        case 2:
            pelipaikka = tjono;
            return null;
        case 3:
            aloitusvuosi = (Mjonot.erota(sb, '$', aloitusvuosi));
            return null;
        case 4:
            pelejaPelattu = Mjonot.erota(sb, '$', pelejaPelattu);
            return null;
        default:
            return "ÄÄliö"; 
        }
    }
    
    /**
     * Palauttaa pelipaikan tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return pelipaikka tolppaeroteltuna 
     * @example
     * <pre name="test">
     *   Pelipaikka pelipaikka = new Pelipaikka();
     *   pelipaikka.parse("   2   |  3  |   Hyökkääjä  | 2001 | 231 ");
     *   pelipaikka.toString()    === "2|3|Hyökkääjä|2001|231";
     * </pre>
     */
    @Override
    public String toString() {
        return "" + 
                pelaajaNro + "|" +
                getTunnusNro() + "|" +
                pelipaikka + "|" + 
                aloitusvuosi + "|" + 
                pelejaPelattu;
    }
    
    /**
     * Selvitää pelipaikan tiedot | erotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta pelipaikan tiedot otetaan
     * @example
     * <pre name="test">
     *   Pelipaikka pelipaikka = new Pelipaikka();
     *   pelipaikka.parse("   2   |  3  |   Hyökkääjä  | 2001 | 231 ");
     *   pelipaikka.getPelaajaNro() === 3;
     *   pelipaikka.toString()    === "2|3|Hyökkääjä|2001|231";
     *   
     *   pelipaikka.rekisteroi();
     *   int n = pelipaikka.getTunnusNro();
     *   pelipaikka.parse(""+(n+20));
     *   pelipaikka.rekisteroi();
     *   pelipaikka.getTunnusNro() === n+20+1;
     *   pelipaikka.toString()     === "" + (n+20+1) + "|3|Hyökkääjä|2001|231";
     * </pre>
     */

    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        pelaajaNro = Mjonot.erota(sb, '|', pelaajaNro);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        pelipaikka = Mjonot.erota(sb, '|', pelipaikka);
        aloitusvuosi = Mjonot.erota(sb, '|', aloitusvuosi);
        pelejaPelattu = Mjonot.erota(sb, '|', pelejaPelattu);
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
     * Alustetaan pelipaikka
     */
    public Pelipaikka() {
        //
    }
    
    /**
     * Alustetaan tietyn pelaajan pelipaikka
     * @param pelaajaNro pelaajan viitenumero
     */
    public Pelipaikka(int pelaajaNro) {
        this.pelaajaNro = pelaajaNro;
    }
    
    /**
     * Palautetaan pelipaikan oma id
     * @return pelipaikan id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Palautetaan mille pelaajalle pelipaikka kuuluu
     * @return pelaajan id
     */
    public int getPelaajaNro() {
        return pelaajaNro;
    }

    /**
     * Palautetaana pelipaikka
     * @return pelipaikka
     */
    public String getPelipaikka() {
        return pelipaikka;
    }
    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot pelipaikalle.
     * Aloitusvuosi ja pelatut pelit arvotaan.
     * @param nro viite pelaajaan, jonka pelipaikka kyseessä
     */
    public void vastaaPuolustaja(int nro) {
        pelaajaNro = nro;
        pelipaikka = "Puolustaja";
        aloitusvuosi = rand(1900, 2000);
        pelejaPelattu = rand(0, 100);
    }
    
    /**
     * Tulostetaan pelipaikan tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(pelipaikka + " " + aloitusvuosi + " " + pelejaPelattu);
    }
    
    /**
     * Tulostetaan pelaajan tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    
}
