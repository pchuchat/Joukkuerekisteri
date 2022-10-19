package fxJoukkue;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import joukkue.Pelipaikka;

/**
 * @author jojohyva 
 * @version 21.4.2021
 *
 */
public class PelipaikkaMuokkausController implements ModalControllerInterface<Pelipaikka>,Initializable {
    @FXML private Label labelVirhe;
    @FXML private GridPane gridPelipaikka;
    
    @FXML void handleCancel() {
        pelipaikanKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    @FXML void handleOk() {   
       if (pelipaikanKohdalla != null && pelipaikanKohdalla.getPelipaikka().equals("")) {
           naytaVirhe("Nimi ei saa olla tyhjä");
           return;
       }
       ModalController.closeStage(labelVirhe);
    }


    @Override
    public void handleShown() {
        alusta();
        kentta = Math.max(apupelipaikka.ekaKentta(), Math.min(kentta,  apupelipaikka.getKenttia()-1));
        edits[this.kentta].requestFocus();
        naytaPelipaikka(pelipaikanKohdalla);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //alusta();
    }

    @Override
    public Pelipaikka getResult() {
        return pelipaikanKohdalla;
    }

    @Override
    public void setDefault(Pelipaikka oletus) {
        pelipaikanKohdalla = oletus;   
        //naytaPelipaikka(pelipaikanKohdalla);
    }

    //=======================================================================================
    
    private Pelipaikka pelipaikanKohdalla;
    private TextField[] edits;
    private int kentta = 2;
    private static Pelipaikka apupelipaikka = new Pelipaikka();
    
    /**
     * @param grid mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane grid) {
        grid.getChildren().clear();
        TextField[] fedits = new TextField[apupelipaikka.getKenttia()];
        
        for (int i = 0, k = apupelipaikka.ekaKentta(); k < apupelipaikka.getKenttia(); k++, i++) {
            Label label = new Label(apupelipaikka.getKysymys(k));
            grid.add(label, 0, i);
            TextField edit = new TextField();
            fedits[k] = edit;
            edit.setId("e"+k);
            grid.add(edit, 1, i); 
        }
        return fedits;
    }
    
    
    private void alusta() {
        edits = luoKentat(gridPelipaikka);
        for (TextField edit : edits) {
            if (edit != null) 
                edit.setOnKeyReleased(e -> kasitteleMuutosPelipaikkaan(edit));
        }
    }
    /**
     * Käsitellään jäseneen tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosPelipaikkaan(TextField edit) {
        if (pelipaikanKohdalla == null) return;
        int k = getFieldId(edit, apupelipaikka.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = pelipaikanKohdalla.aseta(k,s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }

    
    private void naytaVirhe(String virhe) {
        if (virhe == null || virhe.isEmpty()) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    /**
     * Palautetaan komponentin id:stä saatava luku
     * @param obj tutkittava komponentti
     * @param oletus mikä arvo jos id ei ole kunnollinen
     * @return komponenetin id lukuna
     */
    public static int getFieldId(Object obj, int oletus) {
        if (!(obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1), oletus);
    }
    
    
    private void naytaPelipaikka(Pelipaikka pelipaikka) {
        naytaPelipaikka(edits, pelipaikka);
    }
    
    /**
     * Näytetään pelaajan tiedot TextField komponentteihin
     * @param edits taulukko jossa tekstikentät
     * @param pelipaikka näytettävä pelaaja
     */ 
    public static void naytaPelipaikka(TextField[] edits, Pelipaikka pelipaikka) {
        if (pelipaikka == null) return;
        for (int k = pelipaikka.ekaKentta(); k < pelipaikka.getKenttia(); k++) {
            edits[k].setText(pelipaikka.anna(k));
        }
    }
    
    /**
     * Luodaan pelipaikan kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan cancel, muuten täytetty tietue
     */
    public static Pelipaikka kysyPelipaikka(Stage modalityStage, Pelipaikka oletus, int kentta) {
        return ModalController.<Pelipaikka, PelipaikkaMuokkausController>showModal(
                PelipaikkaMuokkausController.class.getResource("PelipaikkaMuokkausView.fxml"),
                "Joukkue",
                modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta));
    }

    private void setKentta(int kentta) {
        this.kentta = kentta;
    }

}