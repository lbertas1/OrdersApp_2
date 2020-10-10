package bartos.lukasz.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rate {

    private String no;
    private String effectiveDate;
    private String bid;
    private String ask;
}
