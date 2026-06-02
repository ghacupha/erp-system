export interface ISystemModule {
  id?: number;
  moduleName?: string;
}

export class SystemModule implements ISystemModule {
  constructor(public id?: number, public moduleName?: string) {}
}

export function getSystemModuleIdentifier(systemModule: ISystemModule): number | undefined {
  return systemModule.id;
}
