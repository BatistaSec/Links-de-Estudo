package com.jozo.multitenancy2.bookmark;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookmarkRepository bookmarkRepository;

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
        bookmark = bookmarkRepository.save(bookmark);

        mockMvc.perform(get("/api/bookmarks/" + bookmark.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Spring"))
                .andExpect(jsonPath("$.url").value("https://spring.io"));

    }

    @Test
    void deveCriarBookmarkComSucesso()throws Exception {
        String json =  "{\"url\":\"https://spring.io\"}";
        mockMvc.perform(post("/api/bookmarks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void deveExcluirBookmarkComSucesso()throws Exception {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("https://spring.io");
        bookmark.setTitle("Spring");
        bookmark = bookmarkRepository.save(bookmark);

        mockMvc.perform(delete("/api/bookmarks/" + bookmark.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    }

    @Test
    void deveAtualizarBookmarkComSucesso()throws Exception {
        Bookmark bookmark = new Bookmark();
        bookmark.setUrl("https://spring.io");
        bookmark.setTitle("Spring");
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