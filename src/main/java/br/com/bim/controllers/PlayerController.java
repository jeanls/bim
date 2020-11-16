package br.com.bim.controllers;

import br.com.bim.dtos.output.PlayerValueOutputDto;
import br.com.bim.services.PlayerService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/player")
@RestController
public class PlayerController {

    private final PlayerService playerService;

    @GetMapping(path = "/{code}")
    public ResponseEntity<Object> getHistory(@PathVariable("code") final String code) {
        return ResponseEntity.ok(playerService.getAllPlayers(code));
    }

    @GetMapping(path = "/value/{playerId}")
    public ResponseEntity<PlayerValueOutputDto> getValue(@PathVariable("playerId") final Integer playerId) {
        return ResponseEntity.ok(playerService.getValue(playerId));
    }
}
