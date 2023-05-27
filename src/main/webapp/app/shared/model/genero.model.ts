import { ILibro } from 'app/shared/model/libro.model';

export interface IGenero {
  id?: number;
  nombreGenero?: string;
  descripcionGenero?: string | null;
  libros?: ILibro[] | null;
}

export const defaultValue: Readonly<IGenero> = {};
