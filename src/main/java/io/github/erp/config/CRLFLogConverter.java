package io.github.erp.config;

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
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiElement;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;

public class CRLFLogConverter extends CompositeConverter<ILoggingEvent> {

    public static final Marker CRLF_SAFE_MARKER = MarkerFactory.getMarker("CRLF_SAFE");

    private static final String[] SAFE_LOGGERS = { "org.hibernate" };
    private static final Map<String, AnsiElement> ELEMENTS;

    static {
        Map<String, AnsiElement> ansiElements = new HashMap<>();
        ansiElements.put("faint", AnsiStyle.FAINT);
        ansiElements.put("red", AnsiColor.RED);
        ansiElements.put("green", AnsiColor.GREEN);
        ansiElements.put("yellow", AnsiColor.YELLOW);
        ansiElements.put("blue", AnsiColor.BLUE);
        ansiElements.put("magenta", AnsiColor.MAGENTA);
        ansiElements.put("cyan", AnsiColor.CYAN);
        ELEMENTS = Collections.unmodifiableMap(ansiElements);
    }

    @Override
    protected String transform(ILoggingEvent event, String in) {
        AnsiElement element = ELEMENTS.get(getFirstOption());
        if ((event.getMarker() != null && event.getMarker().contains(CRLF_SAFE_MARKER)) || isLoggerSafe(event)) {
            return in;
        }
        String replacement = element == null ? "_" : toAnsiString("_", element);
        return in.replaceAll("[\n\r\t]", replacement);
    }

    protected boolean isLoggerSafe(ILoggingEvent event) {
        for (String safeLogger : SAFE_LOGGERS) {
            if (event.getLoggerName().startsWith(safeLogger)) {
                return true;
            }
        }
        return false;
    }

    protected String toAnsiString(String in, AnsiElement element) {
        return AnsiOutput.toString(element, in);
    }
}
