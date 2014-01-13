package com.lebo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.utils.Encodes;

import java.util.HashMap;
import java.util.Map;

/**
 * 苹果应用内购买.
 *
 * @author: Wei Liu
 * Date: 14-1-10
 * Time: PM7:03
 */
@Service
public class InAppPurchaseService {

    private RestTemplate restTemplate = new RestTemplate();
    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    @Value("${app_store.verify_receipt_url}")
    private String verifyReceiptUrl;

    public Receipt verifyReceipt(String receiptData) {

        try {

            Map<String, Object> data = new HashMap<String, Object>(1);
            data.put("receipt-data", Encodes.encodeBase64(receiptData.getBytes()));

            String body = jsonMapper.toJson(data);

            String responseText = restTemplate.postForObject(verifyReceiptUrl, body, String.class);

            VerifyReceiptResult verifyReceiptResult = jsonMapper.fromJson(responseText, VerifyReceiptResult.class);

            if (verifyReceiptResult.status == 0) {
                return verifyReceiptResult.getReceipt();
            }

            throw new ServiceException("收据验证失败, status " + verifyReceiptResult.getStatus());

        } catch (Exception e) {

            throw new ServiceException("收据验证失败", e);
        }
    }
    /*
    receiptData:
    {
	"signature" = "AjHtNEy5yiewlQamAKxxakusAEp8NBq0bbmCyZeOzHIFUm76bZJ1xGRNrTceheP1KN7kKeZkZmLEQ5MIgKLCW2eYki3F8YIuTcEDMUMSkgOs168jMpk9PhWDUWlOsBvd9Vtat6ERO0hRodPWKKYDtJLFS7rNlRRuGeeDVEg/34XIAAADVzCCA1MwggI7oAMCAQICCGUUkU3ZWAS1MA0GCSqGSIb3DQEBBQUAMH8xCzAJBgNVBAYTAlVTMRMwEQYDVQQKDApBcHBsZSBJbmMuMSYwJAYDVQQLDB1BcHBsZSBDZXJ0aWZpY2F0aW9uIEF1dGhvcml0eTEzMDEGA1UEAwwqQXBwbGUgaVR1bmVzIFN0b3JlIENlcnRpZmljYXRpb24gQXV0aG9yaXR5MB4XDTA5MDYxNTIyMDU1NloXDTE0MDYxNDIyMDU1NlowZDEjMCEGA1UEAwwaUHVyY2hhc2VSZWNlaXB0Q2VydGlmaWNhdGUxGzAZBgNVBAsMEkFwcGxlIGlUdW5lcyBTdG9yZTETMBEGA1UECgwKQXBwbGUgSW5jLjELMAkGA1UEBhMCVVMwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMrRjF2ct4IrSdiTChaI0g8pwv/cmHs8p/RwV/rt/91XKVhNl4XIBimKjQQNfgHsDs6yju++DrKJE7uKsphMddKYfFE5rGXsAdBEjBwRIxexTevx3HLEFGAt1moKx509dhxtiIdDgJv2YaVs49B0uJvNdy6SMqNNLHsDLzDS9oZHAgMBAAGjcjBwMAwGA1UdEwEB/wQCMAAwHwYDVR0jBBgwFoAUNh3o4p2C0gEYtTJrDtdDC5FYQzowDgYDVR0PAQH/BAQDAgeAMB0GA1UdDgQWBBSpg4PyGUjFPhJXCBTMzaN+mV8k9TAQBgoqhkiG92NkBgUBBAIFADANBgkqhkiG9w0BAQUFAAOCAQEAEaSbPjtmN4C/IB3QEpK32RxacCDXdVXAeVReS5FaZxc+t88pQP93BiAxvdW/3eTSMGY5FbeAYL3etqP5gm8wrFojX0ikyVRStQ+/AQ0KEjtqB07kLs9QUe8czR8UGfdM1EumV/UgvDd4NwNYxLQMg4WTQfgkQQVy8GXZwVHgbE/UC6Y7053pGXBk51NPM3woxhd3gSRLvXj+loHsStcTEqe9pBDpmG5+sk4tw+GK3GMeEN5/+e1QT9np/Kl1nj+aBw7C0xsy0bFnaAd1cSS6xdory/CUvM6gtKsmnOOdqTesbp0bs8sn6Wqs0C9dgcxRHuOMZ2tm8npLUm7argOSzQ==";
	"purchase-info" = "ewoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtcHN0IiA9ICIyMDE0LTAxLTAyIDAwOjMwOjExIEFtZXJpY2EvTG9zX0FuZ2VsZXMiOwoJInVuaXF1ZS1pZGVudGlmaWVyIiA9ICJmZjdjM2IyZGQ5MTVmZjVlYTZjNTQ0ZWIyOTAwYjc1Y2E4ODJlYWQ5IjsKCSJvcmlnaW5hbC10cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDA5NzYyOTM1OCI7CgkiYnZycyIgPSAiMS4wIjsKCSJ0cmFuc2FjdGlvbi1pZCIgPSAiMTAwMDAwMDA5NzYyOTM1OCI7CgkicXVhbnRpdHkiID0gIjEiOwoJIm9yaWdpbmFsLXB1cmNoYXNlLWRhdGUtbXMiID0gIjEzODg2NTE0MTE5NzUiOwoJInVuaXF1ZS12ZW5kb3ItaWRlbnRpZmllciIgPSAiQTBFREU4MDItQzNDOC00QkI3LTg2MjMtNDhGOUM5QjQxNERGIjsKCSJwcm9kdWN0LWlkIiA9ICJjb20ua3V3YW5na2UucHJvZHVjdGlvbjEwMDEiOwoJIml0ZW0taWQiID0gIjc5MDIwOTIwMSI7CgkiYmlkIiA9ICJjb20ua3V3YW5na2Uuc3VwZXJtYW4iOwoJInB1cmNoYXNlLWRhdGUtbXMiID0gIjEzODg2NTE0MTE5NzUiOwoJInB1cmNoYXNlLWRhdGUiID0gIjIwMTQtMDEtMDIgMDg6MzA6MTEgRXRjL0dNVCI7CgkicHVyY2hhc2UtZGF0ZS1wc3QiID0gIjIwMTQtMDEtMDIgMDA6MzA6MTEgQW1lcmljYS9Mb3NfQW5nZWxlcyI7Cgkib3JpZ2luYWwtcHVyY2hhc2UtZGF0ZSIgPSAiMjAxNC0wMS0wMiAwODozMDoxMSBFdGMvR01UIjsKfQ==";
	"environment" = "Sandbox";
	"pod" = "100";
	"signing-status" = "0";
}
     */
    //14:20:06.509 [main] DEBUG o.s.web.client.RestTemplate - Created POST request for "https://sandbox.itunes.apple.com/verifyReceipt"
    //14:20:06.512 [main] DEBUG o.s.web.client.RestTemplate - Setting request Accept header to [text/plain, application/json, application/*+json, */*]
    //14:20:06.514 [main] DEBUG o.s.web.client.RestTemplate - Writing [{"receipt-data":"ewogICAgInNpZ25hdHVyZSIgPSAiQWpIdE5FeTV5aWV3bFFhbUFLeHhha3VzQUVwOE5CcTBiYm1DeVplT3pISUZVbTc2YlpKMXhHUk5yVGNlaGVQMUtON2tLZVprWm1MRVE1TUlnS0xDVzJlWWtpM0Y4WUl1VGNFRE1VTVNrZ09zMTY4ak1wazlQaFdEVVdsT3NCdmQ5VnRhdDZFUk8waFJvZFBXS0tZRHRKTEZTN3JObFJSdUdlZURWRWcvMzRYSUFBQURWekNDQTFNd2dnSTdvQU1DQVFJQ0NHVVVrVTNaV0FTMU1BMEdDU3FHU0liM0RRRUJCUVVBTUg4eEN6QUpCZ05WQkFZVEFsVlRNUk13RVFZRFZRUUtEQXBCY0hCc1pTQkpibU11TVNZd0pBWURWUVFMREIxQmNIQnNaU0JEWlhKMGFXWnBZMkYwYVc5dUlFRjFkR2h2Y21sMGVURXpNREVHQTFVRUF3d3FRWEJ3YkdVZ2FWUjFibVZ6SUZOMGIzSmxJRU5sY25ScFptbGpZWFJwYjI0Z1FYVjBhRzl5YVhSNU1CNFhEVEE1TURZeE5USXlNRFUxTmxvWERURTBNRFl4TkRJeU1EVTFObG93WkRFak1DRUdBMVVFQXd3YVVIVnlZMmhoYzJWU1pXTmxhWEIwUTJWeWRHbG1hV05oZEdVeEd6QVpCZ05WQkFzTUVrRndjR3hsSUdsVWRXNWxjeUJUZEc5eVpURVRNQkVHQTFVRUNnd0tRWEJ3YkdVZ1NXNWpMakVMTUFrR0ExVUVCaE1DVlZNd2daOHdEUVlKS29aSWh2Y05BUUVCQlFBRGdZMEFNSUdKQW9HQkFNclJqRjJjdDRJclNkaVRDaGFJMGc4cHd2L2NtSHM4cC9Sd1YvcnQvOTFYS1ZoTmw0WElCaW1LalFRTmZnSHNEczZ5anUrK0RyS0pFN3VLc3BoTWRkS1lmRkU1ckdYc0FkQkVqQndSSXhleFRldngzSExFRkdBdDFtb0t4NTA5ZGh4dGlJZERnSnYyWWFWczQ5QjB1SnZOZHk2U01xTk5MSHNETHpEUzlvWkhBZ01CQUFHamNqQndNQXdHQTFVZEV3RUIvd1FDTUFBd0h3WURWUjBqQkJnd0ZvQVVOaDNvNHAyQzBnRVl0VEpyRHRkREM1RllRem93RGdZRFZSMFBBUUgvQkFRREFnZUFNQjBHQTFVZERnUVdCQlNwZzRQeUdVakZQaEpYQ0JUTXphTittVjhrOVRBUUJnb3Foa2lHOTJOa0JnVUJCQUlGQURBTkJna3Foa2lHOXcwQkFRVUZBQU9DQVFFQUVhU2JQanRtTjRDL0lCM1FFcEszMlJ4YWNDRFhkVlhBZVZSZVM1RmFaeGMrdDg4cFFQOTNCaUF4dmRXLzNlVFNNR1k1RmJlQVlMM2V0cVA1Z204d3JGb2pYMGlreVZSU3RRKy9BUTBLRWp0cUIwN2tMczlRVWU4Y3pSOFVHZmRNMUV1bVYvVWd2RGQ0TndOWXhMUU1nNFdUUWZna1FRVnk4R1had1ZIZ2JFL1VDNlk3MDUzcEdYQms1MU5QTTN3b3hoZDNnU1JMdlhqK2xvSHNTdGNURXFlOXBCRHBtRzUrc2s0dHcrR0szR01lRU41LytlMVFUOW5wL0tsMW5qK2FCdzdDMHhzeTBiRm5hQWQxY1NTNnhkb3J5L0NVdk02Z3RLc21uT09kcVRlc2JwMGJzOHNuNldxczBDOWRnY3hSSHVPTVoydG04bnBMVW03YXJnT1N6UT09IjsKICAgICJwdXJjaGFzZS1pbmZvIiA9ICJld29KSW05eWFXZHBibUZzTFhCMWNtTm9ZWE5sTFdSaGRHVXRjSE4wSWlBOUlDSXlNREUwTFRBeExUQXlJREF3T2pNd09qRXhJRUZ0WlhKcFkyRXZURzl6WDBGdVoyVnNaWE1pT3dvSkluVnVhWEYxWlMxcFpHVnVkR2xtYVdWeUlpQTlJQ0ptWmpkak0ySXlaR1E1TVRWbVpqVmxZVFpqTlRRMFpXSXlPVEF3WWpjMVkyRTRPREpsWVdRNUlqc0tDU0p2Y21sbmFXNWhiQzEwY21GdWMyRmpkR2x2YmkxcFpDSWdQU0FpTVRBd01EQXdNREE1TnpZeU9UTTFPQ0k3Q2draVluWnljeUlnUFNBaU1TNHdJanNLQ1NKMGNtRnVjMkZqZEdsdmJpMXBaQ0lnUFNBaU1UQXdNREF3TURBNU56WXlPVE0xT0NJN0Nna2ljWFZoYm5ScGRIa2lJRDBnSWpFaU93b0pJbTl5YVdkcGJtRnNMWEIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFek9EZzJOVEUwTVRFNU56VWlPd29KSW5WdWFYRjFaUzEyWlc1a2IzSXRhV1JsYm5ScFptbGxjaUlnUFNBaVFUQkZSRVU0TURJdFF6TkRPQzAwUWtJM0xUZzJNak10TkRoR09VTTVRalF4TkVSR0lqc0tDU0p3Y205a2RXTjBMV2xrSWlBOUlDSmpiMjB1YTNWM1lXNW5hMlV1Y0hKdlpIVmpkR2x2YmpFd01ERWlPd29KSW1sMFpXMHRhV1FpSUQwZ0lqYzVNREl3T1RJd01TSTdDZ2tpWW1sa0lpQTlJQ0pqYjIwdWEzVjNZVzVuYTJVdWMzVndaWEp0WVc0aU93b0pJbkIxY21Ob1lYTmxMV1JoZEdVdGJYTWlJRDBnSWpFek9EZzJOVEUwTVRFNU56VWlPd29KSW5CMWNtTm9ZWE5sTFdSaGRHVWlJRDBnSWpJd01UUXRNREV0TURJZ01EZzZNekE2TVRFZ1JYUmpMMGROVkNJN0Nna2ljSFZ5WTJoaGMyVXRaR0YwWlMxd2MzUWlJRDBnSWpJd01UUXRNREV0TURJZ01EQTZNekE2TVRFZ1FXMWxjbWxqWVM5TWIzTmZRVzVuWld4bGN5STdDZ2tpYjNKcFoybHVZV3d0Y0hWeVkyaGhjMlV0WkdGMFpTSWdQU0FpTWpBeE5DMHdNUzB3TWlBd09Eb3pNRG94TVNCRmRHTXZSMDFVSWpzS2ZRPT0iOwogICAgImVudmlyb25tZW50IiA9ICJTYW5kYm94IjsKICAgICJwb2QiID0gIjEwMCI7CiAgICAic2lnbmluZy1zdGF0dXMiID0gIjAiOwp9"}] as "application/json" using [org.springframework.http.converter.StringHttpMessageConverter@1a83cf00]
    //14:20:09.875 [main] DEBUG o.s.web.client.RestTemplate - POST request for "https://sandbox.itunes.apple.com/verifyReceipt" resulted in 200 (Apple WebObjects)
    //14:20:09.876 [main] DEBUG o.s.web.client.RestTemplate - Reading [java.lang.String] as "application/octet-stream" using [org.springframework.http.converter.StringHttpMessageConverter@1a83cf00]
    /*{
        "receipt":{"original_purchase_date_pst":"2014-01-02 00:30:11 America/Los_Angeles", "purchase_date_ms":"1388651411975", "unique_identifier":"ff7c3b2dd915ff5ea6c544eb2900b75ca882ead9", "original_transaction_id":"1000000097629358", "bvrs":"1.0", "transaction_id":"1000000097629358", "quantity":"1", "unique_vendor_identifier":"A0EDE802-C3C8-4BB7-8623-48F9C9B414DF", "item_id":"790209201", "product_id":"com.kuwangke.production1001", "purchase_date":"2014-01-02 08:30:11 Etc/GMT", "original_purchase_date":"2014-01-02 08:30:11 Etc/GMT", "purchase_date_pst":"2014-01-02 00:30:11 America/Los_Angeles", "bid":"com.kuwangke.superman", "original_purchase_date_ms":"1388651411975"}, "status":0}

    */

    public static class VerifyReceiptResult {
        private int status;
        private Receipt receipt;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Receipt getReceipt() {
            return receipt;
        }

        public void setReceipt(Receipt receipt) {
            this.receipt = receipt;
        }
    }

    public static class Receipt {
        private String bid;
        private String bvrs;
        private String item_id;
        private String original_purchase_date;
        private String original_purchase_date_ms;
        private String original_purchase_date_pst;
        private String original_transaction_id;
        private String product_id;
        private String purchase_date;
        private String purchase_date_ms;
        private String purchase_date_pst;
        private int quantity;
        private String transaction_id;
        private String unique_identifier;
        private String unique_vendor_identifier;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBvrs() {
            return bvrs;
        }

        public void setBvrs(String bvrs) {
            this.bvrs = bvrs;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getOriginal_purchase_date() {
            return original_purchase_date;
        }

        public void setOriginal_purchase_date(String original_purchase_date) {
            this.original_purchase_date = original_purchase_date;
        }

        public String getOriginal_purchase_date_ms() {
            return original_purchase_date_ms;
        }

        public void setOriginal_purchase_date_ms(String original_purchase_date_ms) {
            this.original_purchase_date_ms = original_purchase_date_ms;
        }

        public String getOriginal_purchase_date_pst() {
            return original_purchase_date_pst;
        }

        public void setOriginal_purchase_date_pst(String original_purchase_date_pst) {
            this.original_purchase_date_pst = original_purchase_date_pst;
        }

        public String getOriginal_transaction_id() {
            return original_transaction_id;
        }

        public void setOriginal_transaction_id(String original_transaction_id) {
            this.original_transaction_id = original_transaction_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getPurchase_date() {
            return purchase_date;
        }

        public void setPurchase_date(String purchase_date) {
            this.purchase_date = purchase_date;
        }

        public String getPurchase_date_ms() {
            return purchase_date_ms;
        }

        public void setPurchase_date_ms(String purchase_date_ms) {
            this.purchase_date_ms = purchase_date_ms;
        }

        public String getPurchase_date_pst() {
            return purchase_date_pst;
        }

        public void setPurchase_date_pst(String purchase_date_pst) {
            this.purchase_date_pst = purchase_date_pst;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getUnique_identifier() {
            return unique_identifier;
        }

        public void setUnique_identifier(String unique_identifier) {
            this.unique_identifier = unique_identifier;
        }

        public String getUnique_vendor_identifier() {
            return unique_vendor_identifier;
        }

        public void setUnique_vendor_identifier(String unique_vendor_identifier) {
            this.unique_vendor_identifier = unique_vendor_identifier;
        }
    }
}
