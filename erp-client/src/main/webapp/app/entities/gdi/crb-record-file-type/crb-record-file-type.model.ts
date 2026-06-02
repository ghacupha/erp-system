export interface ICrbRecordFileType {
  id?: number;
  recordFileTypeCode?: string;
  recordFileType?: string;
  recordFileTypeDetails?: string | null;
}

export class CrbRecordFileType implements ICrbRecordFileType {
  constructor(
    public id?: number,
    public recordFileTypeCode?: string,
    public recordFileType?: string,
    public recordFileTypeDetails?: string | null
  ) {}
}

export function getCrbRecordFileTypeIdentifier(crbRecordFileType: ICrbRecordFileType): number | undefined {
  return crbRecordFileType.id;
}
