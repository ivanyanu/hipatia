import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ejemplar from './ejemplar';
import EjemplarDetail from './ejemplar-detail';
import EjemplarUpdate from './ejemplar-update';
import EjemplarDeleteDialog from './ejemplar-delete-dialog';

const EjemplarRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ejemplar />} />
    <Route path="new" element={<EjemplarUpdate />} />
    <Route path=":id">
      <Route index element={<EjemplarDetail />} />
      <Route path="edit" element={<EjemplarUpdate />} />
      <Route path="delete" element={<EjemplarDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EjemplarRoutes;
