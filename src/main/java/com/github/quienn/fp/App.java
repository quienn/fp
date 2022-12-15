package com.github.quienn.fp;

import java.util.Scanner;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.ArrayList;

// Este objeto representa un producto tanto en el inventario como en la venta
class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}

class Inventory {
    // El inventario consiste en un HashMap, donde un código de barras
    // (la clave, en forma de String) apunta hacía un producto.
    private HashMap<String, Product> products = new HashMap<>();

    // Agrega un producto, registrando su código de barras y un objeto de Product
    public void addProduct(String barcode, Product product) {
        products.put(barcode, product);
    }

    // Obtiene un producto a partir de su código de barras
    public Product getProduct(String barcode) {
        return products.get(barcode);
    }

    // Imprime todo el inventario disponible
    public void printInventory() {
        System.out.format("+------------------+--------------+----------+----------+%n");
        System.out.format("| Código de barras | Nombre       | Precio   | Cantidad |%n");
        System.out.format("+------------------+--------------+----------+----------+%n");

        for (String barcode : products.keySet()) {
            Product product = products.get(barcode);
            System.out.format("| %-16s | %-12s | %-8.2f | %-8d |%n", barcode, product.getName(), product.getPrice(),
                    product.getQuantity());
        }

        System.out.format("+------------------+--------------+----------+----------+%n");
    }

    // Quita todo el producto disponible del inventario
    public void removeAllProduct(String barcode) {
        products.remove(barcode);
    }

    // Elimina `quantity` cantidad de productos disponibles de un producto
    public void removeByQuantity(String barcode, int quantity) {
        Product product = products.get(barcode);
        if (product.getQuantity() == 1) {
            products.remove(barcode);
        } else {
            products.put(barcode, new Product(product.getName(), product.getPrice(), product.getQuantity() - quantity));
        }
    }
}

class Cart {
    // El carrito consiste en un HashMap, donde un código de barras
    // (la clave, en forma de String) apunta hacía un producto.
    private HashMap<String, Product> products = new HashMap<>();

    // Agrega un producto al carrito
    public void addProduct(String barcode, Product product) {
        products.put(barcode, product);
    }

    // Quita todo el producto disponible del carrito
    public void removeAllProduct(String barcode) {
        products.remove(barcode);
    }

    // Calcula el precio total del carrito
    public double getTotalPrice() {
        double total = 0;
        for (String key : products.keySet()) {
            Product product = products.get(key);
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }

    // Imprime el carrito (como una tabla)
    public void print() {
        System.out.format("+-----------------+--------+----------+%n");
        System.out.format("|          Abarratos El Inge          |%n");
        System.out.format("+-----------------+--------+----------+%n");
        System.out.format("| Producto        | Precio | Cantidad |%n");
        System.out.format("+-----------------+--------+----------+%n");
        for (String barcode : products.keySet()) {
            Product product = products.get(barcode);
            System.out.format("| %-15s | %6.2f | %8d |%n", product.getName(), product.getPrice(),
                    product.getQuantity());
        }
        System.out.format("+-----------------+--------+----------+%n");
        System.out.format("| Total: %,.2f    |%n", getTotalPrice());
        System.out.format("+-----------------+%n");
    }
}

public class App {
    final static Scanner scanner = new Scanner(System.in);
    final static Inventory inventory = new Inventory();
    final static ArrayList<Cart> sales = new ArrayList<>();

    // Limpia la pantalla en cada opción del menú
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {
        clearScreen();
        System.out.println("--------------- Punto de venta v0.2.0 -----------------");
        int opcion = 0;
        do {
            System.out.println("Menú:\n"
                    + "  1) Registrar un nuevo producto\n"
                    + "  2) Eliminar un producto\n"
                    + "  3) Mostrar productos disponibles\n"
                    + "  4) Registrar una venta\n"
                    + "  5) Listar ventas recientes\n"
                    + "  6) Salir");
            try {
                System.out.print("Selecciona una opción: ");
                opcion = scanner.nextInt();
                switch (opcion) {
                    case 1: {
                        scanner.nextLine();
                        clearScreen();
                        System.out.println("---------------  Agregar un producto  -----------------");
                        do {
                            System.out.print("Nombre del producto: ");
                            String name = scanner.nextLine();
                            System.out.print("Precio del producto: ");
                            double price = scanner.nextDouble();
                            System.out.print("Cantidad del producto: ");
                            int quantity = scanner.nextInt();
                            System.out.print("Código de barras: ");
                            String barcode = scanner.next();

                            Product product = new Product(name, price, quantity);
                            inventory.addProduct(barcode, product);

                            System.out.print("¿Desea agregar otro producto? (s/n): ");
                            scanner.nextLine();
                        } while (scanner.nextLine().equalsIgnoreCase("s"));
                        System.out.println("-------------------------------------------------------");
                        break;
                    }
                    case 2: {
                        scanner.nextLine();
                        clearScreen();
                        System.out.println("---------------  Eliminar un producto  -----------------");
                        do {
                            System.out.print("Código de barras: ");
                            String barcode = scanner.next();
                            inventory.removeAllProduct(barcode);

                            System.out.print("¿Desea eliminar otro producto? (s/n): ");
                            scanner.nextLine();
                        } while (scanner.nextLine().equalsIgnoreCase("s"));
                        System.out.println("-------------------------------------------------------");
                        break;
                    }
                    case 3: {
                        clearScreen();
                        System.out.println("--------------- Artículos Disponibles -----------------");
                        inventory.printInventory();
                        System.out.println("-------------------------------------------------------");
                        break;
                    }
                    case 4: {
                        scanner.nextLine();
                        clearScreen();
                        System.out.println("---------------    Registrar Venta    -----------------");
                        Cart cart = new Cart();
                        do {
                            System.out.print("Código de barras: ");
                            String barcode = scanner.next();
                            System.out.print("Cantidad: ");
                            int quantity = scanner.nextInt();

                            Product product = inventory.getProduct(barcode);
                            product = new Product(product.getName(), product.getPrice(), quantity);
                            cart.addProduct(barcode, product);

                            // Elimina la cantidad ingresada de la compra en el inventario
                            inventory.removeByQuantity(barcode, quantity);
                            System.out.print("¿Desea agregar otro producto? (s/n): ");
                            scanner.nextLine();
                        } while (scanner.nextLine().equalsIgnoreCase("s"));
                        sales.add(cart); // Esto agrega la compra en una lista que imprimiremos más tarde

                        cart.print(); // Imprimir la última venta agregada (o sea, la que se
                                      // acaba de
                                      // de registrar)
                        System.out.println("-------------------------------------------------------");
                        break;
                    }
                    case 5: {
                        clearScreen();
                        System.out.println("---------------    Ventas Recientes   -----------------");
                        for (Cart cart : sales) {
                            cart.print();
                            System.out.println("");
                        }
                        System.out.println("-------------------------------------------------------");
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                scanner.reset();
                scanner.nextLine();
                System.err.println(
                        "error: La opción ingresada no existe. Ingrésala de vuelta.");
                continue;
            }
        } while (opcion != 6);
        System.out.println("-------------------------------------------------------");
        scanner.close();
    }
}