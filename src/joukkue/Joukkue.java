/**
 * 
 */
package joukkue;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * CRC sisällöt tähän
 * @author jojohyva
 * @version 23.2.2021
 *
 */
public class Joukkue {
    private Pelaajat pelaajat = new Pelaajat();
    private Pelipaikat pelipaikat= new Pelipaikat();

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Joukkue joukkue = new Joukkue();

        Pelaaja aku = new Pelaaja();
        Pelaaja aku2 = new Pelaaja();
        aku.rekisteroi();
        aku.taytaAkuAnkkaTiedoilla();
        aku2.rekisteroi();
        aku2.taytaAkuAnkkaTiedoilla();
        
        joukkue.lisaa(aku);
        joukkue.lisaa(aku2);
        joukkue.lisaa(aku2);
        
        int id1 = aku.getTunnusNro();
        int id2 = aku2.getTunnusNro();
        Pelipaikka puolustaja11 = new Pelipaikka(id1); puolustaja11.vastaaPuolustaja(id1); joukkue.lisaa(puolustaja11);
        Pelipaikka puolustaja12 = new Pelipaikka(id1); puolustaja12.vastaaPuolustaja(id1); joukkue.lisaa(puolustaja12);
        Pelipaikka puolustaja21 = new Pelipaikka(id2); puolustaja21.vastaaPuolustaja(id2); joukkue.lisaa(puolustaja21);
        Pelipaikka puolustaja22 = new Pelipaikka(id2); puolustaja22.vastaaPuolustaja(id2); joukkue.lisaa(puolustaja22);
        Pelipaikka puolustaja23 = new Pelipaikka(id2); puolustaja23.vastaaPuolustaja(id2); joukkue.lisaa(puolustaja23);
        
        for (int i = 0; i < joukkue.getPelaajia(); i++) {
            Pelaaja pelaaja = joukkue.annaPelaaja(i);
            pelaaja.tulosta(System.out);
            List<Pelipaikka> loytyneet = joukkue.annaPelipaikat(pelaaja);
            for (Pelipaikka pelipaikka : loytyneet)
                pelipaikka.tulosta(System.out);

        }    
    }

    /**
     * @param pelaaja pelaaja joka poistetaan
     * @return montako pelaajaa poistettiin
     */
    public int poista(Pelaaja pelaaja) {
        if (pelaaja == null) return 0;
        int ret = pelaajat.poista(pelaaja.getTunnusNro());
        pelipaikat.poistaPelaajanPelipaikat(pelaaja.getTunnusNro());
        return ret;
    }
    
    /**
     * Lisätään uusi pelaaja
     * @param pelaaja lisättävä pelaaja
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
        this.pelaajat.lisaa(pelaaja);
    }
    
    /**
     * Poistaa tämän pelipaikan
     * @param pelipaikka poistettava pelipaikka
     */
    public void poistaPelipaikka(Pelipaikka pelipaikka) {
        pelipaikat.poista(pelipaikka);
    }
    
    /**
     * Lisätään uusi pelipaikka
     * @param pp lisättävä pelipaikka
     */
    public void lisaa(Pelipaikka pp) {
        pelipaikat.lisaa(pp);
    }
    
    /**
     * Korvaa pelaajan tietorakenteessa. Ottaa pelaajan omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva pelaaja. Jos ei löydy, 
     * niin lisätään uutena pelaajana.
     * @param pelaaja lisättävän pelaajan viite
     * @throws SailoException jos tietorakenne täynnä
     */
    public void korvaaTaiLisaa(Pelaaja pelaaja) throws SailoException {
        pelaajat.korvaaTaiLisaa(pelaaja);
    }
    
    /** 
     * Korvaa pelipaikan tietorakenteessa.  Ottaa pelipaikan omistukseensa. 
     * Etsitään samalla tunnusnumerolla oleva pelipaikka.  Jos ei löydy, 
     * niin lisätään uutena pelipaikkana. 
     * @param pelipaikka lisättävän pelipaikan viite.  Huom tietorakenne muuttuu omistajaksi 
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Pelipaikka pelipaikka) throws SailoException { 
        pelipaikat.korvaaTaiLisaa(pelipaikka); 
    } 

    
    /**
     * Palauttaa pelaajien lukumäärän
     * @return pelaajien lukumäärän
     */
    public int getPelaajia() {
        return this.pelaajat.getLkm();
    }
    
    /**
     * Antaa joukkueen i:n pelaaja
     * @param i monesko pelaaja
     * @return pelaaja paikasta i
     */
    public Pelaaja annaPelaaja(int i) {
        return this.pelaajat.anna(i);
    }
    
    /**
     * @param hakuehto hakuehto
     * @param k etsittävän kentän indeksi
     * @return tietorakenteen löytyneistä pelaajista
     * @throws SailoException jos jotakin menee pieleen
     */
    public Collection<Pelaaja> etsi(String hakuehto, int k) throws SailoException {
        return pelaajat.etsi(hakuehto, k);
    }
    
    /**
     * Haetaan pelaajan pelipaikat
     * @param pelaaja pelaaja jolle pelipaikkoja lisätään
     * @return tietorakenne jossa viitteet löydettyihin pelipaikkoihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Joukkue joukkue = new Joukkue();
     *  Pelaaja aku1 = new Pelaaja(), aku2 = new Pelaaja(), aku3 = new Pelaaja();
     *  aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi();
     *  int id1 = aku1.getTunnusNro();
     *  int id2 = aku2.getTunnusNro();
     *  Pelipaikka puolustaja11 = new Pelipaikka(id1); joukkue.lisaa(puolustaja11);
     *  Pelipaikka puolustaja12 = new Pelipaikka(id1); joukkue.lisaa(puolustaja12);
     *  Pelipaikka puolustaja21 = new Pelipaikka(id2); joukkue.lisaa(puolustaja21);
     *  Pelipaikka puolustaja22 = new Pelipaikka(id2); joukkue.lisaa(puolustaja22);
     *  Pelipaikka puolustaja23 = new Pelipaikka(id2); joukkue.lisaa(puolustaja23);
     *  
     *  List<Pelipaikka> loytyneet;
     *  loytyneet = joukkue.annaPelipaikat(aku3);
     *  loytyneet.size() === 0; 
     *  loytyneet = joukkue.annaPelipaikat(aku1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == puolustaja11 === true;
     *  loytyneet.get(1) == puolustaja12 === true;
     *  loytyneet = joukkue.annaPelipaikat(aku2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == puolustaja21 === true;
     * </pre> 
     */
    public List<Pelipaikka> annaPelipaikat(Pelaaja pelaaja) {
        return pelipaikat.annaPelipaikat(pelaaja.getTunnusNro());
    }

    /**
     * @param nimi tiedoston nimi
     * @throws SailoException exception
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *  Kerho kerho = new Kerho();
     *  
     *  Pelaaja aku1 = new Pelaaja(); aku1.taytaAkuAnkkaTiedoilla(); aku1.rekisteroi();
     *  Pelaaja aku2 = new Pelaaja(); aku2.taytaAkuAnkkaTiedoilla(); aku2.rekisteroi();
     *  Pelipaikka puolustaja21 = new Pelipaikka(); puolustaja21.vastaaPuolustaja(aku2.getTunnusNro());
     *  Pelipaikka puolustaja11 = new Pelipaikka(); puolustaja11.vastaaPuolustaja(aku1.getTunnusNro());
     *  Pelipaikka puolustaja22 = new Pelipaikka(); puolustaja22.vastaaPuolustaja(aku2.getTunnusNro()); 
     *  Pelipaikka puolustaja12 = new Pelipaikka(); puolustaja12.vastaaPuolustaja(aku1.getTunnusNro()); 
     *  Pelipaikka puolustaja23 = new Pelipaikka(); puolustaja23.vastaaPuolustaja(aku2.getTunnusNro());
     *   
     *  String hakemisto = "testjyps";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/pelaajat.dat");
     *  File fptied = new File(hakemisto+"/pelipaikat.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  joukkue.lueTiedostosta(hakemisto); #THROWS SailoException
     *  joukkue.lisaa(aku1);
     *  joukkue.lisaa(aku2);
     *  joukkue.lisaa(pitsi21);
     *  joukkue.lisaa(pitsi11);
     *  joukkue.lisaa(pitsi22);
     *  joukkue.lisaa(pitsi12);
     *  joukkue.lisaa(pitsi23);
     *  joukkue.tallenna();
     *  joukkue = new Joukkue();
     *  joukkue.lueTiedostosta(hakemisto);
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        pelaajat = new Pelaajat();
        pelipaikat = new Pelipaikat();
        
        setTiedosto(nimi);
        pelaajat.lueTiedostosta();
        pelipaikat.lueTiedostosta();
    }

    /**
     * @throws SailoException virheilmoitus
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            pelaajat.tallenna();
            pelipaikat.tallenna();
        } catch (SailoException ex) {
            virhe += ex.getMessage();
        }
        if (!"".equals(virhe)) throw new SailoException(virhe);
    }
    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        pelaajat.setTiedostonPerusNimi(hakemistonNimi + "pelaajat");
        pelipaikat.setTiedostonPerusNimi(hakemistonNimi + "pelipaikat");
    }
    



}
