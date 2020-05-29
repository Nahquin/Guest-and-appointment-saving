package vendeg_es_idopont_tarolo;

/**
 *
 * @author Bottyán Deli Dániel
 */

public class Idopont {
    private String vendeg;
    private String honap;
    private int nap;
    private int ora;
    private int id;

    public Idopont() {
        this.vendeg = "";
        this.honap = "";
        this.nap =  0;
        this.ora = 0;
        this.id = 0;
    }
    public Idopont(String a, String b, int c, int d, int e) {
        this.vendeg = a;
        this.honap = b;
        this.nap =  c;
        this.ora = d;
        this.id = e;
    }
        
    public int getOra() {
        return ora;
    }
    public String getVendeg() {
        return vendeg;
    }
    public int getId() {
        return id;
    }
    public String getHonap() {
        return honap;
    }
    public int getNap() {
        return nap;
    }

    public void setOra(int ora) {
        this.ora = ora;
    }
    public void setVendeg(String vendeg) {
        this.vendeg = vendeg;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setHonap(String honap) {
        this.honap = honap;
    }
    public void setNap(int nap) {
        this.nap = nap;
    }
    
    @Override
    public String toString() {
        return vendeg + ";" + honap + ";" + nap + ";" + ora + ";" + id;
    }
    
    public String torleshez_nev() {
        String x = "Vendég: " + vendeg;
        return x;
    }
    
    public String torleshez_data() {
        String x = "Időpont: " + honap + ". " + nap + ". " + ora + " óra";
        return x;
    }
    
}
