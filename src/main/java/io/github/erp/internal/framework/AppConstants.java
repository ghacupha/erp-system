package io.github.erp.internal.framework;

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
