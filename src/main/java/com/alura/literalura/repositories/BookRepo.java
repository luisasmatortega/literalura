package com.alura.literalura.repositories;


import com.alura.literalura.entities.AluraBook;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<AluraBook, Long>
{
    List<AluraBook> findBooksByLanguage(String language);

    @EntityGraph(attributePaths = {"author"})
    @Query("SELECT b FROM AluraBook b WHERE LOWER(b.title) LIKE LOWER(concat('%', :title,'%'))")
    List<AluraBook> findByTitleContainingIgnoreCase(@Param("title") String title);

    @Query("SELECT b FROM AluraBook b ORDER BY b.numberOfDownloads DESC LIMIT 10")
    List<AluraBook> findTop10ByOrderByNumberOfDownloadsDesc();
}
