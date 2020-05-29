package vendeg_es_idopont_tarolo;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.util.Calendar;
import java.util.TimeZone;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 *
 * @author Bottyán Deli Dániel
 */

public class FXMLDocumentController implements Initializable {
     
    
    // Mindent összecsukni: CTRL SHIFT -
    
    
    //Változók------------------------------------------------------------
    
    DB ab = new DB();
    String[] napok = {"Hétfő", "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"};
    String[] honapok = {"JAN", "FEB", "MAR", "APR", "MAJ", "JUN", "JUL", "AUG", "SZEP", "OKT", "NOV", "DEC"};
    //                      0,      2,     0,     1,     0,     1,    0,     0,    1,      0,     1,     0
    //                   0 = 0,2,4,6,7,9,11  ->31
    //                   1 = 3,5,8,10        ->30
    //                   2 = 1;              ->29
    
    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Budapest"));

    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);
    
    int[] hovegek = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    int counter = 0;
    int hanyadika = 5;
    int hanyadikho = 0;
    
    boolean elorre = true;
    boolean ahanyadikaMaVan;
    
    boolean váltásVoltChekker = false;
    
    int megjelenitettIdopontokANaptárban = 0;
    
    String utoljáraLátogatottOldal = "";
    
    
    
    //Idő beállítása------------------------------------------------------------
    
    public void setTime() {
        ev.setText(Integer.toString(year));
        while (hanyadikho != month){
            right();
        }
        ahanyadikaMaVan = false;
        for (int i = hanyadika; i <= day; i++) {
            right();
        System.out.println(hanyadika);
        System.out.println(day);
            if (ahanyadikaMaVan == true) {
                return;
            }
        }
    }
    
    
    //Lapozás a naptárban------------------------------------------------------------
    
    boolean vasranapavalto = false;
    private void napok(){
        Text[] texts = { nap1, nap2, nap3, nap4, nap5, nap6, nap7 };
        boolean váltásVolt = false;
        int nap = 0;
        vasranapavalto = false;
        if (!elorre) {
            elorre = true;
            napok();
        }
        for (Text text : texts) {
            hanyadika++;
            text.setText(napok[nap] + " " + (hanyadika) + ".");
            nap++;
            váltásVoltChekker = váltásVolt;
            if (hanyadika == day) {
                ahanyadikaMaVan = true;
            }
            if (hanyadika == 31 && (hanyadikho == 0 
                    || hanyadikho == 2 || hanyadikho == 4 
                    || hanyadikho == 6 || hanyadikho == 7 
                    || hanyadikho == 9 || hanyadikho == 11 )) {
                hanyadikho++;
                hanyadika = 0;
                váltásVolt = true;
                if (napok[nap-1].equals("Vasárnap")) {
                    vasranapavalto = true;
                }
                
                
            }            
            if (hanyadika == 30 && (hanyadikho == 3 || hanyadikho == 5 
                    || hanyadikho == 8 || hanyadikho == 10 )) {
                hanyadikho++;
                hanyadika = 0;
                váltásVolt = true;
                if (napok[nap-1].equals("Vasárnap")) {
                    vasranapavalto = true;
                }
            }            
            if (hanyadika == 29 && hanyadikho == 1) {
                hanyadikho++;
                hanyadika = 0;
                váltásVolt = true;
                if (napok[nap-1].equals("Vasárnap")) {
                    vasranapavalto = true;
                }
            }
        }
        honap.setText(honapok[hanyadikho]);
        if (váltásVolt) {
            honap.setText(honapok[hanyadikho-1] + " - " + honapok[hanyadikho]);
            if (vasranapavalto) {
                honap.setText(honapok[hanyadikho-1]);
            }
        }
    }
    
    
    private void napokVissza(){
        Text[] texts = { nap7, nap6, nap5, nap4, nap3, nap2, nap1 };
        boolean váltásVolt = false;
        int nap = 6;
        vasranapavalto = false;
        if (elorre) {
            elorre = false;
            napokVissza();
        }
        for (Text text : texts) {
            if (hanyadika < 1) {
                hanyadikho--;
                hanyadika = hovegek[hanyadikho];
                váltásVolt = true;
                System.out.println(napok[nap]);
                if (napok[nap].equals("Vasárnap")) {
                    vasranapavalto = true;
                }
            }
            text.setText(napok[nap] + " " + (hanyadika) + ".");
            hanyadika--;
            nap--;
        }
        honap.setText(honapok[hanyadikho]);
        váltásVoltChekker = váltásVolt;
        if (váltásVolt) {
            honap.setText(honapok[hanyadikho] + " - " + honapok[hanyadikho+1]);
            if (vasranapavalto) {
                honap.setText(honapok[hanyadikho]);
            }
        }
    }
    
    @FXML
    void left() {
        if (counter == 1) {
            return;
        }
        counter--;
        napokVissza();
        naptarCleaner();
        idopontokMegjelenitese();
    }
    
    @FXML
    void right() {
        counter++;
        napok();
        naptarCleaner();
        idopontokMegjelenitese();
    }
    
    
    //Váltás a lapok között------------------------------------------------------------
    
    public void ablakokLe(){
        menu.setVisible(false);
        popup.setVisible(false);
        popupT.setVisible(false);
        naptar.setVisible(false);
        nevjegyek.setVisible(false);
        idopontfoglalas.setVisible(false);
        idopontokModositasa.setVisible(false);
    }
    
    @FXML
    void naptarA() {
        naptarCleaner();
        idopontokMegjelenitese();
        ablakokLe();
        naptar.setVisible(true);
    }
    
    @FXML
    void menuA() {
        ablakokLe();
        menu.setVisible(true);
    }
    
    @FXML
    void idopontfoglalasA() {
        ablakokLe();
        idopontfoglalas.setVisible(true);
        utoljáraLátogatottOldal = "idopontFoglalas";
        loadAll();
    }
    
    @FXML
    void nevjegyekA() {
        ablakokLe();
        utoljáraLátogatottOldal = "nevjegyek";
        nevjegyek.setVisible(true);
    }
    
    @FXML
    void bPopup() {
        ablakokLe();
        if (utoljáraLátogatottOldal.equals("idopontFoglalas")) {
            idopontfoglalas.setVisible(true);
        }
        else if (utoljáraLátogatottOldal.equals("idopontModositas")) {
            idopontokModositasa.setVisible(true);
        }
        else if (utoljáraLátogatottOldal.equals("nevjegy_hozzad")) {
            nevjegyekA();
        }
    }

    @FXML
    void idopontmodositasA() {
        ablakokLe();
        idopontokModositasa.setVisible(true);
        utoljáraLátogatottOldal = "idopontModositas";
        loadAllI();
        ab.idopont_be(tblIdopontok.getItems());
    }
    
    
    //Időpont foglalás------------------------------------------------------------
    
    private void loadVendegek() {
        cbVendegek.getItems().clear();
        int i = 0;
        while(oNev.getCellData(i)!=null){
            cbVendegek.getItems().add(oNev.getCellData(i));
            i++;
        }
    }
    
    private void loadHonapok() {
        cbHonap.getItems().clear();
        cbHonap.getItems().addAll(honapok);
        cbHonap.setValue(honapok[hanyadikho]);
        if (vasranapavalto) {
                cbHonap.setValue(honapok[hanyadikho-1]);
        }
        
    }
    
    private void loadNapok() {
        cbNap.getItems().clear();
        for (int i = 1; i <= hovegek[cbHonap.getSelectionModel().getSelectedIndex()]; i++) {
            cbNap.getItems().add(i);
        }
        cbNap.setValue(day);
    }
    
    private void loadOra(){
        cbOra.getItems().clear();
        for (int i = 8; i < 16; i++) {
            cbOra.getItems().add(i);
        }
        cbOra.setValue(9);
    }
    
    private void loadAll(){
        loadVendegek();
        loadHonapok();
        loadNapok();
        loadOra();
    }
    
    @FXML
    void idopontHozzaad() {
        int a = cbVendegek.getSelectionModel().getSelectedIndex();
        if(a <= -1) {
            return;
        }
        Idopont i = new Idopont();
        i.setVendeg(cbVendegek.getSelectionModel().getSelectedItem());
        i.setHonap(cbHonap.getSelectionModel().getSelectedItem());
        i.setNap(cbNap.getSelectionModel().getSelectedItem());
        i.setOra(cbOra.getSelectionModel().getSelectedItem());
        i.setId(ab.idopont_id());
        
        boolean ellenorzes = idopontEllenorzes(i.toString());
        if (ellenorzes) {
            idopontfoglalasPopup();
            return;
        }
        String[] iToDb = i.toString().split(";");
        
        ab.idopontDB(iToDb[0], iToDb[1], Integer.parseInt(iToDb[2]), 
                Integer.parseInt(iToDb[3]), Integer.parseInt(iToDb[4]));
        
        
        naptarCleaner();
        naptarA();
    }

    public boolean idopontEllenorzes(String adat){
        String[] a = adat.split(";");
        String adatHonap = a[1];
        int adatNap = Integer.parseInt(a[2]);
        int adatOra = Integer.parseInt(a[3]);
        int adatId = Integer.parseInt(a[4]);
        
        boolean modositas = false;
        boolean back = false;

        ab.idopont_be(tblIdopontokI.getItems());
        
        for (int i = 0; i < tblIdopontokI.getItems().size(); i++) {
            
            String sor = tblIdopontokI.getItems().get(i).toString();
            String[] s = sor.split(";");
            String ho = s[1];
            int nap = Integer.parseInt(s[2]);
            int ora = Integer.parseInt(s[3]);
            int id = Integer.parseInt(s[4]);
            if (adatHonap.equals(ho) & adatNap == nap  &  adatOra == ora) {
              back = true;
                if (adatId == id) {
                    modositas = true;
                }
            }
        }
        if (modositas == true) {
                back = false;
        }
        return back;
    }
    
    public void idopontfoglalasPopup(){
        ablakokLe();
        popup.setVisible(true);
        txtPopup.setText("Ez az időpont már foglalt!");
    }
    
    public void foglalt_nev_Popup(){
        ablakokLe();
        popup.setVisible(true);
        txtPopup.setText("Ez a név már szerepel!");
    }
    
    
    //Időpont módosítása, törlése------------------------------------------------------------
    
    private void loadVendegI() {
        cbVendegI.getItems().clear();
        int i = 0;
        while(oNev.getCellData(i)!=null){
            cbVendegI.getItems().add(oNev.getCellData(i));
            i++;
        }
    }
        
    private void loadHonapI() {
        cbHonapI.getItems().clear();
        cbHonapI.getItems().addAll(honapok);
        cbHonapI.setValue(honapok[hanyadikho]);
        if (vasranapavalto) {
                cbHonapI.setValue(honapok[hanyadikho-1]);
        }
    }
    
    private void loadNapI(){
        cbNapI.getItems().clear();
        int id = cbHonapI.getSelectionModel().getSelectedIndex();
        for (int i = 1; i <= hovegek[id]; i++) {
            cbNapI.getItems().add(i);
        }
        cbNapI.setValue(day);
    }
    
    private void loadOraI(){
        cbOraI.getItems().clear();
        for (int i = 8; i < 16; i++) {
            cbOraI.getItems().add(i);
        }
        cbOraI.setValue(9);
    }
    
    private void loadHonapIKer() {
        cbHonapIKer.getItems().clear();
        cbHonapIKer.getItems().addAll(honapok);
        cbHonapIKer.setValue(honapok[hanyadikho]);
    }
    
    private void loadAllI(){
        loadVendegI();
        loadHonapI();
        loadNapI();
        loadOraI();
        loadHonapIKer();
    }
 
    @FXML
    void modositI(ActionEvent event) {
        String[] s = tblIdopontok.getSelectionModel().getSelectedItems()
                .toString().substring(1, tblIdopontok.getSelectionModel()
                        .getSelectedItems().toString().length()-1).split(";");
       
        int id = Integer.parseInt(s[4]);
        if(id <= -1) {
            return;
        }
        Idopont i = new Idopont();
        i.setVendeg(cbVendegI.getValue());
        i.setHonap(cbHonapI.getValue());
        i.setNap(cbNapI.getValue());
        i.setOra(cbOraI.getValue());
        i.setId(id);
        
        boolean ellenorzes = idopontEllenorzes(i.toString());
        if (ellenorzes) {
            idopontfoglalasPopup();
            return;
        }
        String[] iToDb = i.toString().split(";");
        ab.idopont_modosit(iToDb[0], iToDb[1], Integer.parseInt(iToDb[2]), 
                Integer.parseInt(iToDb[3]), Integer.parseInt(iToDb[4]));
        naptarA();
    }

    @FXML
    void torolI() {
        String[] s = tblIdopontok.getSelectionModel().getSelectedItems().
                toString().substring(1, tblIdopontok.getSelectionModel().
                        getSelectedItems().toString().length()-1).split(";");
        int id = Integer.parseInt(s[4]);
        if(id <= -1) {
            return;
        }
        ab.idopont_torol(id);
        int x = ab.idopont_utolso_id();
        for (int j = id; j < x; j++) {
            ab.idopont_id_csokkentes(j);
        }
        naptarA();
    }    
    
    @FXML
    void keresesI(){
        ab.idopont_kereses_ho(tblIdopontok.getItems(),
                cbHonapIKer.getSelectionModel().getSelectedItem());
    }    

    private void beallitI(Idopont i) {
        cbVendegI.setValue(i.getVendeg());
        cbHonapI.setValue(i.getHonap());
        cbNapI.setValue(i.getNap());
        cbOraI.setValue(i.getOra());
    }   
    
    @FXML
    void ujI() {
        beallitI(new Idopont());
        tblIdopontok.getSelectionModel().select(null);
    }
    
    
    //Időpontok naptárba------------------------------------------------------------
    
    public void idopontokMegjelenitese(){
        megjelenitettIdopontokANaptárban = 0;
        String[] elsoHok = new String[0];
        String[] masodikHok = new String[0];
        String ho = honap.getText();
        String[] hok = ho.split(" - ");
        ab.idopont_be(tblIdopontokI.getItems());
        if (hok.length == 1) {
            for (Idopont i : tblIdopontokI.getItems()) {
                if (i.getHonap().equals(hok[0])) {
                    elsoHok = addX(elsoHok.length,  elsoHok,  i.toString());
                }
            }
            Text[] texts = { nap1, nap2, nap3, nap4, nap5, nap6, nap7 };
            for (int i = 0; i < texts.length; i++) {
                String []hanyadikaNaptarban = texts[i].getText().split(" ");
                int naptarDatum = Integer.parseInt(hanyadikaNaptarban[1].
                        substring(0, hanyadikaNaptarban[1].length()-1));
                for (int j = 0; j < elsoHok.length; j++) {
                    String[] foglalasiadatok = elsoHok[j].split(";");
                    String foglalasiNev = foglalasiadatok[0];
                    int foglalasiNap = Integer.parseInt(foglalasiadatok[2]);
                    int foglalasiOra = Integer.parseInt(foglalasiadatok[3]);
                    if (naptarDatum == foglalasiNap ) {
                            megjelenitettIdopontokANaptárban++;
                            int sorNaptarba = foglalasiOra - 7 ;
                            int oszlopNaptarba = i + 1;
                            Text text = new Text();
                            text.setText(foglalasiNev);
                            gNaptar.add(text, oszlopNaptarba, sorNaptarba);
                    }
                }
            }
        }
        if (hok.length == 2) {
            for (Idopont i : tblIdopontokI.getItems()) {
                if (i.getHonap().equals(hok[0])) {
                    elsoHok = addX(elsoHok.length,  elsoHok,  i.toString());
                }
                if (i.getHonap().equals(hok[1])) {
                    masodikHok = addX(masodikHok.length,  masodikHok,  i.
                            toString());
                }
            }
            Text[] texts = { nap1, nap2, nap3, nap4, nap5, nap6, nap7 };
            for (int i = 0; i < texts.length; i++) {
                String []hanyadikaNaptarban = texts[i].getText().split(" ");
                int naptarDatum = Integer.parseInt(hanyadikaNaptarban[1].
                        substring(0, hanyadikaNaptarban[1].length()-1));
                for (int j = 0; j < elsoHok.length; j++) {
                    String[] foglalasiadatok = elsoHok[j].split(";");
                    String foglalasiNev = foglalasiadatok[0];
                    String foglalasiHo = foglalasiadatok[1];
                    int foglalasiNap = Integer.parseInt(foglalasiadatok[2]);
                    int foglalasiOra = Integer.parseInt(foglalasiadatok[3]);
                    if (naptarDatum == foglalasiNap && foglalasiHo.
                            equals(hok[0]) && foglalasiNap > 8) {
                            megjelenitettIdopontokANaptárban++;
                            int sorNaptarba = foglalasiOra - 7 ;
                            int oszlopNaptarba = i + 1;
                            Text text = new Text();
                            text.setText(foglalasiNev);
                            gNaptar.add(text, oszlopNaptarba, sorNaptarba);
                    }
                }
                for (int j = 0; j < masodikHok.length; j++) {
                    String[] foglalasiadatok = masodikHok[j].split(";");
                    String foglalasiNev = foglalasiadatok[0];
                    String foglalasiHo = foglalasiadatok[1];
                    int foglalasiNap = Integer.parseInt(foglalasiadatok[2]);
                    int foglalasiOra = Integer.parseInt(foglalasiadatok[3]);
                    if (naptarDatum == foglalasiNap && foglalasiHo.
                            equals(hok[1]) && foglalasiNap < 8) {
                            megjelenitettIdopontokANaptárban++;
                            int sorNaptarba = foglalasiOra - 7 ;
                            int oszlopNaptarba = i + 1;
                            Text text = new Text();
                            text.setText(foglalasiNev);
                            gNaptar.add(text, oszlopNaptarba, sorNaptarba);
                    }  
                }
            }
        }   
    }
    
    public static String[] addX(int hossz, String arr[], String ujAdat){ 
        int i; 
        String newarr[] = new String[hossz + 1]; 
  
        for (i = 0; i < hossz; i++) 
            newarr[i] = arr[i]; 
        newarr[hossz] = ujAdat; 
        return newarr; 
    } 
    
    public void naptarCleaner(){
        while(gNaptar.getChildren().size() != 16){
            gNaptar.getChildren().remove(16);
        }
    }

    
    //Névjegyek------------------------------------------------------------
    public void nevjegyek_ujratoltese() {
        ab.vendeg_be(tblVendegek.getItems());
    }
    
    @FXML
    void hozzaad() {
        Vendeg v = new Vendeg();
        int id = tblVendegek.getItems().size();
        if (txtVendeg.getText().isEmpty()) {
            return;
        }
        for (int i = 0; i < id; i++) {
            if (tblVendegek.getItems().get(i).getNev().equals(txtVendeg.getText())) {
                foglalt_nev_Popup();
                utoljáraLátogatottOldal = "nevjegy_hozzad";
                return;
            }
        }
        v.setNev(txtVendeg.getText());
        v.setInfo(txtInfo.getText());
        if (txtInfo.getText().isEmpty()) {
            v.setInfo("-");
        }
        v.setTel(txtTel.getText());
        if (txtTel.getText().isEmpty()) {
            v.setTel("-");
        }
        v.setId(id);
        ab.vendegDB(v.getNev(), v.getInfo(), v.getTel(), id);
        nevjegyek_ujratoltese();
    }

    @FXML
    void modosit() {
        Vendeg v = new Vendeg();
        int id = tblVendegek.getSelectionModel().getSelectedIndex();
        if(id <= -1) {
            return;
        }
        if (txtVendeg.getText().isEmpty()) {
            return;
        }
        for (int i = 0; i < id; i++) {
            if (tblVendegek.getItems().get(i).getNev().equals(txtVendeg.getText())) {
                foglalt_nev_Popup();
                utoljáraLátogatottOldal = "nevjegy_hozzad";
                return;
            }
        }
        v.setNev(txtVendeg.getText());
        v.setInfo(txtInfo.getText());
        if (txtInfo.getText().isEmpty()) {
            v.setInfo("-");
        }
        v.setTel(txtTel.getText());
        if (txtTel.getText().isEmpty()) {
            v.setTel("-");
        }
        ab.vendeg_modosit(v.getNev(), v.getInfo(), v.getTel(), id);
        nevjegyek_ujratoltese();
    }

    @FXML
    void torol() {
        int id = tblVendegek.getSelectionModel().getSelectedIndex();
        if (id >-1 ){
            tblVendegek.getItems().remove(id);
            ab.vendeg_torol(id);
        }
        for (int j = id; j < tblVendegek.getItems().size(); j++) {
            ab.vendeg_id_modosit(j);
        }
        nevjegyek_ujratoltese();
    }
    
    private void beallit(Vendeg v) {
        txtVendeg.setText(v.getNev());
        txtInfo.setText(v.getInfo());
        txtTel.setText(v.getTel());
    }     
    
    @FXML
    void uj() {
        beallit(new Vendeg());
        tblVendegek.getSelectionModel().select(null);
    }
      
    
    //Időpontok, Névjegyek törlésének biztonsági kérdése-------------------------------
    
    @FXML
    void biztosanTorlod(){
        if (utoljáraLátogatottOldal.equals("nevjegyek")) {
            if (tblVendegek.getSelectionModel().getSelectedIndex() < 0) {
                return;
            }
            txtPopupTname.setText(tblVendegek.getSelectionModel().
                    getSelectedItem().torleshez_nev());
            txtPopupTdata.setText(tblVendegek.getSelectionModel().
                    getSelectedItem().torleshez_data());
        }
        
        if (utoljáraLátogatottOldal.equals("idopontModositas")) {
            if (tblIdopontok.getSelectionModel().getSelectedIndex() < 0) {
                return;
            }
            txtPopupTname.setText(tblIdopontok.getSelectionModel().
                    getSelectedItem().torleshez_nev());
            txtPopupTdata.setText(tblIdopontok.getSelectionModel().
                    getSelectedItem().torleshez_data());
        }
        ablakokLe();
        popupT.setVisible(true);
        txtPopupT.setText("Biztosan törlöd?");
    }
    
    @FXML
    void bIgenT(){
        popupT.setVisible(false);
        if (utoljáraLátogatottOldal.equals("nevjegyek")) {
            nevjegyek.setVisible(true);
            torol();
        }
        if (utoljáraLátogatottOldal.equals("idopontModositas")) {
            idopontokModositasa.setVisible(true);
            torolI();
        }
    }
    
    @FXML
    void bNemT(){
        popupT.setVisible(false);
        if (utoljáraLátogatottOldal.equals("nevjegyek")) {
            nevjegyek.setVisible(true);
        }
        if (utoljáraLátogatottOldal.equals("idopontModositas")) {
            idopontokModositasa.setVisible(true);
        }
    }

    
    //START------------------------------------------------------------
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ab.vendeg_be(tblVendegek.getItems());
        ab.idopont_be(tblIdopontokI.getItems());
        ab.idopont_be(tblIdopontok.getItems());
        
        menuA();
        
        oNev.setCellValueFactory(new PropertyValueFactory<>("nev"));
        oInfo.setCellValueFactory(new PropertyValueFactory<>("info"));
        oTel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        
        oVendeg.setCellValueFactory(new PropertyValueFactory<>("vendeg"));
        oHonap.setCellValueFactory(new PropertyValueFactory<>("honap"));
        oNap.setCellValueFactory(new PropertyValueFactory<>("nap"));
        oOra.setCellValueFactory(new PropertyValueFactory<>("ora"));
        
        tblVendegek.getSelectionModel().selectedItemProperty().addListener(
                (o, regi, uj) -> {
                    if (uj != null)
                        beallit(uj);
                }
        );
        tblIdopontok.getSelectionModel().selectedItemProperty().addListener(
                (o, regi, ujI) -> {
                    if (ujI != null)
                        beallitI(ujI);
                }
        );
        setTime();
        loadAll();
        
    }  
    
    
    //FXML importok------------------------------------------------------------
               
    @FXML
    private ChoiceBox<String> cbVendegek;
    
    @FXML
    private ChoiceBox<String> cbHonap;

    @FXML
    private ChoiceBox<Integer> cbNap;
    
    @FXML
    private ChoiceBox<Integer> cbOra;
    
    @FXML
    private ChoiceBox<String> cbHonapIKer;
    
    @FXML
    private ChoiceBox<String> cbVendegI;
    
    @FXML
    private ChoiceBox<String> cbHonapI;
    
    @FXML
    private ChoiceBox<Integer> cbNapI;
    
    @FXML
    private ChoiceBox<Integer> cbOraI;
    
    @FXML
    private TextField txtPopup;
    
    @FXML
    private TextField txtPopupT;
    
    @FXML
    private TextField txtPopupTdata;
    
    @FXML
    private TextField txtPopupTname;
    
    @FXML
    private BorderPane popup;
    
    @FXML
    private GridPane popupT;
    
    @FXML
    private TableColumn<Vendeg, String> oNev;

    @FXML
    private TableColumn<Vendeg, String> oInfo;

    @FXML
    private TableColumn<Vendeg, String> oTel;
    
    @FXML
    private TableColumn<Vendeg, String> oVendeg;

    @FXML
    private TableColumn<Vendeg, String> oHonap;

    @FXML
    private TableColumn<Vendeg, String> oNap;
    
    @FXML
    private TableColumn<Vendeg, String> oOra;
    
    @FXML
    private TableView<Vendeg> tblVendegek;
    
    @FXML
    private TableView<Idopont> tblIdopontokI;
    
    @FXML
    private TableView<Idopont> tblIdopontok;
    
    @FXML
    private TextField txtInfo;
    
    @FXML
    private TextField txtTel;
    
    @FXML
    private TextField txtVendeg;
    
    @FXML
    private Label ev;
            
    @FXML
    private Label honap;   
    
    @FXML
    private Text  nap7;

    @FXML
    private Text  nap6;
    
    @FXML
    private Text  nap5;
    
    @FXML
    private Text  nap4;

    @FXML
    private Text  nap3;

    @FXML
    private Text  nap2;

    @FXML
    private Text  nap1;
    
    @FXML
    private FlowPane naptar;

    @FXML
    private AnchorPane menu;
    
    @FXML
    private GridPane idopontfoglalas;
    
    @FXML
    private GridPane idopontokModositasa;
    
    @FXML
    private GridPane gNaptar;
    
    @FXML
    private GridPane nevjegyek;
}
