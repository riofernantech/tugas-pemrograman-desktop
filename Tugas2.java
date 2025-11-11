import java.util.Scanner;
import java.util.ArrayList;

class Menu {
    String nama;
    int harga;
    String kategori;

    Menu(String nama, int harga, String kategori) {
        this.nama = nama;
        this.harga = harga;
        this.kategori = kategori;
    }
}

class Pesanan {
    Menu menu;
    int jumlah;

    Pesanan(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
    }

    int getTotalHarga() {
        return this.menu.harga * this.jumlah;
    }   
}

class RestaurantData {
    ArrayList<Menu> menu = new ArrayList<>();
    
    ArrayList<Pesanan> daftarPesanan = new ArrayList<>();
    Pesanan[] daftarBonus = new Pesanan[1];

    int getTotal() {
        int total = 0;
        for (Pesanan pesanan : daftarPesanan) {
            if (pesanan != null) {
                total += pesanan.getTotalHarga();
            }
        }
        return total;
    }

    void addMenu(Menu newItem) {
        if (menu.isEmpty()) {
            menu.add(newItem);
            return;
        }

        if (newItem.kategori.equalsIgnoreCase("makanan")) {
            int insertIndex = 0;
            for (int i = 0; i < menu.size(); i++) {
                if (menu.get(i).kategori.equalsIgnoreCase("minuman")) {
                    insertIndex = i;
                    break;
                }
                insertIndex = i + 1;
            }
            menu.add(insertIndex, newItem);
        } 

        else {
            menu.add(newItem);
        }
    }

    void addPesanan(Pesanan newOrder) {
        if (daftarPesanan.isEmpty()) {
            daftarPesanan.add(newOrder);
            return;
        }

        for (int i = 0; i < daftarPesanan.size(); i++) {
            if (daftarPesanan.get(i).menu.equals(newOrder.menu)) {
                daftarPesanan.get(i).jumlah += newOrder.jumlah;
                return;
            }
        }
        
        daftarPesanan.add(newOrder);
    }

    Menu[] getAllMenus() {
        return menu.toArray(new Menu[0]);
    }

    Menu[] getMakanan() {
        return menu.stream()
            .filter(item -> item.kategori.equals("makanan"))
            .toArray(Menu[]::new);
    }

    Menu[] getMinuman() {
        return menu.stream()
            .filter(item -> item.kategori.equals("minuman"))
            .toArray(Menu[]::new);
    }

    Menu getMenuByIndex(int index) {
        if (index >= 0 && index < menu.size()) {
            return menu.get(index);
        }
        return null;
    }

    Boolean editMenu(int index, Menu newItem) {
        if (index >= 0 && index < menu.size()) {
            menu.set(index, newItem);
            return true;
        }
        return false;
    }

    Boolean deleteByIndex(int index) {
        if (index >= 0 && index < menu.size()) {
            menu.remove(index);
            return true;
        }
        return false;
    }
}

class App {
    private RestaurantData data;
    protected Scanner input = new Scanner(System.in);

    public App() {
        this.data = new RestaurantData();
    }

    void start(){
        isiMenu();
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

        cetakStruk();
    }

    void isiMenu(){
        data.addMenu(new Menu("Nasi Goreng", 15000, "makanan"));
        data.addMenu(new Menu("Mie Ayam", 12000, "makanan"));
        data.addMenu(new Menu("Sate Ayam", 20000, "makanan"));
        data.addMenu(new Menu("Gado-Gado", 10000, "makanan"));

        data.addMenu(new Menu("Es Teh", 5000, "minuman"));
        data.addMenu(new Menu("Es Jeruk", 7000, "minuman"));
        data.addMenu(new Menu("Kopi Hitam", 8000, "minuman"));
        data.addMenu(new Menu("Jus Alpukat", 15000, "minuman"));
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

            data.addMenu(new Menu(nama, harga, kategori));
            System.out.println("Menu baru berhasil ditambahkan.");
          
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

            Menu dipilih = data.getMenuByIndex(index - 1);
            if (dipilih == null) throw new Exception(); 
            this.input.nextLine();

            if(confirm("Apakah Anda yakin ingin menghapus menu " + dipilih.nama + "?")) {
                data.deleteByIndex(index - 1);
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

            Menu dipilih = data.getMenuByIndex(index - 1);
            if (dipilih == null) throw new Exception("Nomor menu tidak valid.");
            this.input.nextLine(); 
            
            System.out.println("Menu dipilih : " + dipilih.nama);
            System.out.print("Masukkan nama baru: ");
            String namaBaru = this.input.nextLine();

            System.out.print("Masukkan harga baru: ");
            int hargaBaru = this.input.nextInt();
            this.input.nextLine(); 

            if(confirm("Apakah Anda yakin ingin mengubah menu " + dipilih.nama + "?")) {
                data.editMenu(index - 1, new Menu(namaBaru, hargaBaru, dipilih.kategori));
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

            Menu menuDipilih = data.getMenuByIndex(index - 1);
            if (menuDipilih == null) throw new Exception();
            
            System.out.print("Jumlah: ");
            int jumlah = this.input.nextInt();

            data.addPesanan(new Pesanan(menuDipilih, jumlah));
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
        Menu[] menu = data.getAllMenus();
        for (int i = 0; i < menu.length; i++) {
            if (menu[i].kategori.equals("minuman")) {
                System.out.println(i + 1 + " " + menu[i].nama + " - Rp " + menu[i].harga);
            }
        }

        try {
            System.out.print("Pilih: ");
            int index = this.input.nextInt();
            
            Menu menuDipilih = data.getMenuByIndex(index - 1);
            if (menuDipilih.kategori.equals("makanan")) throw new Exception();

            data.daftarBonus[0] = new Pesanan(menuDipilih, 1);
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
        Menu[] makanan = data.getMakanan();
        for (Menu item : makanan) {
            System.out.println(index + " " + item.nama + " - Rp " + item.harga);
            index++;
        }
        
        System.out.println("\n=== MENU MINUMAN ===");
        Menu[] minuman = data.getMinuman();
        for (Menu item : minuman) {
            System.out.println(index + " " + item.nama + " - Rp " + item.harga);
            index++;
        }
    }

    void cetakStruk(){
        int total = data.getTotal();

        int totalSebelumPajak = total;
        int pajak = total * 10 / 100;
        Integer pelayanan = 20000;

        int diskon = 0;
        if (total > 100000) {
            diskon = total * 10 / 100;
            total -= diskon;
        }

        if (totalSebelumPajak > 50000) {
            pilihBonus();
        }

        int totalAkhir = total + pajak + pelayanan;

        System.out.println("\n===== DAFTAR PESANAN =====");
        System.out.printf("%-25s %8s %15s%n", "Nama Item", "Jumlah", "Subtotal");
        System.out.println("---------------------------------------------------------");

        for (Pesanan pesanan : data.daftarPesanan) {
            if (pesanan != null) {
                System.out.printf(
                    "%-25s %8d %15s%n", 
                    pesanan.menu.nama, 
                    pesanan.jumlah, 
                    "Rp " + pesanan.getTotalHarga()
                );
            }
        }
        
        System.out.println("\n===== STRUK PEMBAYARAN =====");
        System.out.println("Total Harga Pesanan : Rp " + totalSebelumPajak);
        if (diskon > 0) System.out.println("Diskon 10% : - Rp " + diskon);
        System.out.println("Pajak 10% : Rp " + pajak);
        System.out.println("Biaya Pelayanan : Rp " + pelayanan);
        System.out.println("----------------------------");
        System.out.println("Total Bayar : Rp " + totalAkhir);

        if (data.daftarBonus[0] != null)
            System.out.println("\n* BONUS: Anda mendapatkan gratis 1 " + data.daftarBonus[0].menu.nama + "!");

        System.out.println("==============================");
    }

}

public class Tugas2 {
    public static void main(String[] args) {
        App app = new App();

        app.start();
    }
}
