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

class restaurantData {
    Menu[] menu = new Menu[8];

    Pesanan[] daftarPesanan = new Pesanan[4]; 
    Pesanan[] daftarBonus = new Pesanan[1];

    Integer total = 0;
}

class customer extends restaurantData {
    protected Scanner input = new Scanner(System.in);

    void pilihMenu(Integer num){
        System.out.print("\nPilih menu ke " + num + ": ");

        try {
            String pilihan = this.input.nextLine();

            if(pilihan.isEmpty()){
                throw new Exception("Input tidak boleh kosong");
            }
            
            System.out.print("Jumlah: ");
            int jumlah = this.input.nextInt();

            Menu menuDipilih = this.menu[Integer.parseInt(pilihan) - 1];

            this.daftarPesanan[num - 1] = new Pesanan(menuDipilih, jumlah);

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
        System.out.println("1. " + menu[4].nama + " - Rp " + menu[4].harga);
        System.out.println("2. " + menu[5].nama + " - Rp " + menu[5].harga);
        System.out.println("3. " + menu[6].nama + " - Rp " + menu[6].harga);
        System.out.println("4. " + menu[7].nama + " - Rp " + menu[7].harga);

        try {
            System.out.print("Pilih: ");
            String pilihan = this.input.nextLine();

            if(pilihan.isEmpty()){
                throw new Exception("Input tidak boleh kosong");
            }
            
            Menu menuDipilih = this.menu[Integer.parseInt(pilihan) + 3];

            this.daftarBonus[0] = new Pesanan(menuDipilih, 1);

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

class admin extends restaurantData {
    protected Scanner input = new Scanner(System.in);

    void isiMenu(){
        this.menu[0] = new Menu("Nasi Goreng", 15000, "makanan");
        this.menu[1] = new Menu("Mie Ayam", 12000, "makanan");
        this.menu[2] = new Menu("Sate Ayam", 20000, "makanan");
        this.menu[3] = new Menu("Gado-Gado", 10000, "makanan");

        this.menu[4] = new Menu("Es Teh", 5000, "minuman");
        this.menu[5] = new Menu("Es Jeruk", 7000, "minuman");
        this.menu[6] = new Menu("Kopi Hitam", 8000, "minuman");
        this.menu[7] = new Menu("Jus Alpukat", 15000, "minuman");
    }

    void tampilkanMenu(){
        System.out.println("=== MENU MAKANAN ===");
        System.out.println("1. " + menu[0].nama + " - Rp " + menu[0].harga);
        System.out.println("2. " + menu[1].nama + " - Rp " + menu[1].harga);
        System.out.println("3. " + menu[2].nama + " - Rp " + menu[2].harga);
        System.out.println("4. " + menu[3].nama + " - Rp " + menu[3].harga);

        System.out.println("\n=== MENU MINUMAN ===");
        System.out.println("5. " + menu[4].nama + " - Rp " + menu[4].harga);
        System.out.println("6. " + menu[5].nama + " - Rp " + menu[5].harga);
        System.out.println("7. " + menu[6].nama + " - Rp " + menu[6].harga);
        System.out.println("8. " + menu[7].nama + " - Rp " + menu[7].harga);
    }

}

class App extends restaurantData{
    private customer customer;
    private admin admin;

    App() {
        this.customer = new customer();
        this.admin = new admin();
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
        if (daftarPesanan[0] != null) {
            this.total += daftarPesanan[0].getTotalHarga();
        }
        
        if (daftarPesanan[1] != null) {
            this.total += daftarPesanan[1].getTotalHarga();
        }
        
        if (daftarPesanan[2] != null) {
            this.total += daftarPesanan[2].getTotalHarga();
        }
        
        if (daftarPesanan[3] != null) {
            this.total += daftarPesanan[3].getTotalHarga();
        }
    }

    void cetakStruk(){
        this.hitungTotal();

        int totalSebelumPajak = this.total;
        int pajak = this.total * 10 / 100;
        Integer pelayanan = 20000;

        int diskon = 0;
        if (this.total > 100000) {
            diskon = this.total * 10 / 100;
            this.total -= diskon;
        }

        if (totalSebelumPajak > 50000) {
            customer.pilihBonus();
        }

        int totalAkhir = this.total + pajak + pelayanan;

        System.out.println("\n===== DAFTAR PESANAN =====");
        System.out.printf("%-25s %8s %15s%n", "Nama Item", "Jumlah", "Subtotal");
        System.out.println("---------------------------------------------------------");
        if (daftarPesanan[0] != null) {
            System.out.printf(
                "%-25s %8d %15s%n", 
                daftarPesanan[0].menu.nama, 
                daftarPesanan[0].jumlah, 
                "Rp " + daftarPesanan[0].getTotalHarga()
            );
        }
        if (daftarPesanan[1] != null) {
            System.out.printf(
                "%-25s %8d %15s%n", 
                daftarPesanan[1].menu.nama, 
                daftarPesanan[1].jumlah, 
                "Rp " + daftarPesanan[1].getTotalHarga()
            );
        }
        if (daftarPesanan[2] != null) {
            System.out.printf(
                "%-25s %8d %15s%n", 
                daftarPesanan[2].menu.nama, 
                daftarPesanan[2].jumlah, 
                "Rp " + daftarPesanan[2].getTotalHarga()
            );
        }
        if (daftarPesanan[3] != null) {
            System.out.printf(
                "%-25s %8d %15s%n", 
                daftarPesanan[3].menu.nama, 
                daftarPesanan[3].jumlah, 
                "Rp " + daftarPesanan[3].getTotalHarga()
            );
        }
        
        System.out.println("\n===== STRUK PEMBAYARAN =====");
        System.out.println("Total Harga Pesanan : Rp " + totalSebelumPajak);
        if (diskon > 0) System.out.println("Diskon 10% : - Rp " + diskon);
        System.out.println("Pajak 10% : Rp " + pajak);
        System.out.println("Biaya Pelayanan : Rp " + pelayanan);
        System.out.println("----------------------------");
        System.out.println("Total Bayar : Rp " + totalAkhir);

        if (this.daftarBonus[0] != null)
            System.out.println("\n* BONUS: Anda mendapatkan gratis 1 " + this.daftarBonus[0].menu.nama + "!");

        System.out.println("==============================");
    }

}

public class Tugas2 {
    public static void main(String[] args) {
        new App();
    }
}
