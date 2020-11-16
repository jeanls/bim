package br.com.bim.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameCreateInputDto {

    @NotNull
    @JsonProperty(index = 0, value = "start_value")
    private BigDecimal startValue;

    @NotNull
    @JsonProperty(index = 1, value = "pass")
    private String pass;
}
