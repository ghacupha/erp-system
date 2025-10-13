package io.github.erp.internal.report;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
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
