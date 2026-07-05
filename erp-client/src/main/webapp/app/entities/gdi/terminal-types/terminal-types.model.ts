export interface ITerminalTypes {
  id?: number;
  txnTerminalTypeCode?: string;
  txnChannelType?: string;
  txnChannelTypeDetails?: string | null;
}

export class TerminalTypes implements ITerminalTypes {
  constructor(
    public id?: number,
    public txnTerminalTypeCode?: string,
    public txnChannelType?: string,
    public txnChannelTypeDetails?: string | null
  ) {}
}

export function getTerminalTypesIdentifier(terminalTypes: ITerminalTypes): number | undefined {
  return terminalTypes.id;
}
