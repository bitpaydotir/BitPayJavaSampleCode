
package com.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import bitpay.library.BitPay;
import bitpay.library.SendRequestResult;
import bitpay.library.PaymentResult;

@RestController
public class PaymentController {
    // This is test token, replace it with yours
    private static String token = "adxcv-zzadq-polkjsad-opp13opoz-1sdf455aadzmck1244567";
    // Set IsTest to false for live testing
    private static boolean isTest = true;

   @RequestMapping("/")
    public static Object index() {
        String amount = "10000";
        String factorId = "123";
        String name = "Test Payment";
        String email = "test@test.com";
        String description = "This is for test";

        try {

            // Create an instance of the BitPay class from the external library
            BitPay bitpay = new BitPay(token, isTest);

            String redirectUrl = "http://localhost:8080/PaymentResault";

            // Call the send method from the BitPay class
            SendRequestResult result = bitpay.Send(amount, redirectUrl, factorId, name, email, description);

            if (result.getStatus() > 0) {
                // Redirect to a URL based on the result received from the send method
                   return new RedirectView( result.getGatewayUrl());
              
            }
            // Handle other scenarios or errors based on the result
            // Assuming TempData is a placeholder for storing messages
            return "Failed Request :  " + result.getMessage();
        } catch (Exception ex) {
            // Handle exceptions thrown by the external library
            return "Error :  " + ex.getMessage();
        }

    }
    @RequestMapping("/PaymentResault")
    public String handlePaymentResult(@RequestParam(name = "trans_id") int transId,
                                      @RequestParam(name = "id_get") int idGet,
                                      @RequestParam(name = "factorId") int factorId) {
         try
            {
                BitPay bitpay = new BitPay(token, isTest);

                PaymentResult result =  bitpay.Get(String.valueOf(transId), String.valueOf(idGet));

                if (result.getStatus() == 1)
                {
                    //Success payment
                    return String.format("پرداخت شما با شماره فاکتور %s با موفقیت انجام شد", factorId);
                
                }
                //Failed payment
               return result.getMessage();

                
            }
            catch (Exception ex)
            {
                // Handle exceptions thrown by the external DLL
                return "An error occurred while processing payment: " + ex.getMessage();
            }
       
    }
   
}