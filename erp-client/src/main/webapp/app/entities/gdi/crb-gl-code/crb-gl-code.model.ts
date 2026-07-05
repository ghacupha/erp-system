export interface ICrbGlCode {
  id?: number;
  glCode?: string;
  glDescription?: string;
  glType?: string;
  institutionCategory?: string;
}

export class CrbGlCode implements ICrbGlCode {
  constructor(
    public id?: number,
    public glCode?: string,
    public glDescription?: string,
    public glType?: string,
    public institutionCategory?: string
  ) {}
}

export function getCrbGlCodeIdentifier(crbGlCode: ICrbGlCode): number | undefined {
  return crbGlCode.id;
}
