package parent.controller;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import parent.domain.Category;
import parent.domain.Vendor;
import parent.repositories.VendorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    private final VendorRepository vendorRepository ;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping("api/v1/vendors")
    public Flux<Vendor> getAllVendors(){
        return vendorRepository.findAll();
    }

    @GetMapping("api/v1/vendors/{id}")
    public Mono<Vendor> getVendorById(@PathVariable(name = "id") String id){
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/addVendor")
    public Mono<Void> addVendor(@RequestBody Publisher<Vendor> vendorStream){
        return vendorRepository.saveAll(vendorStream).then();
    }

    @PatchMapping("/api/v1/updateVendor/{id}")
    public Mono<Vendor> updateVendorPatch(@PathVariable String id , @RequestBody Vendor vendor){
        Vendor vendorFound = vendorRepository.findById(id).block();
        if(!vendorFound.getFirstName().equalsIgnoreCase(vendor.getFirstName())){
            vendorFound.setFirstName(vendor.getFirstName());
            if(!vendorFound.getLastName().equalsIgnoreCase(vendor.getLastName())){
                vendorFound.setLastName(vendor.getLastName());
            }
            return vendorRepository.save(vendorFound);
        }
        return Mono.just(vendor);
    }

    @PutMapping("/api/v1/updateVendor/{id}")
    public Mono<Vendor> updateCategory(@PathVariable String id,@RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }
}
