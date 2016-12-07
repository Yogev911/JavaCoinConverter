/*
    Final Project - Java 2016
    by:
         Nir Mekin      301734158
         Yogev Heskia   305166860
 */
package controller;

import model.MyCurrencyModel;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import view.MainView;

import javax.swing.*;


public class main {
	final static Logger log =Logger.getLogger(main.class);
    public static void main(String [] args){
    	PropertyConfigurator.configure("log4j.properties");
    	log.trace("starting final project of Nir Mekin and Yogev Heskia");

        //creating default currencies data
        final MyCurrencyModel myCurrencyModel = new MyCurrencyModel();
        log.trace("defualt currency data was created");

        //creating default main view (GUI)
        log.trace("defualt GUI was created");
        final MainView ex = new MainView();

        //implement onFinishLoad method
        log.trace("start processing data and initialize User Interface with update data");
        myCurrencyModel.callXmlUpdateData(new OnFinishLoad() {
            @Override
            public void onFinishLoad(final String[] currencyNames, final String[] currencyCodes) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        //adding the currencies data to GUI
                        ex.create(currencyNames, currencyCodes, myCurrencyModel);
                        ex.setVisible(true);
                        ex.setOnCalculateListener(new OnCalculateListener() {
                            @Override
                            public void onCalculateClicked(String fromCurrencyCode, String toCurrencyCode, float fromAmount) {
                                String calculatedData = myCurrencyModel.CalculateCurrencies(fromCurrencyCode,
                                        toCurrencyCode, fromAmount);
                                ex.updateCurrencyAfterCalculating(String.valueOf(calculatedData));
                            }
                        });
                    }
                });
            }
            //implement onUpdateView
            @Override
            public void onUpdateView(String[] currencyNames, String[] currencyCodes) {
                ex.update(currencyNames, currencyCodes);
            }
        });
        	
    }
   

}
