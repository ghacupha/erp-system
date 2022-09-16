package io.github.erp.security;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.1.0-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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

    public static final String REPORT_ACCESSOR = "ROLE_REPORT_ACCESSOR";

    public static final String REPORT_DESIGNER = "ROLE_REPORT_DESIGNER";

    private AuthoritiesConstants() {}
}
