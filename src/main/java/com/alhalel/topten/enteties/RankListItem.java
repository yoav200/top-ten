package com.alhalel.topten.enteties;

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
@Table(name = "rank_list_item",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"rank_list_id", "player_id"})
                //@UniqueConstraint(columnNames = {"rank_list_id", "rank"})
        })
public class RankListItem {

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne
    private RankList rankList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    private int rank;


    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RankListItem that = (RankListItem) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RankListItem{" +
                "id=" + id +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", player=" + player +
                ", rank=" + rank +
                '}';
    }
}
