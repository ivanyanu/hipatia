import { ILibro } from 'app/shared/model/libro.model';

export interface IEditorial {
  id?: number;
  nombreEditorial?: string;
  cantidadTitulos?: number | null;
  libros?: ILibro[] | null;
}

export const defaultValue: Readonly<IEditorial> = {};
