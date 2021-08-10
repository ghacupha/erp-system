import * as dayjs from 'dayjs';

export interface IFileUpload {
  id?: number;
  description?: string;
  fileName?: string;
  periodFrom?: dayjs.Dayjs | null;
  periodTo?: dayjs.Dayjs | null;
  fileTypeId?: number;
  dataFileContentType?: string;
  dataFile?: string;
  uploadSuccessful?: boolean | null;
  uploadProcessed?: boolean | null;
  uploadToken?: string | null;
}

export class FileUpload implements IFileUpload {
  constructor(
    public id?: number,
    public description?: string,
    public fileName?: string,
    public periodFrom?: dayjs.Dayjs | null,
    public periodTo?: dayjs.Dayjs | null,
    public fileTypeId?: number,
    public dataFileContentType?: string,
    public dataFile?: string,
    public uploadSuccessful?: boolean | null,
    public uploadProcessed?: boolean | null,
    public uploadToken?: string | null
  ) {
    this.uploadSuccessful = this.uploadSuccessful ?? false;
    this.uploadProcessed = this.uploadProcessed ?? false;
  }
}

export function getFileUploadIdentifier(fileUpload: IFileUpload): number | undefined {
  return fileUpload.id;
}
