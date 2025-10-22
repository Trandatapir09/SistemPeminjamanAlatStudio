/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistempeminjamanalatstudio;

/**
 *
 * @author Chris
 */
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;

public class Alat {

    private final StringProperty idAlat;
    private final StringProperty namaAlat;
    private final StringProperty kategori;
    private final IntegerProperty stok;
    private final DoubleProperty hargaSewa;

    public Alat(String idAlat, String namaAlat, String kategori, int stok, double hargaSewa) {
        this.idAlat = new SimpleStringProperty(idAlat);
        this.namaAlat = new SimpleStringProperty(namaAlat);
        this.kategori = new SimpleStringProperty(kategori);
        this.stok = new SimpleIntegerProperty(stok);
        this.hargaSewa = new SimpleDoubleProperty(hargaSewa);
    }

    public String getIdAlat() {
        return idAlat.get();
    }
    public StringProperty idAlatProperty() {
        return idAlat;
    }

    public String getNamaAlat() {
        return namaAlat.get();
    }
    public StringProperty namaAlatProperty() {
        return namaAlat;
    }

    public String getKategori() {
        return kategori.get();
    }
    public StringProperty kategoriProperty() {
        return kategori;
    }

    public int getStok() {
        return stok.get();
    }
    public IntegerProperty stokProperty() {
        return stok;
    }

    public double getHargaSewa() {
        return hargaSewa.get();
    }
    public DoubleProperty hargaSewaProperty() {
        return hargaSewa;
    }      
}
