package cn.com.xyc.study.springboot.common.excel;

import java.util.Arrays;

public class ExcelTest {
    public static void main(String[] args) throws Exception {
        Excel07Reader reader = new Excel07Reader("/Users/xuyuancheng/os/upload/签名测试.xlsx", 2);
        reader.setRowReader(new RowReader() {
            @Override
            public void dealRowData(int curRow, String[] rowData) {
                System.out.println(Arrays.asList(rowData));
            }
        });
        reader.processOneSheet();

    }
}
