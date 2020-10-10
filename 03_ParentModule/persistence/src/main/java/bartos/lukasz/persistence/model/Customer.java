package bartos.lukasz.persistence.model;

import bartos.lukasz.persistence.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private String email;

    @Override
    public String toString() {
        return String.join("\n",
                "ID: " + id,
                "NAME: " + name,
                "SURNAME: " + surname,
                "AGE: " + age,
                "EMAIL: " + email);
    }

    public static final class Builder {
        private Long id;
        private String name;
        private String surname;
        private Integer age;
        private String email;

        public Builder name(String name) {
            if (name.matches("[A-Z ]+")) this.name = name;
            else throw new ValidationException("Invalid name");
            return this;
        }

        public Builder surname(String surname) {
            if (surname.matches("[A-Z ]+")) this.surname = surname;
            else throw new ValidationException("Invalid surname");
            return this;
        }

        public Builder age(Integer age) {
            if (age >= 18) this.age = age;
            else throw new ValidationException("Invalid age");
            return this;
        }

        public Builder email(String email) {
            if (email.matches("[a-z\\d]@[a-z]//.[a-z]")) this.email = email;
            else throw new ValidationException("Invalid email");
            return this;
        }

        public Customer build() {
            Customer customer = new Customer();
            customer.id = this.id;
            customer.name = this.name;
            customer.surname = this.surname;
            customer.age = this.age;
            customer.email = this.email;
            return customer;
        }
    }
}
