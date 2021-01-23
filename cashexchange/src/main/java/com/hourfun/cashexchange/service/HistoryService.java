package com.hourfun.cashexchange.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hourfun.cashexchange.model.History;
import com.hourfun.cashexchange.repository.HistoryRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class HistoryService {

    @Autowired
    HistoryRepository historyRepository;


    

    public History insertLoginHistory(History history) {
        history.setType("LOGIN");
        return historyRepository.save(history);
    }

    public History insertConfigHistory(History history) {
        return  historyRepository.save(history);
    }

    public Page<History> selectLoginHistory(Pageable pageRequest) {
        return historyRepository.findByTypeOrderByCreateDateDesc(pageRequest, "LOGIN");
    }

    public History upsertHistory(History history, String type) {
        history.setType(type);
        return historyRepository.save(history);
    }

    public Page<History> selectHistory(Pageable pageRequest, String type) {
        type = type.toUpperCase();
        return historyRepository.findByTypeOrderByCreateDateDesc(pageRequest, type);
    }
    
    public History selectLastLoginHistory(String userId) {
    	return historyRepository.findTopByUserAndTypeOrderByCreateDateDesc(userId, "login");
    }
}

