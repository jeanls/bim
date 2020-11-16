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
public class OperationInputDto {

    @NotNull
    @JsonProperty(value = "code")
    private String code;

    @NotNull
    @JsonProperty(value = "pass")
    private String pass;

    @NotNull
    @JsonProperty(value = "operation_type")
    private String operationType;

    @NotNull
    @JsonProperty(value = "value")
    private BigDecimal value;

    @NotNull
    @JsonProperty(value = "player_id")
    private Integer playerId;
}
