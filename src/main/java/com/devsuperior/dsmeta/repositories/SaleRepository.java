package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

	
	@Query(value = "SELECT DISTINCT s, seller "
		       + "FROM Sale s "
		       + "JOIN FETCH s.seller seller "
		       + "WHERE s.date BETWEEN :minDate AND :maxDate "
		       + "AND UPPER(seller.name) LIKE UPPER(CONCAT('%', :name , '%'))",
		       countQuery = "SELECT COUNT(DISTINCT s) "
		       + "FROM Sale s "
		       + "JOIN s.seller seller "
		       + "WHERE s.date BETWEEN :minDate AND :maxDate "
		       + "AND UPPER(seller.name) LIKE UPPER(CONCAT('%', :name , '%'))")
		public Page<Sale> getReport(@Param("minDate") LocalDate minDate,
		                                               @Param("maxDate") LocalDate maxDate,
		                                               @Param("name") String name,
		                                               Pageable pageable);
}
