package com.capstone.controller;

import org.apache.maven.model.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

@RestController
public class PointController {

    @PostMapping("/payments/complete")
    public int chargePoint(@RequestBody HashMap<String, Object> paymentInfo) {
        return postService.chargePoint(paymentInfo);
    }

    @GetMapping("/payments/complete/mobile")
    public String orderCompleteMobile(
            @RequestParam(required = false) String imp_uid
            , @RequestParam(required = false) String merchant_uid
            , Model model
            , Locale locale
            , HttpSession session) throws IOException
    {

        System.out.println("hi");
        return "home";
    }

}
