import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './estudiante.reducer';

export const EstudianteDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const estudianteEntity = useAppSelector(state => state.estudiante.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="estudianteDetailsHeading">
          <Translate contentKey="hipatiaApp.estudiante.detail.title">Estudiante</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.id}</dd>
          <dt>
            <span id="nombreEstudiante">
              <Translate contentKey="hipatiaApp.estudiante.nombreEstudiante">Nombre Estudiante</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.nombreEstudiante}</dd>
          <dt>
            <span id="apellidoEstudiante">
              <Translate contentKey="hipatiaApp.estudiante.apellidoEstudiante">Apellido Estudiante</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.apellidoEstudiante}</dd>
          <dt>
            <span id="carrera">
              <Translate contentKey="hipatiaApp.estudiante.carrera">Carrera</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.carrera}</dd>
          <dt>
            <span id="dni">
              <Translate contentKey="hipatiaApp.estudiante.dni">Dni</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.dni}</dd>
          <dt>
            <span id="legajo">
              <Translate contentKey="hipatiaApp.estudiante.legajo">Legajo</Translate>
            </span>
          </dt>
          <dd>{estudianteEntity.legajo}</dd>
          <dt>
            <span id="fechaNacimiento">
              <Translate contentKey="hipatiaApp.estudiante.fechaNacimiento">Fecha Nacimiento</Translate>
            </span>
          </dt>
          <dd>
            {estudianteEntity.fechaNacimiento ? (
              <TextFormat value={estudianteEntity.fechaNacimiento} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/estudiante" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/estudiante/${estudianteEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EstudianteDetail;
