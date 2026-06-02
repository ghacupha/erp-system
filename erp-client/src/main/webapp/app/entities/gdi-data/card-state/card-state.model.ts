import { CardStateFlagTypes } from 'app/entities/enumerations/card-state-flag-types.model';

export interface ICardState {
  id?: number;
  cardStateFlag?: CardStateFlagTypes;
  cardStateFlagDetails?: string;
  cardStateFlagDescription?: string | null;
}

export class CardState implements ICardState {
  constructor(
    public id?: number,
    public cardStateFlag?: CardStateFlagTypes,
    public cardStateFlagDetails?: string,
    public cardStateFlagDescription?: string | null
  ) {}
}

export function getCardStateIdentifier(cardState: ICardState): number | undefined {
  return cardState.id;
}
