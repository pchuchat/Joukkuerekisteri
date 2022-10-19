package joukkue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

/**
 * @author jojohyva
 * @version 19.3.2021
 *
 */
public class Pelipaikat implements Iterable<Pelipaikka> {
    private ArrayList<Pelipaikka> alkiot = new ArrayList<Pelipaikka>();
    private String tiedostonPerusNimi = "jyps";
    private boolean muutettu = false;
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        
        Pelipaikat pelipaikat = new Pelipaikat();
        
        try {
            pelipaikat.lueTiedostosta();
        } catch (SailoException ex) {
            System.err.println(ex.getMessage());
        }
        
        Pelipaikka puolustaja1 = new Pelipaikka();
        puolustaja1.vastaaPuolustaja(2);
        Pelipaikka puolustaja2 = new Pelipaikka();
        puolustaja2.vastaaPuolustaja(1);
        Pelipaikka puolustaja3 = new Pelipaikka();
        puolustaja3.vastaaPuolustaja(4);
        Pelipaikka puolustaja4 = new Pelipaikka();
        puolustaja4.vastaaPuolustaja(5);
        
        //pelipaikat.lisaa(puolustaja1);
        //pelipaikat.lisaa(puolustaja2);
        //pelipaikat.lisaa(puolustaja3);
        //pelipaikat.lisaa(puolustaja4);
        
        System.out.println("=========== pp testi =========");
        //List<Pelipaikka> pelipaikat2 = pelipaikat.annaPelipaikat(2);
        
        //for (Pelipaikka pp: pelipaikat2) {
        //    System.out.print(pp.getPelaajaNro() + " ");
        //    pp.tulosta(System.out);
        //}
        //for (Pelipaikka pp: alkiot) {
        //    System.out.print(pp.getPelaajaNro() + " ");
        //    pp.tulosta(System.out);
        //}
        
        try {
            pelipaikat.tallenna();
        } catch (SailoException e) {
            e.printStackTrace(); // tekee tulostuksen mikä menee pieleen
        }
    }

    
    /**
     * Alustaminen
     */
    public Pelipaikat() {
        //
    }
    
    /**
     * Korvaa pelipaikan tietorakenteessa.  Ottaa pelipaikan omistukseensa.
     * Etsitään samalla tunnusnumerolla oleva pelipaikka.  Jos ei löydy,
     * niin lisätään uutena pelipaikkana.
     * @param pelipaikka lisättävän pelipaikan viite.  Huom tietorakenne muuttuu omistajaksi
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Pelipaikat pelipaikat = new Pelipaikat();
     * Pelipaikat pp1 = new Pelipaikat(), pp2 = new Pelipaikat();
     * pp1.rekisteroi(); pp2.rekisteroi();
     * pelipaikat.getLkm() === 0;
     * pelipaikat.korvaaTaiLisaa(har1); pelipaikat.getLkm() === 1;
     * pelipaikat.korvaaTaiLisaa(har2); pelipaikat.getLkm() === 2;
     * Pelipaikka pp3 = pp1.clone();
     * pp3.aseta(2,"kkk");
     * Iterator<Pelipaikka> i2=pelipaikat.iterator();
     * i2.next() === har1;
     * pelipaikat.korvaaTaiLisaa(har3); pelipaikat.getLkm() === 2;
     * i2=pelipaikat.iterator();
     * Pelipaikka h = i2.next();
     * h === har3;
     * h == har3 === true;
     * h == har1 === false;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Pelipaikka pelipaikka) throws SailoException {
        int id = pelipaikka.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, pelipaikka);
                muutettu = true;
                return;
            }
        }
        lisaa(pelipaikka);
    }

    
    /**
     * @param pp lisättävä pelipaikka
     */
    public void lisaa(Pelipaikka pp) {
        alkiot.add(pp);
        muutettu = true;
    }
    
    
    /**
     * Etsii pelaajan pelipaikan 
     * @param tunnusnro ketä etsitään
     * @return lista löytyneistä
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Pelipaikat pelipaikat = new Pelipaikat();
     *  Pelipaikka puolustaja21 = new Pelipaikka(2); pelipaikat.lisaa(puolustaja21);
     *  Pelipaikka puolustaja11 = new Pelipaikka(1); pelipaikat.lisaa(puolustaja11);
     *  Pelipaikka puolustaja22 = new Pelipaikka(2); pelipaikat.lisaa(puolustaja22);
     *  Pelipaikka puolustaja12 = new Pelipaikka(1); pelipaikat.lisaa(puolustaja12);
     *  Pelipaikka puolustaja23 = new Pelipaikka(2); pelipaikat.lisaa(puolustaja23);
     *  Pelipaikka puolustaja51 = new Pelipaikka(5); pelipaikat.lisaa(puolustaja51);
     *  
     *  List<Pelipaikka> loytyneet;
     *  loytyneet = pelipaikat.annaPelipaikat(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = pelipaikat.annaPelipaikat(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == puolustaja11 === true;
     *  loytyneet.get(1) == puolustaja12 === true;
     *  loytyneet = pelipaikat.annaPelipaikat(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == puolustaja51 === true;
     * </pre> 
     */
    public List<Pelipaikka> annaPelipaikat(int tunnusnro) {
        List<Pelipaikka> loydetyt = new ArrayList<Pelipaikka>();
        for (Pelipaikka pp: alkiot) {
            if (pp.getPelaajaNro() == tunnusnro) loydetyt.add(pp);
        }
        return loydetyt;
    }
    
    /**
     * @param pelipaikka poistettava pelipaikka
     * @return tosi jos löytyi poistettava tietue
     */
    public boolean poista(Pelipaikka pelipaikka) {
        boolean ret = alkiot.remove(pelipaikka);
        if (ret) muutettu = true;
        return ret;
    }
    
    /**
     * Poistaa kaikki tietyn pelaajan pelipaikat
     * @param tunnusNro viite siihen mihin liittyvät tietueet poistetaan
     * @return montako poistettiin
     */
    public int poistaPelaajanPelipaikat(int tunnusNro) {
        int n = 0;
        for (Iterator<Pelipaikka> it = alkiot.iterator(); it.hasNext();) {
            Pelipaikka pp = it.next();
            if (pp.getPelaajaNro() == tunnusNro) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }
    
    /**
     * Lukee pelaajat tiedostosta.
     * @param hakemisto tiedoston hakemisto
     * @throws SailoException jos tiedoston lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Pelipaikat pelipaikat= new Pelipaikat();
     *  Pelipaikka puolustaja21 = new Pelipaikka(); puolustaja21.vastaaPuolustaja(2);
     *  Pelipaikka puolustaja11 = new Pelipaikka(); puolustaja11.vastaaPuolustaja(1);
     *  Pelipaikka puolustaja22 = new Pelipaikka(); puolustaja22.vastaaPuolustaja(2); 
     *  Pelipaikka puolustaja12 = new Pelipaikka(); puolustaja12.vastaaPuolustaja(1); 
     *  Pelipaikka puolustaja23 = new Pelipaikka(); puolustaja23.vastaaPuolustaja(2); 
     *  String tiedNimi = "testjyps";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  pelipaikat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  pelipaikat.lisaa(puolustaja21);
     *  pelipaikat.lisaa(puolustaja11);
     *  pelipaikat.lisaa(puolustaja22);
     *  pelipaikat.lisaa(puolustaja12);
     *  pelipaikat.lisaa(puolustaja23);
     *  pelipaikat.tallenna();
     *  pelipaikat = new Pelipaikat();
     *  pelipaikat.lueTiedostosta(tiedNimi);
     *  pelipaikat.lisaa(puolustaja23);
     *  pelipaikat.tallenna();
     * </pre>
     */
    public void lueTiedostosta(String hakemisto) throws SailoException {
        String nimi = hakemisto + ".dat";
        File tiedosto = new File(nimi);
        
        try (Scanner fi = new Scanner(new FileInputStream(tiedosto))) {
            while (fi.hasNext()) {
                String s = "";
                s = fi.nextLine();
                Pelipaikka pelipaikka = new Pelipaikka();
                pelipaikka.parse(s);
                lisaa(pelipaikka);
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
     * @throws SailoException jos talletus epäonnistuus
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;
        
        File tiedosto = new File(tiedostonPerusNimi + ".dat");
        try (PrintStream fo = new PrintStream(new FileOutputStream(tiedosto, false))) {
            for (int i = 0; i < getLkm(); i++) {
                Pelipaikka pp = annaPelipaikka(i);
                fo.println(pp.toString());
            }
        } catch (FileNotFoundException ex) {
            throw new SailoException("Tiedosto " + tiedosto.getAbsolutePath() + " ei aukea");
        }
        muutettu = false;
    }
    
    /**
     * @param i pelipaikan paikka
     * @return i:es pelipaikka
     */
    public Pelipaikka annaPelipaikka(int i) {
        return alkiot.get(i);
    }

    /**
     * Palauttaa joukkueen pelipaikkojen lkm
     * @return pelipaikkojen lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
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

    @Override
    public Iterator<Pelipaikka> iterator() {
        return alkiot.iterator();
    }
    
}
