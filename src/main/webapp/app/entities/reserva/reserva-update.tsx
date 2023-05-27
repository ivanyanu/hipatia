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
import { IReserva } from 'app/shared/model/reserva.model';
import { EstadoReserva } from 'app/shared/model/enumerations/estado-reserva.model';
import { getEntity, updateEntity, createEntity, reset } from './reserva.reducer';

export const ReservaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const estudiantes = useAppSelector(state => state.estudiante.entities);
  const reservaEntity = useAppSelector(state => state.reserva.entity);
  const loading = useAppSelector(state => state.reserva.loading);
  const updating = useAppSelector(state => state.reserva.updating);
  const updateSuccess = useAppSelector(state => state.reserva.updateSuccess);
  const estadoReservaValues = Object.keys(EstadoReserva);

  const handleClose = () => {
    navigate('/reserva');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getEstudiantes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...reservaEntity,
      ...values,
      nombreEstudiante: estudiantes.find(it => it.id.toString() === values.nombreEstudiante.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          estadoReserva: 'SOLICITADA',
          ...reservaEntity,
          nombreEstudiante: reservaEntity?.nombreEstudiante?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hipatiaApp.reserva.home.createOrEditLabel" data-cy="ReservaCreateUpdateHeading">
            <Translate contentKey="hipatiaApp.reserva.home.createOrEditLabel">Create or edit a Reserva</Translate>
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
                  id="reserva-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hipatiaApp.reserva.estadoReserva')}
                id="reserva-estadoReserva"
                name="estadoReserva"
                data-cy="estadoReserva"
                type="select"
              >
                {estadoReservaValues.map(estadoReserva => (
                  <option value={estadoReserva} key={estadoReserva}>
                    {translate('hipatiaApp.EstadoReserva.' + estadoReserva)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="reserva-nombreEstudiante"
                name="nombreEstudiante"
                data-cy="nombreEstudiante"
                label={translate('hipatiaApp.reserva.nombreEstudiante')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reserva" replace color="info">
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

export default ReservaUpdate;
