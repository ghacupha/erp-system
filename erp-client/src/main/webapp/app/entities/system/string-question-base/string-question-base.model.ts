import { IUniversallyUniqueMapping } from 'app/entities/system/universally-unique-mapping/universally-unique-mapping.model';
import { IPlaceholder } from 'app/entities/system/placeholder/placeholder.model';
import { ControlTypes } from 'app/entities/enumerations/control-types.model';

export interface IStringQuestionBase {
  id?: number;
  value?: string | null;
  key?: string;
  label?: string;
  required?: boolean | null;
  order?: number;
  controlType?: ControlTypes;
  placeholder?: string | null;
  iterable?: boolean | null;
  parameters?: IUniversallyUniqueMapping[] | null;
  placeholderItems?: IPlaceholder[] | null;
}

export class StringQuestionBase implements IStringQuestionBase {
  constructor(
    public id?: number,
    public value?: string | null,
    public key?: string,
    public label?: string,
    public required?: boolean | null,
    public order?: number,
    public controlType?: ControlTypes,
    public placeholder?: string | null,
    public iterable?: boolean | null,
    public parameters?: IUniversallyUniqueMapping[] | null,
    public placeholderItems?: IPlaceholder[] | null
  ) {
    this.required = this.required ?? false;
    this.iterable = this.iterable ?? false;
  }
}

export function getStringQuestionBaseIdentifier(stringQuestionBase: IStringQuestionBase): number | undefined {
  return stringQuestionBase.id;
}
