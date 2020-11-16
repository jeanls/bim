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
public class HistoryOutputDto {

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "time")
    private LocalDateTime time;
}
