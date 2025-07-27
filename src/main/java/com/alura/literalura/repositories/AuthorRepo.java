package com.alura.literalura.repositories;


import com.alura.literalura.entities.AluraAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepo extends JpaRepository<AluraAuthor, Long>
{
    AluraAuthor findAuthorByName(String name);

    @Query("SELECT a FROM AluraAuthor a WHERE a.birthYear <= :year AND a.deathYear >= :year")
    List<AluraAuthor> getAliveAuthorsInAGivenYear(@Param("year") Integer year);


}
