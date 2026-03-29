package stackoverflow.demo;

import stackoverflow.dto.Filter;
import stackoverflow.enums.VoteType;
import stackoverflow.model.*;
import stackoverflow.repository.InMemoryQuestionRepository;
import stackoverflow.repository.InMemoryUserRepository;
import stackoverflow.service.QuestionService;
import stackoverflow.service.StackOverflowService;
import stackoverflow.service.UserService;

import java.util.List;
import java.util.Set;

public class MainDemo {
    public static void main(String[] args) {
        System.out.println("--- Starting Stack Overflow LLD Demo ---");

        // 1. Initialize System
        UserService userService = new UserService(new InMemoryUserRepository());
        QuestionService questionService = new QuestionService(new InMemoryQuestionRepository());
        StackOverflowService system = new StackOverflowService(userService, questionService);

        // 2. Create Users
        User alice = system.createUser("Alice_Dev");
        User bob = system.createUser("Bob_Coder");

        // 3. Alice asks a question
        Tag javaTag = new Tag("java");
        Tag concurrencyTag = new Tag("concurrency");
        Tag hackingTag = new Tag("hacking");
        Question q1 = system.askQuestion(
                alice.getId(),
                "How does ConcurrentHashMap work?",
                "I am confused about segment locking vs node locking in Java 8.",
                Set.of(javaTag, concurrencyTag)
        );

        // Bob Asking Question
        Question q2 = system.askQuestion(
                bob.getId(),
                "Can I hack NASA using HTML",
                "Can I do it without using CSS?",
                Set.of(hackingTag)
        );

        // 4. Bob answers the question
        Answer a1 = system.answerQuestion(
                bob.getId(),
                q1.getId(),
                "In Java 8, ConcurrentHashMap uses CAS operations and synchronized blocks on the first node of the bin, abandoning segment locking."
        );


        // Printing Answer
        System.out.println(a1);

        // 5. Simulate Thread-Safe Voting
        System.out.println("Simulating concurrent upvotes...");

        // We will simulate 100 threads upvoting Bob's answer simultaneously
        Runnable upvoteTask = () -> {
            system.voteAnswer(q1.getId(), a1.getId(), VoteType.UPVOTE);// +1 upvote
        };

        Thread[] threads = new Thread[100];
        for (int i = 0; i < 100; i++) {
            threads[i] = new Thread(upvoteTask);
            threads[i].start();
        }

        // Wait for all threads to finish
        for (int i = 0; i < 100; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 6. Output Results
        System.out.println("\n--- Results ---");
        System.out.println(a1);
        System.out.println("Question: " + q1.getTitle());
        System.out.println("Answer by " + alice + ": " + a1.getContent());
        System.out.println("Answer Votes (Should be 100): " + a1.getVoteCount());
        System.out.println("Bob's Reputation (Should be 100 * 10 = 1000): " + bob.getReputation());

        // 7. Test Search
        List<Question> searchResults = system.searchQuestion(new Filter.Builder().build());
        System.out.println("\nSearch results for 'concurrency': " + searchResults.size() + " question(s) found.");
    }
}
