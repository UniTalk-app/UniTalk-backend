package dev.backend.UniTalk.component;

import dev.backend.UniTalk.model.Category;
import dev.backend.UniTalk.controller.CategoryController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CategoryModelAssembler implements RepresentationModelAssembler<Category, EntityModel<Category>>
{
    @Override
    public EntityModel<Category> toModel(Category category)
    {
        return EntityModel.of(category,
                linkTo(methodOn(CategoryController.class).one(category.getId())).withSelfRel(),
                linkTo(methodOn(CategoryController.class).all()).withRel("categories"));
    }
}