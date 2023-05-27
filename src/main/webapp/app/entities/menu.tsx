import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/estudiante">
        <Translate contentKey="global.menu.entities.estudiante" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/editorial">
        <Translate contentKey="global.menu.entities.editorial" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/libro">
        <Translate contentKey="global.menu.entities.libro" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/autor">
        <Translate contentKey="global.menu.entities.autor" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/ejemplar">
        <Translate contentKey="global.menu.entities.ejemplar" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reserva">
        <Translate contentKey="global.menu.entities.reserva" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/prestamo">
        <Translate contentKey="global.menu.entities.prestamo" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/genero">
        <Translate contentKey="global.menu.entities.genero" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
