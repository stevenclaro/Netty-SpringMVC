package org.springframework.sandbox.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
@Controller
@RequestMapping("/Netty")

/*用户访问的地址http://localhost:8080/Netty/Spring*/

public class StevenTest {

        @RequestMapping(value="/Spring", produces = "text/html; charset=utf-8")
        public @ResponseBody
        String getShopInJSON(HttpServletRequest request) {
            return "Netty,Spring bind togther";
        }

    }

