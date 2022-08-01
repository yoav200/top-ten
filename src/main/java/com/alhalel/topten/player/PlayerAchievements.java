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
@Table(name = "player_achievements")
@JsonIgnoreProperties(value = {"id", "createDateTime", "player"})
public class PlayerAchievements {

    @Id
    @Column(name = "player_id")
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @OneToOne
    @MapsId
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private int championships;

    private int championshipsAba;

    private int finalsMvp;

    private int leagueMvp;

    private int scoringChamp;

    private int reboundChamp;

    private int assistChamp;

    private int stealsChamp;

    private int blocksChamp;

    private int defensivePlayerOfTheYear;

    private int allNba;

    private int allDefensive;

    private int allStar;

    private int allStarMvp;

    private int rookieOfTheYear;

    private int mostImprove;


    public int getTotalChamps() {
        return getChampionships() + getChampionshipsAba();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PlayerAchievements that = (PlayerAchievements) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PlayerAchievements{" +
                "id=" + id +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", championships=" + championships +
                ", championshipsAba=" + championshipsAba +
                ", finalsMvp=" + finalsMvp +
                ", leagueMvp=" + leagueMvp +
                ", scoringChamp=" + scoringChamp +
                ", reboundChamp=" + reboundChamp +
                ", assistChamp=" + assistChamp +
                ", stealsChamp=" + stealsChamp +
                ", blocksChamp=" + blocksChamp +
                ", defensivePlayerOfTheYear=" + defensivePlayerOfTheYear +
                ", allNba=" + allNba +
                ", allDefensive=" + allDefensive +
                ", allStar=" + allStar +
                ", allStarMvp=" + allStarMvp +
                ", rookieOfTheYear=" + rookieOfTheYear +
                ", mostImprove=" + mostImprove +
                '}';
    }
}
