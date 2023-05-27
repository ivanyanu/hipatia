import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAutor } from 'app/shared/model/autor.model';
import { getEntities } from './autor.reducer';

export const Autor = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const autorList = useAppSelector(state => state.autor.entities);
  const loading = useAppSelector(state => state.autor.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="autor-heading" data-cy="AutorHeading">
        <Translate contentKey="hipatiaApp.autor.home.title">Autors</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hipatiaApp.autor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/autor/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hipatiaApp.autor.home.createLabel">Create new Autor</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {autorList && autorList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="hipatiaApp.autor.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.autor.nombreAutor">Nombre Autor</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.autor.apellidoAutor">Apellido Autor</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.autor.origenAutor">Origen Autor</Translate>
                </th>
                <th>
                  <Translate contentKey="hipatiaApp.autor.libro">Libro</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {autorList.map((autor, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/autor/${autor.id}`} color="link" size="sm">
                      {autor.id}
                    </Button>
                  </td>
                  <td>{autor.nombreAutor}</td>
                  <td>{autor.apellidoAutor}</td>
                  <td>{autor.origenAutor}</td>
                  <td>
                    {autor.libros
                      ? autor.libros.map((val, j) => (
                          <span key={j}>
                            <Link to={`/libro/${val.id}`}>{val.nombreLibro}</Link>
                            {j === autor.libros.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/autor/${autor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/autor/${autor.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/autor/${autor.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="hipatiaApp.autor.home.notFound">No Autors found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Autor;
