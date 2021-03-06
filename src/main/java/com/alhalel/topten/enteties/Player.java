package com.alhalel.topten.enteties;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(unique = true, nullable = false, updatable = false)
    private String uniqueName;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PlayerInfo playerInfo;

    @OneToOne(mappedBy = "player", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn
    private PlayerAchievements achievements;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    private Set<PlayerStats> playerStats;


    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
        playerInfo.setPlayer(this);
    }

    public void setAchievements(PlayerAchievements achievements) {
        this.achievements = achievements;
        achievements.setPlayer(this);
    }

    public Set<PlayerStats> getPlayerStats() {
        if (playerStats == null) {
            this.playerStats = new HashSet<>();
        }
        return playerStats;
    }

    public PlayerStats getCareerPerGame() {
        return getPlayerStats().stream()
                .filter(s -> s.getStatsFor().equals(PlayerStats.StatsFor.REGULAR_SEASON))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Player has no career stats"));
    }

    public PlayerStats getPlayoffsPerGame() {
        return getPlayerStats().stream()
                .filter(s -> s.getStatsFor().equals(PlayerStats.StatsFor.PLAYOFFS))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Player has no career stats"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Player player = (Player) o;
        return id != null && Objects.equals(id, player.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
