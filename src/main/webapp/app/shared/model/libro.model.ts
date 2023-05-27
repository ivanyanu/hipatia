import dayjs from 'dayjs';
import { IEjemplar } from 'app/shared/model/ejemplar.model';
import { IGenero } from 'app/shared/model/genero.model';
import { IEditorial } from 'app/shared/model/editorial.model';
import { IAutor } from 'app/shared/model/autor.model';

export interface ILibro {
  id?: number;
  nombreLibro?: string;
  isbn?: string;
  fechaPublicacion?: string;
  ejemplars?: IEjemplar[] | null;
  genero?: IGenero | null;
  editorial?: IEditorial | null;
  autors?: IAutor[] | null;
}

export const defaultValue: Readonly<ILibro> = {};
