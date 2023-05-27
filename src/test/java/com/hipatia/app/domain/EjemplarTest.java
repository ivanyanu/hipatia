package com.hipatia.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipatia.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EjemplarTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ejemplar.class);
        Ejemplar ejemplar1 = new Ejemplar();
        ejemplar1.setId(1L);
        Ejemplar ejemplar2 = new Ejemplar();
        ejemplar2.setId(ejemplar1.getId());
        assertThat(ejemplar1).isEqualTo(ejemplar2);
        ejemplar2.setId(2L);
        assertThat(ejemplar1).isNotEqualTo(ejemplar2);
        ejemplar1.setId(null);
        assertThat(ejemplar1).isNotEqualTo(ejemplar2);
    }
}
