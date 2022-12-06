package org.dmitrysulman.innopolis.diplomaproject.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableHelper {
    public Pageable preparePageable(Integer page, Integer perPage, String direction, String defaultSortField) {
        if (page == null) {
            page = 0;
        }
        if (perPage == null) {
            perPage = 10;
        }
        Sort.Direction sortDirection = Sort.Direction.ASC;
        if (direction != null && direction.equals("DESC")) {
            sortDirection = Sort.Direction.DESC;
        }
        Sort sort = Sort.by(new Sort.Order(sortDirection, defaultSortField));

        return PageRequest.of(page, perPage, sort);
    }
}
