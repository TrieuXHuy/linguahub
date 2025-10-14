package com.huy.linguahub.controller;

import com.huy.linguahub.controller.error.ResourceAlreadyExistsException;
import com.huy.linguahub.controller.error.ResourceNotFoundException;
import com.huy.linguahub.domain.Category;
import com.huy.linguahub.repository.CategoryRepository;
import com.huy.linguahub.service.CategoryService;
import com.huy.linguahub.service.dto.response.category.ResCreateCategoryDTO;
import com.huy.linguahub.service.dto.response.category.ResGetCategoryDTO;
import com.huy.linguahub.service.dto.response.category.ResUpdateCategoryDTO;
import com.huy.linguahub.service.dto.response.filter.ResultPaginationDTO;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService,
                              CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/categories")
    public ResponseEntity<ResCreateCategoryDTO> createCategory(@Valid @RequestBody Category reqCategory) throws ResourceAlreadyExistsException {
        String name = reqCategory.getName();
        if(this.categoryRepository.existsByName(name)) {
            throw new ResourceAlreadyExistsException("Category already exists!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.createCategory(reqCategory));
    }

    @PutMapping("/categories")
    public ResponseEntity<ResUpdateCategoryDTO> updateCategory(@Valid @RequestBody Category reqCategory) throws ResourceNotFoundException {
        Category categoryDB = categoryRepository.findById(reqCategory.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        if(categoryDB.isDeleted()) {
            throw new ResourceNotFoundException("Category not found!");
        }
        return ResponseEntity.ok().body(this.categoryService.updateCategory(categoryDB, reqCategory));
    }

    @GetMapping("/categories")
    public ResponseEntity<ResultPaginationDTO> getAllCategories(@Filter Specification<Category> specCategory, Pageable pageable) {
        Specification<Category> spec = specCategory.and((root, query, cb) -> cb.equal(root.get("deleted"), false));
        return ResponseEntity.ok(this.categoryService.getAllCategories(spec, pageable));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ResGetCategoryDTO> getCategoryById(@PathVariable Long id) throws ResourceNotFoundException {
        Category categoryDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        if(categoryDB.isDeleted()) {
            throw new ResourceNotFoundException("Category not found!");
        }
        return ResponseEntity.ok(this.categoryService.toGetCategoryDTO(categoryDB));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable Long id) throws ResourceNotFoundException {
        Category categoryDB = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        if(categoryDB.isDeleted()) {
            throw new ResourceNotFoundException("Category not found!");
        }
        this.categoryService.deleteCategoryById(categoryDB);
        return ResponseEntity.ok().build();
    }
}
