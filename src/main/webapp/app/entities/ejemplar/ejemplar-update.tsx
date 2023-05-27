import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ILibro } from 'app/shared/model/libro.model';
import { getEntities as getLibros } from 'app/entities/libro/libro.reducer';
import { IEjemplar } from 'app/shared/model/ejemplar.model';
import { EstadoEjemplar } from 'app/shared/model/enumerations/estado-ejemplar.model';
import { getEntity, updateEntity, createEntity, reset } from './ejemplar.reducer';

export const EjemplarUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const libros = useAppSelector(state => state.libro.entities);
  const ejemplarEntity = useAppSelector(state => state.ejemplar.entity);
  const loading = useAppSelector(state => state.ejemplar.loading);
  const updating = useAppSelector(state => state.ejemplar.updating);
  const updateSuccess = useAppSelector(state => state.ejemplar.updateSuccess);
  const estadoEjemplarValues = Object.keys(EstadoEjemplar);

  const handleClose = () => {
    navigate('/ejemplar');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLibros({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...ejemplarEntity,
      ...values,
      libro: libros.find(it => it.id.toString() === values.libro.toString()),
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
          estadoEjemplar: 'DISPONIBLE',
          ...ejemplarEntity,
          libro: ejemplarEntity?.libro?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hipatiaApp.ejemplar.home.createOrEditLabel" data-cy="EjemplarCreateUpdateHeading">
            <Translate contentKey="hipatiaApp.ejemplar.home.createOrEditLabel">Create or edit a Ejemplar</Translate>
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
                  id="ejemplar-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hipatiaApp.ejemplar.estadoEjemplar')}
                id="ejemplar-estadoEjemplar"
                name="estadoEjemplar"
                data-cy="estadoEjemplar"
                type="select"
              >
                {estadoEjemplarValues.map(estadoEjemplar => (
                  <option value={estadoEjemplar} key={estadoEjemplar}>
                    {translate('hipatiaApp.EstadoEjemplar.' + estadoEjemplar)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hipatiaApp.ejemplar.fechaAltaEjemplar')}
                id="ejemplar-fechaAltaEjemplar"
                name="fechaAltaEjemplar"
                data-cy="fechaAltaEjemplar"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField id="ejemplar-libro" name="libro" data-cy="libro" label={translate('hipatiaApp.ejemplar.libro')} type="select">
                <option value="" key="0" />
                {libros
                  ? libros.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ejemplar" replace color="info">
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

export default EjemplarUpdate;
