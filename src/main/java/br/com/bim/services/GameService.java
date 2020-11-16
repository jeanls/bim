package br.com.bim.services;

import br.com.bim.domains.Game;
import br.com.bim.domains.History;
import br.com.bim.domains.Player;
import br.com.bim.dtos.input.*;
import br.com.bim.dtos.output.AddPlayerGameOutputDto;
import br.com.bim.dtos.output.GameCreateOutputDto;
import br.com.bim.dtos.output.GamePlayersOutputDto;
import br.com.bim.dtos.output.PlayerOutputDto;
import br.com.bim.enums.OperationType;
import br.com.bim.exceptions.BadRequestException;
import br.com.bim.exceptions.NotFoundException;
import br.com.bim.exceptions.UnprocessableException;
import br.com.bim.repositories.GameRepository;
import br.com.bim.repositories.HistoryRepository;
import br.com.bim.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GameService {

    public static final String JOGO_NAO_ENCONTRADO = "Jogo não encontrado";
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final HistoryRepository historyRepository;

    @Transactional
    public GameCreateOutputDto createGame(final GameCreateInputDto gameCreateInputDto) {
        final Game game = new Game();
        game.setCode(RandomStringUtils.random(6, true, false).toLowerCase());
        game.setStartAt(LocalDateTime.now());
        game.setPass(gameCreateInputDto.getPass());
        game.setStartValue(gameCreateInputDto.getStartValue());
        gameRepository.save(game);

        return new GameCreateOutputDto(game.getCode());
    }

    @Transactional
    public AddPlayerGameOutputDto addPlayer(final AddPlayerGameInputDto addPlayerGameInputDto) {
        final Optional<Game> gameResult = gameRepository.findById(addPlayerGameInputDto.getGameCode());
        if (gameResult.isEmpty()) {
            throw new NotFoundException(JOGO_NAO_ENCONTRADO);
        }
        final Game game = gameResult.get();
        final Player player = new Player(addPlayerGameInputDto.getPlayerName(), game.getStartValue(), LocalDateTime.now());
        player.setGame(game);
        playerRepository.save(player);

        final AddPlayerGameOutputDto outputDto = new AddPlayerGameOutputDto();
        outputDto.setName(player.getName());
        outputDto.setValue(player.getTotal());
        outputDto.setPlayerId(player.getId());
        return outputDto;
    }

    public GamePlayersOutputDto getPlayers(final GamePlayersInputDto gamePlayersInputDto) {
        final Game game = gameRepository.findAllByCodeAndAndPass(gamePlayersInputDto.getCode(), gamePlayersInputDto.getPass());

        if (Objects.isNull(game)) {
            throw new NotFoundException(JOGO_NAO_ENCONTRADO);
        }

        final GamePlayersOutputDto outputDto = new GamePlayersOutputDto();

        outputDto.setCode(game.getCode());
        outputDto.setPlayers(game.getPlayers().stream().map(player -> {
            final PlayerOutputDto playerOutputDto = new PlayerOutputDto();
            playerOutputDto.setEntryAt(player.getEntryAt());
            playerOutputDto.setPlayerName(player.getName());
            playerOutputDto.setTotal(player.getTotal().doubleValue());
            playerOutputDto.setId(player.getId());
            return playerOutputDto;
        }).collect(Collectors.toList()));

        return outputDto;
    }

    @Transactional
    public void operation(final OperationInputDto operationInputDto) {
        final Game game = gameRepository.findAllByCodeAndAndPass(operationInputDto.getCode(), operationInputDto.getPass());

        if (Objects.isNull(game)) {
            throw new NotFoundException(JOGO_NAO_ENCONTRADO);
        }

        if (!OperationType.exists(operationInputDto.getOperationType())) {
            throw new BadRequestException("Tipo de operação inválida");
        }

        final Optional<Player> playerResult = playerRepository.findById(operationInputDto.getPlayerId());

        if (playerResult.isEmpty()) {
            throw new NotFoundException("Jogador não encontrado");
        }

        final Player player = playerResult.get();

        if (OperationType.SUM.getType().equals(operationInputDto.getOperationType())) {
            player.setTotal(player.getTotal().add(operationInputDto.getValue()));
        } else {
            if (player.getTotal().doubleValue() < operationInputDto.getValue().doubleValue()) {
                throw new UnprocessableException("Valor solicitado é inválido, maior que o saldo atual");
            }
            player.setTotal(player.getTotal().subtract(operationInputDto.getValue()));
        }

        playerRepository.save(player);

        final String optType = operationInputDto.getOperationType().equals(OperationType.SUM.getType())
                ? "recebeu" : "perdeu";

        final History history = new History();
        history.setGame(game);
        history.setDate(LocalDateTime.now());
        history.setPlayer(player);
        history.setMessage(String.format("[BANCO] %s %s %s", player.getName(), optType, operationInputDto.getValue().toString()));
        historyRepository.save(history);
    }

    @Transactional
    public void transference(final TransferenceInputDto transferenceInputDto) {
        final Optional<Player> payerResult = playerRepository.findById(transferenceInputDto.getPayer());
        final Optional<Player> receiverResult = playerRepository.findById(transferenceInputDto.getReceiver());
        final Optional<Game> game = gameRepository.findById(transferenceInputDto.getCode());
        if (payerResult.isEmpty() || receiverResult.isEmpty() || game.isEmpty()) {
            throw new NotFoundException("Jogador não encontrado");
        }

        final Player payer = payerResult.get();
        final Player receiver = receiverResult.get();

        if (payer.getTotal().doubleValue() < transferenceInputDto.getValue().doubleValue()) {
            throw new UnprocessableException("Não é possível completar essa transação");
        }

        if (payer.getId().equals(receiver.getId())) {
            throw new UnprocessableException("Não é possível completar essa transação");
        }

        payer.transfer(receiver, transferenceInputDto.getValue());

        playerRepository.save(payer);
        playerRepository.save(receiver);

        final History history = new History();
        history.setGame(game.get());
        history.setDate(LocalDateTime.now());
        history.setPlayer(payer);
        history.setMessage(String.format("[JOGO] %s pagou %s para %s", payer.getName(), transferenceInputDto.getValue().toString(), receiver.getName()));
        historyRepository.save(history);
    }
}
