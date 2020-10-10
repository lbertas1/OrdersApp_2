package bartos.lukasz.ui;

import bartos.lukasz.persistence.model.Customer;
import bartos.lukasz.persistence.model.ExchangeRate;
import bartos.lukasz.persistence.model.Order;
import bartos.lukasz.service.DataGenerator;
import bartos.lukasz.service.EmailService;
import bartos.lukasz.service.OrderService;
import bartos.lukasz.ui.controller.ReadExchangeRate;
import bartos.lukasz.ui.data.UserData;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class MenuService {
    private final OrderService orderService;
    private final DataGenerator dataGenerator;
    private final ReadExchangeRate readExchangeRate;

    private int chooseOption() {
        System.out.println("Menu:");
        System.out.println("1. Show cars");
        System.out.println("2. Get average from products bought in period");
        System.out.println("3. Get product with biggest price from category ");
        System.out.println("4. Get set of client and their products");
        System.out.println("5. Get dates with highest and lowest number of orders");
        System.out.println("6. Get client who paid the most");
        System.out.println("7. Get max price for every orders included discounts");
        System.out.println("8. Get number of clients who every time bought given quantity of products");
        System.out.println("9. Get category with product bought most often");
        System.out.println("10. Get month and number of products sold this month");
        System.out.println("11. Get month and category bought most often in this month");
        System.out.println("12. Enter new order");
        System.out.println("13. Read exchange rate");
        System.out.println("0. End of app");
        return UserData.getInt("Choose option:");
    }

    public void menu() {

        int option;
        do {
            option = chooseOption();
            switch (option) {
                case 1 -> option1();
                case 2 -> option2();
                case 3 -> option3();
                case 4 -> option4();
                case 5 -> option5();
                case 6 -> option6();
                case 7 -> option7();
                case 8 -> option8();
                case 9 -> option9();
                case 10 -> option10();
                case 11 -> option11();
                case 12 -> option12();
                case 13 -> option13();
                case 0 -> {
                    System.out.println("Have a nice day!");
                    return;
                }
                default -> System.out.println("No such option");
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private void option1() {
        System.out.println(orderService.orders());
    }

    private void option2() {
        System.out.println(orderService.averageFromProductsBoughtInPeriod(
                UserData.getDate("Enter first date in format: yyyy-MM-dd"),
                UserData.getDate("Enter second date in format: yyyy-MM-dd")
        ));
    }

    private void option3() {
        System.out.println(orderService.productWithBiggestPriceForCategory());
    }

    private void option4() {
        String to = UserData.getString("Provide email adress");
        String title = "List of ordered products";
        Long id = UserData.getLong("Choose customer, provide his id");
        Map.Entry<Customer, List<Order>> customerListEntry = orderService
                .clientProductMap()
                .entrySet()
                .stream()
                .filter(customerListEntry1 -> customerListEntry1.getKey().getId().equals(id))
                .findFirst()
                .orElseThrow();

        EmailService.send(to, title, customerListEntry.getValue().toString());
    }

    private void option5() {
        orderService.getDateWithHighestNumberOfOrders();
    }

    private void option6() {
        System.out.println(orderService.getClientWhoPaidTheMost());
    }

    private void option7() {
        System.out.println(orderService.getSumPriceForEveryOrdersIncludedDiscounts());
    }

    private void option8() {
        System.out.println(orderService.quantityOfClients(
                UserData.getInt("Enter product quantity")
        ));
    }

    private void option9() {
        System.out.println(orderService.categoryFromProductMostOftenBought());
    }

    private void option10() {
        System.out.println(orderService.monthQuantityOfProductMap());
    }

    private void option11() {
        System.out.println(orderService.monthCategoryMap());
    }

    private void option12() {
        var newCustomer = UserData.newCustomer();
        var newProduct = UserData.newProduct();
        var newOrder = UserData.newOrder(newProduct, newCustomer);
        System.out.println("NEW ORDER: " + dataGenerator.saveOrder(newOrder));
    }

    private void option13() { ;
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate = readExchangeRate.getLastReadingExchangeRate(exchangeRate);
        System.out.println(exchangeRate);
    }
}
