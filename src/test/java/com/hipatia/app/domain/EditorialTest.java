package com.hipatia.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipatia.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EditorialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Editorial.class);
        Editorial editorial1 = new Editorial();
        editorial1.setId(1L);
        Editorial editorial2 = new Editorial();
        editorial2.setId(editorial1.getId());
        assertThat(editorial1).isEqualTo(editorial2);
        editorial2.setId(2L);
        assertThat(editorial1).isNotEqualTo(editorial2);
        editorial1.setId(null);
        assertThat(editorial1).isNotEqualTo(editorial2);
    }
}
