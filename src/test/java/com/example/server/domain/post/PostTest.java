package com.example.server.domain.post;

import com.example.server.domain.post.Post;
import com.example.server.domain.post.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// TODO: Change to @DataJpaTest
@SpringBootTest
@Transactional
class PostTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    EntityManager entityManager;

    @Test
    void test() {
        Post save = postRepository.save(new Post("foo", "bar"));
        entityManager.clear();

        Post post = postRepository.findById(save.getId())
                .orElseThrow();
        System.out.println("post.getCreatedAt() = " + post.getCreatedAt());
        System.out.println("post.getLastModifiedAt() = " + post.getLastModifiedAt());
        System.out.println("post.getId() = " + post.getId());
        System.out.println("post.getTitle() = " + post.getTitle());
        System.out.println("post.getContent() = " + post.getContent());
    }

}