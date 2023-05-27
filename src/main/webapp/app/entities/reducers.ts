import estudiante from 'app/entities/estudiante/estudiante.reducer';
import editorial from 'app/entities/editorial/editorial.reducer';
import libro from 'app/entities/libro/libro.reducer';
import autor from 'app/entities/autor/autor.reducer';
import ejemplar from 'app/entities/ejemplar/ejemplar.reducer';
import reserva from 'app/entities/reserva/reserva.reducer';
import prestamo from 'app/entities/prestamo/prestamo.reducer';
import genero from 'app/entities/genero/genero.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  estudiante,
  editorial,
  libro,
  autor,
  ejemplar,
  reserva,
  prestamo,
  genero,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
