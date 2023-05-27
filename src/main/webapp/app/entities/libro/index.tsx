import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Libro from './libro';
import LibroDetail from './libro-detail';
import LibroUpdate from './libro-update';
import LibroDeleteDialog from './libro-delete-dialog';

const LibroRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Libro />} />
    <Route path="new" element={<LibroUpdate />} />
    <Route path=":id">
      <Route index element={<LibroDetail />} />
      <Route path="edit" element={<LibroUpdate />} />
      <Route path="delete" element={<LibroDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LibroRoutes;
