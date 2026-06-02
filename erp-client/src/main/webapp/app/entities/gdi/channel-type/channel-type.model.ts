export interface IChannelType {
  id?: number;
  channelsTypeCode?: string;
  channelTypes?: string;
  channelTypeDetails?: string | null;
}

export class ChannelType implements IChannelType {
  constructor(
    public id?: number,
    public channelsTypeCode?: string,
    public channelTypes?: string,
    public channelTypeDetails?: string | null
  ) {}
}

export function getChannelTypeIdentifier(channelType: IChannelType): number | undefined {
  return channelType.id;
}
