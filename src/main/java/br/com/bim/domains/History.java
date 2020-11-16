package br.com.bim.domains;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "game_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Game game;

    @JoinColumn(name = "player_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Player player;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}
