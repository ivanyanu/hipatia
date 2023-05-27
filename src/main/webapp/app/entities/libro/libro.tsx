import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILibro } from 'app/shared/model/libro.model';
import { getEntities } from './libro.reducer';

export const Libro = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const libroList = useAppSelector(state => state.libro.entities);
  const loading = useAppSelector(state => state.libro.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="libro-heading" data-cy="LibroHeading">
        <Translate contentKey="hipatiaApp.libro.home.title">Libros</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hipatiaApp.libro.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/libro/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hipatiaApp.libro.home.createLabel">Create new Libro</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {libroList && libroList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hipatiaApp.libro.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.libro.nombreLibro">Nombre Libro</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.libro.isbn">Isbn</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.libro.fechaPublicacion">Fecha Publicacion</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.libro.genero">Genero</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.libro.editorial">Editorial</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {libroList.map((libro, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/libro/${libro.id}`} color="link" size="sm">
                      {libro.id}
                    </Button>
                  </td>
                  <td>{libro.nombreLibro}</td>
                  <td>{libro.isbn}</td>
                  <td>
                    {libro.fechaPublicacion ? (
                      <TextFormat type="date" value={libro.fechaPublicacion} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{libro.genero ? <Link to={`/genero/${libro.genero.id}`}>{libro.genero.id}</Link> : ''}</td>
                  <td>{libro.editorial ? <Link to={`/editorial/${libro.editorial.id}`}>{libro.editorial.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/libro/${libro.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/libro/${libro.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/libro/${libro.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="hipatiaApp.libro.home.notFound">No Libros found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Libro;
