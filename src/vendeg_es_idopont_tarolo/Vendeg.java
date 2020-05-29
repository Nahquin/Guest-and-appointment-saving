package vendeg_es_idopont_tarolo;

/**
 *
 * @author Bottyán Deli Dániel
 */

public class Vendeg {
    private String nev;
    private String info;
    private String tel;
    private int id;
    
    public Vendeg() {
        this.nev = "";
        this.info = "-";
        this.tel = "-";
        this.id = 0;
    }
    public Vendeg(String a, String b, String c, int d) {
        this.nev = a;
        this.info = b;
        this.tel = c;
        this.id = d;
    }
    
    
    public String getNev() {
        return nev;
    }
    public String getInfo() {
        return info;
    }
    public String getTel() {
        return tel;
    }
    public int getId() {
        return id;
    }
    
    public void setNev(String nev) {
        this.nev = nev;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    
    @Override
    public String toString() {
        return nev + ";" + info + ";" + tel + ";" + id;
    }
    
    public String torleshez_nev() {
        String x = "Vendég: " + nev;
        return x;
    }
    
    public String torleshez_data() {
        String x = "Tel: " + tel ;
        return x;
    }
    
}
