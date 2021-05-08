package dev.backend.unitalk.category;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.http.ResponseEntity;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/group")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController
{
    private final CategoryControllerService categoryControllerService;

    public CategoryController(CategoryControllerService categoryControllerService) {
        this.categoryControllerService = categoryControllerService;
    }

    @GetMapping("/{idGroup}/category/all")
    public CollectionModel<EntityModel<Category>> all(@PathVariable Long idGroup) {
        List<EntityModel<Category>> categories = categoryControllerService.all(idGroup);
        return CollectionModel.of(categories, linkTo(methodOn(CategoryController.class).all(idGroup)).withSelfRel());
    }

    @GetMapping("/{idGroup}/category/{idCategory}")
    public EntityModel<Category> one(@PathVariable Long idGroup,@PathVariable Long idCategory) {
        var category = categoryControllerService.one(idGroup, idCategory);
        return EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategoryId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories"));
    }

    @PostMapping("/{idGroup}/category")
    public ResponseEntity<Category> newCategory(@Valid @RequestBody Category newCategory, @PathVariable Long idGroup) {
        return categoryControllerService.newCategory(newCategory, idGroup);
    }

    @PutMapping("/{idGroup}/category/{idCategory}")
    public ResponseEntity<EntityModel<Category>> replaceCategory(@Valid @RequestBody Category newCategory,
                                      @PathVariable Long idGroup,
                                      @PathVariable Long idCategory) {

        EntityModel<Category> entityModel = categoryControllerService
                .replaceCategory(newCategory, idGroup, idCategory);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{idGroup}/category/{idCategory}")
    public ResponseEntity<HttpStatus> deleteOne(@PathVariable Long idGroup, @PathVariable Long idCategory) {
        return categoryControllerService.deleteOne(idCategory);
    }

    @DeleteMapping("/{idGroup}/category/")
    public ResponseEntity<HttpStatus> deleteAll(@PathVariable Long idGroup) {
        return categoryControllerService.deleteAll(idGroup);
    }
}