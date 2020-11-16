package br.com.bim.dtos.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddPlayerGameOutputDto {
    private Integer playerId;

    private BigDecimal value;

    private String name;
}
