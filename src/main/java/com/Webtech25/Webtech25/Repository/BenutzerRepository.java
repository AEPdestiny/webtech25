package com.Webtech25.Webtech25.Repository;

import com.Webtech25.Webtech25.Entity.Benutzer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BenutzerRepository  extends JpaRepository<Benutzer, Long>{

    Optional<Benutzer> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(b) FROM Benutzer b WHERE b.rolle = 'ADMIN'")
    long zaehleAdmins();

    @Query("SELECT b FROM Benutzer b WHERE b.vorname LIKE %:name% OR b.nachname LIKE %:name%")
    List<Benutzer> findeNachName(String name);
}