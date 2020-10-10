package bartos.lukasz.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRate {
    private String currency;
    private String code;
    private List<Rate> rates;
}
