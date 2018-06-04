package cn.com.xyc.study.springboot.vo;

import java.math.BigDecimal;
import java.util.Date;

public class Employee {
    private String name;
    private Date birthDate;
    private BigDecimal payment;
    private BigDecimal bonus;

    public Employee(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setBonus(BigDecimal bonus) {
        this.bonus = bonus;
    }

    public void setPayment(BigDecimal payment) {
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBonus() {
        return bonus;
    }

    public BigDecimal getPayment() {
        return payment;
    }

    public Date getBirthDate() {
        return birthDate;
    }
}
