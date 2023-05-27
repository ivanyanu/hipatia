import { ILibro } from 'app/shared/model/libro.model';

export interface IAutor {
  id?: number;
  nombreAutor?: string;
  apellidoAutor?: string;
  origenAutor?: string;
  libros?: ILibro[] | null;
}

export const defaultValue: Readonly<IAutor> = {};
