import * as dayjs from 'dayjs';

export interface ICardFraudInformation {
  id?: number;
  reportingDate?: dayjs.Dayjs;
  totalNumberOfFraudIncidents?: number;
  valueOfFraudIncedentsInLCY?: number;
}

export class CardFraudInformation implements ICardFraudInformation {
  constructor(
    public id?: number,
    public reportingDate?: dayjs.Dayjs,
    public totalNumberOfFraudIncidents?: number,
    public valueOfFraudIncedentsInLCY?: number
  ) {}
}

export function getCardFraudInformationIdentifier(cardFraudInformation: ICardFraudInformation): number | undefined {
  return cardFraudInformation.id;
}
