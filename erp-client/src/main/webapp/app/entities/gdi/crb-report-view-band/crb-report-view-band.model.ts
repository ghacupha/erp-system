export interface ICrbReportViewBand {
  id?: number;
  reportViewCode?: string;
  reportViewCategory?: string;
  reportViewCategoryDescription?: string | null;
}

export class CrbReportViewBand implements ICrbReportViewBand {
  constructor(
    public id?: number,
    public reportViewCode?: string,
    public reportViewCategory?: string,
    public reportViewCategoryDescription?: string | null
  ) {}
}

export function getCrbReportViewBandIdentifier(crbReportViewBand: ICrbReportViewBand): number | undefined {
  return crbReportViewBand.id;
}
