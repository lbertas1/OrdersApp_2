package bartos.lukasz.service;
import bartos.lukasz.persistence.converters.JsonOrderConverter;
import bartos.lukasz.persistence.model.Customer;
import bartos.lukasz.persistence.model.Order;
import bartos.lukasz.persistence.model.Product;
import bartos.lukasz.persistence.model.enums.Category;
import bartos.lukasz.service.exception.OrderServiceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

public class DataGenerator {

    private Set<Order> orders;
    private String fileName;

    public DataGenerator(String filename) {
        this.fileName = filename;
        this.orders = createOrders();
    }

    public void saveToFile() {
        JsonOrderConverter jsonOrderConverter = new JsonOrderConverter(createFile(fileName));
        jsonOrderConverter.toJson(orders);
    }

    public Order saveOrder(Order order) {
        JsonOrderConverter jsonOrderConverter = new JsonOrderConverter(createFileIfNotExist(fileName));
        Set<Order> jsonArray = jsonOrderConverter.fromJson().orElseThrow(() -> new OrderServiceException("Error while loading data from json file"));
        jsonArray.add(order);
        jsonOrderConverter.toJson(jsonArray);
        return order;
    }

    private String createFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(fileName));
            Files.createFile(Paths.get(fileName));
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String createFileIfNotExist(String fileName){
        try {
            if (Files.notExists(Paths.get(fileName))) Files.createFile(Paths.get(fileName));
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Set<Order> createOrders() {
        Customer customer1 = Customer.builder().id(1L).name("Wojciech").surname("Brzeziak").age(54).email("wojciech@wp.pl").build();
        Customer customer2 = Customer.builder().id(2L).name("Sylwia").surname("Jajnik").age(24).email("sylwia@wp.pl").build();
        Customer customer3 = Customer.builder().id(3L).name("Magda").surname("Glowacka").age(28).email("magda@wp.pl").build();
        Customer customer4 = Customer.builder().id(4L).name("Karolina").surname("Kupczi").age(29).email("karolina@wp.pl").build();
        Customer customer5 = Customer.builder().id(5L).name("Ania").surname("Miki").age(47).email("ania@wp.pl").build();
        Customer customer6 = Customer.builder().id(6L).name("Marian").surname("Wolski").age(34).email("marian@wp.pl").build();

        Product product1 = Product.builder().name("Sobieski").price(20d).category(Category.A).build();
        Product product2 = Product.builder().name("Zubrowka").price(22d).category(Category.A).build();
        Product product3 = Product.builder().name("Finlandia").price(24d).category(Category.A).build();
        Product product4 = Product.builder().name("Persil").price(7d).category(Category.B).build();
        Product product5 = Product.builder().name("Olmo").price(29d).category(Category.C).build();
        Product product6 = Product.builder().name("Gancia").price(1d).category(Category.A).build();
        Product product7 = Product.builder().name("Kadarka").price(17d).category(Category.C).build();
        Product product8 = Product.builder().name("Krajowa").price(16d).category(Category.C).build();
        Product product9 = Product.builder().name("Gruszki").price(19d).category(Category.B).build();

        this.orders = new LinkedHashSet<>();

        orders.add(Order.builder().customer(customer1).product(product1).quantity(1).orderDate(LocalDate.now().plusDays(30)).build());
        orders.add(Order.builder().customer(customer2).product(product2).quantity(2).orderDate(LocalDate.now().plusDays(20)).build());
        orders.add(Order.builder().customer(customer3).product(product5).quantity(3).orderDate(LocalDate.now().plusDays(30)).build());
        orders.add(Order.builder().customer(customer4).product(product4).quantity(2).orderDate(LocalDate.now().plusDays(40)).build());
        orders.add(Order.builder().customer(customer5).product(product3).quantity(4).orderDate(LocalDate.now().plusDays(30)).build());
        orders.add(Order.builder().customer(customer6).product(product7).quantity(1).orderDate(LocalDate.now().plusDays(30)).build());
        orders.add(Order.builder().customer(customer1).product(product6).quantity(1).orderDate(LocalDate.now().plusDays(40)).build());
        orders.add(Order.builder().customer(customer2).product(product1).quantity(2).orderDate(LocalDate.now().plusDays(20)).build());
        orders.add(Order.builder().customer(customer2).product(product8).quantity(2).orderDate(LocalDate.now().plusDays(50)).build());
        orders.add(Order.builder().customer(customer3).product(product2).quantity(3).orderDate(LocalDate.now().plusDays(30)).build());
        orders.add(Order.builder().customer(customer3).product(product3).quantity(4).orderDate(LocalDate.now().plusDays(70)).build());
        orders.add(Order.builder().customer(customer4).product(product4).quantity(2).orderDate(LocalDate.now().plusDays(70)).build());
        orders.add(Order.builder().customer(customer4).product(product5).quantity(5).orderDate(LocalDate.now().plusDays(70)).build());
        orders.add(Order.builder().customer(customer5).product(product6).quantity(1).orderDate(LocalDate.now().plusDays(70)).build());
        orders.add(Order.builder().customer(customer6).product(product9).quantity(2).orderDate(LocalDate.now().plusDays(30)).build());
        orders.add(Order.builder().customer(customer1).product(product2).quantity(1).orderDate(LocalDate.now().plusDays(30)).build());

        return orders;
    }
}
