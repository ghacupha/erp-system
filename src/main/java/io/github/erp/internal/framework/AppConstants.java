package io.github.erp.internal.framework;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AppConstants {

    // Regex for acceptable logins
    public static final String EMAIL_REGEX = "^[_.@A-Za-z0-9-]*$";

    public static final int TOKEN_BYTE_LENGTH = 20;
    public static final String DATETIME_FORMAT = "yyyy/MM/dd";
    public static DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern(DATETIME_FORMAT);

    public static final List<String> PROCESSED_TOKENS = new CopyOnWriteArrayList<>();
    public static final List<String> ENQUEUED_TOKENS = new CopyOnWriteArrayList<>();
    public static final List<String> ENQUEUED_DEPOSIT_TOKENS = new CopyOnWriteArrayList<>();
    public static final int MAX_DEPOSIT_UPDATE_SIZE = 5000;

    public static final int LIST_PAGE_SIZE = 5000;
    public static final int LIST_PAGE_SIZE_TABLES = 5;

    private AppConstants() {
    }
}
