package bartos.lukasz.persistence.model;

import bartos.lukasz.persistence.exception.ValidationException;
import bartos.lukasz.persistence.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private String name;
    private Double price;
    private Category category;

    @Override
    public String toString() {
        return String.join("\n",
                "NAME: " + name,
                "PRICE: " + price,
                "CATEGORY: " + category);
    }

    public static final class Builder {
        private String name;
        private Double price;
        private Category category;

        public Builder name(String name) {
            if (name.matches("[A-Z ]+")) this.name = name;
            else throw new ValidationException("Invalid name");
            return this;
        }

        public Builder price(double price) {
            if (price > 0) this.price = price;
            else throw new ValidationException("Price smaller than 0");
            return this;
        }

        public Builder category(Category category) {
            if (Objects.isNull(category)) throw new ValidationException("Given category object is null");
            this.category = category;
            return this;
        }

        public Product build() {
            Product product = new Product();
            product.name = this.name;
            product.price = this.price;
            product.category = this.category;
            return product;
        }
    }
}
