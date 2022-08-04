package com.alhalel.topten.social.voting;

import lombok.*;
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
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "object_id", "object"})})
public class Votes {

    public enum VoteOption {
        UP, DOWN
    }

    public enum VoteObject {
        RANK_LIST, COMMENT
    }

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "object_id", nullable = false, updatable = false)
    private Long objectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private VoteObject object;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteOption vote;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Votes votes = (Votes) o;
        return id.equals(votes.id) && userId.equals(votes.userId) && objectId.equals(votes.objectId) && object == votes.object;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, objectId, object);
    }

    @Override
    public String toString() {
        return "Votes{" +
                "id=" + id +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", userId=" + userId +
                ", objectId=" + objectId +
                ", vote=" + vote +
                ", object=" + object +
                '}';
    }
}
