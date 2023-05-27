import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './reserva.reducer';

export const ReservaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const reservaEntity = useAppSelector(state => state.reserva.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservaDetailsHeading">
          <Translate contentKey="hipatiaApp.reserva.detail.title">Reserva</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.id}</dd>
          <dt>
            <span id="estadoReserva">
              <Translate contentKey="hipatiaApp.reserva.estadoReserva">Estado Reserva</Translate>
            </span>
          </dt>
          <dd>{reservaEntity.estadoReserva}</dd>
          <dt>
            <Translate contentKey="hipatiaApp.reserva.nombreEstudiante">Nombre Estudiante</Translate>
          </dt>
          <dd>{reservaEntity.nombreEstudiante ? reservaEntity.nombreEstudiante.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reserva" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reserva/${reservaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ReservaDetail;
