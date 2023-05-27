package com.hipatia.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hipatia.app.domain.enumeration.EstadoPrestamo;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Prestamo.
 */
@Entity
@Table(name = "prestamo")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_prestamo")
    private EstadoPrestamo estadoPrestamo;

    @Column(name = "fecha_prestamo")
    private Instant fechaPrestamo;

    @Column(name = "fecha_devolucion")
    private Instant fechaDevolucion;

    @ManyToOne
    @JsonIgnoreProperties(value = { "reservas", "prestamos" }, allowSetters = true)
    private Estudiante nombreEstudiante;

    @ManyToOne
    @JsonIgnoreProperties(value = { "prestamos", "libro" }, allowSetters = true)
    private Ejemplar ejemplar;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Prestamo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EstadoPrestamo getEstadoPrestamo() {
        return this.estadoPrestamo;
    }

    public Prestamo estadoPrestamo(EstadoPrestamo estadoPrestamo) {
        this.setEstadoPrestamo(estadoPrestamo);
        return this;
    }

    public void setEstadoPrestamo(EstadoPrestamo estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
    }

    public Instant getFechaPrestamo() {
        return this.fechaPrestamo;
    }

    public Prestamo fechaPrestamo(Instant fechaPrestamo) {
        this.setFechaPrestamo(fechaPrestamo);
        return this;
    }

    public void setFechaPrestamo(Instant fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Instant getFechaDevolucion() {
        return this.fechaDevolucion;
    }

    public Prestamo fechaDevolucion(Instant fechaDevolucion) {
        this.setFechaDevolucion(fechaDevolucion);
        return this;
    }

    public void setFechaDevolucion(Instant fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Estudiante getNombreEstudiante() {
        return this.nombreEstudiante;
    }

    public void setNombreEstudiante(Estudiante estudiante) {
        this.nombreEstudiante = estudiante;
    }

    public Prestamo nombreEstudiante(Estudiante estudiante) {
        this.setNombreEstudiante(estudiante);
        return this;
    }

    public Ejemplar getEjemplar() {
        return this.ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public Prestamo ejemplar(Ejemplar ejemplar) {
        this.setEjemplar(ejemplar);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Prestamo)) {
            return false;
        }
        return id != null && id.equals(((Prestamo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Prestamo{" +
            "id=" + getId() +
            ", estadoPrestamo='" + getEstadoPrestamo() + "'" +
            ", fechaPrestamo='" + getFechaPrestamo() + "'" +
            ", fechaDevolucion='" + getFechaDevolucion() + "'" +
            "}";
    }
}
