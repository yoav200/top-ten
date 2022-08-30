package com.alhalel.topten.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Entity
@Table(name = "player_stats")
@JsonIgnoreProperties(value = {"id", "createDateTime", "player"})
public class PlayerStats {

    public enum StatsFor {
        REGULAR_SEASON, PLAYOFFS
    }

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Enumerated(EnumType.STRING)
    private StatsFor statsFor;

    private double games;

    private double ptsPerGame;

    private double trbPerGme;

    private double astPerGame;

    private double stlPerGame;

    private double blkPerGame;

    private double tovPerGame;

    private double fgPct;

    private double fg3Pct;

    private double ftPct;

    private double efgPct;

    // advanced stats
    private double per;

    private double ws;

    private double wsPer48;

    private double bpm;


    public void update(PlayerStats stats) {
        setGames(stats.games);
        setPtsPerGame(stats.ptsPerGame);
        setTrbPerGme(stats.trbPerGme);
        setAstPerGame(stats.astPerGame);
        setStlPerGame(stats.stlPerGame);
        setBlkPerGame(stats.blkPerGame);
        setTovPerGame(stats.tovPerGame);
        setFgPct(stats.fgPct);
        setFg3Pct(stats.fg3Pct);
        setFtPct(stats.ftPct);
        setEfgPct(stats.efgPct);
        setPer(stats.per);
        setWs(stats.ws);
        setWsPer48(stats.wsPer48);
        setBpm(stats.bpm);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PlayerStats that = (PlayerStats) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PlayerStats{" +
                "id=" + id +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", statsFor=" + statsFor +
                ", games=" + games +
                ", ptsPerGame=" + ptsPerGame +
                ", trbPerGme=" + trbPerGme +
                ", astPerGame=" + astPerGame +
                ", stlPerGame=" + stlPerGame +
                ", blkPerGame=" + blkPerGame +
                ", tovPerGame=" + tovPerGame +
                ", fgPct=" + fgPct +
                ", fg3Pct=" + fg3Pct +
                ", ftPct=" + ftPct +
                ", efgPct=" + efgPct +
                ", per=" + per +
                ", ws=" + ws +
                ", wsPer48=" + wsPer48 +
                ", bpm=" + bpm +
                '}';
    }
}

