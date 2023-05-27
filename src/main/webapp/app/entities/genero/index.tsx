import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Genero from './genero';
import GeneroDetail from './genero-detail';
import GeneroUpdate from './genero-update';
import GeneroDeleteDialog from './genero-delete-dialog';

const GeneroRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Genero />} />
    <Route path="new" element={<GeneroUpdate />} />
    <Route path=":id">
      <Route index element={<GeneroDetail />} />
      <Route path="edit" element={<GeneroUpdate />} />
      <Route path="delete" element={<GeneroDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default GeneroRoutes;
