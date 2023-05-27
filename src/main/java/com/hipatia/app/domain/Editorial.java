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
 * A Editorial.
 */
@Entity
@Table(name = "editorial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Editorial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "nombre_editorial", length = 50, nullable = false)
    private String nombreEditorial;

    @Max(value = 100L)
    @Column(name = "cantidad_titulos")
    private Long cantidadTitulos;

    @OneToMany(mappedBy = "editorial")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ejemplars", "genero", "editorial", "autors" }, allowSetters = true)
    private Set<Libro> libros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Editorial id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEditorial() {
        return this.nombreEditorial;
    }

    public Editorial nombreEditorial(String nombreEditorial) {
        this.setNombreEditorial(nombreEditorial);
        return this;
    }

    public void setNombreEditorial(String nombreEditorial) {
        this.nombreEditorial = nombreEditorial;
    }

    public Long getCantidadTitulos() {
        return this.cantidadTitulos;
    }

    public Editorial cantidadTitulos(Long cantidadTitulos) {
        this.setCantidadTitulos(cantidadTitulos);
        return this;
    }

    public void setCantidadTitulos(Long cantidadTitulos) {
        this.cantidadTitulos = cantidadTitulos;
    }

    public Set<Libro> getLibros() {
        return this.libros;
    }

    public void setLibros(Set<Libro> libros) {
        if (this.libros != null) {
            this.libros.forEach(i -> i.setEditorial(null));
        }
        if (libros != null) {
            libros.forEach(i -> i.setEditorial(this));
        }
        this.libros = libros;
    }

    public Editorial libros(Set<Libro> libros) {
        this.setLibros(libros);
        return this;
    }

    public Editorial addLibro(Libro libro) {
        this.libros.add(libro);
        libro.setEditorial(this);
        return this;
    }

    public Editorial removeLibro(Libro libro) {
        this.libros.remove(libro);
        libro.setEditorial(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Editorial)) {
            return false;
        }
        return id != null && id.equals(((Editorial) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Editorial{" +
            "id=" + getId() +
            ", nombreEditorial='" + getNombreEditorial() + "'" +
            ", cantidadTitulos=" + getCantidadTitulos() +
            "}";
    }
}
