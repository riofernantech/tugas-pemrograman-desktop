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

    Integer total = 0;

    void addMenu(Menu item) {
        this.menu.add(item);
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

    Menu getMenuByName(String name) {
        for (Menu item : menu) {
            if (item.nama.equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    Boolean deleteByIndex(int index) {
        if (index >= 0 && index < menu.size()) {
            menu.remove(index);
            return true;
        }
        return false;
    }

    Boolean deleteByName(String name) {
        Menu itemToRemove = getMenuByName(name);
        if (itemToRemove != null) {
            menu.remove(itemToRemove);
            return true;
        }
        return false;
    }
}

class Customer {
    private RestaurantData data;
    protected Scanner input = new Scanner(System.in);

    Customer(RestaurantData data) {
        this.data = data;
    }

    void pilihMenu(Integer num){
        System.out.print("\nPilih menu ke " + num + ": ");

        try {
            String pilihan = this.input.nextLine();

            if(pilihan.isEmpty()){
                throw new Exception("Input tidak boleh kosong");
            }
            
            System.out.print("Jumlah: ");
            // int jumlah = this.input.nextInt();

            // Menu menuDipilih = data.menu[Integer.parseInt(pilihan) - 1];

            // data.daftarPesanan[num - 1] = new Pesanan(menuDipilih, jumlah);

            // System.out.println("Anda memesan " + menuDipilih.nama + " sebanyak " + jumlah);

            // this.input.nextLine(); 
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
        for (Menu item : data.menu) {
            if (item.kategori.equals("minuman")) {
                System.out.println(item.nama + " - Rp " + item.harga);
            }
        }

        try {
            System.out.print("Pilih: ");
            String pilihan = this.input.nextLine();

            if(pilihan.isEmpty()){
                throw new Exception("Input tidak boleh kosong");
            }
            
            // Menu menuDipilih = data.menu[Integer.parseInt(pilihan) + 3];

            // data.daftarBonus[0] = new Pesanan(menuDipilih, 1);

            // System.out.println("Anda memilih " + menuDipilih.nama + " sebagai bonus minuman.");

            this.input.nextLine(); 
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine(); 
            this.pilihBonus();
            return;
        }
    }

    Boolean isLanjut(){
        System.out.print("Apakah Anda ingin memesan lagi? (y/n): ");
        String lanjut = this.input.nextLine();

        if(lanjut.equalsIgnoreCase("y")){
            return true;
        } else if(lanjut.equalsIgnoreCase("n")){
            return false;
        } 
        return false;
    }
}

class Admin{
    private RestaurantData data;
    protected Scanner input = new Scanner(System.in);

    Admin(RestaurantData data) {
        this.data = data;
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

    void tampilkanMenu(){
        System.out.println("=== MENU MAKANAN ===");
        Menu[] makanan = data.getMakanan();
        for (Menu item : makanan) {
                System.out.println(item.nama + " - Rp " + item.harga);
        }

        System.out.println("\n=== MENU MINUMAN ===");
        Menu[] minuman = data.getMinuman();
        for (Menu item : minuman) {
            System.out.println(item.nama + " - Rp " + item.harga);
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
            System.out.print("Masukkan nama menu yang akan dihapus: ");
            String nama = this.input.nextLine();

            if (data.deleteByName(nama)) {
                System.out.println("Menu berhasil dihapus.");
            } else {
                throw new Exception("Nama menu tidak valid.");
            }

            this.input.nextLine(); 
        } catch (Exception e) {
            System.out.println("Input tidak valid. Silakan coba lagi.");
            this.input.nextLine(); 
            this.hapusMenu();
            return;
        }
    }

}

class App {
    private RestaurantData data;
    private Customer customer;
    private Admin admin;

    public App() {
        this.data = new RestaurantData(); 
        this.admin = new Admin(this.data);
        this.customer = new Customer(this.data);
    }

    void start(){
        admin.isiMenu();
        admin.tampilkanMenu();

        admin.tambahMenu();
        admin.tampilkanMenu();

        admin.hapusMenu();
        admin.tampilkanMenu();

        
        customer.input.close();
        admin.input.close();
        return;
    }

    void hitungTotal() {
        for (Pesanan pesanan : data.daftarPesanan) {
            if (pesanan != null) {
                data.total += pesanan.getTotalHarga();
            }
        }
    }

    void cetakStruk(){
        this.hitungTotal();

        int totalSebelumPajak = data.total;
        int pajak = data.total * 10 / 100;
        Integer pelayanan = 20000;

        int diskon = 0;
        if (data.total > 100000) {
            diskon = data.total * 10 / 100;
            data.total -= diskon;
        }

        if (totalSebelumPajak > 50000) {
            customer.pilihBonus();
        }

        int totalAkhir = data.total + pajak + pelayanan;

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
