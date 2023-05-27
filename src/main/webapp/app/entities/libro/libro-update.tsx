import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IGenero } from 'app/shared/model/genero.model';
import { getEntities as getGeneros } from 'app/entities/genero/genero.reducer';
import { IEditorial } from 'app/shared/model/editorial.model';
import { getEntities as getEditorials } from 'app/entities/editorial/editorial.reducer';
import { IAutor } from 'app/shared/model/autor.model';
import { getEntities as getAutors } from 'app/entities/autor/autor.reducer';
import { ILibro } from 'app/shared/model/libro.model';
import { getEntity, updateEntity, createEntity, reset } from './libro.reducer';

export const LibroUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const generos = useAppSelector(state => state.genero.entities);
  const editorials = useAppSelector(state => state.editorial.entities);
  const autors = useAppSelector(state => state.autor.entities);
  const libroEntity = useAppSelector(state => state.libro.entity);
  const loading = useAppSelector(state => state.libro.loading);
  const updating = useAppSelector(state => state.libro.updating);
  const updateSuccess = useAppSelector(state => state.libro.updateSuccess);

  const handleClose = () => {
    navigate('/libro');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getGeneros({}));
    dispatch(getEditorials({}));
    dispatch(getAutors({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...libroEntity,
      ...values,
      genero: generos.find(it => it.id.toString() === values.genero.toString()),
      editorial: editorials.find(it => it.id.toString() === values.editorial.toString()),
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
          ...libroEntity,
          genero: libroEntity?.genero?.id,
          editorial: libroEntity?.editorial?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hipatiaApp.libro.home.createOrEditLabel" data-cy="LibroCreateUpdateHeading">
            <Translate contentKey="hipatiaApp.libro.home.createOrEditLabel">Create or edit a Libro</Translate>
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
                  id="libro-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hipatiaApp.libro.nombreLibro')}
                id="libro-nombreLibro"
                name="nombreLibro"
                data-cy="nombreLibro"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 5, message: translate('entity.validation.minlength', { min: 5 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                label={translate('hipatiaApp.libro.isbn')}
                id="libro-isbn"
                name="isbn"
                data-cy="isbn"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 13, message: translate('entity.validation.minlength', { min: 13 }) },
                  maxLength: { value: 13, message: translate('entity.validation.maxlength', { max: 13 }) },
                }}
              />
              <ValidatedField
                label={translate('hipatiaApp.libro.fechaPublicacion')}
                id="libro-fechaPublicacion"
                name="fechaPublicacion"
                data-cy="fechaPublicacion"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField id="libro-genero" name="genero" data-cy="genero" label={translate('hipatiaApp.libro.genero')} type="select">
                <option value="" key="0" />
                {generos
                  ? generos.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="libro-editorial"
                name="editorial"
                data-cy="editorial"
                label={translate('hipatiaApp.libro.editorial')}
                type="select"
              >
                <option value="" key="0" />
                {editorials
                  ? editorials.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/libro" replace color="info">
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

export default LibroUpdate;
