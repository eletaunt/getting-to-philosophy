package philosophy.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

import org.springframework.web.bind.annotation.CrossOrigin;
import philosophy.api.models.PathResponseNode;
import philosophy.data.models.Path;
import philosophy.data.models.PathNode;
import philosophy.api.models.PathResponse;
import org.hibernate.Session;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import philosophy.Application;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class PhilosophyController {

    @RequestMapping(value = "/findPhilosophy", method = POST)
    public ResponseEntity<PathResponse> findPhilosophy(@RequestParam(value="url") String url) {
        try {
            url = url.trim();
            if (!isValidWikiUrl(url)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }

            String nextUrl = new String(url);
            HashSet<String> trackedUrls = new HashSet<>();
            ArrayList<PathResponseNode> path = new ArrayList<>();
            int hops = 0;

            Session session = Application.sessionFactory.openSession();
            session.beginTransaction();
            do {
                if (trackedUrls.contains(nextUrl)) {
                    // Found loop
                    session.getTransaction().commit();
                    return ResponseEntity.ok(new PathResponse(hops,
                            "Found loop",
                            false,
                            path));
                } else {
                    Document doc = getWikiPage(nextUrl);
                    if (doc == null) {
                        session.close();
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
                    }
                    String title = findTitle(doc);
                    trackedUrls.add(nextUrl);
                    path.add(new PathResponseNode(title, nextUrl));
                    // store path in database
                    savePathNode(session, title, nextUrl);
                    // find next URL in doc
                    // set next URL
                    if (isPhilosophyWiki(doc.baseUri())) {
                        savePath(session, url, hops);
                        session.getTransaction().commit();
                        session.close();
                        return ResponseEntity.ok(new PathResponse(hops,
                                "Found philosophy",
                                true,
                                path));
                    } else {
                        hops++;
                        nextUrl = findNextUrl(doc);
                    }
                }
            } while (nextUrl != null);
            session.close();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception ex) {
            System.out.println(ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Checks if given URL is valid Wikipedia URL
     * @param url URL to check
     * @return boolean
     */
    private boolean isValidWikiUrl(String url) {
        String urlCopy = url.trim();
        return urlCopy.startsWith("https://en.wikipedia.org/wiki")
            || urlCopy.startsWith("http://en.wikipedia.org/wiki");
    }

    /**
     * Checks if given URL is Philosophy Wikipedia URL
     * @param url URL to check
     * @return boolean
     */
    private boolean isPhilosophyWiki(String url) {
        return url.endsWith("en.wikipedia.org/wiki/Philosophy");
    }

    /**
     * Fetches Wikipedia page at given URL
     * @param url Wikipedia URL
     * @return document or null
     */
    private Document getWikiPage(String url) {
        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private PathNode savePathNode(Session session, String title, String url) {
        PathNode pathNode = new PathNode(title, url);
        session.save(pathNode);
        return pathNode;
    }

    private Path savePath(Session session, String url, int hops) {
        Path path = new Path(url, hops);
        session.save(path);
        return path;
    }

    /**
     * Finds next valid Wikipedia URL in given Wikipedia document
     * @param doc Current Wikipedia document
     * @return URL of next valid Wikipedia URL in document
     */
    private String findNextUrl(Document doc) {
        doc.getElementsByTag("i").remove();
        Elements paragraphs = doc.select("#mw-content-text > .mw-parser-output > p");
        for (Element paragraph : paragraphs) {
            Elements links = paragraph.select("a[href^=/wiki/]");
            for (Element link : links) {
                if (!areEqualURLs(link.absUrl("href"), doc.baseUri())
                        && !isParenthesized(paragraph, link)) {
                    return link.absUrl("href");
                }
            }
        }
        return null;
    }

    /**
     * Checks if two URLs are equal
     * @param url1 first URL to compare
     * @param url2 second URL to compare
     * @return boolean
     */
    private boolean areEqualURLs(String url1, String url2) {
        if (url1 == null && url2 == null) {
            return true;
        } else if (url1 == null || url2 == null) {
            return false;
        } else {
            url1 = url1.trim().toLowerCase()
                    .replaceFirst("https://", "")
                    .replaceFirst("http://", "");
            url2 = url2.trim().toLowerCase()
                    .replaceFirst("https://", "")
                    .replaceFirst("http://", "");;
            return url1.equals(url2);
        }
    }

    /**
     * Checks if link is enclosed in parentheses within paragraph
     * @param paragraph paragraph element containing link
     * @param link link element
     * @return boolean
     */
    private boolean isParenthesized(Element paragraph, Element link) {
        String precedingText = paragraph.html().split(Pattern.quote(link.outerHtml()), 2)[0];
        int openCount = StringUtils.countOccurrencesOf(precedingText, "(");
        int closeCount = StringUtils.countOccurrencesOf(precedingText, ")");
        return openCount != closeCount;
    }

    private String findTitle(Document doc) {
        return doc.select("h1#firstHeading").text();
    }
}
