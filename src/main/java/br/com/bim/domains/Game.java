package br.com.bim.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "game")
@AllArgsConstructor
@NoArgsConstructor
public class Game {

    public Game(String code) {
        this.code = code;
    }

    @Id
    @Column(name = "code", nullable = false, unique = true, length = 6)
    private String code;

    @Column(name = "pass", nullable = false, length = 6)
    private String pass;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "start_value", nullable = false, precision = 19, scale = 2)
    private BigDecimal startValue;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "game")
    private List<Player> players = new ArrayList<>();

    public void addPlayer(final Player player) {
        player.setGame(this);
        players.add(player);
    }

}
