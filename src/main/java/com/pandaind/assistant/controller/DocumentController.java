package com.pandaind.assistant.controller;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/document")
public class DocumentController {

    private final EmbeddingStoreIngestor embeddingStoreIngestor;

    public DocumentController(EmbeddingStoreIngestor embeddingStoreIngestor) {
        this.embeddingStoreIngestor = embeddingStoreIngestor;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> handleFileUploadUsingCurl(
            @RequestParam("file") MultipartFile file) throws IOException {


        byte[] bytes = file.getBytes();
        Path path = Paths.get(file.getOriginalFilename());
        Files.write(path, bytes);

        Document document = FileSystemDocumentLoader.loadDocument(path.getFileName(),
                new ApachePdfBoxDocumentParser());
        embeddingStoreIngestor.ingest(document);


        var map = new HashMap<String, String>();
        map.put("fileName", file.getOriginalFilename());
        map.put("fileSize", String.valueOf(file.getSize()));
        map.put("fileContentType", file.getContentType());
        map.put("message", "File upload done");

        return ResponseEntity.ok(map);
    }
}
