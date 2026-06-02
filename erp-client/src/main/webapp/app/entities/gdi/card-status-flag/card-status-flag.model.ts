import { FlagCodes } from 'app/entities/enumerations/flag-codes.model';

export interface ICardStatusFlag {
  id?: number;
  cardStatusFlag?: FlagCodes;
  cardStatusFlagDescription?: string;
  cardStatusFlagDetails?: string | null;
}

export class CardStatusFlag implements ICardStatusFlag {
  constructor(
    public id?: number,
    public cardStatusFlag?: FlagCodes,
    public cardStatusFlagDescription?: string,
    public cardStatusFlagDetails?: string | null
  ) {}
}

export function getCardStatusFlagIdentifier(cardStatusFlag: ICardStatusFlag): number | undefined {
  return cardStatusFlag.id;
}
