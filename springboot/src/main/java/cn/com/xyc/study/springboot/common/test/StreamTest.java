package cn.com.xyc.study.springboot.common.test;

import java.time.Duration;
import java.time.Instant;

public class StreamTest {
    public static void main(String[] args) {
        System.out.println(Duration.between(Instant.now(),Instant.now()));
    }
}
