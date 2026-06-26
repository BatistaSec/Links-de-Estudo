package com.jozo.multitenancy2.bookmark;

import com.jozo.multitenancy2.Scaper.ScraperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController  {

    private final BookmarkService service;
    private  final ScraperService scraperService;


    public BookmarkController(BookmarkService service, ScraperService scraperService) {
        this.service = service;
        this.scraperService = scraperService;
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
    public ResponseEntity<Bookmark> save(@RequestBody Map<String, String> payload) {
        String url = payload.get("url");
        Bookmark bookmark = scraperService.fetchBookmark(url);
        Bookmark saved = service.save(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> update(@PathVariable long id, @RequestBody Bookmark bookmark) {
        Bookmark update = service.update(id,bookmark);
        return ResponseEntity.ok(update);

    }

}
