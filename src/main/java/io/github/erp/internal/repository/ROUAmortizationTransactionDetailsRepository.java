package io.github.erp.internal.repository;

import io.github.erp.domain.TransactionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ROUAmortizationTransactionDetailsRepository
    extends JpaRepository<TransactionDetails, Long>, JpaSpecificationExecutor<TransactionDetails> {

    @Modifying
    @Transactional
    @Query(
        nativeQuery = true,
        value = "" +
            "INSERT INTO transaction_details (id, entry_id, transaction_date, description, amount, debit_account_id, credit_account_id) " +
            "SELECT " +
            "    nextval('sequence_generator') AS id," +
            "    nextval('transaction_entry_id_sequence') AS entry_id," +
            "    lp.end_date AS transaction_date," +
            "    re.description AS description," +
            "    re.depreciation_amount AS amount," +
            "    ar.debit_id AS debit_account_id," +
            "    ar.credit_id AS credit_account_id " +
            "FROM " +
            "    rou_depreciation_entry re " +
            "JOIN " +
            "    ifrs16lease_contract lc ON re.lease_contract_id = lc.id " +
            "JOIN " +
            "    taamortization_rule ar ON ar.lease_contract_id = lc.id " +
            "LEFT JOIN " +
            "    lease_period lp ON re.lease_period_id = lp.id"
    )
    void insertTransactionDetails();
}
