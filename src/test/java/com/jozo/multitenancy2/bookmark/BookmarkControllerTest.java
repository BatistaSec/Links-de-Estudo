package com.jozo.multitenancy2.bookmark;

import com.jozo.multitenancy2.user.User;
import com.jozo.multitenancy2.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "teste@email.com")
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserRepository userRepository;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private com.jozo.multitenancy2.Scaper.ScraperService scraperService;

    private User testUser;

    @BeforeEach
    void setUp() {
        bookmarkRepository.deleteAll();
        userRepository.deleteAll();

        testUser = User.builder()
                .email("teste@email.com")
                .senha("password")
                .build();
        testUser = userRepository.save(testUser);
    }

    @Test
    void deveRetornarListaBookmarks() throws Exception {
        mockMvc.perform(get("/api/bookmarks")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarListaBookmarksById() throws Exception {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("https://spring.io");
        bookmark.setTitle("Spring");
        bookmark.setUser(testUser);
        bookmark = bookmarkRepository.save(bookmark);

        mockMvc.perform(get("/api/bookmarks/" + bookmark.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring"))
                .andExpect(jsonPath("$.url").value("https://spring.io"));
    }

    @Test
    void deveCriarBookmarkComSucesso() throws Exception {
        Bookmark mockBookmark = new Bookmark();
        mockBookmark.setUrl("https://spring.io");
        mockBookmark.setTitle("Spring");
        mockBookmark.setUser(testUser);
        org.mockito.Mockito.when(scraperService.fetchBookmark("https://spring.io")).thenReturn(mockBookmark);

        String json = "{\"url\":\"https://spring.io\"}";
        mockMvc.perform(post("/api/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void deveExcluirBookmarkComSucesso() throws Exception {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("https://spring.io");
        bookmark.setTitle("Spring");
        bookmark.setUser(testUser);
        bookmark = bookmarkRepository.save(bookmark);

        mockMvc.perform(delete("/api/bookmarks/" + bookmark.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveAtualizarBookmarkComSucesso() throws Exception {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("https://spring.io");
        bookmark.setTitle("Spring");
        bookmark.setUser(testUser);
        bookmark = bookmarkRepository.save(bookmark);

        String json = "{\"url\":\"https://spring.io/projects\", \"title\":\"Spring Updated\"}";

        mockMvc.perform(put("/api/bookmarks/" + bookmark.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring Updated"))
                .andExpect(jsonPath("$.url").value("https://spring.io/projects"));
    }
}