package bartos.lukasz.persistence.model;

import bartos.lukasz.persistence.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {

    private Customer customer;
    private Product product;
    private Integer quantity;
    private LocalDate orderDate;

    @Override
    public String toString() {
        return String.join("\n",
                "CUSTOMER: " + customer,
                "PRODUCT: " + product,
                "QUANTITY: " + quantity,
                "ORDER DATE: " + orderDate);
    }

    public static final class Builder {
        private Customer customer;
        private Product product;
        private Integer quantity;
        private LocalDate orderDate;

        public Builder customer(Customer customer) {
            if (Objects.isNull(customer)) throw new ValidationException("Given customer is null!");
            else this.customer = customer;
            return this;
        }

        public Builder product(Product product) {
            if (Objects.isNull(product)) throw new ValidationException("Given product is null!");
            else this.product = product;
            return this;
        }

        public Builder quantity(Integer quantity) {
            if (quantity > 0) this.quantity = quantity;
            else throw new ValidationException("Quantity must be greater than zero");
            return this;
        }

        public Builder orderDate(LocalDate localDate) {
            if (localDate.isBefore(LocalDate.now())) throw new ValidationException("Given date is incorrect");
            else this.orderDate = localDate;
            return this;
        }

        public Order build() {
            Order order = new Order();
            order.customer = this.customer;
            order.product = this.product;
            order.quantity = this.quantity;
            order.orderDate = this.orderDate;
            return order;
        }
    }
}
