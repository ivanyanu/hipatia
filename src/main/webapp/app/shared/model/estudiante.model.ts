import dayjs from 'dayjs';
import { IReserva } from 'app/shared/model/reserva.model';
import { IPrestamo } from 'app/shared/model/prestamo.model';

export interface IEstudiante {
  id?: number;
  nombreEstudiante?: string;
  apellidoEstudiante?: string;
  carrera?: string;
  dni?: string;
  legajo?: string;
  fechaNacimiento?: string;
  reservas?: IReserva[] | null;
  prestamos?: IPrestamo[] | null;
}

export const defaultValue: Readonly<IEstudiante> = {};
