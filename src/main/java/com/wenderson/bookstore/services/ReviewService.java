package com.wenderson.bookstore.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private static final int MAX_REVIEW_LENGTH = 500;

    private final ChatClient chatClient;

    public ReviewService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public String generateReview(String title) {
        try {
            var prompt = """
                    Escreva um resumo objetivo e direto do livro "%s".
                    Não dê opinião sobre o livro.
                    Não repita o título do livro na resposta.
                    Responda em português.
                    A resposta deve ter no máximo %d caracteres.
                    """.formatted(title, MAX_REVIEW_LENGTH);

            var review = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

            if (review != null && review.length() > MAX_REVIEW_LENGTH) {
                review = review.substring(0, MAX_REVIEW_LENGTH);
            }

            return review;
        } catch (Exception e) {
            System.out.println("Erro ao gerar o review do livro. Esse erro precisa ser tratado: " + e.getMessage());
            // Aqui caberia tratamento adequado: envio para fila de erro, retentativa ou circuit breaker.
            return null;
        }
    }
}
