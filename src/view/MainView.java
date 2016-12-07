package view;

import controller.OnCalculateListener;
import model.MyCurrencyModel;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainView extends JFrame {
    final static Logger log = Logger.getLogger(MainView.class);
    private int flag;

    private JLabel mUpdate;

    private JTextField mTextCalculatedTitle;
    private JButton mClose;
    private JButton mRefreshData;
    private JButton mCalc;
    private JButton mOpenC;
    private JButton mChange;

    private JTextField mFirstAmount;
    private JTextField mCalculated;
    private String[] mCurrencies;
    private String[] mCurrencyCode;
    private JLabel mHeader1;
    private JLabel mHeader2;
    private JLabel mConvertFrom;
    private JLabel mConvertTo;

    private JLabel mAmount;

    private DefaultTableModel dTable;
    private JTable mTable;


    private JComboBox<String> mFirstCurrencyCombo;
    private JComboBox<String> mSecondCurrencyCombo;
    private OnCalculateListener mOnCalculateListener;

    private JPanel convertFP;
    private JPanel convertTP;

    private JPanel convertFTP;
    private JPanel resultP;
    private JPanel topP;
    private JPanel bottomP;
    private JPanel mTitle;

    private JPanel headP;
    private JPanel buttonP;

    private JFrame mJcalc;

    private MyCurrencyModel mCurrencyData;

    public MainView() {
        flag = 0;
    }

    public void create(String[] currencies, String[] currencyCodes, MyCurrencyModel currencyData) {
        log.trace("initializing Currencies DATA");
        mCurrencies = currencies;
        mCurrencyCode = currencyCodes;
        mCurrencyData = currencyData;
        initUI();
    }

    public void update(String[] currencies, String[] currencyCodes) {
        log.trace("updating Currencies DATA");
        mCurrencies = currencies;
        mCurrencyCode = currencyCodes;
        initTableOfData();
    }

    @SuppressWarnings("unchecked")
    public final void initUI() {
        log.trace("creating User Interface");
        getContentPane().setLayout(new BorderLayout());
        dTable = new DefaultTableModel(16, 16);
        log.trace("initializing Table of exchange rates");
        initTableOfData();

        mTable = new JTable(dTable);
        mTable.setEnabled(false);
        mTable.setFont(new Font("Arial", 1, 15));
        mTable.setBackground(new Color(253, 231, 178));

        mTable.setBorder(BorderFactory.createMatteBorder(
                1, 0, 1, 0, Color.orange));


        mHeader1 = new JLabel("Yogev and Nir Final Project", SwingConstants.CENTER);
        mHeader1.setFont(new Font("arial", 0, 48));

        mHeader2 = new JLabel("Java 2016", SwingConstants.CENTER);
        mHeader2.setFont(new Font("arial", 0, 36));

        headP = new JPanel(new BorderLayout());
        headP.add(mHeader1, BorderLayout.NORTH);
        headP.add(mHeader2, BorderLayout.SOUTH);
        headP.setBackground(new Color(253, 221, 146));

        mConvertFrom = new JLabel("  Convert From: ");
        mConvertFrom.setFont(new Font("arial", 0, 26));

        String[] temp = mCurrencies;
        temp[14] = "NIS - Israel";
        mFirstCurrencyCombo = new JComboBox<String>(temp);
        mFirstCurrencyCombo.setSelectedIndex(14);

        convertFP = new JPanel(new BorderLayout());
        convertFP.add(mConvertFrom, BorderLayout.WEST);
        convertFP.add(mFirstCurrencyCombo, BorderLayout.EAST);
        convertFP.setPreferredSize(new Dimension(335, 28));

        mConvertTo = new JLabel("  Convert To: ");
        mConvertTo.setFont(new Font("arial", 0, 26));

        mSecondCurrencyCombo = new JComboBox<String>(mCurrencies);
        mSecondCurrencyCombo.setSelectedIndex(14);

        convertTP = new JPanel(new BorderLayout());
        convertTP.add(mConvertTo, BorderLayout.WEST);
        convertTP.add(mSecondCurrencyCombo, BorderLayout.EAST);
        convertTP.setPreferredSize(new Dimension(295, 28));

        mCalc = new JButton("Calculate");
        mCalc.setPreferredSize(new Dimension(50, 16));

        mChange = new JButton("<== SWITCH ==>");
        mChange.setFont(new Font("arial", 0, 12));


        convertFTP = new JPanel(new BorderLayout());
        convertFTP.add(convertFP, BorderLayout.WEST);
        convertFTP.add(mChange);
        convertFTP.add(convertTP, BorderLayout.EAST);

        mClose = new JButton("Close");
        mRefreshData = new JButton("Refresh");
        mOpenC = new JButton("Open Calculator");
        mOpenC.setFont(new Font("arial", 1, 28));
        mRefreshData.setFont(new Font("arial", 1, 28));
        mClose.setFont(new Font("arial", 1, 20));
        mClose.setPreferredSize(new Dimension(290, 50));
        mRefreshData.setPreferredSize(new Dimension(290, 50));
        mOpenC.setPreferredSize(new Dimension(290, 50));


        buttonP = new JPanel(new BorderLayout());
        buttonP.add(mRefreshData, BorderLayout.WEST);
        buttonP.add(mOpenC);
        buttonP.add(mClose, BorderLayout.EAST);


        mFirstAmount = new JTextField();
        mFirstAmount.setFont(new Font("arial", 1, 16));
        mAmount = new JLabel("  Amount: ");
        mAmount.setFont(new Font("arial", 0, 26));

        bottomP = new JPanel(new BorderLayout());
        bottomP.add(mAmount, BorderLayout.WEST);
        bottomP.add(mFirstAmount);
        bottomP.setPreferredSize(new Dimension(320, 25));


        mCalculated = new JTextField();
        mCalculated.setEditable(false);
        mCalculated.setForeground(Color.green);
        mCalculated.setFont(new Font("arial", 1, 16));
        mCalculated.setPreferredSize(new Dimension(200, 25));

        mTextCalculatedTitle = new JTextField("  Result: ");
        mTextCalculatedTitle.setFont(new Font("arial", 0, 26));

        resultP = new JPanel(new BorderLayout());
        resultP.add(mTextCalculatedTitle, BorderLayout.WEST);
        resultP.add(mCalculated);

        topP = new JPanel(new BorderLayout());
        topP.add(resultP, BorderLayout.EAST);
        topP.add(mCalc);
        topP.add(bottomP, BorderLayout.WEST);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        mUpdate = new JLabel("LAST UPDATE: " + mCurrencyData.getLastUpdate() + " " + sdf.format(cal.getTime()), SwingConstants.CENTER);
        mUpdate.setFont(new Font("arial", 0, 16));
        mUpdate.setBorder(BorderFactory.createMatteBorder(
                2, 0, 0, 0, Color.orange));

        mTitle = new JPanel(new BorderLayout());
        mTitle.add(mUpdate, BorderLayout.SOUTH);
        mTitle.add(headP);

        // adding titles, table and buttons to main Frame  //
        getContentPane().add(mTitle, BorderLayout.NORTH);
        getContentPane().add(mTable);
        getContentPane().add(buttonP, BorderLayout.SOUTH);


        //setting Main frame
        setTitle("Yogev & Nir Final Java Project 2016");
        setSize(1350, 460);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        setListeners();


        //// Calculator Frame////
        mJcalc = new JFrame("Calculat");
        mJcalc.setLayout(new BorderLayout());
        mJcalc.setBounds(200, 300, 800, 98);
        mJcalc.setResizable(false);
        mJcalc.setVisible(false);
        mJcalc.add(convertFTP, BorderLayout.NORTH);
        mJcalc.add(topP, BorderLayout.SOUTH);


        mJcalc.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                log.trace("Calculator window closed by X");
                mOpenC.setText("Open Calculator");
                flag = 0;
            }
        });


    }

    private void setListeners() {
        //exit on click "close" button
        mClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.trace("Ending Program");
                dispose();
                System.exit(0);
            }
        });

        //open and close calculator frame
        mOpenC.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                if (flag == 0) {
                    log.trace("Opennig new window for calculator");
                    mJcalc.setVisible(true);
                    mOpenC.setText("Close Calculator");
                    flag = 1;
                    return;
                }
                log.trace("Clossing window for calculator");
                mJcalc.setVisible(false);
                mOpenC.setText("Open Calculator");
                flag = 0;

            }
        });
        //switch between currencies and calculate in case thier is an amount
        mChange.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int index = mFirstCurrencyCombo.getSelectedIndex();
                mFirstCurrencyCombo.setSelectedIndex(mSecondCurrencyCombo.getSelectedIndex());
                mSecondCurrencyCombo.setSelectedIndex(index);
                if (!mCalculated.getText().isEmpty())
                    mCalc.doClick();

            }
        });
        //exit on click "X" button - tab button
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                log.trace("Ending Program");
                dispose();
                System.exit(0);
            }
        });


        mRefreshData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.trace("Data is been update");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                mCurrencyData.notifyObservers();
                mUpdate.setText("LAST UPDATE: " + mCurrencyData.getLastUpdate() + " " + sdf.format(cal.getTime()));
            }
        });

        mCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                log.trace("Calculating Result");
                int fromCurrencyIndex = mFirstCurrencyCombo.getSelectedIndex();
                int toCurrencyIndex = mSecondCurrencyCombo.getSelectedIndex();
                String fromAmount = mFirstAmount.getText();

                if (!fromAmount.equals("")) {
                    mOnCalculateListener.onCalculateClicked(mCurrencyCode[fromCurrencyIndex], mCurrencyCode[toCurrencyIndex],
                            Float.valueOf(fromAmount));
                } else {
                    JOptionPane.showMessageDialog(MainView.this, "There is empty fields");
                }
            }
        });
    }

    public void setOnCalculateListener(OnCalculateListener onCalculateListener) {
        mOnCalculateListener = onCalculateListener;
    }

    public void updateCurrencyAfterCalculating(String calculated) {
        mCalculated.setText(calculated);
    }

    private void initTableOfData() {
        int i = 1, j = 1;

        dTable.setValueAt("Currencies", 0, 0);
        for (String temp : mCurrencyCode) {
            dTable.setValueAt(" " + temp, 0, i);
            dTable.setValueAt(" " + temp, i, 0);
            i++;
        }
        dTable.setValueAt(" NIS", i - 1, 0);
        dTable.setValueAt(" NIS", 0, i - 1);

        for (i = 1; i < 15; i++) {
            for (j = 1; j < 15; j++) {
                dTable.setValueAt("  " + mCurrencyData.CalculateCurrencies(mCurrencyCode[i - 1], mCurrencyCode[j - 1], 1), i, j);
            }
        }

        for (i = 1; i < 15; i++) {
            DecimalFormat temp = new DecimalFormat("#.##");
            dTable.setValueAt("  " + temp.format(mCurrencyData.getRateOF(mCurrencyCode[i - 1])), i, j);
            dTable.setValueAt("  " + temp.format(1 / mCurrencyData.getRateOF(mCurrencyCode[i - 1])), j, i);
        }

        dTable.setValueAt("  " + 1, 15, 15);

    }
}