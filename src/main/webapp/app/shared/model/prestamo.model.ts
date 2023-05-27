import dayjs from 'dayjs';
import { IEstudiante } from 'app/shared/model/estudiante.model';
import { IEjemplar } from 'app/shared/model/ejemplar.model';
import { EstadoPrestamo } from 'app/shared/model/enumerations/estado-prestamo.model';

export interface IPrestamo {
  id?: number;
  estadoPrestamo?: EstadoPrestamo | null;
  fechaPrestamo?: string | null;
  fechaDevolucion?: string | null;
  nombreEstudiante?: IEstudiante | null;
  ejemplar?: IEjemplar | null;
}

export const defaultValue: Readonly<IPrestamo> = {};
