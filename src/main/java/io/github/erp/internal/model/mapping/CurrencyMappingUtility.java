package io.github.erp.internal.model.mapping;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
