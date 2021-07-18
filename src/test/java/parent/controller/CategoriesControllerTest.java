package parent.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import parent.domain.Category;
import parent.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class CategoriesControllerTest {

    WebTestClient webTestClient;
    CategoriesController categoriesController;
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp()throws Exception{
        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoriesController = new CategoriesController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoriesController).build();
    }

    @Test
    void list() {
        BDDMockito.given(categoryRepository.findAll()).willReturn(Flux.just(
                Category.builder().description("blabla").build(),
                Category.builder().description("blabla2").build()
                ));

        webTestClient.get().uri("/api/v1/categories/")
                .exchange().expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    void getCategoryById() {
        BDDMockito.given(categoryRepository.findById("id"))
                .willReturn(Mono.just(Category.builder().id("fekflkef").build()));
        webTestClient.get().uri("/api/v1/categories/if")
                .exchange().expectBody(Category.class);
    }

    @Test
    void addCategoryTest(){
        BDDMockito.given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().id("fekflkef").build()));

        Mono<Category> categoryMono = Mono.just(Category.builder().description("hicham").build());

        webTestClient.post().uri("/api/v1/addCategory")
                .body(categoryMono,Category.class)
                .exchange().expectStatus().isCreated();


    }
}