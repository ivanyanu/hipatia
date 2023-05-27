import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Estudiante from './estudiante';
import EstudianteDetail from './estudiante-detail';
import EstudianteUpdate from './estudiante-update';
import EstudianteDeleteDialog from './estudiante-delete-dialog';

const EstudianteRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Estudiante />} />
    <Route path="new" element={<EstudianteUpdate />} />
    <Route path=":id">
      <Route index element={<EstudianteDetail />} />
      <Route path="edit" element={<EstudianteUpdate />} />
      <Route path="delete" element={<EstudianteDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EstudianteRoutes;
