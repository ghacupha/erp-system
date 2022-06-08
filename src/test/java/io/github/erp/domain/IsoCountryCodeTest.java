package io.github.erp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class IsoCountryCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IsoCountryCode.class);
        IsoCountryCode isoCountryCode1 = new IsoCountryCode();
        isoCountryCode1.setId(1L);
        IsoCountryCode isoCountryCode2 = new IsoCountryCode();
        isoCountryCode2.setId(isoCountryCode1.getId());
        assertThat(isoCountryCode1).isEqualTo(isoCountryCode2);
        isoCountryCode2.setId(2L);
        assertThat(isoCountryCode1).isNotEqualTo(isoCountryCode2);
        isoCountryCode1.setId(null);
        assertThat(isoCountryCode1).isNotEqualTo(isoCountryCode2);
    }
}
