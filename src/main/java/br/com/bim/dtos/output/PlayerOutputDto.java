package br.com.bim.dtos.output;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerOutputDto {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "player_name")
    private String playerName;

    @JsonProperty(value = "total")
    private Double total;

    @JsonProperty(value = "entry_at")
    private LocalDateTime entryAt;
}
