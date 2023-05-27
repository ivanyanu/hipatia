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
 * A Autor.
 */
@Entity
@Table(name = "autor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Autor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "nombre_autor", length = 50, nullable = false)
    private String nombreAutor;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "apellido_autor", length = 50, nullable = false)
    private String apellidoAutor;

    @NotNull
    @Size(max = 50)
    @Column(name = "origen_autor", length = 50, nullable = false)
    private String origenAutor;

    @ManyToMany
    @JoinTable(name = "rel_autor__libro", joinColumns = @JoinColumn(name = "autor_id"), inverseJoinColumns = @JoinColumn(name = "libro_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ejemplars", "genero", "editorial", "autors" }, allowSetters = true)
    private Set<Libro> libros = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Autor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreAutor() {
        return this.nombreAutor;
    }

    public Autor nombreAutor(String nombreAutor) {
        this.setNombreAutor(nombreAutor);
        return this;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public String getApellidoAutor() {
        return this.apellidoAutor;
    }

    public Autor apellidoAutor(String apellidoAutor) {
        this.setApellidoAutor(apellidoAutor);
        return this;
    }

    public void setApellidoAutor(String apellidoAutor) {
        this.apellidoAutor = apellidoAutor;
    }

    public String getOrigenAutor() {
        return this.origenAutor;
    }

    public Autor origenAutor(String origenAutor) {
        this.setOrigenAutor(origenAutor);
        return this;
    }

    public void setOrigenAutor(String origenAutor) {
        this.origenAutor = origenAutor;
    }

    public Set<Libro> getLibros() {
        return this.libros;
    }

    public void setLibros(Set<Libro> libros) {
        this.libros = libros;
    }

    public Autor libros(Set<Libro> libros) {
        this.setLibros(libros);
        return this;
    }

    public Autor addLibro(Libro libro) {
        this.libros.add(libro);
        libro.getAutors().add(this);
        return this;
    }

    public Autor removeLibro(Libro libro) {
        this.libros.remove(libro);
        libro.getAutors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Autor)) {
            return false;
        }
        return id != null && id.equals(((Autor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Autor{" +
            "id=" + getId() +
            ", nombreAutor='" + getNombreAutor() + "'" +
            ", apellidoAutor='" + getApellidoAutor() + "'" +
            ", origenAutor='" + getOrigenAutor() + "'" +
            "}";
    }
}
