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
    public ResponseEntity<List<BookmarkResponse>> getAllBookmarks(Principal principal) {
        String email = getEmail(principal);
        List<BookmarkResponse> bookmarks = service.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(bookmarks);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkResponse> getBookmarkById(@PathVariable Long id, Principal principal) {
        try {
            String email = getEmail(principal);
            Bookmark bookmark = service.findById(id);
            return ResponseEntity.ok(toResponse(bookmark));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping
    public ResponseEntity<BookmarkResponse> save(@RequestBody Map<String, String> payload,
                                         Principal principal) {
        String email = getEmail(principal);
        String url = payload.get("url");
        Bookmark bookmark = scraperService.fetchBookmark(url);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder().email(email).senha("password").build()));
        bookmark.setUser(user);

        Bookmark saved = service.save(bookmark);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        String email = getEmail(principal);
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookmarkResponse> update(@PathVariable long id, @RequestBody Map<String, String> payload, Principal principal) {
        String email = getEmail(principal);
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl(payload.get("url"));
        bookmark.setTitle(payload.get("title"));
        Bookmark update = service.update(id, bookmark);
        return ResponseEntity.ok(toResponse(update));
    }

    private String getEmail(Principal principal) {
        // Para reativar o JWT e ler o usuário autenticado pelo token, use a linha comentada abaixo:
        return "teste@email.com";
        // return principal != null ? principal.getName() : "teste@email.com";
    }

    private BookmarkResponse toResponse(Bookmark bookmark) {
        return new BookmarkResponse(
                bookmark.getId(),
                bookmark.getUrl(),
                bookmark.getTitle(),
                bookmark.getDescription(),
                bookmark.getCreated(),
                bookmark.getUser() != null ? bookmark.getUser().getId() : null,
                bookmark.getUser() != null ? bookmark.getUser().getEmail() : null
        );
    }

}
