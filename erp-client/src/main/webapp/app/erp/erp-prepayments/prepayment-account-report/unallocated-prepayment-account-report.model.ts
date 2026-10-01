///
/// Erp System - Mark X No 12 (Kadar Series) Client 1.8.0
/// Copyright © 2021 - 2026 Edwin Njeru (mailnjeru@gmail.com)
///
/// This program is free software: you can redistribute it and/or modify
/// it under the terms of the GNU General Public License as published by
/// the Free Software Foundation, either version 3 of the License, or
/// (at your option) any later version.
///
/// This program is distributed in the hope that it will be useful,
/// but WITHOUT ANY WARRANTY; without even the implied warranty of
/// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/// GNU General Public License for more details.
///
/// You should have received a copy of the GNU General Public License
/// along with this program. If not, see <http://www.gnu.org/licenses/>.
///

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
