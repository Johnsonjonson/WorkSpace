package fish.data;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileOutputStream;

public class XmlParseUtil {
    public static FishTankData readXml() throws DocumentException {
        SAXReader reader = new SAXReader();
        File file = new File("fishTank.xml");
        if(!file.exists()){
            return new FishTankData(FishTankData.getNeedChange(),FishTankData.getCurTemp());
        }
        Document doc = reader.read(new File("fishTank.xml"));
        Element root = doc.getRootElement();
        System.out.println("获取了根元素:"+root.getName());
        Element tempEle =  root.element("temp");
        double temp =Double.parseDouble(tempEle.getText());
        Element changeEle =  root.element("needChange");
        boolean needChange =Boolean.parseBoolean(changeEle.getText());

        System.out.println("解析完毕!"  + " temp = "+temp + " needChange = "+needChange );
        return new FishTankData(needChange,temp);
    }

    public static void writeXml(FishTankData fishTankData) {
        Document doc = DocumentHelper.createDocument();

        Element root = doc.addElement("fishTank");
        Element tempEle = root.addElement("temp");
        tempEle.addText(String.valueOf(fishTankData.getCurTemp()));
        Element changeEle = root.addElement("needChange");
        changeEle.addText(String.valueOf(fishTankData.getNeedChange()));

        try {
            XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
            FileOutputStream fos = new FileOutputStream("fishTank.xml");
            writer.setOutputStream(fos);
            writer.write(doc);
            System.out.println("写出完毕!");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
