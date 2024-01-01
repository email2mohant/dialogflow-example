package org.example;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.StreamingResponseHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.store.embedding.opensearch.OpenSearchEmbeddingStore;

import java.util.List;
import java.util.Map;
import java.util.Scanner;


import static dev.langchain4j.internal.Utils.randomUUID;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;


public class LangChainDemoWithLlama2 {
    private static final String llama2 = "http://localhost:11434";
    private static final String modelName = "llama2";

    private static final String vector_db = "http://localhost:12000";
    private static final int MAX_MESSAGES_TO_MEMORIZE_IN_CHAT_SESSION = 100;

    public static void main(String[] args) {
        StreamingChatLanguageModel model = initializeStreamingLLM();
        String documentName = "/kotak-mahindra-bank-limited-FY22-23.pdf";
        Document document = readDocument(documentName);
        understandDocument(document);
        beginChat(model);
    }

    private static Document readDocument(String documentName) {
        String filePath = LangChainDemoWithLlama2.class.getResource(documentName).getPath();
        out.println("Reading Document...");
        Document document = dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument(filePath, new ApachePdfBoxDocumentParser());
        return document;
    }

    private static void understandDocument(Document document) {
        out.println("Understanding the Document...");
        EmbeddingModel embeddingModel = getEmbeddingModel();
        EmbeddingStore<TextSegment> embeddingStore = getOpenSearchEmbeddingStore();
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(300, 20))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(document);
    }

    private static EmbeddingModel getEmbeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    private static EmbeddingModel getOllamaEmbeddingModel() {
        return OllamaEmbeddingModel.builder().baseUrl(llama2).modelName(modelName).build();
    }


    private static void beginChat(StreamingChatLanguageModel model) {

        out.println("\nI am ready to answer your questions now:");
        EmbeddingStoreRetriever retriever = EmbeddingStoreRetriever.from(getOpenSearchEmbeddingStore(), getEmbeddingModel());
        Scanner scanner = new Scanner(System.in);
        PromptTemplate promptTemplate = getPromptTemplate();
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(MAX_MESSAGES_TO_MEMORIZE_IN_CHAT_SESSION);
        while (true) {
            out.println();
            out.print("You: ");
            String prompt = scanner.nextLine();
            if (prompt != null) {
                prompt = prompt.trim();
                if (!prompt.isBlank()) {
                    List<TextSegment> relevantSegments = retriever.findRelevant(prompt);
                    UserMessage userMessage = promptTemplate.apply(Map.of("question", prompt, "information", format(relevantSegments))).toUserMessage();
                    chatMemory.add(userMessage);
                    submitPromptAndStreamResponse(model, chatMemory.messages());
                }
            }
        }
    }


    private static StreamingChatLanguageModel initializeStreamingLLM() {
        StreamingChatLanguageModel model = OllamaStreamingChatModel.builder()
                .baseUrl(llama2)
                .modelName(modelName)
                .temperature(0.0)
                .build();
        return model;
    }

    private static PromptTemplate getPromptTemplate() {
        return PromptTemplate.from(
                "Answer the following question to the best of your ability: {{question}}\n" +
                        "\n" +
                        "Base your answer on the following information:\n" +
                        "{{information}}");
    }

    private static String format(List<TextSegment> relevantSegments) {
        return relevantSegments.stream()
                .map(TextSegment::text)
                .map(segment -> "..." + segment + "...")
                .collect(joining("\n\n"));
    }


    private static void submitPromptAndStreamResponse(StreamingChatLanguageModel model, List<ChatMessage> userMessages) {
        StreamingResponseDelegate streamingResponseDelegate = new StreamingResponseDelegate();
        model.generate(userMessages, streamingResponseDelegate);
        while (streamingResponseDelegate.isStreaming()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static EmbeddingStore<TextSegment> getOpenSearchEmbeddingStore() {
        return OpenSearchEmbeddingStore.builder()
                .serverUrl(vector_db)
                .build();
    }


    private static ChromaEmbeddingStore getChromaEmbeddingStore() {
        return ChromaEmbeddingStore.builder()
                .baseUrl(vector_db)
                .collectionName(randomUUID())
                .build();
    }

    private static class StreamingResponseDelegate implements StreamingResponseHandler {
        boolean streaming;


        int num_tokens = 0;
        StreamingResponseDelegate() {
            out.print("Bot: ");
            streaming = true;
        }

        @Override
        public void onNext(String token) {
            ++num_tokens;
            out.print(token);
        }

        @Override
        public void onComplete(Response response) {
            streaming = false;
            out.println();
        }

        @Override
        public void onError(Throwable error) {
            streaming = false;
            System.err.println("Total number of tokens responded "+ num_tokens);
            System.err.println("Something went wrong: " + error.getMessage());
        }

        public boolean isStreaming() {
            return streaming;
        }
    }
}
