import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estudiante from './estudiante';
import Editorial from './editorial';
import Libro from './libro';
import Autor from './autor';
import Ejemplar from './ejemplar';
import Reserva from './reserva';
import Prestamo from './prestamo';
import Genero from './genero';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="estudiante/*" element={<Estudiante />} />
        <Route path="editorial/*" element={<Editorial />} />
        <Route path="libro/*" element={<Libro />} />
        <Route path="autor/*" element={<Autor />} />
        <Route path="ejemplar/*" element={<Ejemplar />} />
        <Route path="reserva/*" element={<Reserva />} />
        <Route path="prestamo/*" element={<Prestamo />} />
        <Route path="genero/*" element={<Genero />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
