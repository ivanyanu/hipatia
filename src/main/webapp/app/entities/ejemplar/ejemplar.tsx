import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEjemplar } from 'app/shared/model/ejemplar.model';
import { getEntities } from './ejemplar.reducer';

export const Ejemplar = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ejemplarList = useAppSelector(state => state.ejemplar.entities);
  const loading = useAppSelector(state => state.ejemplar.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="ejemplar-heading" data-cy="EjemplarHeading">
        <Translate contentKey="hipatiaApp.ejemplar.home.title">Ejemplars</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hipatiaApp.ejemplar.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ejemplar/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hipatiaApp.ejemplar.home.createLabel">Create new Ejemplar</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ejemplarList && ejemplarList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hipatiaApp.ejemplar.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.ejemplar.estadoEjemplar">Estado Ejemplar</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.ejemplar.fechaAltaEjemplar">Fecha Alta Ejemplar</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.ejemplar.libro">Libro</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ejemplarList.map((ejemplar, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ejemplar/${ejemplar.id}`} color="link" size="sm">
                      {ejemplar.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`hipatiaApp.EstadoEjemplar.${ejemplar.estadoEjemplar}`} />
                  </td>
                  <td>{ejemplar.fechaAltaEjemplar}</td>
                  <td>{ejemplar.libro ? <Link to={`/libro/${ejemplar.libro.id}`}>{ejemplar.libro.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ejemplar/${ejemplar.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ejemplar/${ejemplar.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ejemplar/${ejemplar.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="hipatiaApp.ejemplar.home.notFound">No Ejemplars found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Ejemplar;
