package cn.com.xyc.study.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class SystemController {

    @RequestMapping("/login")
    public String login(String params, HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        String text = "abcdefghijk";
        session.setAttribute("admin",text);
        Cookie cookie = new Cookie("demo-session",session.getId());
        response.addCookie(cookie);
        return "success";
    }

    @RequestMapping("/hello")
    public String hello(String params,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if (session == null){
            return "no auth session!";
        }
        Cookie[] cookies = request.getCookies();
        if (cookies == null){
            return "no auth cookies";
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("demo-session")){
                if (session.getId().equals(cookie.getValue())){
                    return "hello";
                }
            }
        }
        return "no auth!";
    }

}
