package com.huy.linguahub.service;

import com.huy.linguahub.domain.Category;
import com.huy.linguahub.repository.CategoryRepository;
import com.huy.linguahub.service.dto.response.category.ResCreateCategoryDTO;
import com.huy.linguahub.service.dto.response.category.ResGetCategoryDTO;
import com.huy.linguahub.service.dto.response.category.ResUpdateCategoryDTO;
import com.huy.linguahub.service.dto.response.filter.Pagination;
import com.huy.linguahub.service.dto.response.filter.ResultPaginationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResCreateCategoryDTO createCategory(Category category) {
        Category savedCategory = this.categoryRepository.save(category);

        return ResCreateCategoryDTO.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .description(savedCategory.getDescription())
                .createdBy(savedCategory.getCreatedBy())
                .createdDate(savedCategory.getCreatedDate())
                .build();
    }

    public ResUpdateCategoryDTO updateCategory(Category categoryDB, Category reqCategory) {
        categoryDB.setName(reqCategory.getName());
        categoryDB.setDescription(reqCategory.getDescription());

        Category updatedCategory = categoryRepository.save(categoryDB);

        return ResUpdateCategoryDTO.builder()
                .id(updatedCategory.getId())
                .name(updatedCategory.getName())
                .description(updatedCategory.getDescription())
                .lastModifiedBy(updatedCategory.getLastModifiedBy())
                .lastModifiedDate(updatedCategory.getLastModifiedDate())
                .build();
    }

    public ResultPaginationDTO getAllCategories(Specification<Category> spec, Pageable pageable) {
        Page<Category> categories = this.categoryRepository.findAll(spec, pageable);

        Pagination pagination = new Pagination();
        pagination.setPage(pageable.getPageNumber() + 1);
        pagination.setSize(pageable.getPageSize());
        pagination.setTotalPages(categories.getTotalPages());
        pagination.setTotalElements(categories.getTotalElements());

        List<ResGetCategoryDTO> categoriesDTO = categories.getContent().stream()
                .map(this::toGetCategoryDTO)
                .collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        resultPaginationDTO.setPagination(pagination);
        resultPaginationDTO.setResult(categoriesDTO);

        return resultPaginationDTO;
    }

    public ResGetCategoryDTO toGetCategoryDTO(Category category) {
        return ResGetCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .createdBy(category.getCreatedBy())
                .createdDate(category.getCreatedDate())
                .lastModifiedBy(category.getLastModifiedBy())
                .lastModifiedDate(category.getLastModifiedDate())
                .build();
    }

    public void deleteCategoryById(Category categoryDB) {
        categoryDB.setDeleted(true);
        this.categoryRepository.save(categoryDB);
    }
}
