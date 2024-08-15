package com.dailycodebuffer.budget_ai.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

@Configuration
public class Config {

    @Value("classpath:/Budget_Speech.txt")
    private Resource budget;


    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore
                    = new SimpleVectorStore(embeddingModel);

        File vectorStoreFile =
                new File("/Users/shabbir/Downloads/budget-ai/src/main/resources/vector_store.json");


        if(vectorStoreFile.exists()) {
            System.out.println("Loaded Vector Store File!");
            vectorStore.load(vectorStoreFile);
        } else {

            System.out.println("Create Vector File");
            TextReader textReader = new TextReader(budget);
            textReader.getCustomMetadata()
                    .put("filename", "Budget_Speech.txt");

            List<Document> documents = textReader.get();

            TextSplitter textSplitter = new TokenTextSplitter();

            List<Document> splitDocuments = textSplitter.apply(documents);

            vectorStore.add(documents);
            vectorStore.save(vectorStoreFile);
        }
        return vectorStore;
    }
}
