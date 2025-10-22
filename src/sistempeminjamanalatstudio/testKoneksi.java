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
public class testKoneksi {
    public static void main(String[] args) {
        System.out.println("Mencoba membuat koneksi...");
        
        Koneksi tesKoneksi = new Koneksi();
        
        tesKoneksi.bukaKoneksi();
        
        if (tesKoneksi.dbKoneksi != null) {
            System.out.println("Pengecekan di main: Objek koneksi berhasil dibuat (tidak null).");
            
            tesKoneksi.tutupKoneksi();
        } else {
            System.err.println("Pengecekan di main: Objek koneksi gagal dibuat (masih null).");
        }
    }
}
