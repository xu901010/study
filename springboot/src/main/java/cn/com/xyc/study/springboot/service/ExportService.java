package cn.com.xyc.study.springboot.service;

import cn.com.xyc.study.springboot.common.ExcelUtils;
import cn.com.xyc.study.springboot.vo.Account;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.util.List;

@Service
public class ExportService {


    public void exportAccount() throws FileNotFoundException {
        SXSSFWorkbook book = ExcelUtils.createBook(ResourceUtils.getFile("classpath:template.xlsx").getPath(), 100);
        if (book == null) {
            //TODO 创建失败
        }
        List<Account> list = Lists.newArrayList();
        list.add(new Account("张三", "zhangsan1", "套餐一"));
        list.add(new Account("李四", "lisi", "套餐二"));
        list.add(new Account("王五", "wangwu", "套餐三"));
        list.add(new Account("赵六", "zhaoliu", "套餐四"));

        try {
            Sheet sheet = book.getSheetAt(0);
            Account account;
            for (int count = list.size(), index = 1; index < count + 1; index++) {
                Row row = sheet.createRow(index);
                account = list.get(index-1);
                Cell cell = row.createCell(0);
                cell.setCellValue(account.getName());
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(account.getName());
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(account.getName());
            }
            if (!ExcelUtils.export(book, "/Users/xuyuancheng/sxssf.xlsx")) {
                //TODO 导出失败
            }
        } finally {
            book.dispose();
        }

    }
}
