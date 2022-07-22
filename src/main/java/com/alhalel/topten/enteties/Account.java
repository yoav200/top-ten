//package com.alhalel.topten.enteties;
//
//import lombok.*;
//import org.hibernate.Hibernate;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.time.LocalDateTime;
//import java.util.Objects;
//
//@Builder(toBuilder = true)
//@AllArgsConstructor(access = AccessLevel.PACKAGE)
//@NoArgsConstructor(access = AccessLevel.PACKAGE)
//@Setter(value = AccessLevel.PACKAGE)
//@Getter
//@ToString
//@Entity
//@Table(name = "account")
//public class Account {
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    @CreationTimestamp
//    private LocalDateTime createDateTime;
//
//    @UpdateTimestamp
//    private LocalDateTime updateDateTime;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
//        Account account = (Account) o;
//        return id != null && Objects.equals(id, account.id);
//    }
//
//    @Override
//    public int hashCode() {
//        return getClass().hashCode();
//    }
//}
