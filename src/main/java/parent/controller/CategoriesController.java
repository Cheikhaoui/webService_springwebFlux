package parent.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import parent.domain.Category;
import parent.repositories.CategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoriesController {

    private final CategoryRepository categoryRepository;


    public CategoriesController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/api/v1/categories")
    public Flux<Category> list(){
        return categoryRepository.findAll();
    }

    @GetMapping("/api/v1/categories/{id}")
    public Mono<Category> getCategoryById(@PathVariable String id){
        return categoryRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/addCategory")
    public Mono<Void> addCategory(@RequestBody Publisher<Category> categoryStream){
       return categoryRepository.saveAll(categoryStream).then();
    }

    @PutMapping("/api/v1/updateCategory/{id}")
    public Mono<Category> updateCategory(@PathVariable String id,@RequestBody Category category){
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/api/v1/updateCategory/{id}")
    public Mono<Category> updateCategoryPatch(@PathVariable String id ,@RequestBody Category category){
        /*
        Category categoryFound = categoryRepository.findById(id).block();
        if(!categoryFound.getDescription().equalsIgnoreCase(category.getDescription())){
            categoryFound.setDescription(category.getDescription());
            return categoryRepository.save(categoryFound);
        }
        return Mono.just(category);
*/
        return categoryRepository.findById(id).flatMap(categoryFound->{
            Category newCategory = new Category(categoryFound.getDescription());
            newCategory.setId(id);
            if(category.getDescription() != null){
                newCategory.setDescription(category.getDescription());
            }
            return categoryRepository.save(newCategory);
        });
    }

}
