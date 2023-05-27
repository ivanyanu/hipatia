import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGenero } from 'app/shared/model/genero.model';
import { getEntities } from './genero.reducer';

export const Genero = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const generoList = useAppSelector(state => state.genero.entities);
  const loading = useAppSelector(state => state.genero.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="genero-heading" data-cy="GeneroHeading">
        <Translate contentKey="hipatiaApp.genero.home.title">Generos</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hipatiaApp.genero.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/genero/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hipatiaApp.genero.home.createLabel">Create new Genero</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {generoList && generoList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hipatiaApp.genero.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.genero.nombreGenero">Nombre Genero</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.genero.descripcionGenero">Descripcion Genero</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {generoList.map((genero, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/genero/${genero.id}`} color="link" size="sm">
                      {genero.id}
                    </Button>
                  </td>
                  <td>{genero.nombreGenero}</td>
                  <td>{genero.descripcionGenero}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/genero/${genero.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/genero/${genero.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/genero/${genero.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="hipatiaApp.genero.home.notFound">No Generos found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Genero;
