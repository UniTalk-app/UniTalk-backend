package dev.backend.unitalk.category;

import dev.backend.unitalk.exception.ResourceNotFoundException;
import dev.backend.unitalk.exception.UserAuthenticationException;
import dev.backend.unitalk.group.GroupRepository;
import dev.backend.unitalk.payload.request.CategoryRequest;
import dev.backend.unitalk.user.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CategoryControllerService{
    private static final String NOT_FOUND_GROUP = "Not found group with id = ";
    private static final String NOT_FOUND_CATEGORY = "Not found category with id = ";
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;


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

    public ResponseEntity<Category> newCategory(CategoryRequest newCategory, Long idGroup, User user) throws UserAuthenticationException {
        var group = groupRepository.findById(idGroup)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_GROUP + idGroup));

        if (!user.getGroups().contains(group)) {
            throw new UserAuthenticationException("User not in group");
        }

        var category = new Category(newCategory.getName(), group, new Timestamp(new Date().getTime()));
        categoryRepository.save(category);

        return ResponseEntity
                .created(URI.create("/" + idGroup + "/category/" + category.getCategoryId()))
                .body(category);
    }

    EntityModel<Category> replaceCategory(CategoryRequest newCategory, Long idGroup, Long idCategory) {

        var category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CATEGORY + idCategory));

        category.setName(newCategory.getName());

        categoryRepository.save(category);

        return EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategoryId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories"));
    }

    public ResponseEntity<HttpStatus> deleteOne(Long idGroup, Long idCategory) {
        categoryRepository.deleteById(idCategory);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<HttpStatus> deleteAll() {

        categoryRepository.deleteAll();

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
