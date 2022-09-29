package com.alhalel.topten.ranking;

import com.alhalel.topten.user.User;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Entity
@Table(name = "rank_list")
public class RankList {

    public enum RankListType {
        ALL_TIME_GREATEST("All Time Greatest"),
        ALL_TIME_GREATEST_GUARDS("All Time Greatest Guards"),
        ALL_TIME_GREATEST_FORWARDS("All Time Greatest Forwards"),
        ALL_TIME_GREATEST_CENTERS("All Time Greatest Centers"),
        BEST_ACTIVE_PLAYERS("Best Active players");

        private final String title;

        RankListType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    public enum RankListVisibility {
        PRIVATE, SHARE_ANONYMOUSLY, SHARE
    }

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private String title;

    @Column(name = "list_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private RankListType listType = RankListType.ALL_TIME_GREATEST;

    @Enumerated(EnumType.STRING)
    private RankListVisibility visibility = RankListVisibility.SHARE;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "rank_list_id", referencedColumnName = "id")
    private Set<RankListItem> rankListItems;

    public Set<RankListItem> getRankListItems() {
        if (this.rankListItems == null) {
            this.rankListItems = new HashSet<>();
        }
        return rankListItems;
    }

    // used in UI
    public List<RankListItem> sortedRankListItems() {
        return getRankListItems().stream()
                .sorted(Comparator.comparing(RankListItem::getRank))
                .collect(Collectors.toList());
    }


    // ~ ==================== List Item Actions

    public void updateRankItem(RankListItem item, int number) {
        if (item.getRank() == number) {
            return;
        }
        getRankItemByRank(number).ifPresentOrElse(exist -> {
            while (item.getRank() > number) {
                moveRankUp(item.getRank());
            }
            while (item.getRank() < number) {
                moveRankDown(item.getRank());
            }
        }, () -> item.setRank(number));
    }

    // number 3 -- > 4
    public void moveRankDown(int rank) {
        if (rank == 50) {
            return;
        }
        getRankItemByRank(rank).ifPresent(item -> getRankItemByRank(rank + 1)
                .ifPresentOrElse(lowerItem -> {
                    lowerItem.setRank(rank);
                    item.setRank(rank + 1);
                }, () -> item.setRank(rank + 1)));
    }

    // number 3 -- > 2
    public void moveRankUp(int rank) {
        if (rank == 1) {
            return;
        }
        getRankItemByRank(rank).ifPresent(item -> getRankItemByRank(rank - 1)
                .ifPresentOrElse(upperItem -> {
                    upperItem.setRank(rank);
                    item.setRank(rank - 1);
                }, () -> item.setRank(rank - 1)));
    }

    public void addRankItem(RankListItem item) {
        int nextFreeRank = findNextFreeRank(item.getRank());

        while (nextFreeRank > item.getRank()) {
            int finalNextFreeRank = nextFreeRank;
            getRankItemByRank(nextFreeRank - 1).ifPresent(b -> b.setRank(finalNextFreeRank));
            nextFreeRank = nextFreeRank - 1;
        }
        rankListItems.add(item);
    }

    private boolean hasRank(int rank) {
        return rankListItems.stream().anyMatch(b -> b.getRank() == rank);
    }

    private int findNextFreeRank(int rank) {
        return hasRank(rank) ? findNextFreeRank(rank + 1) : rank;
    }

    public Optional<RankListItem> getRankItemByRank(int rank) {
        return rankListItems.stream().filter(b -> b.getRank() == rank).findFirst();
    }

    // ~ ==================== Hashcode & Equals

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RankList rankList = (RankList) o;
        return id != null && Objects.equals(id, rankList.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "RankList{" +
                "id=" + id +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", title='" + title + '\'' +
                ", rankListItems=" + rankListItems +
                '}';
    }
}
