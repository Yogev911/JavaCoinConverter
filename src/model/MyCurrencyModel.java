package model;


import controller.OnFinishLoad;
import model.currencyXmlRestApi.CurrencyXmlRestInvocationImpl;
import model.entities.Currency;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;

//this class is holding Variable after parser xml file
// in case the parser doesn't work it takes the last update from "currency"

public class MyCurrencyModel extends Observable {
	
	final static Logger log =Logger.getLogger(MyCurrencyModel.class);
	
    private int flag =0;
    private String DEFAULT_XML_FILE_PATH = "src/model/currency.xml";
    private String CURRENCY_RESTFUL_LINK = "http://www.boi.org.il/currency.xml";

    private String mLastUpdate;
    private HashMap<String,Currency> mCurrencies;

    private InputStream mInputStream;
    private DocumentBuilderFactory mDocumentBuilderFactory;
    private DocumentBuilder mBuilder;
    private Document mDocument;
    private String[] mCurrencyNames;
    private String[] mCurrencyCodes;
    private OnFinishLoad mOnFinishLoad;


    public void callXmlUpdateData(OnFinishLoad onFinishLoad) {
        log.trace("parsing model from file or restFull Service");
        mOnFinishLoad = onFinishLoad;
        initXmlData(false);
        
    }


    private void initXmlData(final boolean isNeedToUpdate) {
        CurrencyXmlRestInvocationImpl restInvocation = new CurrencyXmlRestInvocationImpl();
        restInvocation.getCurrencyXml(CURRENCY_RESTFUL_LINK, new Callback() {
            @Override
            public void onSuccess(InputStream inputStream) {
                mInputStream = inputStream;
                try {
                    mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
                    mBuilder = mDocumentBuilderFactory.newDocumentBuilder();
                    mDocument = mBuilder.parse(mInputStream);
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                    log.warn("parsing model from file or restFull Service is failed");
                }
                populateDate(isNeedToUpdate);
            }

            @Override
            public void onFailed(String error) {
                try {
                    mDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
                    mBuilder = mDocumentBuilderFactory.newDocumentBuilder();
                    File xmlFile = new File(DEFAULT_XML_FILE_PATH);
                    mDocument = mBuilder.parse(xmlFile);
                } catch (IOException | SAXException | ParserConfigurationException e) {
                    e.printStackTrace();
                    log.warn("parsing model from file or restFull Service is failed");
                }
                populateDate(isNeedToUpdate);

            }
        });
    }


    private void populateDate(boolean needToRefresh) {

        //init Currency array list
        mCurrencies = new HashMap<String, Currency>();
        
        NodeList allList = mDocument.getElementsByTagName("CURRENCY");
        NodeList nameList = mDocument.getElementsByTagName("NAME");
        NodeList unitList = mDocument.getElementsByTagName("UNIT");
        NodeList currencyCodeList = mDocument.getElementsByTagName("CURRENCYCODE");
        NodeList countryList = mDocument.getElementsByTagName("COUNTRY");
        NodeList rateList = mDocument.getElementsByTagName("RATE");
        NodeList changeList = mDocument.getElementsByTagName("CHANGE");
        NodeList lastUpdate = mDocument.getElementsByTagName("LAST_UPDATE");

        mLastUpdate = lastUpdate.item(0).getFirstChild().getNodeValue();
        mCurrencyNames = new String[allList.getLength() + 1];
        mCurrencyCodes = new String[allList.getLength() + 1];
        for (int i = 0; i < allList.getLength(); i++) {
            mCurrencies.put(
                    currencyCodeList.item(i).getFirstChild().getNodeValue(),
                    new Currency(nameList.item(i).getFirstChild().getNodeValue(),
                            unitList.item(i).getFirstChild().getNodeValue(),
                            rateList.item(i).getFirstChild().getNodeValue(),
                            countryList.item(i).getFirstChild().getNodeValue(),
                            changeList.item(i).getFirstChild().getNodeValue(),
                            currencyCodeList.item(i).getFirstChild().getNodeValue()));
            mCurrencyNames[i] = String.format("%s - %s",
                    nameList.item(i).getFirstChild().getNodeValue(),
                    countryList.item(i).getFirstChild().getNodeValue());
            mCurrencyCodes[i] = currencyCodeList.item(i).getFirstChild().getNodeValue();
        }

        if (mOnFinishLoad != null) {
            if (!needToRefresh) {
                mOnFinishLoad.onFinishLoad(mCurrencyNames, mCurrencyCodes);
            } else {
                mOnFinishLoad.onUpdateView(mCurrencyNames, mCurrencyCodes);
            }
        }

        File f = new File(DEFAULT_XML_FILE_PATH);
        if (checkUpToDate(mLastUpdate, "yyyy-MM-dd") || !f.exists()) {
            updateXmlFile(mDocument, mCurrencies, lastUpdate.item(0).getFirstChild().getNodeValue());
        }

        //adding NIS to currencies
        int i = (allList.getLength());
        mCurrencies.put(
                "NIS",
                new Currency("NIS",
                        "1",
                        "1",
                        "Israel",
                        "0",
                        "NIS"));
        mCurrencyNames[i] = String.format("%s - %s",
                "NIS", "Israel");
        mCurrencyCodes[i] = "NIS";
       
    }

    private void updateXmlFile(Document document, HashMap<String, Currency> currencyList, String lastUpdate) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            /**
             // root- CURRENCIES element
             *
             */
            document = docBuilder.newDocument();
            Element rootElement = document.createElement("CURRENCIES");
            document.appendChild(rootElement);


            Element[] stuff = new Element[currencyList.size()];



            Element lastUpdateElement = document.createElement("LAST_UPDATE");
            lastUpdateElement.appendChild(document.createTextNode(lastUpdate));
            rootElement.appendChild(lastUpdateElement);



            Iterator<String> itr = currencyList.keySet().iterator();
            int i = 0;
            while (itr.hasNext()) {
                String key = itr.next();
                /**
                 // CURRENCY elements
                 *
                 */
                stuff[i] = document.createElement("CURRENCY");
                rootElement.appendChild(stuff[i]);
                /**
                 // NAME elements
                 *
                 */
                Element NAME = document.createElement("NAME");
                NAME.appendChild(document.createTextNode(currencyList.get(key).getName()));
                stuff[i].appendChild(NAME);

                /**
                 // UNIT elements
                 *
                 */
                Element UNIT = document.createElement("UNIT");
                UNIT.appendChild(document.createTextNode(currencyList.get(key).getUnit()));
                stuff[i].appendChild(UNIT);

                /**
                 // CURRENCYCODE elements
                 *
                 */
                Element CURRENCYCODE = document.createElement("CURRENCYCODE");
                CURRENCYCODE.appendChild(document.createTextNode(currencyList.get(key).getCurrencyCode()));
                stuff[i].appendChild(CURRENCYCODE);

                /**
                 // COUNTRY elements
                 *
                 */
                Element COUNTRY = document.createElement("COUNTRY");
                COUNTRY.appendChild(document.createTextNode(currencyList.get(key).getCountry()));
                stuff[i].appendChild(COUNTRY);

                /**
                 // RATE elements
                 *
                 */
                Element RATE = document.createElement("RATE");
                RATE.appendChild(document.createTextNode(currencyList.get(key).getRate()));
                stuff[i].appendChild(RATE);

                /**
                 // CHANGE elements
                 *
                 */
                Element CHANGE = document.createElement("CHANGE");
                CHANGE.appendChild(document.createTextNode(currencyList.get(key).getChange()));
                stuff[i].appendChild(CHANGE);
                i++;
            }

            /** write the content into currency.xml  */

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(DEFAULT_XML_FILE_PATH));

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

    private boolean checkUpToDate(String date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        try {
            Date updatingDate = simpleDateFormat.parse(date);
            Date today = new Date();
            Date todayWithZeroTime = simpleDateFormat.parse(simpleDateFormat.format(today));
            long dateMilli = updatingDate.getTime();
            if (dateMilli < todayWithZeroTime.getTime()) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            log.warn("The date is not in the reggular format");

        }
        return false;
    }

    public String CalculateCurrencies(String fromCurrencyCode, String toCurrencyCode, float fromAmount) {
    	if(flag<196) flag++;
    	else log.debug("Using calculator method to exchange diffrent rates");
        Currency fromCurrency = mCurrencies.get(fromCurrencyCode);
        Currency toCurrency = mCurrencies.get(toCurrencyCode);
        float calculatedResult = ((fromAmount * Float.valueOf(fromCurrency.getRate())) / Float.valueOf(fromCurrency.getUnit()))
                / (Float.valueOf(toCurrency.getRate()) / Float.valueOf(toCurrency.getUnit()));
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(calculatedResult);
        
    }

    public float getRateOF(String i) {
        return Float.valueOf(mCurrencies.get(i).getRate());

    }

    @Override
    public void notifyObservers() {
        initXmlData(true);
    }

    public String getLastUpdate(){
    	return mDocument.getElementsByTagName("LAST_UPDATE").item(0).getFirstChild().getNodeValue();
    }

}

