package bartos.lukasz.service;

import bartos.lukasz.persistence.converters.JsonOrderConverter;
import bartos.lukasz.persistence.model.Customer;
import bartos.lukasz.persistence.model.Order;
import bartos.lukasz.persistence.model.Product;
import bartos.lukasz.persistence.model.enums.Category;
import bartos.lukasz.service.exception.OrderServiceException;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

public class OrderService {
    private Set<Order> orders;

    public OrderService(String filename) {
        this.orders = init(filename);
    }

    private Set<Order> init(String filename) {
        AtomicInteger counter = new AtomicInteger(1);
        return new HashSet<>(new JsonOrderConverter(filename)
                .fromJson()
                .orElseThrow(() -> new OrderServiceException("cannot convert data from file")));
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public String orders() {
        return orders
                .stream()
                .map(Order::toString)
                .collect(Collectors.joining("\n\n"));
    }

    public double averageFromProductsBoughtInPeriod(LocalDate from, LocalDate to) {
        if (Objects.isNull(from) || Objects.isNull(to))
            throw new IllegalArgumentException("One from given argument is null");

        return orders.stream()
                .filter(order -> order.getOrderDate().isAfter(from) && order.getOrderDate().isBefore(to))
                .mapToDouble(value -> value.getProduct().getPrice())
                .average()
                .orElseThrow();
    }

    public Map<Category, Product> productWithBiggestPriceForCategory() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(
                        order -> order.getProduct().getCategory(),
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(o -> o.getProduct().getPrice())),
                                order -> order.orElseThrow().getProduct())
                ));
    }

    public Map<Customer, List<Order>> clientProductMap() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::getCustomer, toList()));
    }

    public LocalDate getDateWithHighestNumberOfOrders() {
        System.out.println("Date with the highest number of orders: ");
        return orders.stream()
                .map(Order::getOrderDate)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Comparator.comparingLong(Map.Entry::getValue))
                .limit(1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    public LocalDate getDateWithLowestNumberOfOrders() {
        return orders.stream()
                .map(Order::getOrderDate)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((o1, o2) -> Long.compare(o2.getValue(), o1.getValue()))
                .limit(1)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow();
    }

    public Customer getClientWhoPaidTheMost() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingDouble(value -> value.getProduct().getPrice() * value.getQuantity())))
                .entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }

    public double getSumPriceForEveryOrdersIncludedDiscounts() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(o -> o, Collectors.summingDouble(value -> value.getProduct().getPrice() * value.getQuantity())))
                .entrySet()
                .stream()
                .peek(orderDoubleEntry -> {
                    if (orderDoubleEntry.getKey().getCustomer().getAge() > 25) {
                        orderDoubleEntry.setValue(orderDoubleEntry.getValue() * 0.02);
                    }
                })
                .peek(orderDoubleEntry -> {
                    if (orderDoubleEntry.getKey().getCustomer().getAge() < 25)
                        orderDoubleEntry.setValue(orderDoubleEntry.getValue() * 0.03);
                })
                .mapToDouble(Map.Entry::getValue)
                .sum();
    }

    public long quantityOfClients(int quantity) {
        if (quantity < 0) throw new IllegalArgumentException("Given quantity is smaller than 0");

        return orders
                .stream()
                .filter(order -> order.getQuantity() >= quantity)
                .map(Order::getCustomer)
                .distinct()
                .count();
    }

    public Category categoryFromProductMostOftenBought() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(order -> order.getProduct().getCategory(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .orElseThrow()
                .getKey();
    }

    public Map<Month, Long> monthQuantityOfProductMap() {
        return orders
                .stream()
                .sorted(Comparator.comparingInt(Order::getQuantity))
                .collect(Collectors.groupingBy(order -> order.getOrderDate().getMonth(), Collectors.counting()));
    }

    public Map<Month, Category> monthCategoryMap() {
        return orders
                .stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().getMonth(),
                        Collectors.collectingAndThen(
                                Collectors.flatMapping(order -> Collections.nCopies(order.getQuantity(), order.getProduct().getCategory()).stream(), Collectors.toList()),
                                categories -> categories
                                        .stream()
                                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                                        .entrySet()
                                        .stream()
                                        .max(Comparator.comparingLong(Map.Entry::getValue))
                                        .orElseThrow()
                                        .getKey()
                        )
                ));
    }
}
