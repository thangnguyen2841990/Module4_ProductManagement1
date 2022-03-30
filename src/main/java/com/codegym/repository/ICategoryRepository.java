package com.codegym.repository;

import com.codegym.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface ICategoryRepository extends PagingAndSortingRepository<Category,Long> {
    Page<Category>  findByNameContaining(String name, Pageable pageable);
    @Modifying
    @Query(value = "call deleteCategory(?1)",nativeQuery = true)
    void deleteCategory(Long categoryId);
}
