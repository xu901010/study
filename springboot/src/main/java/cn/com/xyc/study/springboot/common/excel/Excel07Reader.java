package cn.com.xyc.study.springboot.common.excel;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * @author xuyuancheng
 */
public class Excel07Reader extends DefaultHandler {
    private SharedStringsTable sst;
    private InputStream in;
    /**
     * 当前单元格
     */
    private int currentCell;
    /**
     * 每行包括总列数以标题为准
     */
    private int countCell = -1;
    private int currentRow = 0;
    /**
     * 当前行是否有效
     */
    private boolean effective = false;
    /**
     * 单元格内容
     */
    private String readValue;
    private boolean isEmpty;

    /**
     * 存放一行中的所有数据
     */
    private String[] datas;
    /**
     * 事件提取
     */
    private RowReader rowReader;

    public void setRowReader(RowReader rowReader) {
        this.rowReader = rowReader;
    }

    public Excel07Reader(String filename, int countCell) throws FileNotFoundException {
        this.in = new FileInputStream(filename);
        this.countCell = countCell;
    }

    /**
     * 处理一个Sheet
     *
     * @throws Exception
     */
    public void processOneSheet() throws Exception {
        OPCPackage pkg = OPCPackage.open(in);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst);
        Iterator<InputStream> sheets = r.getSheetsData();
        if (sheets.hasNext()) {
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
        System.out.println("OK");
    }

    private XMLReader fetchSheetParser(SharedStringsTable sst) throws SAXException {
        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        this.sst = sst;
        parser.setContentHandler(this);
        return parser;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("c".equals(qName)) {
            this.currentCell = getColumn(attributes);
            isEmpty = attributes.getValue("t") == null;
        } else if ("row".equals(qName)) {
            datas = new String[countCell];
            effective = false;
        }
        readValue = "";
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("v".equals(qName)) {
            if (!isEmpty) {
                int idx = Integer.parseInt(readValue);
                datas[currentCell] = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
                effective = true;
            }
        } else if ("row".equals(qName)) {
            if (effective) {
                rowReader.dealRowData(currentRow, datas);
            }
            currentRow++;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        readValue += new String(ch, start, length);
    }

    /**
     * 获取当前列号
     *
     * @param attributes
     * @return
     */
    private int getColumn(Attributes attributes) {
        String name = attributes.getValue("r");
        int column = -1;
        for (int i = 0; i < name.length(); ++i) {
            if (Character.isDigit(name.charAt(i))) {
                break;
            }
            int c = name.charAt(i);
            column = (column + 1) * 26 + c - 'A';
        }
        return column;
    }
}
