import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IEstudiante } from 'app/shared/model/estudiante.model';
import { getEntities as getEstudiantes } from 'app/entities/estudiante/estudiante.reducer';
import { IEjemplar } from 'app/shared/model/ejemplar.model';
import { getEntities as getEjemplars } from 'app/entities/ejemplar/ejemplar.reducer';
import { IPrestamo } from 'app/shared/model/prestamo.model';
import { EstadoPrestamo } from 'app/shared/model/enumerations/estado-prestamo.model';
import { getEntity, updateEntity, createEntity, reset } from './prestamo.reducer';

export const PrestamoUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estudiantes = useAppSelector(state => state.estudiante.entities);
  const ejemplars = useAppSelector(state => state.ejemplar.entities);
  const prestamoEntity = useAppSelector(state => state.prestamo.entity);
  const loading = useAppSelector(state => state.prestamo.loading);
  const updating = useAppSelector(state => state.prestamo.updating);
  const updateSuccess = useAppSelector(state => state.prestamo.updateSuccess);
  const estadoPrestamoValues = Object.keys(EstadoPrestamo);

  const handleClose = () => {
    navigate('/prestamo');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstudiantes({}));
    dispatch(getEjemplars({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.fechaPrestamo = convertDateTimeToServer(values.fechaPrestamo);
    values.fechaDevolucion = convertDateTimeToServer(values.fechaDevolucion);

    const entity = {
      ...prestamoEntity,
      ...values,
      nombreEstudiante: estudiantes.find(it => it.id.toString() === values.nombreEstudiante.toString()),
      ejemplar: ejemplars.find(it => it.id.toString() === values.ejemplar.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          fechaPrestamo: displayDefaultDateTime(),
          fechaDevolucion: displayDefaultDateTime(),
        }
      : {
          estadoPrestamo: 'INICIADO',
          ...prestamoEntity,
          fechaPrestamo: convertDateTimeFromServer(prestamoEntity.fechaPrestamo),
          fechaDevolucion: convertDateTimeFromServer(prestamoEntity.fechaDevolucion),
          nombreEstudiante: prestamoEntity?.nombreEstudiante?.id,
          ejemplar: prestamoEntity?.ejemplar?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hipatiaApp.prestamo.home.createOrEditLabel" data-cy="PrestamoCreateUpdateHeading">
            <Translate contentKey="hipatiaApp.prestamo.home.createOrEditLabel">Create or edit a Prestamo</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="prestamo-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hipatiaApp.prestamo.estadoPrestamo')}
                id="prestamo-estadoPrestamo"
                name="estadoPrestamo"
                data-cy="estadoPrestamo"
                type="select"
              >
                {estadoPrestamoValues.map(estadoPrestamo => (
                  <option value={estadoPrestamo} key={estadoPrestamo}>
                    {translate('hipatiaApp.EstadoPrestamo.' + estadoPrestamo)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hipatiaApp.prestamo.fechaPrestamo')}
                id="prestamo-fechaPrestamo"
                name="fechaPrestamo"
                data-cy="fechaPrestamo"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hipatiaApp.prestamo.fechaDevolucion')}
                id="prestamo-fechaDevolucion"
                name="fechaDevolucion"
                data-cy="fechaDevolucion"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="prestamo-nombreEstudiante"
                name="nombreEstudiante"
                data-cy="nombreEstudiante"
                label={translate('hipatiaApp.prestamo.nombreEstudiante')}
                type="select"
              >
                <option value="" key="0" />
                {estudiantes
                  ? estudiantes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="prestamo-ejemplar"
                name="ejemplar"
                data-cy="ejemplar"
                label={translate('hipatiaApp.prestamo.ejemplar')}
                type="select"
              >
                <option value="" key="0" />
                {ejemplars
                  ? ejemplars.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/prestamo" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PrestamoUpdate;
