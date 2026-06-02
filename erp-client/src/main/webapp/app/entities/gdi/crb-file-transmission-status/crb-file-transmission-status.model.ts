import { SubmittedFileStatusTypes } from 'app/entities/enumerations/submitted-file-status-types.model';

export interface ICrbFileTransmissionStatus {
  id?: number;
  submittedFileStatusTypeCode?: string;
  submittedFileStatusType?: SubmittedFileStatusTypes;
  submittedFileStatusTypeDescription?: string | null;
}

export class CrbFileTransmissionStatus implements ICrbFileTransmissionStatus {
  constructor(
    public id?: number,
    public submittedFileStatusTypeCode?: string,
    public submittedFileStatusType?: SubmittedFileStatusTypes,
    public submittedFileStatusTypeDescription?: string | null
  ) {}
}

export function getCrbFileTransmissionStatusIdentifier(crbFileTransmissionStatus: ICrbFileTransmissionStatus): number | undefined {
  return crbFileTransmissionStatus.id;
}
