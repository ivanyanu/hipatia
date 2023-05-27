import { IEstudiante } from 'app/shared/model/estudiante.model';
import { EstadoReserva } from 'app/shared/model/enumerations/estado-reserva.model';

export interface IReserva {
  id?: number;
  estadoReserva?: EstadoReserva;
  nombreEstudiante?: IEstudiante | null;
}

export const defaultValue: Readonly<IReserva> = {};
