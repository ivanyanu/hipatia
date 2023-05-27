import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './prestamo.reducer';

export const PrestamoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const prestamoEntity = useAppSelector(state => state.prestamo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="prestamoDetailsHeading">
          <Translate contentKey="hipatiaApp.prestamo.detail.title">Prestamo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{prestamoEntity.id}</dd>
          <dt>
            <span id="estadoPrestamo">
              <Translate contentKey="hipatiaApp.prestamo.estadoPrestamo">Estado Prestamo</Translate>
            </span>
          </dt>
          <dd>{prestamoEntity.estadoPrestamo}</dd>
          <dt>
            <span id="fechaPrestamo">
              <Translate contentKey="hipatiaApp.prestamo.fechaPrestamo">Fecha Prestamo</Translate>
            </span>
          </dt>
          <dd>
            {prestamoEntity.fechaPrestamo ? <TextFormat value={prestamoEntity.fechaPrestamo} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="fechaDevolucion">
              <Translate contentKey="hipatiaApp.prestamo.fechaDevolucion">Fecha Devolucion</Translate>
            </span>
          </dt>
          <dd>
            {prestamoEntity.fechaDevolucion ? (
              <TextFormat value={prestamoEntity.fechaDevolucion} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="hipatiaApp.prestamo.nombreEstudiante">Nombre Estudiante</Translate>
          </dt>
          <dd>{prestamoEntity.nombreEstudiante ? prestamoEntity.nombreEstudiante.id : ''}</dd>
          <dt>
            <Translate contentKey="hipatiaApp.prestamo.ejemplar">Ejemplar</Translate>
          </dt>
          <dd>{prestamoEntity.ejemplar ? prestamoEntity.ejemplar.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/prestamo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/prestamo/${prestamoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PrestamoDetail;
