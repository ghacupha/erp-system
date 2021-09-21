export interface IPlaceholder {
  id?: number;
  description?: string;
  token?: string | null;
  containingPlaceholder?: IPlaceholder | null;
}

export class Placeholder implements IPlaceholder {
  constructor(
    public id?: number,
    public description?: string,
    public token?: string | null,
    public containingPlaceholder?: IPlaceholder | null
  ) {}
}

export function getPlaceholderIdentifier(placeholder: IPlaceholder): number | undefined {
  return placeholder.id;
}
