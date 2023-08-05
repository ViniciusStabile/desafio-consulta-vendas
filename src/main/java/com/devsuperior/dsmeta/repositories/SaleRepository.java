package com.devsuperior.dsmeta.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;


public interface SaleRepository extends JpaRepository<Sale, Long> {

	@Query(value = "SELECT DISTINCT s, seller " + "FROM Sale s " + "JOIN FETCH s.seller seller "
			+ "WHERE s.date BETWEEN :minDate AND :maxDate "
			+ "AND UPPER(seller.name) LIKE UPPER(CONCAT('%', :name , '%'))", countQuery = "SELECT COUNT(DISTINCT s) "
					+ "FROM Sale s " + "JOIN s.seller seller " + "WHERE s.date BETWEEN :minDate AND :maxDate "
					+ "AND UPPER(seller.name) LIKE UPPER(CONCAT('%', :name , '%'))")
	public Page<Sale> getReport(@Param("minDate") LocalDate minDate, @Param("maxDate") LocalDate maxDate,
			@Param("name") String name, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT tb_seller.name, SUM(tb_sales.amount) AS total "
			+ "FROM tb_sales "
			+ "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id "
			+ "WHERE tb_sales.date BETWEEN :minDate AND :maxDate "
			+ "GROUP BY tb_seller.name; ")
	public List<SaleSummaryProjection> getSummary(@Param("minDate") LocalDate minDate,
			@Param("maxDate") LocalDate maxDate);

}
