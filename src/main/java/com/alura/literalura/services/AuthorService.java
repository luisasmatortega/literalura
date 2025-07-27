package com.alura.literalura.services;


import com.alura.literalura.entities.AluraAuthor;
import com.alura.literalura.repositories.AuthorRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService
{
    private final AuthorRepo authorRepo;

    public AuthorService(AuthorRepo authorRepo) {
        this.authorRepo = authorRepo;
    }

    public AluraAuthor findOrSaveAuthor(AluraAuthor author) {
        if (author == null) return null;
        AluraAuthor existingAuthor = authorRepo.findAuthorByName(author.getName());
        return existingAuthor != null ? existingAuthor : authorRepo.save(author);
    }

    public List<AluraAuthor> getAllAuthors() {
        return authorRepo.findAll();
    }

    public List<AluraAuthor> getAliveAuthorsInYear(int year) {
        return authorRepo.getAliveAuthorsInAGivenYear(year);
    }
}
