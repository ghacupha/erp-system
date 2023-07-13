package io.github.erp.internal.report;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import org.hibernate.Query;

import javax.persistence.TypedQuery;
import java.io.Serializable;

/**
 * Utility model created to return 2 things from a report query. A list making up the report
 * and the query generated thereof. The query may then be used for debugging or unit tests where
 * such may be needful without there being actual data to represent.
 */
public class ReportModel<T> implements Serializable {
    /**
     * This is the list of information that has been summarised and analyzed on the backend
     */
    T reportData;

    /**
     * This is a string showing the query that has ran or will run on the database to generate
     * the results in the report data. This is because it is assumed that the analysis is
     * being executed on the database
     */
    String reportQuery;

    public ReportModel(T reportData, String reportQuery) {
        this.reportData = reportData;
        this.reportQuery = reportQuery;
    }

    public ReportModel() {}

    public static <T> String toSQLString(TypedQuery<T> query) {
        return query.unwrap(Query.class).getQueryString();
    }

    public <T> void sqlString(TypedQuery<T> query) {
        this.reportQuery = toSQLString(query);
    }

    public static <T> ReportModelBuilder<T> builder() {
        return new ReportModelBuilder<T>();
    }

    public String toString() {
        return "Report model for queryString : " + reportQuery;
    }

    public T getReportData() {
        return this.reportData;
    }

    public String getReportQuery() {
        return this.reportQuery;
    }

    public void setReportData(T reportData) {
        this.reportData = reportData;
    }

    public void setReportQuery(String reportQuery) {
        this.reportQuery = reportQuery;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof ReportModel)) return false;
        final ReportModel<?> other = (ReportModel<?>) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$reportData = this.getReportData();
        final Object other$reportData = other.getReportData();
        if (this$reportData == null ? other$reportData != null : !this$reportData.equals(other$reportData)) return false;
        final Object this$reportQuery = this.getReportQuery();
        final Object other$reportQuery = other.getReportQuery();
        if (this$reportQuery == null ? other$reportQuery != null : !this$reportQuery.equals(other$reportQuery)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof ReportModel;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $reportData = this.getReportData();
        result = result * PRIME + ($reportData == null ? 43 : $reportData.hashCode());
        final Object $reportQuery = this.getReportQuery();
        result = result * PRIME + ($reportQuery == null ? 43 : $reportQuery.hashCode());
        return result;
    }

    public static class ReportModelBuilder<T> {
        private T reportData;
        private String reportQuery;

        ReportModelBuilder() {}

        public ReportModelBuilder<T> reportData(T reportData) {
            this.reportData = reportData;
            return this;
        }

        public ReportModelBuilder<T> reportQuery(String reportQuery) {
            this.reportQuery = reportQuery;
            return this;
        }

        public ReportModel<T> build() {
            return new ReportModel<T>(reportData, reportQuery);
        }

        public String toString() {
            return "ReportModel.ReportModelBuilder(reportData=" + this.reportData + ", reportQuery=" + this.reportQuery + ")";
        }
    }
}
