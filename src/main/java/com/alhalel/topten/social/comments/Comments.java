package com.alhalel.topten.social.comments;

import com.alhalel.topten.user.User;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(value = AccessLevel.PACKAGE)
@Getter
@Entity
@Table(name = "comments")
public class Comments {

    public enum CommentsObject {
        RANK_LIST
    }

    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDateTime createDateTime;

    @UpdateTimestamp
    private LocalDateTime updateDateTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Column(name = "object_id", nullable = false, updatable = false)
    private Long objectId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private CommentsObject object;

    @Column(nullable = false)
    private String content;

    // the id of the parent comment, can be null
    private Long parentId;

    private boolean active = true;

}
