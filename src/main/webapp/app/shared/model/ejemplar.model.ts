import { IPrestamo } from 'app/shared/model/prestamo.model';
import { ILibro } from 'app/shared/model/libro.model';
import { EstadoEjemplar } from 'app/shared/model/enumerations/estado-ejemplar.model';

export interface IEjemplar {
  id?: number;
  estadoEjemplar?: EstadoEjemplar;
  fechaAltaEjemplar?: string;
  prestamos?: IPrestamo[] | null;
  libro?: ILibro | null;
}

export const defaultValue: Readonly<IEjemplar> = {};
