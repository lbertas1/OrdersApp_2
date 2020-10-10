package bartos.lukasz.ui.data;

import bartos.lukasz.persistence.model.Customer;
import bartos.lukasz.persistence.model.Order;
import bartos.lukasz.persistence.model.Product;
import bartos.lukasz.persistence.model.enums.Category;
import bartos.lukasz.ui.exception.UserDataException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public final class UserData {

    private final static Scanner sc = new Scanner(System.in);

    public static String getString(String message) {
        System.out.println(message);
        return sc.nextLine();
    }

    public static int getInt(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an integer value");
        }
        return Integer.parseInt(line);
    }

    public static Long getLong(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an long value");
        }
        return Long.parseLong(line);
    }

    public static LocalDate getDate(String message) {
        System.out.println(message);
        var date = sc.nextLine();
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) throw new UserDataException("Invalid format");
        return LocalDate.parse(date);
    }

    public static double getDouble(String message) {
        System.out.println(message);
        var line = sc.nextLine();
        if (!line.matches("\\d+")) {
            throw new UserDataException("not an double value");
        }
        return Double.parseDouble(line);
    }

    public static boolean getBoolean(String message) {
        System.out.println(message + " Press 'y' to say yes");
        var line = sc.nextLine();
        return line.equalsIgnoreCase("y");
    }

    public static Category getCategory() {
        System.out.println("Choose category of product");
        var counter = new AtomicInteger(0);
        Arrays
                .stream(Category.values())
                .forEach(category -> System.out.println(counter.incrementAndGet() + ". " + category));
        int choice;
        do {
            choice = getInt("Choose category [1-" + Category.values().length + "]:");
        } while (choice < 1 || choice > Category.values().length);
        return Category.values()[choice - 1];
    }

    public static Customer newCustomer() {
        return Customer.builder()
                .name(getString("Enter customer name"))
                .surname(getString("Enter customer surname"))
                .age(getInt("Enter customer age"))
                .email(getString("Enter customer email"))
                .build();
    }

    public static Product newProduct() {
        return Product.builder()
                .name(getString("Enter product name"))
                .price(getDouble("Enter product price"))
                .category(getCategory())
                .build();
    }

    public static Order newOrder(Product product, Customer customer) {
        return Order.builder()
                .customer(customer)
                .product(product)
                .orderDate(getDate("Provide date in format: yyyy-MM-dd"))
                .quantity(getInt("Enter quantity"))
                .build();
    }

    public static void close() {
        if (sc != null) {
            sc.close();
        }
    }
}
