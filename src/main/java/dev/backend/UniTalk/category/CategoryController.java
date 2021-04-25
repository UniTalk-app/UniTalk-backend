package dev.backend.UniTalk.category;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import dev.backend.UniTalk.group.GroupException;
import dev.backend.UniTalk.group.Group;
import dev.backend.UniTalk.group.GroupRepository;

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

@RestController
@RequestMapping("/api/group")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CategoryController
{
    private final CategoryRepository categoryRepository;
    private final GroupRepository groupRepository;

    public CategoryController(CategoryRepository categoryRepository,GroupRepository groupRepository)
    {
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
    }

    @GetMapping("/{idGroup}/category/all")
    public CollectionModel<EntityModel<Category>> all(@PathVariable Long idGroup)
    {
        Group group=groupRepository.findById(idGroup).orElseThrow(()->new GroupException(idGroup));

        List<EntityModel<Category>> categories = categoryRepository.findByGroup(group).stream()
                .map(category -> EntityModel.of(category,
                        linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategory_id())).withSelfRel(),
                        linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories")))
                .collect(Collectors.toList());

        return CollectionModel.of(categories, linkTo(methodOn(CategoryController.class).all(idGroup)).withSelfRel());
    }

    @GetMapping("/{idGroup}/category/{idCategory}")
    public EntityModel<Category> one(@PathVariable Long idGroup,@PathVariable Long idCategory)
    {
        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new CategoryException(idCategory));

        Group group=groupRepository.findById(idGroup).orElseThrow(()->new GroupException(idGroup));

        if(category.getGroup()!= group)throw new CategoryException(idCategory);

        return EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategory_id())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories"));
    }

    @PostMapping("/{idGroup}/category")
    public ResponseEntity<?> newCategory( @PathVariable Long idGroup,@RequestBody Category newCategory)
    {
        Group group=groupRepository.findById(idGroup).orElseThrow(()->new GroupException(idGroup));

        Category category = new Category(newCategory.getName(), group, newCategory.getCreation_timestamp());
        categoryRepository.save(category);

        return ResponseEntity
                .created(URI.create("/" + idGroup + "/category/" + category.getCategory_id()))
                .body(category);
    }

    @PutMapping("/{idGroup}/category/{idCategory}")
    ResponseEntity<?> replaceCategory(@RequestBody Category newCategory,
                                      @PathVariable Long idGroup,
                                      @PathVariable Long idCategory)
    {
        Group group=groupRepository.findById(idGroup).orElseThrow(()->new GroupException(idGroup));

        Category category = categoryRepository.findById(idCategory)
                .orElseThrow(() -> new CategoryException(idCategory));

        category.setName(newCategory.getName());
        category.setCreation_timestamp(newCategory.getCreation_timestamp());

        categoryRepository.save(category);

        EntityModel<Category> entityModel = EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).one(idGroup, category.getCategory_id())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).all(idGroup)).withRel("categories"));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{idGroup}/category/{idCategory}")
    ResponseEntity<?> deleteCategory(@PathVariable Long idGroup,@PathVariable Long idCategory)
    {
        Group group = groupRepository.findById(idGroup).orElseThrow(() -> new GroupException(idGroup));

        categoryRepository.deleteById(idCategory);

        return ResponseEntity.noContent().build();
    }
}