package bartos.lukasz.service;

import bartos.lukasz.persistence.model.Customer;
import bartos.lukasz.persistence.model.Order;
import bartos.lukasz.persistence.model.Product;
import bartos.lukasz.persistence.model.enums.Category;
import bartos.lukasz.service.extensions.ExtensionForOrders;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ExtensionForOrders.class)
@RequiredArgsConstructor
class OrderServiceTest {

    private final OrderService orderService;

    @Test
    public void getOrders() {
        assertNotNull(orderService.getOrders());
    }

    @ParameterizedTest
    @CsvSource({"2020-10-01, 2020-11-01", "2020-10-10, 2020-11-11"})
    public void averageFromProductsBoughtInPeriod(LocalDate from, LocalDate to) {
        double avg = orderService.averageFromProductsBoughtInPeriod(from, to);

        double average = orderService
                .getOrders()
                .stream()
                .filter(order -> order.getOrderDate().isAfter(from))
                .filter(order -> order.getOrderDate().isBefore(to))
                .mapToDouble(value -> value.getProduct().getPrice())
                .average()
                .orElseThrow();

        assertEquals(average, avg);
    }

    @ParameterizedTest
    @EnumSource(Category.class)
    public void productWithBiggestPriceForCategory(Category category) {
        Map<Category, Product> categoryProductMap = orderService.productWithBiggestPriceForCategory();

        Product productWithMaxPriceInGivenCategory = orderService
                .getOrders()
                .stream()
                .map(Order::getProduct)
                .filter(product -> product.getCategory().equals(category))
                .max(Comparator.comparing(Product::getPrice))
                .orElseThrow();

        assertEquals(productWithMaxPriceInGivenCategory, categoryProductMap.get(category));
    }

    @Test
    public void clientProductMap() {
        List<Customer> customers = orderService
                .getOrders()
                .stream()
                .map(Order::getCustomer)
                .collect(Collectors.toList());

        Map<Customer, List<Order>> customerOrdersMap = orderService.clientProductMap();

        customers.forEach(customer -> {
            List<Order> ordersForGivenCustomer = orderService
                    .getOrders()
                    .stream()
                    .filter(order -> order.getCustomer().equals(customer))
                    .collect(Collectors.toList());

            assertEquals(ordersForGivenCustomer, customerOrdersMap.get(customer));
        });
    }

    @Test
    public void getDateWithHighestNumberOfOrders() {
        LocalDate dateWithHighestNumberOfOrders = orderService
                .getDateWithHighestNumberOfOrders();

        assertNotNull(dateWithHighestNumberOfOrders);
    }

    @Test
    public void getDateWithLowestNumberOfOrders() {
        LocalDate dateWithLowestNumberOfOrders = orderService
                .getDateWithLowestNumberOfOrders();

        assertNotNull(dateWithLowestNumberOfOrders);
    }

    @Test
    public void getClientWhoPaidTheMost() {
        assertNotNull(orderService.getClientWhoPaidTheMost());
    }

    @Test
    public void getSumPriceForEveryOrdersIncludedDiscounts() {
        double sumPriceForCustomerAbove25Years = orderService
                .getOrders()
                .stream()
                .filter(order -> order.getCustomer().getAge() > 25)
                .mapToDouble(value -> value.getQuantity() * value.getProduct().getPrice())
                .map(operand -> operand * 0.02)
                .sum();

        double sumPriceForCustomerUnder25Years = orderService
                .getOrders()
                .stream()
                .filter(order -> order.getCustomer().getAge() < 25)
                .mapToDouble(value -> value.getQuantity() * value.getProduct().getPrice())
                .map(operand -> operand * 0.03)
                .sum();

        double finalSum = sumPriceForCustomerAbove25Years + sumPriceForCustomerUnder25Years;

        assertEquals(finalSum, orderService.getSumPriceForEveryOrdersIncludedDiscounts());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    public void quantityOfClients(int quantity) {
        long l = orderService.quantityOfClients(quantity);

        long count = orderService
                .getOrders()
                .stream()
                .filter(order -> order.getQuantity() >= quantity)
                .map(Order::getCustomer)
                .distinct()
                .count();

        assertEquals(count, l);
    }

    @Test
    public void categoryFromProductMostOftenBought() {
        assertNotNull(orderService.categoryFromProductMostOftenBought());
    }

    @Test
    public void monthQuantityOfProductMap() {
        Map<Month, Long> collect = orderService
                .getOrders()
                .stream()
                .collect(Collectors.groupingBy(order -> order.getOrderDate().getMonth(), Collectors.counting()));

        assertEquals(collect, orderService.monthQuantityOfProductMap());
    }

    @Test
    public void monthCategoryMap() {
        Map<Month, Category> monthCategoryMap = orderService.monthCategoryMap();

        Set<Month> months = monthCategoryMap.keySet();

        months.forEach(month -> {
            List<Order> ordersFromGivenMonth = orderService
                    .getOrders()
                    .stream()
                    .filter(order -> order.getOrderDate().getMonth().equals(month))
                    .collect(Collectors.toList());

            Category categoryFromGivenMonth = ordersFromGivenMonth
                    .stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getProduct().getCategory(),
                            Collectors.counting()
                    ))
                    .entrySet()
                    .stream()
                    .max(Map.Entry.comparingByValue())
                    .orElseThrow()
                    .getKey();

            assertEquals(categoryFromGivenMonth, monthCategoryMap.get(month));
        });
    }
}