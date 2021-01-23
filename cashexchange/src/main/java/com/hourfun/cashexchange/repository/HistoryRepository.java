package com.hourfun.cashexchange.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    Page<History> findByTypeOrderByCreateDateDesc(Pageable pageRequest, String historyType);
    
    History findTopByUserAndTypeOrderByCreateDateDesc(String user, String type);

}
