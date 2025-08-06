package helpers;

import base.TestDataBase.TestDataSetBase;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class ReadWriteHelper extends TestDataSetBase {

    private static Properties properties = new Properties();
    private static Map<String, String> MAP = new HashMap<>();
    public static String fileName;

    public static void readTxtFile(String filePath){
        try (FileInputStream inputStream = new FileInputStream(fileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        properties.entrySet().forEach(e -> MAP.put(String.valueOf(e.getKey()), String.valueOf(e.getValue())));
    }

    public static String getValue(String key) {
        return MAP.get(key);
    }


    public static String readPropertiesData(String fileName, String par) {
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/DataProvider/" + fileName + ".properties");
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(file);
        } catch (Throwable e) {
            e.printStackTrace(System.out);
            Assert.fail("\nPlease check config file if exist\n");
        }
        Properties prop = new Properties();
        // load properties file
        try {
            prop.load(fileInput);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            Assert.fail("\nPlease check config file Inputs\n");
        }
        return prop.getProperty(par);
    }


    public static String readXMLFile(String fileName, String tagName, String tagValue) {

        String value = "";

        try {

            File file = new File(System.getProperty("user.dir") + "/src/main/resources/DataProvider/" + fileName + ".xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName(tagName);

            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if ("value".equals(tagValue)) {
                        value = eElement.getElementsByTagName("value").item(0).getTextContent();
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }


    public void writeIntoXMLFile(String fileName, String value) {
        String xmlFilePath = System.getProperty("user.dir") + "/src/main/resources/DataProvider/" + fileName + ".xml";


        try {
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            //doc.getDocumentElement().normalize();

            // root element
            Element root = doc.createElement("class");
            doc.appendChild(root);

            // employee element
            Element employee = doc.createElement("referenceNumber");
            root.appendChild(employee);

            // firstname element
            Element firstName = doc.createElement("value");
            firstName.appendChild(doc.createTextNode(value));
            employee.appendChild(firstName);

            // create the xml file //transform the DOM Object to an XML File
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(new File(xmlFilePath));

            // If you use // StreamResult result = new StreamResult(System.out); // the output will be pushed to the standard output ... // You can use that for debugging

            transformer.transform(domSource, streamResult);

            System.out.println("Done creating XML File");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }

    }

    public static void writeIntoTxtFile(String fileName, String body) {
        File file = new File("src/main/resources/DataProvider/" + fileName + ".txt");

        //Create the file
        try {
            if (file.createNewFile()) {
                System.out.println("File is created!");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Write Content
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(body);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static Object getJsonInfo(String jsonFile) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(new FileReader(jsonFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
        }
        return obj;
    }

    public static Map<String, Object> saveJsonInMap(JSONObject object) {
        Map<String, Object> map = new HashMap();

        Iterator keys = object.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = object.get(key);
            if (value instanceof JSONObject) {
                value = saveJsonInMap((JSONObject) value);
            }
            if (value instanceof JSONArray) {
                value = saveJsonInList((JSONArray) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> saveJsonInList(JSONArray object) {
        List<Object> list = new ArrayList<Object>();

        for (int i = 0; i < object.size(); i++) {
            Object value = object.get(i);
            if (value instanceof JSONObject) {
                value = saveJsonInMap((JSONObject) value);
            }
            if (value instanceof JSONArray) {
                value = saveJsonInList((JSONArray) value);
            }
            list.add(value);
        }

        return list;
    }

    public static Map<String, Object> getDataFromJson(String filePath) {
        JSONParser parser = new JSONParser();
        Map<String, Object> map = new HashMap();
        try {
            JSONObject obj = (JSONObject) parser.parse(new FileReader(filePath));

            Iterator keys = obj.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = obj.get(key);

                if (value instanceof JSONObject) {
                    value = saveJsonInMap((JSONObject) value);
                }
                if (value instanceof JSONArray) {
                    value = saveJsonInList((JSONArray) value);
                }
                map.put(key, value);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static String compressHtmlFile(String path) {
        try {
            // Input HTML file
            FileInputStream input = new FileInputStream(path);
            // Compressed output file
            FileOutputStream output = new FileOutputStream(path + ".gz");
            // Create GZIP output stream
            GZIPOutputStream gzipOutput = new GZIPOutputStream(output);

            // Read the input HTML file and write it to the GZIP output stream
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {

                gzipOutput.write(buffer, 0, len);

            }

            // Close streams
            input.close();
            gzipOutput.close();
            System.out.println("HTML file compressed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path + ".gz";
    }


}