export interface ITerminalFunctions {
  id?: number;
  functionCode?: string;
  terminalFunctionality?: string;
}

export class TerminalFunctions implements ITerminalFunctions {
  constructor(public id?: number, public functionCode?: string, public terminalFunctionality?: string) {}
}

export function getTerminalFunctionsIdentifier(terminalFunctions: ITerminalFunctions): number | undefined {
  return terminalFunctions.id;
}
