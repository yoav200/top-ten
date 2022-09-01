package com.alhalel.topten.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findPlayerByUniqueName(String uniqueName);

    List<Player> findByUniqueNameIn(List<String> names);

    @Query("SELECT uniqueName FROM Player player")
    List<String> getPlayersUniqueNames();
}
