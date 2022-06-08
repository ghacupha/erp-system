package io.github.erp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.erp.web.rest.utils.TestUtil;
import org.junit.jupiter.api.Test;

class FileTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FileTypeDTO.class);
        FileTypeDTO fileTypeDTO1 = new FileTypeDTO();
        fileTypeDTO1.setId(1L);
        FileTypeDTO fileTypeDTO2 = new FileTypeDTO();
        assertThat(fileTypeDTO1).isNotEqualTo(fileTypeDTO2);
        fileTypeDTO2.setId(fileTypeDTO1.getId());
        assertThat(fileTypeDTO1).isEqualTo(fileTypeDTO2);
        fileTypeDTO2.setId(2L);
        assertThat(fileTypeDTO1).isNotEqualTo(fileTypeDTO2);
        fileTypeDTO1.setId(null);
        assertThat(fileTypeDTO1).isNotEqualTo(fileTypeDTO2);
    }
}
