package com.alhalel.topten.enteties;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@ToString
@Entity
@Table(name = "rank_list")
public class RankList {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    private String title;

//    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "account_id", referencedColumnName = "id")
//    private Account account;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "rank_list_id", referencedColumnName = "id")
    private Set<RankListItem> rankListItems;


    public Set<RankListItem> getRankListItems() {
        if (this.rankListItems == null) {
            this.rankListItems = new HashSet<>();
        }
        return rankListItems;
    }

    public List<RankListItem> sortedRankListItems() {
        return getRankListItems().stream()
                .sorted(Comparator.comparing(RankListItem::getRank))
                .collect(Collectors.toList());
    }

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
}
