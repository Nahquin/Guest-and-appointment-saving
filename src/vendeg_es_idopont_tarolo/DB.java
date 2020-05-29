package vendeg_es_idopont_tarolo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;

/**
 *
 * @author Bottyán Deli Dániel
 */


//Adatbázis létrehozása------------------------------------------------------------

public class DB {
    
    final String dbUrl1 = "jdbc:mysql://localhost:3306";
    final String dbUrl2 = "jdbc:mysql://localhost:3306/vendeg_es_idopont_tarolo"
                    + "?useUnicode=true&characterEncoding=UTF-8";
    final String user = "root";
    final String pass = "";
    

    public DB() {
        String s1 = "CREATE DATABASE IF NOT EXISTS vendeg_es_idopont_tarolo "
                + "DEFAULT CHARSET=utf8mb4 "
                + "COLLATE=utf8mb4_hungarian_ci";
        String s2 = "USE vendeg_es_idopont_tarolo";
        String s3 = "CREATE TABLE IF NOT EXISTS vendegek ("
                + "vendeg varchar(50) UNIQUE,"
                + "info varchar(50),"
                + "tel varchar(15),"
                + "id int NOT NULL UNIQUE"
                + ");";
        String s4 = "CREATE TABLE IF NOT EXISTS idopontok ("
                + "vendeg varchar(50) UNIQUE,"
                + "honap varchar(10),"
                + "nap int(3),"
                + "ora int(3),"
                + "id int NOT NULL UNIQUE,"
                + "FOREIGN KEY (vendeg) REFERENCES vendegek(vendeg) ON DELETE CASCADE  "
                + ");";
        try (Connection kapcs = DriverManager.getConnection(dbUrl1, user, pass);
                PreparedStatement ekp1 = kapcs.prepareStatement(s1);
                PreparedStatement ekp2 = kapcs.prepareStatement(s2);
                PreparedStatement ekp3 = kapcs.prepareStatement(s3);
                PreparedStatement ekp4 = kapcs.prepareStatement(s4)) {
            ekp1.execute();
            ekp2.execute();
            ekp3.execute();
            ekp4.execute();
            
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
            
//Mentés adatbázisba--------------------------------------------------------
    
    public void idopontDB(String vendeg, String honap, int nap, int ora, int id){
        String s = "INSERT INTO idopontok (vendeg, honap, nap, ora, id) "
                + "VALUES (?,?,?,?,?);";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)){
                ekp.setString(1, vendeg);
                ekp.setString(2, honap);
                ekp.setInt(3, nap);
                ekp.setInt(4, ora);
                ekp.setInt(5, id);
                ekp.executeUpdate();
        } 
        catch (Exception e) {
        }
    }    
        
    public void vendegDB(String vendeg, String info, String tel, int id){
        String s = "INSERT INTO vendegek (vendeg, info, tel, id) "
                + "VALUES (?,?,?,?);";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)){
                ekp.setString(1, vendeg);
                ekp.setString(2, info);
                ekp.setString(3, tel);
                ekp.setInt(4, id);
                ekp.executeUpdate();
        } 
        catch (Exception e) {
        }
    }   
    
//Adatbázis módosítása--------------------------------------------------------
    
    public void idopont_modosit(String vendeg, String honap, int nap, int ora, int id) {
        String s = "UPDATE idopontok SET vendeg = ?, honap = ?, nap = ?, ora = ?  WHERE id = ?;";
        
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)) {
                ekp.setString(1, vendeg);
                ekp.setString(2, honap);
                ekp.setInt(3, nap);
                ekp.setInt(4, ora);
                ekp.setInt(5, id);
                ekp.executeUpdate();
        } catch (SQLException e) {
        }
    }
    
    public void idopont_id_csokkentes(int id) {
        String s = "UPDATE idopontok SET id = ?  WHERE id = ?;";
        int ujId = id;
        id = id + 1;
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setInt(1, ujId);
            ekp.setInt(2, id);
            ekp.executeUpdate();
        } catch (SQLException e) {
        }
    }
    
    public void idopont_torol(int id) {
        String s = "DELETE FROM idopontok WHERE id =?;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setInt(1, id);
            ekp.executeUpdate();
        }  catch (SQLException e) {
        }
    }
    
    
    public void vendeg_modosit(String vendeg, String info, String tel, int id) {
        String s = "UPDATE vendegek SET vendeg = ?, info = ?, tel = ?  WHERE id = ?;";
        
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)) {
                ekp.setString(1, vendeg);
                ekp.setString(2, info);
                ekp.setString(3, tel);
                ekp.setInt(4, id);
                ekp.executeUpdate();
        } catch (SQLException e) {
        }
    }
        
    public void vendeg_id_modosit(int id) {
        String s = "UPDATE vendegek SET id = ?  WHERE id = ?;";
        int ujId = id;
        id = id + 1;
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setInt(1, ujId);
            ekp.setInt(2, id);
            ekp.executeUpdate();
        } catch (SQLException e) {
        }
    }
    
    public void vendeg_torol(int id) {
        String s = "DELETE FROM vendegek WHERE id =?;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
            PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setInt(1, id);
            ekp.executeUpdate();
        }  catch (SQLException e) {
        }
    }
    
//Lekérdezés az adatbázisból--------------------------------------------------------
    
    public void idopont_be(ObservableList<Idopont> tabla) {
        String s = "SELECT * FROM idopontok ORDER BY id;";

        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
             PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ResultSet eredmeny = ekp.executeQuery();
            tabla.clear();
            
            
            while (eredmeny.next()) {
                tabla.add(new Idopont(eredmeny.getString("vendeg"), 
                        eredmeny.getString("honap"), 
                        eredmeny.getInt("nap"), 
                        eredmeny.getInt("ora"), 
                        eredmeny.getInt("id")));
            }
        } catch (SQLException ex) {
        }
    }  
    
    public void idopont_kereses_ho(ObservableList<Idopont> tabla, String ho) {
        String s = "SELECT * FROM idopontok WHERE honap = ? ORDER BY id;";

        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
             PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ekp.setString(1, ho);
            ResultSet eredmeny = ekp.executeQuery();
            tabla.clear();
            
            
            while (eredmeny.next()) {
                tabla.add(new Idopont(eredmeny.getString("vendeg"), 
                        eredmeny.getString("honap"), 
                        eredmeny.getInt("nap"), 
                        eredmeny.getInt("ora"), 
                        eredmeny.getInt("id")));
            }
        } catch (SQLException ex) {
        }
    } 
    
    public int idopont_utolso_id() {
        int x = 0;
        String s = "SELECT * FROM idopontok ORDER BY id DESC LIMIT 1;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
             PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ResultSet eredmeny = ekp.executeQuery();
            while (eredmeny.next()) {
                
                Idopont i = new Idopont(eredmeny.getString("vendeg"), 
                        eredmeny.getString("honap"), 
                        eredmeny.getInt("nap"), 
                        eredmeny.getInt("ora"), 
                        eredmeny.getInt("id"));
              x = i.getId();
            }
            
        } catch (SQLException ex) {
        }
        
        return x;
    }
    
    public int idopont_id() {
        int x = 0;
        String s = "SELECT COUNT(*) FROM idopontok;";
        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
             PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ResultSet eredmeny = ekp.executeQuery();
            while (eredmeny.next()) {
                x = eredmeny.getInt("COUNT(*)");
            }
            
        } catch (SQLException ex) {
        }
        
        return x;
    }
    
    public void vendeg_be(ObservableList<Vendeg> tabla) {
        String s = "SELECT * FROM vendegek ORDER BY id;";

        try (Connection kapcs = DriverManager.getConnection(dbUrl2, user, pass);
             PreparedStatement ekp = kapcs.prepareStatement(s)) {
            ResultSet eredmeny = ekp.executeQuery();
            tabla.clear();
            
            while (eredmeny.next()) {
                tabla.add(new Vendeg(eredmeny.getString("vendeg"), 
                        eredmeny.getString("info"), 
                        eredmeny.getString("tel"), 
                        eredmeny.getInt("id")));
            }
        } catch (SQLException ex) {
        }
    }  
}
