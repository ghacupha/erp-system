package io.github.erp.internal.repository;

import io.github.erp.domain.DepreciationReport;
import io.github.erp.repository.DepreciationReportRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InternalDepreciationReportRepository
    extends DepreciationReportRepository,
    JpaRepository<DepreciationReport, Long>, JpaSpecificationExecutor<DepreciationReport> {


    @Query("SELECT " +
        "au " +
        "FROM DepreciationReport dr " +
        "LEFT JOIN FETCH dr.depreciationPeriod dp " +
        "LEFT JOIN FETCH dr.requestedBy au " +
        "LEFT JOIN FETCH dr.serviceOutlet so " +
        "LEFT JOIN FETCH dr.assetCategory ac " +
        "WHERE dr.id = :id")
    @NotNull
    Optional<DepreciationReport> findById(@Param("id") @NotNull Long id);
}
