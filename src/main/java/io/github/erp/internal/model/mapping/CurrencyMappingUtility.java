package io.github.erp.internal.model.mapping;

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
