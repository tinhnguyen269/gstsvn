package com.example.serviceapp.sitemap;

import com.example.serviceapp.admin.post.repository.ADPostRepository;
import com.example.serviceapp.admin.project_image.repository.ADProjectRepository;
import com.example.serviceapp.admin.project_image.repository.ImageProjectRepository;
import com.example.serviceapp.common.entity.ImageProject;
import com.example.serviceapp.common.entity.ProjectImage;
import com.example.serviceapp.common.entity.Services;
import com.example.serviceapp.common.entity.Post;
import com.example.serviceapp.customer.post.repositoty.PostRepository;
import com.example.serviceapp.customer.service.repository.ServiceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class SitemapController {

    private final ServiceRepository serviceRepository;
    private final PostRepository postRepository;
    private final ADProjectRepository imageProjectRepository;

    @Value("${app.host}")
    private String baseUrl;

    public SitemapController(ServiceRepository serviceRepository, PostRepository postRepository, ADProjectRepository imageProjectRepository) {
        this.serviceRepository = serviceRepository;
        this.postRepository = postRepository;
        this.imageProjectRepository = imageProjectRepository;
    }

    // ------------------ 🧭 Sitemap Index ------------------
    @GetMapping(value = "/sitemap_index.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String sitemapIndex() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <sitemapindex xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                    <sitemap>
                        <loc>""" + baseUrl + "/sitemap-static.xml</loc>\n" +
                "        <lastmod>" + today + "</lastmod>\n" +
                "    </sitemap>\n" +
                "    <sitemap>\n" +
                "        <loc>" + baseUrl + "/sitemap-services.xml</loc>\n" +
                "        <lastmod>" + today + "</lastmod>\n" +
                "    </sitemap>\n" +
                "    <sitemap>\n" +
                "        <loc>" + baseUrl + "/sitemap-posts.xml</loc>\n" +
                "        <lastmod>" + today + "</lastmod>\n" +
                "    </sitemap>\n" +
                "    <sitemap>\n" +
                "        <loc>" + baseUrl + "/sitemap-projects.xml</loc>\n" +
                "        <lastmod>" + today + "</lastmod>\n" +
                "    </sitemap>\n" +
                "</sitemapindex>";
    }

    // ------------------ 🏠 Sitemap trang tĩnh ------------------
    @GetMapping(value = "/sitemap-static.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String staticPagesSitemap() {
        StringBuilder xml = new StringBuilder();
        xml.append("""
                <?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                """);

        LocalDate today = LocalDate.now();

        xml.append(createUrl(baseUrl + "/", today));
        xml.append(createUrl(baseUrl + "/gioi-thieu", today));

        xml.append("</urlset>");
        return xml.toString();
    }

    // ------------------ 🧩 Sitemap dịch vụ ------------------
    @GetMapping(value = "/sitemap-services.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String servicesSitemap() {
        List<Services> services = serviceRepository.findAll();
        StringBuilder xml = new StringBuilder();
        xml.append("""
                <?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                """);

        for (Services s : services) {
            xml.append(createUrl(baseUrl + "/dich-vu/" + s.getSlug(), LocalDate.now()));
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    // ------------------ 📰 Sitemap bài viết ------------------
    @GetMapping(value = "/sitemap-posts.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String postsSitemap() {
        List<Post> posts = postRepository.findAll();
        StringBuilder xml = new StringBuilder();
        xml.append("""
                <?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                """);

        for (Post p : posts) {
            LocalDate updatedAt = p.getUpdateAt() != null ? LocalDate.from(p.getUpdateAt()) : LocalDate.now();
            xml.append(createUrl(baseUrl + "/tin-tuc/" + p.getSlug(), updatedAt));
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    // ------------------ 📰 Sitemap hình ảnh dự án ------------------
    @GetMapping(value = "/sitemap-projects.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public String imageProjectSitemap() {
        List<ProjectImage> imageProjects = imageProjectRepository.findAllByOrderByCreateAtDesc();
        StringBuilder xml = new StringBuilder();
        xml.append("""
                <?xml version="1.0" encoding="UTF-8"?>
                <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
                """);

        for (ProjectImage i : imageProjects) {
            LocalDate updatedAt = i.getUpdateAt() != null ? LocalDate.from(i.getUpdateAt()) : LocalDate.now();
            xml.append(createUrl(baseUrl + "/du-an/" + i.getSlug(), updatedAt));
        }

        xml.append("</urlset>");
        return xml.toString();
    }

    // ------------------ ⚒️ Hàm tạo XML URL node ------------------
    private String createUrl(String loc, LocalDate lastmod) {
        return String.format("""
                <url>
                    <loc>%s</loc>
                    <lastmod>%s</lastmod>
                    <changefreq>weekly</changefreq>
                    <priority>0.8</priority>
                </url>
                """, loc, lastmod.format(DateTimeFormatter.ISO_DATE));
    }
}
