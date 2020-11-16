package br.com.bim.domains;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "player")
public class Player {

    public Player(final String name, final BigDecimal total, final LocalDateTime entryAt) {
        this.name = name;
        this.total = total;
        this.entryAt = entryAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "total", scale = 2, precision = 19, nullable = false)
    private BigDecimal total;

    @Column(name = "entry_at", nullable = false)
    private LocalDateTime entryAt;

    @JoinColumn(name = "game_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Game game;

    public void transfer(final Player player, final BigDecimal value) {
        this.setTotal(this.getTotal().subtract(value));
        player.setTotal(player.getTotal().add(value));
    }
}
