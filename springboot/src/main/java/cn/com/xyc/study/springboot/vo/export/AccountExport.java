package cn.com.xyc.study.springboot.vo.export;

import cn.com.xyc.study.springboot.vo.Account;
import org.apache.poi.ss.usermodel.Sheet;

public class AccountExport {
    private int row = 0;
    private int cell = 0;
    private Sheet sheet;
    private Account account;

    public Sheet getSheet() {
        return sheet;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
    }

    public int getCell() {
        return cell;
    }

    public int getRow() {
        return row;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
