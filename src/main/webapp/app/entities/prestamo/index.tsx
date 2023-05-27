import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Prestamo from './prestamo';
import PrestamoDetail from './prestamo-detail';
import PrestamoUpdate from './prestamo-update';
import PrestamoDeleteDialog from './prestamo-delete-dialog';

const PrestamoRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Prestamo />} />
    <Route path="new" element={<PrestamoUpdate />} />
    <Route path=":id">
      <Route index element={<PrestamoDetail />} />
      <Route path="edit" element={<PrestamoUpdate />} />
      <Route path="delete" element={<PrestamoDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PrestamoRoutes;
