package br.com.bim.dtos.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GamePlayersOutputDto {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "players")
    private List<PlayerOutputDto> players;
}
