package cn.com.xyc.study.springboot.controller;

import cn.com.xyc.study.springboot.service.ExportService;
import cn.com.xyc.study.springboot.vo.Account;
import cn.com.xyc.study.springboot.vo.Employee;
import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/dowload")
public class DownLoadController {
    @Autowired
    private ExportService exportService;

    @GetMapping(value = "/makeXls")
    public void download(HttpServletRequest request, HttpServletResponse response, @RequestHeader(required = false) String range) throws FileNotFoundException {

        exportService.exportAccount();


    }

    private List<Account> getAccountList() {
        List<Account> list = Lists.newArrayList();
        list.add(new Account("张三", "zhangsan1", "套餐一"));
        list.add(new Account("李四", "lisi", "套餐二"));
        list.add(new Account("王五", "wangwu", "套餐三"));
        list.add(new Account("赵六", "zhaoliu", "套餐四"));
        return list;
    }

    public List<Employee> getEmployeeList(){
        List<Employee> list = Lists.newArrayList();
        Employee p1 = new Employee("张三");
        p1.setBirthDate(new Date());
        p1.setPayment(new BigDecimal(1));
        p1.setBonus(new BigDecimal("2"));

        Employee p2 = new Employee("张三1");
        p2.setBirthDate(new Date());
        p2.setPayment(new BigDecimal(2));
        p2.setBonus(new BigDecimal("3"));

        Employee p3 = new Employee("张三2");
        p3.setBirthDate(new Date());
        p3.setPayment(new BigDecimal(4));
        p3.setBonus(new BigDecimal("5"));

        Employee p4 = new Employee("张三3");
        p4.setBirthDate(new Date());
        p4.setPayment(new BigDecimal(6));
        p4.setBonus(new BigDecimal("7"));
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        return list;
    }
}
