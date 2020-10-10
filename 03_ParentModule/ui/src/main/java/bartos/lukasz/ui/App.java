package bartos.lukasz.ui;

import bartos.lukasz.persistence.model.ExchangeRate;
import bartos.lukasz.service.DataGenerator;
import bartos.lukasz.service.OrderService;
import bartos.lukasz.ui.controller.Controller;
import bartos.lukasz.ui.controller.ReadExchangeRate;

import static spark.Spark.initExceptionHandler;
import static spark.Spark.port;

public class App {
    public static void main(String[] args) {

        final String FILENAME_PATH = "C:/IntelijProjects/03_Multimodule_Streams/ParentModule/ui/src/main/resources/data/orders.json";
        var dataGenerator = new DataGenerator(FILENAME_PATH);
        dataGenerator.saveToFile();
        var orderService = new OrderService(FILENAME_PATH);
        var readExchangeRate = new ReadExchangeRate();
        var menuService = new MenuService(orderService, dataGenerator, readExchangeRate);
        ExchangeRate exchangeRate = new ExchangeRate();
        readExchangeRate.getLastReadingExchangeRate(exchangeRate);
        menuService.menu();

        port(8090);
        initExceptionHandler(e -> System.out.println(e.getMessage()));

        Controller controller = new Controller(orderService);
        controller.initRoutes();
    }
}
