package io.github.erp.internal.report;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * General interface representing report generating services. It's expected that sunch are
 *
 * created with the query api and this though having nothing to do with file handling perse
 *
 * might be the only way of checking that your file data has been uploaded
 *
 * A typical implementation might look like so :
 *
 * {@code
        @Slf4j
        @Service("currencySummaryReportService")
        public class CurrencySummaryReportService implements Report<List<CurrencySummary>, CurrencySummaryParameters> {

            private final SessionFactory sessionFactory;

            public CurrencySummaryReportService(EntityManagerFactory entityManager) {
                this.sessionFactory = entityManager.unwrap(SessionFactory.class);
            }

            @Override
            @Cacheable("currency-summary-report-cache")
            public List<CurrencySummary> createReport(final CurrencySummaryParameters reportParameter) {
                // @formatter:off

                long requestStart = System.currentTimeMillis();
                CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
                CriteriaQuery<CurrencySummary> criteriaQuery = cb.createQuery(CurrencySummary.class);

                Root<DepositAccount> depositRoot = criteriaQuery.from(DepositAccount.class);

                Predicate monthOfStudyPredicate = cb.equal(depositRoot.get(MONTH_OF_STUDY), reportParameter.getMonthOfStudy());
                Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)), BigDecimal.ZERO);

                // Define the SELECT clause
                criteriaQuery.multiselect(
                    depositRoot.get(CURRENCY_CODE),
                    cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)),
                    cb.count(depositRoot.get(ACCOUNT_NUMBER)))
                .groupBy(depositRoot.get(CURRENCY_CODE))
                .where(monthOfStudyPredicate)
                .having(noneZeroSumTotalPredicate);

                List<CurrencySummary> resultList = sessionFactory.createEntityManager().createQuery(criteriaQuery).getResultList();

                log.info("Currency list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

                return resultList;

                // @formatter:on
            }
        }
 * }
 *
 * @param reportParameter
 * @return
 */
public interface Report<Report, Parameter> {

    Report createReport(Parameter reportParameter);
}
