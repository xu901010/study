package cn.com.xyc.study.springboot.controller;

import cn.com.xyc.study.springboot.vo.Channel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class ChannelController {

    @PostMapping("os/channel/get")
    public Object osGetChannel(@RequestBody(required = false) String params) {
        return new Channel();
    }

    @PostMapping("api/channel/get")
    public Object apiGetChannel(@RequestBody(required = false) String params) {
        return new Channel();
    }

    @RequestMapping(value = "api/channel/test",method = RequestMethod.POST)
    @ResponseBody
    public Channel test(HttpServletRequest request){
        Channel channel = null;
        try {
            request.getInputStream();
           channel =  new ObjectMapper().readValue(request.getInputStream(), Channel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(channel);
        return channel;
    }
}
