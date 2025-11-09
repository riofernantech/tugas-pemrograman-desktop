import java.util.Scanner;

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
    Menu[] menu = new Menu[8];

    Pesanan[] daftarPesanan = new Pesanan[4]; 
    Pesanan[] daftarBonus = new Pesanan[1];

    Integer total = 0;
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
            int jumlah = this.input.nextInt();

            Menu menuDipilih = data.menu[Integer.parseInt(pilihan) - 1];

            data.daftarPesanan[num - 1] = new Pesanan(menuDipilih, jumlah);

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
        System.out.println("1. " + data.menu[4].nama + " - Rp " + data.menu[4].harga);
        System.out.println("2. " + data.menu[5].nama + " - Rp " + data.menu[5].harga);
        System.out.println("3. " + data.menu[6].nama + " - Rp " + data.menu[6].harga);
        System.out.println("4. " + data.menu[7].nama + " - Rp " + data.menu[7].harga);

        try {
            System.out.print("Pilih: ");
            String pilihan = this.input.nextLine();

            if(pilihan.isEmpty()){
                throw new Exception("Input tidak boleh kosong");
            }
            
            Menu menuDipilih = data.menu[Integer.parseInt(pilihan) + 3];

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
        data.menu[0] = new Menu("Nasi Goreng", 15000, "makanan");
        data.menu[1] = new Menu("Mie Ayam", 12000, "makanan");
        data.menu[2] = new Menu("Sate Ayam", 20000, "makanan");
        data.menu[3] = new Menu("Gado-Gado", 10000, "makanan");

        data.menu[4] = new Menu("Es Teh", 5000, "minuman");
        data.menu[5] = new Menu("Es Jeruk", 7000, "minuman");
        data.menu[6] = new Menu("Kopi Hitam", 8000, "minuman");
        data.menu[7] = new Menu("Jus Alpukat", 15000, "minuman");
    }

    void tampilkanMenu(){
        System.out.println("=== MENU MAKANAN ===");
        System.out.println("1. " + data.menu[0].nama + " - Rp " + data.menu[0].harga);
        System.out.println("2. " + data.menu[1].nama + " - Rp " + data.menu[1].harga);
        System.out.println("3. " + data.menu[2].nama + " - Rp " + data.menu[2].harga);
        System.out.println("4. " + data.menu[3].nama + " - Rp " + data.menu[3].harga);

        System.out.println("\n=== MENU MINUMAN ===");
        System.out.println("5. " + data.menu[4].nama + " - Rp " + data.menu[4].harga);
        System.out.println("6. " + data.menu[5].nama + " - Rp " + data.menu[5].harga);
        System.out.println("7. " + data.menu[6].nama + " - Rp " + data.menu[6].harga);
        System.out.println("8. " + data.menu[7].nama + " - Rp " + data.menu[7].harga);
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

        customer.pilihMenu(1);
        if (!customer.isLanjut()) {
            cetakStruk();
            return;
        }
        
        customer.pilihMenu(2);
        if (!customer.isLanjut()) {
            cetakStruk();
            return;
        }
        
        customer.pilihMenu(3);
        if (!customer.isLanjut()) {
            cetakStruk();
            return;
        }

        customer.pilihMenu(4);
        cetakStruk();

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
