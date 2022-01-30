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
import joukkue.Pelaaja;

/**
 * @author jojohyva
 * @version 11.2.2021
 *
 */
public class PelaajaMuokkausController implements ModalControllerInterface<Pelaaja>,Initializable {
    
    @FXML private Label labelVirhe;
    @FXML private GridPane gridPelaaja;
    
    @FXML void handleCancel() {
        pelaajanKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    @FXML void handleOk() {   
       if (pelaajanKohdalla != null && pelaajanKohdalla.getNimi().trim().equals("")) {
           naytaVirhe("Nimi ei saa olla tyhjä");
           return;
       }
       ModalController.closeStage(labelVirhe);
    }


    @Override
    public void handleShown() {
        kentta = Math.max(apupelaaja.ekaKentta(), Math.min(kentta,  apupelaaja.getKenttia()-1));
        edits[this.kentta].requestFocus();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    @Override
    public Pelaaja getResult() {
        return pelaajanKohdalla;
    }

    @Override
    public void setDefault(Pelaaja oletus) {
        pelaajanKohdalla = oletus;   
        naytaPelaaja(pelaajanKohdalla);
    }

    //=======================================================================================
    
    private Pelaaja pelaajanKohdalla;
    private TextField[] edits;
    private int kentta = 1;
    private static Pelaaja apupelaaja = new Pelaaja();
    
    /**
     * @param grid mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane grid) {
        grid.getChildren().clear();
        TextField[] fedits = new TextField[apupelaaja.getKenttia()];
        
        for (int i = 0, k = apupelaaja.ekaKentta(); k < apupelaaja.getKenttia(); k++, i++) {
            Label label = new Label(apupelaaja.getKysymys(k));
            grid.add(label, 0, i);
            TextField edit = new TextField();
            fedits[k] = edit;                                                                                       
            edit.setId("e"+k);
            grid.add(edit, 1, i); 
        }
        return fedits;
    }
    
    
    private void alusta() {
        edits = luoKentat(gridPelaaja);
        for (TextField edit : edits) {
            if (edit != null) 
                edit.setOnKeyReleased(e -> kasitteleMuutosPelaajaan(edit));
        }
    }
    /**
     * Käsitellään jäseneen tullut muutos
     * @param edit muuttunut kenttä
     */
    private void kasitteleMuutosPelaajaan(TextField edit) {
        if (pelaajanKohdalla == null) return;
        int k = getFieldId(edit, apupelaaja.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = pelaajanKohdalla.aseta(k,s);
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
    
    
    private void naytaPelaaja(Pelaaja pelaaja) {
        naytaPelaaja(edits, pelaaja);
    }
    
    /**
     * Näytetään pelaajan tiedot TextField komponentteihin
     * @param edits taulukko jossa tekstikentät
     * @param pelaaja näytettävä pelaaja
     */ 
    public static void naytaPelaaja(TextField[] edits, Pelaaja pelaaja) {
        if (pelaaja == null) return;
        for (int k = pelaaja.ekaKentta(); k < pelaaja.getKenttia(); k++) {
            edits[k].setText(pelaaja.anna(k));
        }
    }
    
    /**
     * Luodaan pelaajan kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan cancel, muuten täytetty tietue
     */
    public static Pelaaja kysyPelaaja(Stage modalityStage, Pelaaja oletus, int kentta) {
        return ModalController.<Pelaaja, PelaajaMuokkausController>showModal(
                PelaajaMuokkausController.class.getResource("PelaajaMuokkausView.fxml"),
                "Joukkue",
                modalityStage, oletus,
                ctrl -> ctrl.setKentta(kentta));
    }
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }

}