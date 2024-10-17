package io.github.erp.internal.service.leases;

import io.github.erp.internal.repository.ROUAmortizationTransactionDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ROUAmortizationTransactionDetailsServiceImpl implements ROUAmortizationTransactionDetailsService {

    private final ROUAmortizationTransactionDetailsRepository rouAmortizationTransactionDetailsRepository;

    public ROUAmortizationTransactionDetailsServiceImpl(ROUAmortizationTransactionDetailsRepository rouAmortizationTransactionDetailsRepository){
        this.rouAmortizationTransactionDetailsRepository = rouAmortizationTransactionDetailsRepository;
    }

    @Override
    public void createTransactionDetails(){

        rouAmortizationTransactionDetailsRepository.insertTransactionDetails();
    }
}
