import { CardPerformanceFlags } from 'app/entities/enumerations/card-performance-flags.model';

export interface ICardPerformanceFlag {
  id?: number;
  cardPerformanceFlag?: CardPerformanceFlags;
  cardPerformanceFlagDescription?: string;
  cardPerformanceFlagDetails?: string | null;
}

export class CardPerformanceFlag implements ICardPerformanceFlag {
  constructor(
    public id?: number,
    public cardPerformanceFlag?: CardPerformanceFlags,
    public cardPerformanceFlagDescription?: string,
    public cardPerformanceFlagDetails?: string | null
  ) {}
}

export function getCardPerformanceFlagIdentifier(cardPerformanceFlag: ICardPerformanceFlag): number | undefined {
  return cardPerformanceFlag.id;
}
