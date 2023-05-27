package com.hipatia.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Genero.
 */
@Entity
@Table(name = "genero")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Genero implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "nombre_genero", length = 50, nullable = false)
    private String nombreGenero;

    @Size(max = 100)
    @Column(name = "descripcion_genero", length = 100)
    private String descripcionGenero;

    @OneToMany(mappedBy = "genero")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ejemplars", "genero", "editorial", "autors" }, allowSetters = true)
    private Set<Libro> libros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Genero id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreGenero() {
        return this.nombreGenero;
    }

    public Genero nombreGenero(String nombreGenero) {
        this.setNombreGenero(nombreGenero);
        return this;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    public String getDescripcionGenero() {
        return this.descripcionGenero;
    }

    public Genero descripcionGenero(String descripcionGenero) {
        this.setDescripcionGenero(descripcionGenero);
        return this;
    }

    public void setDescripcionGenero(String descripcionGenero) {
        this.descripcionGenero = descripcionGenero;
    }

    public Set<Libro> getLibros() {
        return this.libros;
    }

    public void setLibros(Set<Libro> libros) {
        if (this.libros != null) {
            this.libros.forEach(i -> i.setGenero(null));
        }
        if (libros != null) {
            libros.forEach(i -> i.setGenero(this));
        }
        this.libros = libros;
    }

    public Genero libros(Set<Libro> libros) {
        this.setLibros(libros);
        return this;
    }

    public Genero addLibro(Libro libro) {
        this.libros.add(libro);
        libro.setGenero(this);
        return this;
    }

    public Genero removeLibro(Libro libro) {
        this.libros.remove(libro);
        libro.setGenero(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genero)) {
            return false;
        }
        return id != null && id.equals(((Genero) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Genero{" +
            "id=" + getId() +
            ", nombreGenero='" + getNombreGenero() + "'" +
            ", descripcionGenero='" + getDescripcionGenero() + "'" +
            "}";
    }
}
