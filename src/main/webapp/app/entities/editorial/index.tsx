import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Editorial from './editorial';
import EditorialDetail from './editorial-detail';
import EditorialUpdate from './editorial-update';
import EditorialDeleteDialog from './editorial-delete-dialog';

const EditorialRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Editorial />} />
    <Route path="new" element={<EditorialUpdate />} />
    <Route path=":id">
      <Route index element={<EditorialDetail />} />
      <Route path="edit" element={<EditorialUpdate />} />
      <Route path="delete" element={<EditorialDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default EditorialRoutes;
