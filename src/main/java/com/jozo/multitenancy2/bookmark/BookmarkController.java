package com.jozo.multitenancy2.bookmark;

import com.jozo.multitenancy2.Scaper.ScraperService;
import com.jozo.multitenancy2.user.User;
import com.jozo.multitenancy2.user.UserRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkController  {

    private final BookmarkService service;
    private final ScraperService scraperService;
    private final UserRepository userRepository;

    public BookmarkController(BookmarkService service, ScraperService scraperService, UserRepository userRepository) {
        this.service = service;
        this.scraperService = scraperService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Bookmark>> getAllBookmarks(Principal principal) {
        String email = principal.getName();
        List<Bookmark> bookmarks = service.findAll();
        return ResponseEntity.ok(bookmarks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Bookmark> getBookmarkById(@PathVariable Long id, Principal principal) {
        try {
            String email = principal.getName();
            Bookmark bookmark = service.findById(id);
            return ResponseEntity.ok(bookmark);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping
    public ResponseEntity<Bookmark> save(@RequestBody Map<String, String> payload,
                                         Principal principal) {
        String email = principal.getName();
        String url = payload.get("url");
        Bookmark bookmark = scraperService.fetchBookmark(url);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario nao encontrado"));
        bookmark.setUser(user);

        Bookmark saved = service.save(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        String email = principal.getName();
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Bookmark> update(@PathVariable long id, @RequestBody Bookmark bookmark, Principal principal) {
        String email = principal.getName();
        Bookmark update = service.update(id,bookmark);
        return ResponseEntity.ok(update);

    }

}
