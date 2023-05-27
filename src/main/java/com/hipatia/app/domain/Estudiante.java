package com.hipatia.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Estudiante.
 */
@Entity
@Table(name = "estudiante")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Estudiante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "nombre_estudiante", length = 50, nullable = false)
    private String nombreEstudiante;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "apellido_estudiante", length = 50, nullable = false)
    private String apellidoEstudiante;

    @NotNull
    @Size(max = 50)
    @Column(name = "carrera", length = 50, nullable = false)
    private String carrera;

    @NotNull
    @Size(min = 5, max = 10)
    @Column(name = "dni", length = 10, nullable = false)
    private String dni;

    @NotNull
    @Size(min = 7, max = 7)
    @Column(name = "legajo", length = 7, nullable = false)
    private String legajo;

    @NotNull
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @OneToMany(mappedBy = "nombreEstudiante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nombreEstudiante" }, allowSetters = true)
    private Set<Reserva> reservas = new HashSet<>();

    @OneToMany(mappedBy = "nombreEstudiante")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "nombreEstudiante", "ejemplar" }, allowSetters = true)
    private Set<Prestamo> prestamos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Estudiante id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEstudiante() {
        return this.nombreEstudiante;
    }

    public Estudiante nombreEstudiante(String nombreEstudiante) {
        this.setNombreEstudiante(nombreEstudiante);
        return this;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getApellidoEstudiante() {
        return this.apellidoEstudiante;
    }

    public Estudiante apellidoEstudiante(String apellidoEstudiante) {
        this.setApellidoEstudiante(apellidoEstudiante);
        return this;
    }

    public void setApellidoEstudiante(String apellidoEstudiante) {
        this.apellidoEstudiante = apellidoEstudiante;
    }

    public String getCarrera() {
        return this.carrera;
    }

    public Estudiante carrera(String carrera) {
        this.setCarrera(carrera);
        return this;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getDni() {
        return this.dni;
    }

    public Estudiante dni(String dni) {
        this.setDni(dni);
        return this;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getLegajo() {
        return this.legajo;
    }

    public Estudiante legajo(String legajo) {
        this.setLegajo(legajo);
        return this;
    }

    public void setLegajo(String legajo) {
        this.legajo = legajo;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public Estudiante fechaNacimiento(LocalDate fechaNacimiento) {
        this.setFechaNacimiento(fechaNacimiento);
        return this;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Set<Reserva> getReservas() {
        return this.reservas;
    }

    public void setReservas(Set<Reserva> reservas) {
        if (this.reservas != null) {
            this.reservas.forEach(i -> i.setNombreEstudiante(null));
        }
        if (reservas != null) {
            reservas.forEach(i -> i.setNombreEstudiante(this));
        }
        this.reservas = reservas;
    }

    public Estudiante reservas(Set<Reserva> reservas) {
        this.setReservas(reservas);
        return this;
    }

    public Estudiante addReserva(Reserva reserva) {
        this.reservas.add(reserva);
        reserva.setNombreEstudiante(this);
        return this;
    }

    public Estudiante removeReserva(Reserva reserva) {
        this.reservas.remove(reserva);
        reserva.setNombreEstudiante(null);
        return this;
    }

    public Set<Prestamo> getPrestamos() {
        return this.prestamos;
    }

    public void setPrestamos(Set<Prestamo> prestamos) {
        if (this.prestamos != null) {
            this.prestamos.forEach(i -> i.setNombreEstudiante(null));
        }
        if (prestamos != null) {
            prestamos.forEach(i -> i.setNombreEstudiante(this));
        }
        this.prestamos = prestamos;
    }

    public Estudiante prestamos(Set<Prestamo> prestamos) {
        this.setPrestamos(prestamos);
        return this;
    }

    public Estudiante addPrestamo(Prestamo prestamo) {
        this.prestamos.add(prestamo);
        prestamo.setNombreEstudiante(this);
        return this;
    }

    public Estudiante removePrestamo(Prestamo prestamo) {
        this.prestamos.remove(prestamo);
        prestamo.setNombreEstudiante(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Estudiante)) {
            return false;
        }
        return id != null && id.equals(((Estudiante) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Estudiante{" +
            "id=" + getId() +
            ", nombreEstudiante='" + getNombreEstudiante() + "'" +
            ", apellidoEstudiante='" + getApellidoEstudiante() + "'" +
            ", carrera='" + getCarrera() + "'" +
            ", dni='" + getDni() + "'" +
            ", legajo='" + getLegajo() + "'" +
            ", fechaNacimiento='" + getFechaNacimiento() + "'" +
            "}";
    }
}
