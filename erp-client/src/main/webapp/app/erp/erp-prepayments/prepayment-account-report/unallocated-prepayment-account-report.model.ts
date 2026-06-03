export interface IUnallocatedPrepaymentAccountReport {
  prepaymentAccountId?: number;
  catalogueNumber?: string | null;
  particulars?: string | null;
  recognitionDate?: string | null;
  dealerName?: string | null;
  debitAccountNumber?: string | null;
  debitAccountName?: string | null;
  transferAccountNumber?: string | null;
  transferAccountName?: string | null;
  currencyCode?: string | null;
  prepaymentAmount?: number | null;
  amortisedAmount?: number | null;
  outstandingAmount?: number | null;
  amortizationEntryCount?: number | null;
  lastAmortizationDate?: string | null;
}
