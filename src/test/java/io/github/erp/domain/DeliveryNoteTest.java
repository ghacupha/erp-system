package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryNote.class);
        DeliveryNote deliveryNote1 = new DeliveryNote();
        deliveryNote1.setId(1L);
        DeliveryNote deliveryNote2 = new DeliveryNote();
        deliveryNote2.setId(deliveryNote1.getId());
        assertThat(deliveryNote1).isEqualTo(deliveryNote2);
        deliveryNote2.setId(2L);
        assertThat(deliveryNote1).isNotEqualTo(deliveryNote2);
        deliveryNote1.setId(null);
        assertThat(deliveryNote1).isNotEqualTo(deliveryNote2);
    }
}
