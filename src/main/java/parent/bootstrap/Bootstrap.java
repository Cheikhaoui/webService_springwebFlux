package parent.bootstrap;

import parent.domain.Category;
import parent.domain.Vendor;
import parent.repositories.CategoryRepository;
import parent.repositories.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final VendorRepository vendorRepository;

    public Bootstrap(CategoryRepository categoryRepository, VendorRepository vendorRepository) {
        this.categoryRepository = categoryRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(categoryRepository.count().block() == 0){
            //load data
            System.out.println("#### Loading data #####");
            categoryRepository.save(Category.builder().description("Fruits").build()).block();
            categoryRepository.save(Category.builder().description("Nuts").build()).block();
            categoryRepository.save(Category.builder().description("Breads").build()).block();
            categoryRepository.save(Category.builder().description("Meals").build()).block();
            categoryRepository.save(Category.builder().description("Eags").build()).block();
            System.out.println("Loaded categories :"+ categoryRepository.count().block());

            vendorRepository.save(Vendor.builder().firstName("hicham").lastName("cheikhaoui").build()).block();
            vendorRepository.save(Vendor.builder().firstName("zineb").lastName("bousbaa").build()).block();
            vendorRepository.save(Vendor.builder().firstName("lahssen").lastName("azafadi").build()).block();
            System.out.println("Loaded vendors :"+ vendorRepository.count().block());




        }
    }
}
