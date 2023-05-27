import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './editorial.reducer';

export const EditorialDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const editorialEntity = useAppSelector(state => state.editorial.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="editorialDetailsHeading">
          <Translate contentKey="hipatiaApp.editorial.detail.title">Editorial</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{editorialEntity.id}</dd>
          <dt>
            <span id="nombreEditorial">
              <Translate contentKey="hipatiaApp.editorial.nombreEditorial">Nombre Editorial</Translate>
            </span>
          </dt>
          <dd>{editorialEntity.nombreEditorial}</dd>
          <dt>
            <span id="cantidadTitulos">
              <Translate contentKey="hipatiaApp.editorial.cantidadTitulos">Cantidad Titulos</Translate>
            </span>
          </dt>
          <dd>{editorialEntity.cantidadTitulos}</dd>
        </dl>
        <Button tag={Link} to="/editorial" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/editorial/${editorialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EditorialDetail;
