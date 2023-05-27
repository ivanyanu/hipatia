import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './libro.reducer';

export const LibroDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const libroEntity = useAppSelector(state => state.libro.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="libroDetailsHeading">
          <Translate contentKey="hipatiaApp.libro.detail.title">Libro</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{libroEntity.id}</dd>
          <dt>
            <span id="nombreLibro">
              <Translate contentKey="hipatiaApp.libro.nombreLibro">Nombre Libro</Translate>
            </span>
          </dt>
          <dd>{libroEntity.nombreLibro}</dd>
          <dt>
            <span id="isbn">
              <Translate contentKey="hipatiaApp.libro.isbn">Isbn</Translate>
            </span>
          </dt>
          <dd>{libroEntity.isbn}</dd>
          <dt>
            <span id="fechaPublicacion">
              <Translate contentKey="hipatiaApp.libro.fechaPublicacion">Fecha Publicacion</Translate>
            </span>
          </dt>
          <dd>
            {libroEntity.fechaPublicacion ? (
              <TextFormat value={libroEntity.fechaPublicacion} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="hipatiaApp.libro.genero">Genero</Translate>
          </dt>
          <dd>{libroEntity.genero ? libroEntity.genero.id : ''}</dd>
          <dt>
            <Translate contentKey="hipatiaApp.libro.editorial">Editorial</Translate>
          </dt>
          <dd>{libroEntity.editorial ? libroEntity.editorial.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/libro" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/libro/${libroEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LibroDetail;
