package com.jozo.multitenancy2.bookmark;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController  {

    private final BookmarkService service;


    public BookmarkController(BookmarkService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Bookmark>> getAllBookmarks() {
        List<Bookmark> bookmarks = service.findAll();
        return ResponseEntity.ok(bookmarks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmarkById(@PathVariable Long id) {
        try {
            Bookmark bookmark = service.findById(id);
            return ResponseEntity.ok(bookmark);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping
    public ResponseEntity<Bookmark> save(@RequestBody Bookmark bookmark) {
        Bookmark savedBookmark = service.save(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBookmark);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping
    public ResponseEntity<Bookmark> update(@PathVariable long id, @RequestBody Bookmark bookmark) {
        Bookmark update = service.update(id,bookmark);
        return ResponseEntity.ok(update);

    }
}
