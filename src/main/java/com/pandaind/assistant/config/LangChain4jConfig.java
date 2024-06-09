package com.pandaind.assistant.config;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChain4jConfig {

    private final InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    private final GcpProperties gcpProperties;

    private final HuggingFaceProperties huggingFaceProperties;

    private final OllamaProperties ollamaProperties;

    @Autowired
    public LangChain4jConfig(GcpProperties gcpProperties, HuggingFaceProperties huggingFaceProperties,
                             OllamaProperties ollamaProperties) {
        this.gcpProperties = gcpProperties;
        this.huggingFaceProperties = huggingFaceProperties;
        this.ollamaProperties = ollamaProperties;
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }


    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor() {
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 0))
                .embeddingModel(embeddingModel())
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public ConversationalRetrievalChain conversationalRetrievalChain() {
        return ConversationalRetrievalChain.builder()
                /*.chatLanguageModel(HuggingFaceChatModel.builder()
                        .accessToken(huggingFaceProperties.getToken())
                        .modelId("gpt2")
                        .maxNewTokens(250)
                        .waitForModel(true)
                        .build())*/
                .chatLanguageModel(OllamaChatModel.builder()
                        .modelName(ollamaProperties.getModelName())
                        .baseUrl(ollamaProperties.getBaseUrl())
                        .build())
                /*.chatLanguageModel(VertexAiGeminiChatModel.builder()
                        .location(gcpProperties.getLocation())
                        .modelName(gcpProperties.getModelName())
                        .project(gcpProperties.getProject())
                        .build())*/
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel())
                        .build())
                .build();
    }
}
