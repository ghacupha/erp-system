package io.github.erp.internal.model.mapping;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.domain.enumeration.CurrencyTypes;

import static io.github.erp.domain.enumeration.CurrencyTypes.AED;
import static io.github.erp.domain.enumeration.CurrencyTypes.CAD;
import static io.github.erp.domain.enumeration.CurrencyTypes.CHF;
import static io.github.erp.domain.enumeration.CurrencyTypes.EUR;
import static io.github.erp.domain.enumeration.CurrencyTypes.GBP;
import static io.github.erp.domain.enumeration.CurrencyTypes.INR;
import static io.github.erp.domain.enumeration.CurrencyTypes.JPY;
import static io.github.erp.domain.enumeration.CurrencyTypes.KES;
import static io.github.erp.domain.enumeration.CurrencyTypes.TZS;
import static io.github.erp.domain.enumeration.CurrencyTypes.UGX;
import static io.github.erp.domain.enumeration.CurrencyTypes.USD;
import static io.github.erp.domain.enumeration.CurrencyTypes.ZAR;

public class CurrencyMappingUtility {

    public static CurrencyTypes stringToEnum(String currencyCode){

        switch (currencyCode) {
            case "KES":  return KES;
            case "USD":  return USD;
            case "GBP":  return GBP;
            case "EUR":  return EUR;
            case "INR":  return INR;
            case "ZAR":  return ZAR;
            case "AED":  return AED;
            case "CHF":  return CHF;
            case "UGX":  return UGX;
            case "TZS":  return TZS;
            case "JPY":  return JPY;
            case "CAD":  return CAD;
            default: throw new IllegalArgumentException("The currency code" + currencyCode + " is not currently mapped or supported in the system. " +
                "You want to talk to the maintainer about this");
        }

    }

    public static String enumToString(CurrencyTypes currencyCode){

        switch (currencyCode) {
            case KES:  return "KES";
            case USD:  return "USD";
            case GBP:  return "GBP";
            case EUR:  return "EUR";
            case INR:  return "INR";
            case ZAR:  return "ZAR";
            case AED:  return "AED";
            case CHF:  return "CHF";
            case UGX:  return "UGX";
            case TZS:  return "TZS";
            case JPY:  return "JPY";
            case CAD:  return "CAD";
            default: throw new IllegalArgumentException("The currency code" + currencyCode + " is not currently mapped or supported in the system. " +
                "You want to talk to the maintainer about this");
        }

    }
}
