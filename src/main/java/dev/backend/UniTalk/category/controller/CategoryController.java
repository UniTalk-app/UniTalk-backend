package dev.backend.UniTalk.controller;

import java.util.List;
import java.util.stream.Collectors;

import dev.backend.UniTalk.exception.CategoryException;
import dev.backend.UniTalk.model.Category;
import dev.backend.UniTalk.repository.CategoryRepository;
import dev.backend.UniTalk.component.CategoryModelAssembler;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/category")
public class CategoryController
{
    private final CategoryRepository repository;
    private final CategoryModelAssembler assembler;

    public CategoryController(CategoryRepository repository,CategoryModelAssembler assembler)
    {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/all")
    public CollectionModel<EntityModel<Category>> all()
    {
        List<EntityModel<Category>> categories = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(categories, linkTo(methodOn(CategoryController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Category> one(@PathVariable Long id)
    {
        Category category = repository.findById(id)
                .orElseThrow(() -> new CategoryException(id));

        return assembler.toModel(category);
    }

    @PostMapping()
    ResponseEntity<?> newCategory(@RequestBody Category newCategory)
    {
        EntityModel<Category> entityModel = assembler.toModel(repository.save(newCategory));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> replaceCategory(@RequestBody Category newCategory, @PathVariable Long id)
    {
        Category updatedCategory = repository.findById(id)
                .map(category -> {
                    category.setName(newCategory.getName());
                    category.setGroup_id(newCategory.getGroup_id());
                    category.setCreation_timestamp(newCategory.getCreation_timestamp());
                    return repository.save(category);
                })
                .orElseGet(() -> {
                    newCategory.setId(id);
                    return repository.save(newCategory);
                });

        EntityModel<Category> entityModel = assembler.toModel(updatedCategory);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable Long id)
    {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}