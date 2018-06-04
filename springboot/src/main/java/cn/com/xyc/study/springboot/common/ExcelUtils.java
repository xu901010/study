package cn.com.xyc.study.springboot.common;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtils {
    private static final int ACCESS_WINDOW_SIZE = 100;

    /**
     * 创建表格
     *
     * @param tempPath
     * @return
     */
    public static SXSSFWorkbook createBook(String tempPath) {
        return ExcelUtils.createBook(tempPath, ACCESS_WINDOW_SIZE);
    }

    /**
     * 创建表格
     *
     * @param tempPath
     * @return
     */
    public static SXSSFWorkbook createBook(String tempPath, int windowSize) {
        try {
            FileInputStream temp = new FileInputStream(tempPath);
            XSSFWorkbook tempBook = new XSSFWorkbook(temp);
            return new SXSSFWorkbook(tempBook, windowSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 导出到指定路径
     *
     * @param book
     * @param path
     * @return
     */
    public static boolean export(SXSSFWorkbook book, String path) {
        try (FileOutputStream out = new FileOutputStream(path)) {
            book.write(out);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


//    private static void createBook(String tempPath) throws Exception {
//        try (SXSSFWorkbook wb = new SXSSFWorkbook(new XSSFWorkbook(new FileInputStream(tempPath)))) {
//            Sheet sh = wb.getSheetAt(0);
//            for (int rownum = 1; rownum < 1001; rownum++) {
//                Row row = sh.createRow(rownum);
//                for (int cellnum = 0; cellnum < 10; cellnum++) {
//                    Cell cell = row.createCell(cellnum);
//                    String address = new CellReference(cell).formatAsString();
//                    cell.setCellValue(address);
//                }
//            }
//
//            for (int rownum = 1; rownum < 901; rownum++) {
//                Assert.assertNull(sh.getRow(rownum));
//            }
//            for (int rownum = 901; rownum < 1001; rownum++) {
//                Assert.assertNotNull(sh.getRow(rownum));
//            }
//
//            FileOutputStream out = new FileOutputStream("/Users/xuyuancheng/sxssf.xlsx");
//            wb.write(out);
//            out.close();
//
//            // dispose of temporary files backing this workbook on disk
//        } finally {
//            wb.dispose();
//        }
//
//
//    }

    public static void main(String[] args) throws Exception {
        //createBook("/Users/xuyuancheng/projects/study/springboot/src/main/resources/template.xlsx");
        test();
    }

    private static void test() throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sh = wb.createSheet();
        for (int rownum = 0; rownum < 1000; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }
        }

        for (int rownum = 0; rownum < 900; rownum++) {
            Assert.assertNull(sh.getRow(rownum));
        }
        for (int rownum = 900; rownum < 1000; rownum++) {
            Assert.assertNotNull(sh.getRow(rownum));
        }

        FileOutputStream out = new FileOutputStream("/Users/xuyuancheng/sxssf1.xlsx");
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
    }

    private static void test2() throws IOException {
        SXSSFWorkbook wb = new SXSSFWorkbook(-1); // turn off auto-flushing and accumulate all rows in memory
        Sheet sh = wb.createSheet();
        for (int rownum = 0; rownum < 1000; rownum++) {
            Row row = sh.createRow(rownum);
            for (int cellnum = 0; cellnum < 10; cellnum++) {
                Cell cell = row.createCell(cellnum);
                String address = new CellReference(cell).formatAsString();
                cell.setCellValue(address);
            }

            // manually control how rows are flushed to disk
            if (rownum % 100 == 0) {
                ((SXSSFSheet) sh).flushRows(100); // retain 100 last rows and flush all others

                // ((SXSSFSheet)sh).flushRows() is a shortcut for ((SXSSFSheet)sh).flushRows(0),
                // this method flushes all rows
            }

        }

        FileOutputStream out = new FileOutputStream("/temp/sxssf.xlsx");
        wb.write(out);
        out.close();

        // dispose of temporary files backing this workbook on disk
        wb.dispose();
    }
}
