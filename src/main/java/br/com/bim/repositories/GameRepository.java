package br.com.bim.repositories;

import br.com.bim.domains.Game;
import br.com.bim.domains.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GameRepository extends CrudRepository<Game, String> {

    Game findAllByCodeAndAndPass(String code, String pass);

    Game findAllByCodeAndPlayersIsNotIn(String code, List<Player> players);
}
