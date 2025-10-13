package io.github.erp.security;

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
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String DEV = "ROLE_DEV";

    public static final String DBA = "ROLE_DBA";

    public static final String BOOK_KEEPING = "ROLE_BOOK_KEEPING";

    public static final String PAYMENTS_USER = "ROLE_PAYMENTS_USER";

    public static final String FIXED_ASSETS_USER = "ROLE_FIXED_ASSETS_USER";

    public static final String TAX_MODULE_USER = "ROLE_TAX_MODULE_USER";

    public static final String PREPAYMENTS_MODULE_USER = "ROLE_PREPAYMENTS_MODULE_USER";

    public static final String GRANULAR_REPORTS_USER = "ROLE_GRANULAR_REPORTS_USER";
    public static final String GDI_FINANCE_USER = "ROLE_GDI_FINANCE_USER";
    public static final String GDI_OPERATIONS_USER = "ROLE_GDI_OPERATIONS_USER";
    public static final String GDI_CARD_USER = "ROLE_GDI_CARD_CENTRE_USER";
    public static final String GDI_CREDIT_MANAGEMENT_USER = "ROLE_GDI_CREDIT_MANAGEMENT_USER";
    public static final String GDI_PAYMENTS_USER = "ROLE_GDI_PAYMENTS_USER";
    public static final String GDI_PRODUCTS_USER = "ROLE_GDI_PRODUCTS_USER";
    public static final String GDI_SECURITY_USER = "ROLE_SECURITY_USER";
    public static final String GDI_RISK_DEPARTMENT_USER = "ROLE_RISK_DEPARTMENT_USER";
    public static final String GDI_TREASURY_USER = "ROLE_TREASURY_USER";
    public static final String GDI_HR_USER = "ROLE_HR_USER";

    public static final String REPORT_ACCESSOR = "ROLE_REPORT_ACCESSOR";

    public static final String REPORT_DESIGNER = "ROLE_REPORT_DESIGNER";

    public static final String REQUISITION_MANAGER = "ROLE_REQUISITION_MANAGER";

    public static final String DOCUMENT_MODULE_USER = "ROLE_DOCUMENT_MODULE_USER";

    public static final String LEASE_MANAGER = "ROLE_LEASE_MANAGER";

    private AuthoritiesConstants() {}
}
