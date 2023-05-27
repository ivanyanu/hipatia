package com.hipatia.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hipatia.app.domain.enumeration.EstadoEjemplar;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ejemplar.
 */
@Entity
@Table(name = "ejemplar")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ejemplar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_ejemplar", nullable = false)
    private EstadoEjemplar estadoEjemplar;

    @NotNull
    @Column(name = "fecha_alta_ejemplar", nullable = false)
    private String fechaAltaEjemplar;

    @OneToMany(mappedBy = "ejemplar")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nombreEstudiante", "ejemplar" }, allowSetters = true)
    private Set<Prestamo> prestamos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ejemplars", "genero", "editorial", "autors" }, allowSetters = true)
    private Libro libro;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ejemplar id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoEjemplar getEstadoEjemplar() {
        return this.estadoEjemplar;
    }

    public Ejemplar estadoEjemplar(EstadoEjemplar estadoEjemplar) {
        this.setEstadoEjemplar(estadoEjemplar);
        return this;
    }

    public void setEstadoEjemplar(EstadoEjemplar estadoEjemplar) {
        this.estadoEjemplar = estadoEjemplar;
    }

    public String getFechaAltaEjemplar() {
        return this.fechaAltaEjemplar;
    }

    public Ejemplar fechaAltaEjemplar(String fechaAltaEjemplar) {
        this.setFechaAltaEjemplar(fechaAltaEjemplar);
        return this;
    }

    public void setFechaAltaEjemplar(String fechaAltaEjemplar) {
        this.fechaAltaEjemplar = fechaAltaEjemplar;
    }

    public Set<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    public void setPrestamos(Set<Prestamo> prestamos) {
        if (this.prestamos != null) {
            this.prestamos.forEach(i -> i.setEjemplar(null));
        }
        if (prestamos != null) {
            prestamos.forEach(i -> i.setEjemplar(this));
        }
        this.prestamos = prestamos;
    }

    public Ejemplar prestamos(Set<Prestamo> prestamos) {
        this.setPrestamos(prestamos);
        return this;
    }

    public Ejemplar addPrestamo(Prestamo prestamo) {
        this.prestamos.add(prestamo);
        prestamo.setEjemplar(this);
        return this;
    }

    public Ejemplar removePrestamo(Prestamo prestamo) {
        this.prestamos.remove(prestamo);
        prestamo.setEjemplar(null);
        return this;
    }

    public Libro getLibro() {
        return this.libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Ejemplar libro(Libro libro) {
        this.setLibro(libro);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ejemplar)) {
            return false;
        }
        return id != null && id.equals(((Ejemplar) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ejemplar{" +
            "id=" + getId() +
            ", estadoEjemplar='" + getEstadoEjemplar() + "'" +
            ", fechaAltaEjemplar='" + getFechaAltaEjemplar() + "'" +
            "}";
    }
}
