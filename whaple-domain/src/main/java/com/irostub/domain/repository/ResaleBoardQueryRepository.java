package com.irostub.domain.repository;

import com.irostub.domain.entity.market.ResaleBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ResaleBoardQueryRepository {
    Page<ResaleBoard> findBySearch(Pageable pageable, String name, String keyword);
}
