package com.alhalel.topten.player;

import com.alhalel.topten.util.ResourceUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
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
@Table(name = "player_info")
@JsonIgnoreProperties(value = {"id", "createDateTime", "player"})
public class PlayerInfo {
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

    private String fullName;

    private String country;

    private String imageUrl;

    private String position;

    private String DOB;

    private String height;

    private String wight;

    private String draft;

    private String NBADebut;

    private String yearsActive;

    private boolean active;

    public String getImageUrl() {
        return StringUtils.defaultIfBlank(this.imageUrl, ResourceUtils.defaultPlayerAvatar());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PlayerInfo that = (PlayerInfo) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "id=" + id +
                ", createDateTime=" + createDateTime +
                ", updateDateTime=" + updateDateTime +
                ", fullName='" + fullName + '\'' +
                ", country='" + country + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", position='" + position + '\'' +
                ", DOB='" + DOB + '\'' +
                ", height='" + height + '\'' +
                ", wight='" + wight + '\'' +
                ", draft='" + draft + '\'' +
                ", NBADebut='" + NBADebut + '\'' +
                ", yearsActive='" + yearsActive + '\'' +
                ", active=" + active +
                '}';
    }
}
