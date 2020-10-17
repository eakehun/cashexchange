package com.hourfun.cashexchange.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hourfun.cashexchange.model.Fee;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long>{

}
