package com.dailycodebuffer.budget_ai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BudgetController {

    private ChatClient chatClient;

    public BudgetController(ChatClient.Builder builder,
                            VectorStore vectorStore) {
        this.chatClient = builder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore,
                        SearchRequest.defaults()))
                .build();
    }

    @GetMapping("/budget")
    public String budgetQandA(@RequestParam(value = "message",
    defaultValue = "What is the Highlight of the Budget 2024-25")
                              String message) {
            return chatClient
                    .prompt()
                    .user(message)
                    .call()
                    .content();
    }
}
