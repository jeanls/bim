package br.com.bim.controllers;

import br.com.bim.dtos.input.*;
import br.com.bim.dtos.output.AddPlayerGameOutputDto;
import br.com.bim.dtos.output.GameCreateOutputDto;
import br.com.bim.dtos.output.GamePlayersOutputDto;
import br.com.bim.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin("*")
@AllArgsConstructor
@RequestMapping("/game")
@RestController
public class GameController {

    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameCreateOutputDto> create(@RequestBody @Valid final GameCreateInputDto gameCreateInputDto) {
        final GameCreateOutputDto gameCreateOutputDto = gameService.createGame(gameCreateInputDto);
        return ResponseEntity.ok(gameCreateOutputDto);
    }

    @PostMapping("/addPlayer")
    public ResponseEntity<AddPlayerGameOutputDto> addPlayer(@RequestBody @Valid final AddPlayerGameInputDto addPlayerGameInputDto) {

        final AddPlayerGameOutputDto outputDto = gameService.addPlayer(addPlayerGameInputDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(outputDto);
    }

    @PostMapping("/players")
    public ResponseEntity<GamePlayersOutputDto> getAllPlayers(@RequestBody @Valid final GamePlayersInputDto gamePlayersInputDto) {
        final GamePlayersOutputDto gamePlayersOutputDto = gameService.getPlayers(gamePlayersInputDto);

        return ResponseEntity.ok(gamePlayersOutputDto);
    }

    @PostMapping("/operation")
    public ResponseEntity<Object> operation(@RequestBody @Valid final OperationInputDto operationInputDto) {
        gameService.operation(operationInputDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/transference")
    public ResponseEntity<Object> transference(@RequestBody @Valid final TransferenceInputDto transferenceInputDto) {
        gameService.transference(transferenceInputDto);

        return ResponseEntity.ok().build();
    }
}
