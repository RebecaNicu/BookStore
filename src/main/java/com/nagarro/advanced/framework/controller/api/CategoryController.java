package com.nagarro.advanced.framework.controller.api;

import com.nagarro.advanced.framework.controller.model.CategoryDto;
import com.nagarro.advanced.framework.facade.CategoryFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryFacade categoryFacade;

    public CategoryController(CategoryFacade categoryFacade) {
        this.categoryFacade = categoryFacade;
    }

    @GetMapping("/{uuid}")
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<CategoryDto> findCategoryByUuid(@PathVariable("uuid") String uuid) {
        return new ResponseEntity<>(categoryFacade.findCategoryByUuid(uuid), HttpStatus.OK);
    }

    @GetMapping()
    @Secured({"ROLE_ADMIN", "ROLE_USER"})
    public ResponseEntity<CategoryDto> findCategoryByName(@RequestParam String name) {
        return new ResponseEntity<>(categoryFacade.findCategoryByName(name), HttpStatus.OK);
    }

    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CategoryDto> save(@RequestBody @Validated CategoryDto categorySaveDto) {
        return new ResponseEntity<>(categoryFacade.saveCategory(categorySaveDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{uuid}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> delete(@PathVariable("uuid") String uuid) {
        categoryFacade.deleteCategoryByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{uuid}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> update(@PathVariable("uuid") String uuid,
                                       @RequestBody @Validated CategoryDto categoryDto) {
        categoryFacade.updateCategory(uuid, categoryDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
