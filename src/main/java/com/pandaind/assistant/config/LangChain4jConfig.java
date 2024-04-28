package com.pandaind.assistant.config;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.vertexai.VertexAiGeminiChatModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class LangChain4jConfig {

    private final InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

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
    public ConversationalRetrievalChain conversationalRetrievalChain() throws IOException {
        return ConversationalRetrievalChain.builder()
                /* .chatLanguageModel(LocalAiChatModel.builder()
                         .baseUrl("http://localhost:9090")
                         .modelName("gpt-4-vision-preview")
                         .maxTokens(10)
                         .maxRetries(3)
                         .logRequests(true)
                         .logResponses(true)
                         .build())*/
                /*.chatLanguageModel(HuggingFaceChatModel.builder()
                        .accessToken(Constants.HUGGING_FACE_TOKEN)
                        .modelId("gpt2")
                        .maxNewTokens(250)
                        .waitForModel(true)
                        .build())*/
                /*.chatLanguageModel(OllamaChatModel.builder()
                        .modelName("llama2")
                        .baseUrl("http://localhost:11434")
                        .build())*/
                .chatLanguageModel(VertexAiGeminiChatModel.builder()
                        .location(Constants.GCP_LOCATION)
                        .modelName("gemini-experimental")
                        .project(Constants.GCP_PROJECT)
                        .build())
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .contentRetriever(EmbeddingStoreContentRetriever.builder()
                        .embeddingStore(embeddingStore)
                        .embeddingModel(embeddingModel())
                        .build())
                .build();
    }
}
