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
 * A Libro.
 */
@Entity
@Table(name = "libro")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 5, max = 100)
    @Column(name = "nombre_libro", length = 100, nullable = false)
    private String nombreLibro;

    @NotNull
    @Size(min = 13, max = 13)
    @Column(name = "isbn", length = 13, nullable = false)
    private String isbn;

    @NotNull
    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDate fechaPublicacion;

    @OneToMany(mappedBy = "libro")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "prestamos", "libro" }, allowSetters = true)
    private Set<Ejemplar> ejemplars = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "libros" }, allowSetters = true)
    private Genero genero;

    @ManyToOne
    @JsonIgnoreProperties(value = { "libros" }, allowSetters = true)
    private Editorial editorial;

    @ManyToMany(mappedBy = "libros")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "libros" }, allowSetters = true)
    private Set<Autor> autors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Libro id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreLibro() {
        return this.nombreLibro;
    }

    public Libro nombreLibro(String nombreLibro) {
        this.setNombreLibro(nombreLibro);
        return this;
    }

    public void setNombreLibro(String nombreLibro) {
        this.nombreLibro = nombreLibro;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public Libro isbn(String isbn) {
        this.setIsbn(isbn);
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public LocalDate getFechaPublicacion() {
        return this.fechaPublicacion;
    }

    public Libro fechaPublicacion(LocalDate fechaPublicacion) {
        this.setFechaPublicacion(fechaPublicacion);
        return this;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public Set<Ejemplar> getEjemplars() {
        return this.ejemplars;
    }

    public void setEjemplars(Set<Ejemplar> ejemplars) {
        if (this.ejemplars != null) {
            this.ejemplars.forEach(i -> i.setLibro(null));
        }
        if (ejemplars != null) {
            ejemplars.forEach(i -> i.setLibro(this));
        }
        this.ejemplars = ejemplars;
    }

    public Libro ejemplars(Set<Ejemplar> ejemplars) {
        this.setEjemplars(ejemplars);
        return this;
    }

    public Libro addEjemplar(Ejemplar ejemplar) {
        this.ejemplars.add(ejemplar);
        ejemplar.setLibro(this);
        return this;
    }

    public Libro removeEjemplar(Ejemplar ejemplar) {
        this.ejemplars.remove(ejemplar);
        ejemplar.setLibro(null);
        return this;
    }

    public Genero getGenero() {
        return this.genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Libro genero(Genero genero) {
        this.setGenero(genero);
        return this;
    }

    public Editorial getEditorial() {
        return this.editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Libro editorial(Editorial editorial) {
        this.setEditorial(editorial);
        return this;
    }

    public Set<Autor> getAutors() {
        return this.autors;
    }

    public void setAutors(Set<Autor> autors) {
        if (this.autors != null) {
            this.autors.forEach(i -> i.removeLibro(this));
        }
        if (autors != null) {
            autors.forEach(i -> i.addLibro(this));
        }
        this.autors = autors;
    }

    public Libro autors(Set<Autor> autors) {
        this.setAutors(autors);
        return this;
    }

    public Libro addAutor(Autor autor) {
        this.autors.add(autor);
        autor.getLibros().add(this);
        return this;
    }

    public Libro removeAutor(Autor autor) {
        this.autors.remove(autor);
        autor.getLibros().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Libro)) {
            return false;
        }
        return id != null && id.equals(((Libro) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Libro{" +
            "id=" + getId() +
            ", nombreLibro='" + getNombreLibro() + "'" +
            ", isbn='" + getIsbn() + "'" +
            ", fechaPublicacion='" + getFechaPublicacion() + "'" +
            "}";
    }
}
