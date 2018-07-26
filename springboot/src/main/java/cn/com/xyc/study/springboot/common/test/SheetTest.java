package cn.com.xyc.study.springboot.common.test;


import java.util.Map;

public class SheetTest {

    public static void main(String[] args) throws Exception {
        Excel2007Reader reader = new Excel2007Reader("/Users/xuyuancheng/os/upload/签名测试.xlsx");
        reader.setRowReader(new RowReader() {
            @Override
            public void dealRowData(int curRow, Map<String, String> rowData) {
                System.out.println(rowData);
            }
        });
        reader.processOneSheet();
    }
}
