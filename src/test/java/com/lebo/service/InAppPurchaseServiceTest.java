package com.lebo.service;

import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 14-1-10
 * Time: PM7:28
 */
public class InAppPurchaseServiceTest extends SpringContextTestCase {

    @Autowired
    private InAppPurchaseService inAppPurchaseService;

    @Test
    public void verifyReceipt() {
        String receiptData = "{\n" +
                "    \"signature\" = \"AjHtNEy5yiewlQamAKxxakusAEp8NBq0bbmCyZeOzHIFUm76bZJ1xGRNrTceheP1KN7kKeZkZmLEQ5MIgKLCW2eYki3F8YIuTcEDMUMSkgOs168jMpk9PhWDUWlOsBvd9Vtat6ERO0hRodPWKKYDtJLFS7rNlRRuGeeDVEg/34XIAAADVzCCA1MwggI7oAMCAQICCGUUkU3ZWAS1MA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA5MDYxNTIyMDU1NloXDTE0MDYxNDIyMDU1NlowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMrRjF2ct4IrSdiTChaI0g8pwv/cmHs8p/RwV/rt/91XKVhNl4XIBimKjQQNfgHsDs6yju++DrKJE7uKsphMddKYfFE5rGXsAdBEjBwRIxexTevx3HLEFGAt1moKx509dhxtiIdDgJv2YaVs49B0uJvNdy6SMqNNLHsDLzDS9oZHAgMBAAGjcjBwMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUNh3o4p2C0gEYtTJrDtdDC5FYQzowDgYDVR0PAQH/BAQDAgeAMB0GA1UdDgQWBBSpg4PyGUjFPhJXCBTMzaN+mV8k9TAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAEaSbPjtmN4C/IB3QEpK32RxacCDXdVXAeVReS5FaZxc+t88pQP93BiAxvdW/3eTSMGY5FbeAYL3etqP5gm8wrFojX0ikyVRStQ+/AQ0KEjtqB07kLs9QUe8czR8UGfdM1EumV/UgvDd4NwNYxLQMg4WTQfgkQQVy8GXZwVHgbE/UC6Y7053pGXBk51NPM3woxhd3gSRLvXj+loHsStcTEqe9pBDpmG5+sk4tw+GK3GMeEN5/+e1QT9np/Kl1nj+aBw7C0xsy0bFnaAd1cSS6xdory/CUvM6gtKsmnOOdqTesbp0bs8sn6Wqs0C9dgcxRHuOMZ2tm8npLUm7argOSzQ==\";\n" +
                "    \"purchase-info\" = \"ewoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtcHN0IiA9ICIyMDE0LTAxLTAyIDAwOjMwOjExIEFtZXJpY2EvTG9zX0FuZ2VsZXMiOwoJInVuaXF1ZS1pZGVudGlmaWVyIiA9ICJmZjdjM2IyZGQ5MTVmZjVlYTZjNTQ0ZWIyOTAwYjc1Y2E4ODJlYWQ5IjsKCSJvcmlnaW5hbC10cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDA5NzYyOTM1OCI7CgkiYnZycyIgPSAiMS4wIjsKCSJ0cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDA5NzYyOTM1OCI7CgkicXVhbnRpdHkiID0gIjEiOwoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtbXMiID0gIjEzODg2NTE0MTE5NzUiOwoJInVuaXF1ZS12ZW5kb3ItaWRlbnRpZmllciIgPSAiQTBFREU4MDItQzNDOC00QkI3LTg2MjMtNDhGOUM5QjQxNERGIjsKCSJwcm9kdWN0LWlkIiA9ICJjb20ua3V3YW5na2UucHJvZHVjdGlvbjEwMDEiOwoJIml0ZW0taWQiID0gIjc5MDIwOTIwMSI7CgkiYmlkIiA9ICJjb20ua3V3YW5na2Uuc3VwZXJtYW4iOwoJInB1cmNoYXNlLWRhdGUtbXMiID0gIjEzODg2NTE0MTE5NzUiOwoJInB1cmNoYXNlLWRhdGUiID0gIjIwMTQtMDEtMDIgMDg6MzA6MTEgRXRjL0dNVCI7CgkicHVyY2hhc2UtZGF0ZS1wc3QiID0gIjIwMTQtMDEtMDIgMDA6MzA6MTEgQW1lcmljYS9Mb3NfQW5nZWxlcyI7Cgkib3JpZ2luYWwtcHVyY2hhc2UtZGF0ZSIgPSAiMjAxNC0wMS0wMiAwODozMDoxMSBFdGMvR01UIjsKfQ==\";\n" +
                "    \"environment\" = \"Sandbox\";\n" +
                "    \"pod\" = \"100\";\n" +
                "    \"signing-status\" = \"0\";\n" +
                "}";

        inAppPurchaseService.verifyReceipt(receiptData);
    }
}
