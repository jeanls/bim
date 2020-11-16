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
public class AddPlayerGameInputDto {

    @NotNull
    @JsonProperty(value = "player_name")
    private String playerName;

    @NotNull
    @JsonProperty(value = "game_code")
    private String gameCode;
}
