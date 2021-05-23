package dev.backend.unitalk.category;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.group.GroupRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryControllerService{
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;
    private static final String NOT_FOUND_GROUP = "Not found group with id = ";
    private static final String NOT_FOUND_CATEGORY = "Not found category with id = ";

    public CategoryControllerService(CategoryRepository categoryRepository,GroupRepository groupRepository) {
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
    }

    public List<EntityModel<Category>> all(Long idGroup) {
        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        return categoryRepository.findByGroup(group).stream()
                .map(category -> EntityModel.of(category,
                        linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategoryId())).withSelfRel(),
                        linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories")))
                .collect(Collectors.toList());
    }

    public Category one(Long idGroup, Long idCategory) {
        var category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CATEGORY + idCategory));

        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        if(category.getGroup()!= group)
            throw new ResourceNotFoundException(NOT_FOUND_CATEGORY + idCategory);

        return category;
    }

    public ResponseEntity<Category> newCategory(Category newCategory, Long idGroup) {
        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        var category = new Category(newCategory.getName(), group, newCategory.getCreationTimestamp());
        categoryRepository.save(category);

        return ResponseEntity
                .created(URI.create("/" + idGroup + "/category/" + category.getCategoryId()))
                .body(category);
    }

    EntityModel<Category> replaceCategory(Category newCategory, Long idGroup, Long idCategory) {

        var category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CATEGORY + idCategory));

        category.setName(newCategory.getName());
        category.setCreationTimestamp(newCategory.getCreationTimestamp());

        categoryRepository.save(category);

        return EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategoryId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories"));
    }

    public ResponseEntity<HttpStatus> deleteOne(Long idCategory) {

        categoryRepository.deleteById(idCategory);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {


        categoryRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}