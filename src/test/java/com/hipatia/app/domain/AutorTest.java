package com.hipatia.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.hipatia.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AutorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Autor.class);
        Autor autor1 = new Autor();
        autor1.setId(1L);
        Autor autor2 = new Autor();
        autor2.setId(autor1.getId());
        assertThat(autor1).isEqualTo(autor2);
        autor2.setId(2L);
        assertThat(autor1).isNotEqualTo(autor2);
        autor1.setId(null);
        assertThat(autor1).isNotEqualTo(autor2);
    }
}
