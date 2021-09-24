///
/// Payment Records - Payment records is part of the ERP System
/// Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import {Dayjs} from "dayjs";
import * as dayjs from "dayjs";
import {CurrencyTypes} from "../../../../entities/enumerations/currency-types.model";

export const DEFAULT_CATEGORY = "CATEGORY0";
export const DEFAULT_DATE: Dayjs = dayjs();
export const DEFAULT_CURRENCY = CurrencyTypes.KES;
export const DEFAULT_TRANSACTION_AMOUNT = 0.0;
export const DEFAULT_INVOICE_AMOUNT = 0.0;
export const DEFAULT_DISBURSEMENT_COST = 0.0;
export const DEFAULT_VATABLE_AMOUNT = 0.0;
export const DEEFAULT_CONVERSION_RATE = 1.0;
export const DEFAULT_DESCRIPTION = "SETTLEMENT"
