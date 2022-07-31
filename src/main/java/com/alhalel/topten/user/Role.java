package com.alhalel.topten.user;

import lombok.*;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Setter(AccessLevel.PRIVATE)
@Getter(AccessLevel.PUBLIC)
@Builder
@Entity
@Immutable
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Role {

    @Transient
    public static final Role ROLE_USER = Role.builder().id(1L).name("ROLE_USER").build();

    @Transient
    public static final Role ROLE_ADMIN = Role.builder().id(2L).name("ROLE_ADMIN").build();

    @Id
    @Column(name = "role_id")
    private Long id;

    @NotNull
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return id.equals(role.id) && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "role_id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
