export interface IGlMapping {
  id?: number;
  subGLCode?: string;
  subGLDescription?: string | null;
  mainGLCode?: string;
  mainGLDescription?: string | null;
  glType?: string;
}

export class GlMapping implements IGlMapping {
  constructor(
    public id?: number,
    public subGLCode?: string,
    public subGLDescription?: string | null,
    public mainGLCode?: string,
    public mainGLDescription?: string | null,
    public glType?: string
  ) {}
}

export function getGlMappingIdentifier(glMapping: IGlMapping): number | undefined {
  return glMapping.id;
}
