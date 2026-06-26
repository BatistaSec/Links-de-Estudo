package com.jozo.multitenancy2.bookmark;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    public BookmarkService(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    public List<Bookmark> findAll() {
        return bookmarkRepository.findAll();
    }

    public Bookmark save(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    public void excluir (Long id) {
         bookmarkRepository.deleteById(id);
    }

    public Bookmark findById(Long id) {
        return bookmarkRepository.findById(id).orElseThrow(()->new RuntimeException("Bookmark nao encontrado com o id " + id));
    }

    public Bookmark update(long id, Bookmark bookmark) {
        Bookmark existing = findById(id);
        existing.setUrl(bookmark.getUrl());
        existing.setTitle(bookmark.getTitle());
        return bookmarkRepository.save(existing);
    }
}
