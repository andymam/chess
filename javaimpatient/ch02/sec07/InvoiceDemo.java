package ch02.sec07;

public class InvoiceDemo {
    public static void main(String[] args) {
        var invoice = new Invoice();
        invoice.addItem("Blackwell Toaster", 2, 24.95);
        invoice.addItem("ZapXpress Microwave Oven", 1, 49.95);
        invoice.print();
    }
}
