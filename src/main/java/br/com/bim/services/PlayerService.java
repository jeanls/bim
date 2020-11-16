package br.com.bim.services;

import br.com.bim.domains.Game;
import br.com.bim.domains.Player;
import br.com.bim.dtos.output.PlayerOutputDto;
import br.com.bim.dtos.output.PlayerValueOutputDto;
import br.com.bim.exceptions.NotFoundException;
import br.com.bim.repositories.GameRepository;
import br.com.bim.repositories.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlayerService {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public List<PlayerOutputDto> getAllPlayers(final String code) {
        Optional<Game> gameResult = gameRepository.findById(code);

        if (gameResult.isEmpty()) {
            throw new NotFoundException("Jogo não encontrado");
        }

        final Game game = gameResult.get();
        return game.getPlayers().stream().map(player -> {
            final PlayerOutputDto playerOutputDto = new PlayerOutputDto();
            playerOutputDto.setId(player.getId());
            playerOutputDto.setPlayerName(player.getName());
            playerOutputDto.setEntryAt(player.getEntryAt());
            return playerOutputDto;
        }).collect(Collectors.toList());
    }

    public PlayerValueOutputDto getValue(final Integer playerId) {
        final Player player = playerRepository.findById(playerId).orElseThrow(() -> new NotFoundException(""));

        return new PlayerValueOutputDto(player.getTotal());
    }
}
