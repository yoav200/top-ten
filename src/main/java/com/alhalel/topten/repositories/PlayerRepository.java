package com.alhalel.topten.repositories;

import com.alhalel.topten.enteties.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findPlayerByUniqueName(String uniqueName);

}
