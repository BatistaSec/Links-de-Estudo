package com.jozo.multitenancy2.Scaper;

import com.jozo.multitenancy2.bookmark.Bookmark;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;


@Service
public class ScraperService {

    public Bookmark fetchBookmark(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(5000).get();

            Bookmark bookmark = new Bookmark();
            bookmark.setUrl(url);

            bookmark.setTitle(getMetaTag(doc, "og:title", doc.title()));
            bookmark.setDescription(getMetaTag(doc, "og:description", "Sem descrição"));
            bookmark.setUrl(getMetaTag(doc, "og:image", null));
            return bookmark;
        }catch (Exception e){
            throw new RuntimeException("Erro ao extrair metadados da URL: " + url);
        }
    }
    private String getMetaTag(Document doc, String property, String defaultValue){
        String content =  doc.select("meta[property=" + property + "]").attr("content");
        return (!content.isEmpty()) ? content : defaultValue;
    }
}
