package br.com.bim.dtos.input;

import br.com.bim.domains.Game;
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
public class TransferenceInputDto {

    @NotNull
    @JsonProperty(value = "payer")
    private Integer payer;

    @NotNull
    @JsonProperty(value = "receiver")
    private Integer receiver;

    @NotNull
    @JsonProperty(value = "value")
    private BigDecimal value;

    @NotNull
    @JsonProperty(value = "code")
    private String code;
}
