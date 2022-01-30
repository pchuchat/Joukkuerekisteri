package fxJoukkue;

import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import joukkue.Joukkue;
import joukkue.Pelaaja;
import joukkue.Pelipaikka;
import joukkue.SailoException;
import static fxJoukkue.PelaajaMuokkausController.getFieldId;

/**
 * Ei voida poistaa pelaajia tai pelipaikkoja.
 * Ei voida hakea hakukentästä. 
 * @author jojohyva
 * @version 11.2.2021
 *
 */
public class JoukkueGUIController implements Initializable {
    
    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private ScrollPane panelPelaaja;
    @FXML private ListChooser<Pelaaja> chooserPelaajat;
    @FXML private StringGrid<Pelipaikka> tablePelipaikat;
    @FXML private GridPane gridPelaaja;
    @FXML private GridPane gridPelipaikka;

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();
    }
    
    @FXML void handleHakuehto() {
        hae(0);
    }
    
    @FXML void handleApua() {
        avustus();
    }

    @FXML void handleAvaa() {
        avaa();
    }

    @FXML void handleLopeta() {
        Dialogs.showMessageDialog("Ei osata lopettaa");
    }

    @FXML void handlePoistaPelaaja() {
        poistaPelaaja();
    }

    @FXML void handleTallenna() {
        tallenna();
    }

    @FXML void handleTietoja() {
        ModalController.showModal(JoukkueGUIController.class.getResource("TietojaView.fxml"), "Joukkue", null, "");
    }
    
    @FXML void handleTulosta() {
        TulostusController tulostusCtrl = TulostusController.tulosta(null);
        tulostaValitut(tulostusCtrl.getTextArea());
    }

    @FXML void handleTulostaPisteporssi() {
        TulostusController tulostusCtrl = TulostusController.tulosta(null);
        tulostaValitutPisteporssi(tulostusCtrl.getTextArea());
    }
    
    @FXML void handleUusiPelaaja() {
        uusiPelaaja();
    }
    
    @FXML void handleUusiPelipaikka() {
        uusiPelipaikka();
    }
    
    @FXML void handleMuokkaaPelaaja() {
        muokkaa(kentta);
    }
    
    @FXML void handleMuokkaaPelipaikka() {
        ModalController.showModal(JoukkueGUIController.class.getResource("PelipaikkaMuokkausView.fxml"), "Joukkue", null, apupelipaikka);
    }

    @FXML void handlePoistaPelipaikka() {
        poistaPelipaikka();
    }
    
    //====================================================================================
    
    private String joukkueennimi = "";
    private Joukkue joukkue;
    private Pelaaja pelaajanKohdalla;
    private TextField[] edits;
    private int kentta = 0; 
    private static Pelaaja apupelaaja = new Pelaaja();
    private static Pelipaikka apupelipaikka = new Pelipaikka(); 
    
    /**
     * Tulostaa listassa olevat pelaajat textAreaan
     * @param text tulostus alue
     */
    private void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki pelaajat");
            for (int i = 0; i < joukkue.getPelaajia(); i++) {
                Pelaaja pelaaja = joukkue.annaPelaaja(i);
                tulosta(os, pelaaja);
                os.println("\n");
            }
        }
    }
    
    private void tulostaValitutPisteporssi(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki pelaajat ja heidän pisteet");
            for (int i = 0; i < joukkue.getPelaajia(); i++) {
                Pelaaja pelaaja = joukkue.annaPelaaja(i);
                pelaaja.tulostaPisteporssi(os);
                os.println("\n");
            }
        }
    }
    /**
     * Tulostaa pelaajan tiedot
     * @param os tietovirta
     * @param pelaaja tulostettava pelaaja
     */
    private void tulosta(PrintStream os, Pelaaja pelaaja) {
        pelaaja.tulosta(os);
        List<Pelipaikka> loytyneet = joukkue.annaPelipaikat(pelaaja); 
        for (Pelipaikka pp: loytyneet) {
            pp.tulosta(os);
        }
    }

    /**
     * Hakee pelaajien tiedot listaan 
     * @param pnr pelaajan numero, joka aktivoidaan haun jälkeen jos 0 niin aktivoidaan nykyinen jäsen
     */
    protected void hae(int pnr) {
               
        int pnro = pnr; // jnro jäsenen numero, joka aktivoidaan haun jälkeen 
        if ( pnro <= 0 ) { 
            Pelaaja kohdalla = pelaajanKohdalla; 
            if ( kohdalla != null ) pnro = kohdalla.getTunnusNro(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apupelaaja.ekaKentta(); 
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 
        
        chooserPelaajat.clear();
                
        int index = 0;
        Collection<Pelaaja> pelaajat;
        try {
            pelaajat = joukkue.etsi(ehto, k);
            int i = 0;
            for (Pelaaja pelaaja : pelaajat) {
                if (pelaaja.getTunnusNro() == pnro) index = i;
                chooserPelaajat.add(pelaaja.getNimi(), pelaaja);
                i++;
            }
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + e.getMessage());
        }
        chooserPelaajat.setSelectedIndex(index);
    }
    
    
    /**
     * Luo uuden pelaajan
     */
    protected void uusiPelaaja() {
        Pelaaja uusi = new Pelaaja();
        uusi = PelaajaMuokkausController.kysyPelaaja(null, uusi, 1);
        uusi.rekisteroi();
        joukkue.lisaa(uusi);
        hae(uusi.getTunnusNro());
    }
    
    /**
     * Tekee uuden tyhjän pelipaikan editointia varten
     */
    public void uusiPelipaikka() {
        if (pelaajanKohdalla == null) return;
        Pelipaikka uusi = new Pelipaikka(pelaajanKohdalla.getTunnusNro());
        uusi = PelipaikkaMuokkausController.kysyPelipaikka(null, uusi, 0);
        if ( uusi == null ) return;
        uusi.rekisteroi();
        joukkue.lisaa(uusi);
        naytaPelipaikat(pelaajanKohdalla); 
        tablePelipaikat.selectRow(1000);  // järjestetään viimeinen rivi valituksi
    }

    private void muokkaaPelipaikkaa() {
        int r = tablePelipaikat.getRowNr();
        if ( r < 0 ) return; // klikattu ehkä otsikkoriviä
        Pelipaikka pp = tablePelipaikat.getObject();
        if ( pp == null ) return;
        int k = tablePelipaikat.getColumnNr()+pp.ekaKentta();
        try {
            pp = PelipaikkaMuokkausController.kysyPelipaikka(null, pp.clone(), k);
            if ( pp == null ) return;
            joukkue.korvaaTaiLisaa(pp); 
            naytaPelipaikat(pelaajanKohdalla); 
            tablePelipaikat.selectRow(r);  // järjestetään sama rivi takaisin valituksi
        } catch (CloneNotSupportedException  e) { /* clone on tehty */  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }

    
    /**
     * Näyttää listasta valitun pelaajan tiedot
     */
    protected void naytaPelaaja() {
        pelaajanKohdalla = chooserPelaajat.getSelectedObject();
        if (pelaajanKohdalla == null) return;
        PelaajaMuokkausController.naytaPelaaja(edits, pelaajanKohdalla);
        naytaPelipaikat(pelaajanKohdalla);
    }
    

    private void muokkaa(int k) {
        if (pelaajanKohdalla == null) return;
        Pelaaja pelaaja;
        try {
            pelaaja = pelaajanKohdalla.clone();
            pelaaja = PelaajaMuokkausController.kysyPelaaja(null, pelaajanKohdalla, k);
            if (pelaaja == null) return;
            joukkue.korvaaTaiLisaa(pelaaja);
            hae(pelaaja.getTunnusNro());
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SailoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    private void naytaPelipaikat(Pelaaja pelaaja) {
        tablePelipaikat.clear();
        if (pelaaja == null) return;
        List<Pelipaikka> pelipaikat = joukkue.annaPelipaikat(pelaaja);
        if (pelipaikat.size() == 0) return;
        for (Pelipaikka pp : pelipaikat) {
            naytaPelipaikka(pp);
        }
        
    }
    
    private void naytaPelipaikka(Pelipaikka pp) {
        int kenttia = pp.getKenttia();
        String[] rivi = new String[kenttia-pp.ekaKentta()];
        for (int i = 0, k = pp.ekaKentta(); k < kenttia; i++, k++) {
            rivi[i] = pp.anna(k);
        }
        tablePelipaikat.add(pp, rivi);
    }
    
    /**
     * Asetetaan joukkue joka on käytössä
     * @param joukkue Käytössä oleva joukkue
     */
    public void setJoukkue(Joukkue joukkue) {
        this.joukkue = joukkue;
        naytaPelaaja();
    }
    
    /**
     * Alustaa uuden textArean johon tulee pelaajan tiedot 
     * ja tyhjentää listan missä on pelaajia
     */
    protected void alusta() {
        //panelPelaaja.setFitToHeight(true);
        
        chooserPelaajat.clear();
        chooserPelaajat.addSelectionListener(e -> naytaPelaaja());
        
        cbKentat.clear();
        for (int k = apupelaaja.ekaKentta(); k < apupelaaja.getKenttia(); k++)
            cbKentat.add(apupelaaja.getKysymys(k), null);
        cbKentat.getSelectionModel().select(0);
        
        edits = PelaajaMuokkausController.luoKentat(gridPelaaja);
        for (TextField edit: edits)  
            if ( edit != null ) {  
                edit.setEditable(false);  
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta));  
            }    
        int eka = apupelipaikka.ekaKentta();
        int lkm = apupelipaikka.getKenttia();
        String[] headings = new String[lkm-eka];
        for (int i = 0, k = eka; k < lkm; i++, k++) headings[i] = apupelipaikka.getKysymys(k); 
        tablePelipaikat.initTable(headings); 
        tablePelipaikat.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tablePelipaikat.setEditable(false); 
        tablePelipaikat.setPlaceholder(new Label("Ei vielä pelipaikkoja")); 
         
        // Tämä on vielä huono, ei automaattisesti muutu jos kenttiä muutetaan. 
        tablePelipaikat.setColumnSortOrderNumber(1); 
        tablePelipaikat.setColumnSortOrderNumber(2); 
        tablePelipaikat.setColumnWidth(1, 60); 
        tablePelipaikat.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaPelipaikkaa(); } );

    }

    
    
    /**
     * Tallennetaan tiedot
     */
    private String tallenna() {
        try {
            joukkue.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia. " + ex.getMessage());
            return ex.getMessage();
        }
    }
    
    /**
     * Tallennettaan ja tarkistetaan voiko sulkea
     * @return true jos sovelluksen saa sulkea
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    
    /**
     * Kysyys tiedoston nimen ja lukee sen
     * @return true tai false
     */
    public boolean avaa() {
        String uusinimi = JoukkueenNimiController.kysyNimi(null, joukkueennimi);
        if (uusinimi == null) return false;
        lueTiedosto(uusinimi);
        return true;
    }

    /**
     * Luetaan annettu tiedosto
     * @param uusinimi uusitiedoston nimi
     * @return null jos oonnistuu
     */
    protected String lueTiedosto(String uusinimi) {
        joukkueennimi = uusinimi;
        setTitle("Joukkue - " + joukkueennimi);
        try {
            joukkue.lueTiedostosta(uusinimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage();
            if (virhe != null) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
    }

    /**
     * Poistetaaan pelipaikkataulukosta valittu pelipaikka
     */
    private void poistaPelipaikka() {
        int rivi = tablePelipaikat.getRowNr();
        if (rivi < 0) return;
        Pelipaikka pelipaikka = tablePelipaikat.getObject();
        if (pelipaikka == null) return;
        joukkue.poistaPelipaikka(pelipaikka);
        naytaPelipaikat(pelaajanKohdalla);
        int pelipaikkoja = tablePelipaikat.getItems().size();
        if (rivi >= pelipaikkoja) rivi = pelipaikkoja - 1;
        tablePelipaikat.getFocusModel().focus(rivi);
        tablePelipaikat.getSelectionModel().select(rivi);
    }
    
    /**
     * Poistetaan listalta valittu pelaaja
     */
    private void poistaPelaaja()  {
        Pelaaja pelaaja = pelaajanKohdalla;
        if (pelaaja == null) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko pelaaja: " + pelaaja.getNimi(), "Kyllä", "Ei") ) return;
        joukkue.poista(pelaaja);
        int index = chooserPelaajat.getSelectedIndex();
        hae(0);
        chooserPelaajat.setSelectedIndex(index);
    }
    
    /**
     * Näytetään ohjelman suunnitelma erillisessä selaimessa.
     */
    private void avustus() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2021k/ht/jojohyva");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    private void setTitle(String title) {
       ModalController.getStage(hakuehto).setTitle(title); 
    }
    
}
