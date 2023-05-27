import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPrestamo } from 'app/shared/model/prestamo.model';
import { getEntities } from './prestamo.reducer';

export const Prestamo = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const prestamoList = useAppSelector(state => state.prestamo.entities);
  const loading = useAppSelector(state => state.prestamo.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="prestamo-heading" data-cy="PrestamoHeading">
        <Translate contentKey="hipatiaApp.prestamo.home.title">Prestamos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hipatiaApp.prestamo.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/prestamo/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hipatiaApp.prestamo.home.createLabel">Create new Prestamo</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {prestamoList && prestamoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hipatiaApp.prestamo.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.prestamo.estadoPrestamo">Estado Prestamo</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.prestamo.fechaPrestamo">Fecha Prestamo</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.prestamo.fechaDevolucion">Fecha Devolucion</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.prestamo.nombreEstudiante">Nombre Estudiante</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.prestamo.ejemplar">Ejemplar</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {prestamoList.map((prestamo, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/prestamo/${prestamo.id}`} color="link" size="sm">
                      {prestamo.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`hipatiaApp.EstadoPrestamo.${prestamo.estadoPrestamo}`} />
                  </td>
                  <td>
                    {prestamo.fechaPrestamo ? <TextFormat type="date" value={prestamo.fechaPrestamo} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {prestamo.fechaDevolucion ? <TextFormat type="date" value={prestamo.fechaDevolucion} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {prestamo.nombreEstudiante ? (
                      <Link to={`/estudiante/${prestamo.nombreEstudiante.id}`}>{prestamo.nombreEstudiante.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{prestamo.ejemplar ? <Link to={`/ejemplar/${prestamo.ejemplar.id}`}>{prestamo.ejemplar.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/prestamo/${prestamo.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/prestamo/${prestamo.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/prestamo/${prestamo.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="hipatiaApp.prestamo.home.notFound">No Prestamos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Prestamo;
