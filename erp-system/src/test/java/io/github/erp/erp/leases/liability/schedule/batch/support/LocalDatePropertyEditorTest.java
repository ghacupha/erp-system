package io.github.erp.erp.leases.liability.schedule.batch.support;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LocalDatePropertyEditorTest {

    private LocalDatePropertyEditor editor;

    @BeforeEach
    void setUp() {
        editor = new LocalDatePropertyEditor();
    }

    @Test
    void parsesDayMonthAbbreviationWithTwoDigitYear() {
        editor.setAsText("31-Dec-22");

        assertThat(editor.getValue()).isEqualTo(LocalDate.of(2022, 12, 31));
    }

    @Test
    void parsesSingleDigitDayWithMonthAbbreviation() {
        editor.setAsText("1-Jan-2023");

        assertThat(editor.getValue()).isEqualTo(LocalDate.of(2023, 1, 1));
    }
}

