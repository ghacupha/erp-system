import { IPlaceholder } from 'app/entities/erpService/placeholder/placeholder.model';

export interface IMessageToken {
  id?: number;
  description?: string | null;
  timeSent?: number;
  tokenValue?: string;
  received?: boolean | null;
  actioned?: boolean | null;
  contentFullyEnqueued?: boolean | null;
  placeholders?: IPlaceholder[] | null;
}

export class MessageToken implements IMessageToken {
  constructor(
    public id?: number,
    public description?: string | null,
    public timeSent?: number,
    public tokenValue?: string,
    public received?: boolean | null,
    public actioned?: boolean | null,
    public contentFullyEnqueued?: boolean | null,
    public placeholders?: IPlaceholder[] | null
  ) {
    this.received = this.received ?? false;
    this.actioned = this.actioned ?? false;
    this.contentFullyEnqueued = this.contentFullyEnqueued ?? false;
  }
}

export function getMessageTokenIdentifier(messageToken: IMessageToken): number | undefined {
  return messageToken.id;
}
