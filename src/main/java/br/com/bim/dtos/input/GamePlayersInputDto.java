package br.com.bim.dtos.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayersInputDto {

    @NotNull
    @JsonProperty(value = "code")
    private String code;

    @NotNull
    @JsonProperty(value = "pass")
    private String pass;
}
