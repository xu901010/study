package cn.com.xyc.study.springboot.controller;

import cn.com.xyc.study.springboot.vo.Channel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
