package cn.com.xyc.study.springboot.common;

import java.sql.SQLException;

public class ExcelUtils1 {

    public static void readBigExcelToSql(String excelUrl)
            throws Exception, SQLException {
        BigExcelReader reader = new BigExcelReader(excelUrl) {
            @Override
            protected void outputRow(String[] datas, int[] rowTypes, int rowIndex) {
                for (int i = 0; i < datas.length; i++) {
                    System.out.println(datas[i]);
                }
            }
        };
        reader.parse();
    }

    public static void main(String[] args) throws Exception {
        ExcelUtils1.readBigExcelToSql("/Users/xuyuancheng/Documents/报备签名.xlsx");
    }
}
