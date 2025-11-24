import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;

abstract class MenuItem {
    String nama;
    double harga;
    String kategori;

    MenuItem(String nama, double harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }

    abstract void tampilMenu();
}

class PesananItem {
    MenuItem menu;
    int jumlah;

    PesananItem(MenuItem menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
    }

    double getTotalHarga() {
        return this.menu.harga * this.jumlah;
    }
}

class Makanan extends MenuItem {
    static final String jenis = "makanan";

    Makanan(String nama, double harga) {
        super(nama, harga, jenis);
    }

    @Override
    void tampilMenu() {
        System.out.println(nama + " - Rp " + (int)harga);
    }
}

class Minuman extends MenuItem {
    static final String jenis = "minuman";

    Minuman(String nama, double harga) {
        super(nama, harga, jenis);
    }

    @Override
    void tampilMenu() {
        System.out.println(nama + " - Rp " + (int)harga);
    }
}

class Diskon extends MenuItem {
    static final double diskon = 10.0;

    Diskon(String nama) {
        super(nama, diskon, "diskon");
    }

    @Override
    void tampilMenu() {
        System.out.println(nama + " - Diskon " + diskon + "%");
    }
}

class Menu { 
    ArrayList<MenuItem> items = new ArrayList<>();

    void addItem(MenuItem item) {
        if (items.isEmpty()) {
            items.add(item);
            return;
        }

        if (item.kategori.equalsIgnoreCase("makanan")) {
            int insertIndex = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).kategori.equalsIgnoreCase("minuman")) {
                    insertIndex = i;
                    break;
                }
                insertIndex = i + 1;
            }
            items.add(insertIndex, item);
        } else {
            items.add(item);
        }
    }

    MenuItem[] getAll() {
        return items.toArray(new MenuItem[0]);
    }

    MenuItem[] getMakanan() {
        return items.stream()
            .filter(it -> it.kategori.equals("makanan"))
            .toArray(MenuItem[]::new);
    }

    MenuItem[] getMinuman() {
        return items.stream()
            .filter(it -> it.kategori.equals("minuman"))
            .toArray(MenuItem[]::new);
    }

    MenuItem getByIndex(int index) {
        if (index >= 0 && index < items.size()) return items.get(index);
        return null;
    }

    boolean edit(int index, MenuItem newItem) {
        if (index >= 0 && index < items.size()) {
            items.set(index, newItem);
            return true;
        }
        return false;
    }

    boolean delete(int index) {
        if (index >= 0 && index < items.size()) {
            items.remove(index);
            return true;
        }
        return false;
    }
}

class Pesanan {
    ArrayList<PesananItem> items = new ArrayList<>();

    void addPesanan(PesananItem newOrder) {
        if (items.isEmpty()) {
            items.add(newOrder);
            return;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).menu.equals(newOrder.menu)) {
                items.get(i).jumlah += newOrder.jumlah;
                return;
            }
        }
        items.add(newOrder);
    }

    double getTotal() {
        double total = 0;
        for (PesananItem p : items) {
            if (p != null) total += p.getTotalHarga();
        }
        return total;
    }

    PesananItem[] getAll() {
        return items.toArray(new PesananItem[0]);
    }
}

class App {
    Menu menu;
    Pesanan pesanan;
    PesananItem[] daftarBonus = new PesananItem[1];

    Diskon diskon = new Diskon("Diskon 10%");
    
    protected Scanner input = new Scanner(System.in);

    public App() {
        this.menu = new Menu();
        this.pesanan = new Pesanan();
    }

    void start(){
        loadMenu();
        homePage();

        input.close();
        return;
    }

    void homePage(){
        System.out.println("\n=== SELAMAT DATANG DI RESTAURANT SUKA SUKA SAYA ===");
        System.out.println("1. Admin");
        System.out.println("2. Customer");
        System.out.print("Pilih menu: ");

        try {
            int pilihan = this.input.nextInt();
            this.input.nextLine();

            switch (pilihan) {
                case 1:
                    adminPage();
                    break;
                case 2:
                    customerPage();
                    break;
                default:
                    throw new Exception("Pilihan tidak valid.");
            }
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.homePage();
            return;
        }
    }

    void adminPage(){
        System.out.println("\n=== HALAMAN ADMIN ===");
        tampilkanMenu();
        System.out.println("\n=== MANAGEMENT ===");
        System.out.println("1. Tambah Menu");
        System.out.println("2. Hapus Menu");
        System.out.println("3. Edit Menu");
        System.out.println("4. Kembali ke Halaman Utama");
        System.out.print("Pilih menu: ");

        try {
            int pilihan = this.input.nextInt();
            this.input.nextLine();

            switch (pilihan) {
                case 1:
                    tambahMenu();
                    adminPage();
                    break;
                case 2:
                    hapusMenu();
                    adminPage();
                    break;
                case 3:
                    editMenu();
                    adminPage();
                    break;
                case 4:
                    homePage();
                    return;
                default:
                    throw new Exception("Pilihan tidak valid.");
            }
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.adminPage();
            return;
        }
    }

    void customerPage(){
        System.out.println("\n=== HALAMAN CUSTOMER ===");
        tampilkanMenu();

        Boolean lanjut = true;
        int num = 1;

        while(lanjut){
            pilihMenu(num);
            num++;
            lanjut = confirm("Apakah Anda ingin memesan lagi?");
        }

        generateStruk();
    }

    void loadMenu(){
        String fileName = "menu.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length != 3) {
                    System.err.println("Format salah: " + line);
                    continue;
                }

                String nama = parts[0].trim();
                String hargaStr = parts[1].trim();
                String kategori = parts[2].trim().toLowerCase();

                int harga = Integer.parseInt(hargaStr);

                if (kategori.equals("makanan")) {
                    menu.addItem(new Makanan(nama, harga));
                } else if (kategori.equals("minuman")) {
                    menu.addItem(new Minuman(nama, harga));
                } else {
                    System.err.println("Kategori tidak dikenal: " + kategori);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    void storeMenu() {
        String fileName = "menu.txt";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            for (MenuItem item : this.menu.getAll()) {
                String line = item.nama + ", " + (int)item.harga + ", " + item.kategori;
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    void tambahMenu(){
        try {
            System.out.print("Masukkan nama menu baru: ");
            String nama = this.input.nextLine();
            if(nama.isEmpty()) throw new Exception("Input tidak boleh kosong");

            System.out.print("Masukkan harga menu baru: ");
            int harga = this.input.nextInt();
            this.input.nextLine();
            if(harga <= 0) throw new Exception("Harga harus lebih dari 0");

            System.out.print("Masukkan kategori menu baru (makanan/minuman): ");
            String kategori = this.input.nextLine();
            if(!kategori.equals("makanan") && !kategori.equals("minuman")) {
                throw new Exception("Kategori harus 'makanan' atau 'minuman'");
            }

            if (kategori.equals("makanan")) {
                menu.addItem(new Makanan(nama, harga));
            } else {
                menu.addItem(new Minuman(nama, harga));
            }

            System.out.println("Menu baru berhasil ditambahkan.");

            storeMenu();

            this.input.nextLine();
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.tambahMenu();
            return;
        }
    }

    void hapusMenu(){
        try {
            System.out.print("Masukkan nomor menu yang akan dihapus: ");
            int index = this.input.nextInt();

            MenuItem dipilih = menu.getByIndex(index - 1);
            if (dipilih == null) throw new Exception();
            this.input.nextLine();

            if(confirm("Apakah Anda yakin ingin menghapus menu " + dipilih.nama + "?")) {
                menu.delete(index - 1);
                System.out.println("Menu berhasil dihapus.");
            } else {
                System.out.println("Penghapusan dibatalkan.");
            }

            this.input.nextLine();
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.hapusMenu();
            return;
        }
    }

    void editMenu(){
        try {
            System.out.print("Masukkan nomor menu yang akan diubah: ");
            int index = this.input.nextInt();

            MenuItem dipilih = menu.getByIndex(index - 1);
            if (dipilih == null) throw new Exception("Nomor menu tidak valid.");
            this.input.nextLine();

            System.out.println("Menu dipilih : " + dipilih.nama);
            System.out.print("Masukkan nama baru: ");
            String namaBaru = this.input.nextLine();

            System.out.print("Masukkan harga baru: ");
            int hargaBaru = this.input.nextInt();
            this.input.nextLine();

            if(confirm("Apakah Anda yakin ingin mengubah menu " + dipilih.nama + "?")) {
                MenuItem baru;
                if (dipilih instanceof Makanan) {
                    baru = new Makanan(namaBaru, hargaBaru);
                } else {
                    baru = new Minuman(namaBaru, hargaBaru);
                }
                menu.edit(index - 1, baru);
                System.out.println("Menu berhasil diubah.");
            } else {
                System.out.println("Pengubahan dibatalkan.");
            }

            this.input.nextLine();
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.editMenu();
            return;
        }
    }

    void pilihMenu(int num){
        System.out.print("\nPilih menu ke " + num + ": ");

        try {
            int index = this.input.nextInt();

            MenuItem menuDipilih = menu.getByIndex(index - 1);
            if (menuDipilih == null) throw new Exception();

            System.out.print("Jumlah: ");
            int jumlah = this.input.nextInt();

            pesanan.addPesanan(new PesananItem(menuDipilih, jumlah));
            System.out.println("Anda memesan " + menuDipilih.nama + " sebanyak " + jumlah);

            this.input.nextLine();
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.pilihMenu(num);
            return;
        }
    }

    void pilihBonus(){
        System.out.print("\nKamu mendapatkan bonus minuman! Silakan pilih minuman ");
        System.out.println("\n=== MENU MINUMAN ===");
        MenuItem[] menus = this.menu.getAll();
        for (int i = 0; i < menus.length; i++) {
            if (menus[i].kategori.equals("minuman")) {
                System.out.println(i + 1 + " " + menus[i].nama + " - Rp " + (int)menus[i].harga);
            }
        }

        try {
            System.out.print("Pilih: ");
            int index = this.input.nextInt();

            MenuItem menuDipilih = this.menu.getByIndex(index - 1);
            if (!menuDipilih.kategori.equals("minuman")) throw new Exception();

            daftarBonus[0] = new PesananItem(menuDipilih, 1);
            System.out.println("Anda memilih " + menuDipilih.nama + " sebagai bonus minuman.");

            this.input.nextLine();
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine();
            this.pilihBonus();
            return;
        }
    }

    Boolean confirm(String message){
        System.out.print(message + " (ya/tidak): ");
        String lanjut = this.input.nextLine();

        if(lanjut.equalsIgnoreCase("ya")){
            return true;
        } else if(lanjut.equalsIgnoreCase("tidak")){
            return false;
        } else {
            return confirm(message);
        }
    }

    void tampilkanMenu(){
        System.out.println("=== MENU MAKANAN ===");
        int index = 1;
        MenuItem[] makanan = menu.getMakanan();
        for (MenuItem item : makanan) {
            System.out.print(index + " ");
            item.tampilMenu();
            index++;
        }
        
        System.out.println("\n=== MENU MINUMAN ===");
        MenuItem[] minuman = menu.getMinuman();
        for (MenuItem item : minuman) {
            System.out.print(index + " ");
            item.tampilMenu();
            index++;
        }
    }

    void generateStruk(){
        double total = pesanan.getTotal();

        double totalSebelumPajak = total;
        int pajak = (int)(total * 10 / 100);
        int pelayanan = 20000;

        double diskon = 0;
        if (total > 100000) {
            diskon = total * this.diskon.harga / 100;
            total -= diskon;
        }

        if (totalSebelumPajak > 50000) pilihBonus();
        
        double totalAkhir = total + pajak + pelayanan;

        StringBuilder sb = new StringBuilder();

        sb.append("\n===== DAFTAR PESANAN =====\n");
        sb.append(String.format("%-25s %8s %15s%n", "Nama Item", "Jumlah", "Subtotal"));
        sb.append("---------------------------------------------------------\n");

        for (PesananItem pes : pesanan.getAll()) {
            if (pes != null) {
                String line = String.format(
                        "%-25s %8d %15s%n",
                        pes.menu.nama,
                        pes.jumlah,
                        "Rp " + (int) pes.getTotalHarga()
                );
                sb.append(line);
            }
        }

        sb.append("\n===== STRUK PEMBAYARAN =====\n");
        sb.append("Total Harga Pesanan : Rp ").append((int)totalSebelumPajak).append("\n");
        if (diskon > 0) sb.append("Diskon 10% : - Rp ").append((int)diskon).append("\n");
        sb.append("Pajak 10% : Rp ").append(pajak).append("\n");
        sb.append("Biaya Pelayanan : Rp ").append(pelayanan).append("\n");
        sb.append("----------------------------\n");
        sb.append("Total Bayar : Rp ").append((int)totalAkhir).append("\n");

        if (daftarBonus[0] != null) {
            sb.append("\n* BONUS: Anda mendapatkan gratis 1 " + daftarBonus[0].menu.nama + "!\n");
        }

        sb.append("==============================\n");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("struk.txt"))) {
            bw.write(sb.toString());
        } catch (IOException e) {
            System.err.println("Gagal menyimpan struk: " + e.getMessage());
        }
    }

}

public class Tugas3 {
    public static void main(String[] args) {
        App app = new App();

        app.start();
    }
}
