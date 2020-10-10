package bartos.lukasz.ui.controller;

import bartos.lukasz.service.JsonTransformer;
import bartos.lukasz.service.OrderService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static spark.Spark.get;
import static spark.Spark.path;

@RequiredArgsConstructor
public class Controller {

    private final OrderService orders;

    public void initRoutes() {

        path("/get-orders", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return orders.getOrders();
            }), new JsonTransformer());
        });

        path("/average-orders-by-given-dates", () -> {
            get("/:date1/:date2", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                LocalDate date1 = LocalDate.parse(request.params("date1"));
                LocalDate date2 = LocalDate.parse(request.params("date2"));
                return orders.averageFromProductsBoughtInPeriod(date1, date2);
            }), new JsonTransformer());
        });

        path("/category-product-biggest-price", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.productWithBiggestPriceForCategory();
                return null;
            }), new JsonTransformer());
        });

        path("/client-product-map", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.clientProductMap();
                return null;
            }), new JsonTransformer());
        });

        path("/date-by-orders", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.getDateWithHighestNumberOfOrders();
                return null;
            }), new JsonTransformer());
        });

        path("/client-who-paid-most", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return orders.getClientWhoPaidTheMost();
            }), new JsonTransformer());
        });

        path("/product-max-price", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return orders.getSumPriceForEveryOrdersIncludedDiscounts();
            }), new JsonTransformer());
        });

        path("/client-who-every-time-ordered-like-argument", () -> {
            get("/:quantity", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                int quantity = Integer.parseInt(request.params("quantity"));
                orders.quantityOfClients(quantity);
                return null;
            }), new JsonTransformer());
        });

        path("/category-most-often-bought", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                return orders.categoryFromProductMostOftenBought();
            }), new JsonTransformer());
        });

        path("/month-quantity-map", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.monthQuantityOfProductMap();
                return null;
            }), new JsonTransformer());
        });

        path("/month-category-map", () -> {
            get("", ((request, response) -> {
                response.header("Content-Type", "application/json;charset=utf-8");
                response.status(200);
                orders.monthCategoryMap();
                return null;
            }), new JsonTransformer());
        });
    }
}
