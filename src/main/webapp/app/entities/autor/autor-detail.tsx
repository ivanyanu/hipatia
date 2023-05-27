import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './autor.reducer';

export const AutorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const autorEntity = useAppSelector(state => state.autor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="autorDetailsHeading">
          <Translate contentKey="hipatiaApp.autor.detail.title">Autor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{autorEntity.id}</dd>
          <dt>
            <span id="nombreAutor">
              <Translate contentKey="hipatiaApp.autor.nombreAutor">Nombre Autor</Translate>
            </span>
          </dt>
          <dd>{autorEntity.nombreAutor}</dd>
          <dt>
            <span id="apellidoAutor">
              <Translate contentKey="hipatiaApp.autor.apellidoAutor">Apellido Autor</Translate>
            </span>
          </dt>
          <dd>{autorEntity.apellidoAutor}</dd>
          <dt>
            <span id="origenAutor">
              <Translate contentKey="hipatiaApp.autor.origenAutor">Origen Autor</Translate>
            </span>
          </dt>
          <dd>{autorEntity.origenAutor}</dd>
          <dt>
            <Translate contentKey="hipatiaApp.autor.libro">Libro</Translate>
          </dt>
          <dd>
            {autorEntity.libros
              ? autorEntity.libros.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.nombreLibro}</a>
                    {autorEntity.libros && i === autorEntity.libros.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/autor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/autor/${autorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AutorDetail;
