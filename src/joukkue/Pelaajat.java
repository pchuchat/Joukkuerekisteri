/** 
 * 
 */
package joukkue;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import fi.jyu.mit.ohj2.WildChars;
/**
 * Kopioi CRC-kortin tietoja tähän
 * @author jojohyva
 * @version 22.2.2021
 *
 */
public class Pelaajat implements Iterable<Pelaaja> {
    
    private static final int MAX_PELAAJIA = 5;
    private boolean muutettu = false;
    private int lkm = 0;
    private String tiedostonPerusNimi = "jyps";
    private Pelaaja[] alkiot;
    

    /**
     * Luodaan alustava taulukko
     */
    public Pelaajat() {
        alkiot = new Pelaaja[MAX_PELAAJIA];
    }
    
    /**
     * 
     * @param pelaaja lisättävän pelaajan viite
     * @example
     * <pre name="test">
     * Pelaajat pelaajat = new Pelaajat();
     * Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja();
     * pelaajat.getLkm() === 0;
     * pelaajat.lisaa(aku1); pelaajat.getLkm() === 1;
     * pelaajat.lisaa(aku2); pelaajat.getLkm() === 2;
     * pelaajat.lisaa(aku1); pelaajat.getLkm() === 3;
     * pelaajat.anna(0) === aku1;
     * pelaajat.anna(1) === aku2;
     * pelaajat.anna(2) === aku1;
     * pelaajat.anna(1) == aku1 === false;
     * pelaajat.anna(1) == aku2 === true;
     * pelaajat.anna(5) === aku1; #THROWS IndexOutOfBoundsException 
     * pelaajat.lisaa(aku1); pelaajat.getLkm() === 4;
     * pelaajat.lisaa(aku1); pelaajat.getLkm() === 5;
     * pelaajat.lisaa(aku1); pelaajat.getLkm() === 6;
     * </pre>
     */
    public void lisaa(Pelaaja pelaaja) {
        if (lkm >= alkiot.length) {
            Pelaaja[] uusiAlkiot = new Pelaaja[alkiot.length + lkm];
            System.arraycopy(alkiot, 0, uusiAlkiot, 0, alkiot.length);
            alkiot = uusiAlkiot;
        }
        this.alkiot[this.lkm] = pelaaja;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Korvaa pelaajan tietorakenteessa. Ottaa pelaajan omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva pelaaja. Jos ei löydy, 
     * niin lisätään uutena pelaajana.
     * @param pelaaja lisättävän pelaajan viite
     * @throws SailoException jos tietorakenne täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Pelaajat pelaajat = new Pelaajat();
     * Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja();
     * aku1.rekisteroi(); aku2.rekisteroi();
     * pelaajat.getLkm() === 0;
     * pelaajat.korvaaTaiLisaa(aku1); pelaajat.getLkm() === 1;
     * pelaajat.korvaaTaiLisaa(aku2); pelaajat.getLkm() === 2;
     * Pelaaja aku3 = aku1.clone();
     * aku3.setPostinumero("00130");
     * Iterator<Pelaaja> it = pelaajat.iterator();
     * it.next() == aku1 === true;
     * pelaajat.korvaaTaiLisaa(aku3); pelaajat.getLkm() === 2;
     * it = pelaajat.iterator();
     * Pelaaja p0 = it.next();
     * p0 === aku3;
     * p0 == aku3 === true;
     * p0 == aku1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Pelaaja pelaaja) throws SailoException {
        int id = pelaaja.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if (alkiot[i].getTunnusNro() == id) {
                alkiot[i] = pelaaja;
                muutettu = true;
                return;
            }
        }
        lisaa(pelaaja);
    }
    
    /**
     * Palauttaa viitteen i:teen pelaajaan.
     * @param i monennenko pelaajan viite halutaan
     * @return viite pelaajaan, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
     */
    public Pelaaja anna(int i) {
        return alkiot[i];
    }
    
    
    /**
     * Palauttaa joukkueen pelaajien lukumäärän
     * @return pelaajien lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen.
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return this.tiedostonPerusNimi;
    }
    
    /**
     * Asettaa tiedoston perusnimen ilan tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        this.tiedostonPerusNimi = nimi;
    }

    /**
     * @param id poistettavan pelaajan tunnusnumero
     * @return 1 jos poistettiin, 0 jos ei löydy
     */
    public int poista(int id) {
        int ind = etsiId(id);
        if (ind < 0) return 0;
        lkm--;
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i+1];
        alkiot[lkm] = null;
        muutettu = true;
        return 1;
    }
    
    /**
     * Lukee pelaajat tiedostosta.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos tiedoston lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Pelaajat pelaajat = new Pelaajat();
     *  Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja();
     *  aku1.taytaAkuAnkkaTiedoilla();
     *  aku2.taytaAkuAnkkaTiedoilla();
     *  String hakemisto = "testjyps";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  pelaajat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  pelaajat.lisaa(aku1);
     *  pelaajat.lisaa(aku2);
     *  pelaajat.tallenna();
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String nimi = hakemisto + ".dat";
        File tiedosto = new File(nimi);
        
        try (Scanner fi = new Scanner(new FileInputStream(tiedosto))) {
            while (fi.hasNext()) {
                String s = "";
                s = fi.nextLine();
                Pelaaja pelaaja = new Pelaaja();
                pelaaja.parse(s);
                lisaa(pelaaja);
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            throw new SailoException("Ei saa luettua tiedsotoa " + nimi);
        }
    }
    
    /**
     * Luetaan aiemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    /**
     * Tallentaa pelaajat tiedostoon.
     * Tiedoston muoto:
     * <pre>
     * 1|Ankka Aku 1527|090812-9522|180.0|80.1|vasen|10|200.0|200.0
     * 2|Ankka Aku 6833|211216-484Y|180.0|80.1|vasen|10|200.0|200.0
     * </pre>

     * @throws SailoException jos talletus epäonnistuus
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File tiedosto = new File(tiedostonPerusNimi + ".dat");
        try (PrintStream fo = new PrintStream(new FileOutputStream(tiedosto, false))) {
            for (int i = 0; i < getLkm(); i++) {
                Pelaaja pelaaja = anna(i);
                fo.println(pelaaja.toString());
                
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + tiedosto.getAbsolutePath() + " ei aukea");
        }
        muutettu = false;
    }
    
    /**
     * @author jojohyva
     * @version 23.4.2021
     *
     */
    public class PelaajatIterator implements Iterator<Pelaaja> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa jäsentä
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä jäseniä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava jäsen
         * @return seuraava jäsen
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Pelaaja next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    
    
    /**
     * Palautetaan iteraattori pelaajista
     * @return pelaaja iteraattori
     */
    @Override
    public Iterator<Pelaaja> iterator() {
        return new PelaajatIterator();
    }
    
    /**
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä pelaajista
     */
    public Collection<Pelaaja> etsi(String hakuehto, int k) {
        String ehto = "";
        if (hakuehto != null && hakuehto.length() > 0) ehto = hakuehto;
        int hk = k;
        if (hk < 0) hk = 0; //etsii id:n mukaan
        List<Pelaaja> loytyneet = new ArrayList<Pelaaja>();
        for (Pelaaja pelaaja : this) {
            if (WildChars.onkoSamat(pelaaja.anna(hk), ehto)) loytyneet.add(pelaaja);
        }
        Collections.sort(loytyneet, new Pelaaja.Vertailija(hk));
        return loytyneet;
    }
    
    /**
     * @param id tunnusnumero jonka mukaan etsitään
     * @return löytyneen pelaajan indeksi tai -1 jos ei löydy
     */
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 

    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Pelaajat pelaajat = new Pelaajat();
        
        try {
            pelaajat.lueTiedostosta();
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }
        
        Pelaaja aku = new Pelaaja();
        Pelaaja aku2 = new Pelaaja();
        aku.rekisteroi();
        aku2.rekisteroi();
        
        aku.taytaAkuAnkkaTiedoilla();
        aku2.taytaAkuAnkkaTiedoilla();
        
        //pelaajat.lisaa(aku);
        //pelaajat.lisaa(aku2);
        
        System.out.println("=========== Pelaajat testi ===========");
        
        for (int i = 0; i < pelaajat.getLkm(); i++) {
            Pelaaja pelaaja = pelaajat.anna(i);
            System.out.println("Pelaajan indeksi: " + i);
            pelaaja.tulosta(System.out);
        }
        
        try {
            pelaajat.tallenna();
        } catch (SailoException e) {
            e.printStackTrace(); // tekee tulostuksen mikä menee pieleen
        }
    }

}
