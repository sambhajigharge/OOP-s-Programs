package objectOrientedProgram.StockAccountManagement.main.userInterface;

import objectOrientedProgram.StockAccountManagement.main.model.Portfolio;
import objectOrientedProgram.StockAccountManagement.main.persistance.JsonReader;
import objectOrientedProgram.StockAccountManagement.main.persistance.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import objectOrientedProgram.StockAccountManagement.main.exceptions.NegativeDoubleException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.NonCapLetterException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.RiskOutOfBoundaryException;
import objectOrientedProgram.StockAccountManagement.main.exceptions.TicketLengthException;
import objectOrientedProgram.StockAccountManagement.main.model.Stock;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GUI extends JFrame implements ActionListener {
    private static final String JSON_STORE = "./data/portfolio.json";
    private Portfolio portfolio;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private JPanel mainMenu;
    private JLabel balance;
    private JButton addMoneyButton;
    private JButton addStockButton;
    private JButton viewStocksButton;
    private JButton sellStockButton;
    private JButton investInStocksButton;
    private JButton savePortfolioButton;
    private JButton loadPortfolioButton;
    private JButton endButton;

    private JPanel balanceMenu;
    private JTextField balanceField;

    private JPanel addStockMenu;
    private JTextField stockTicker;
    private JTextField stockPriceCurrent;
    private JTextField marketCap;
    private JTextField risk;

    private JPanel viewStockMenu;

    private JPanel investStocksMenu;
    private ArrayList<JTextField> investTextFields;
    private JTextField investDays;

    private JLabel stockList;


    public GUI() {
        super("Stock Portfolio");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 500));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));

        initializeMainMenu();

        initializeSubMenus();
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: creates the main menu ui and initializes a new portfolio
    public void initializeMainMenu() {
        portfolio = new Portfolio();

        mainMenu = new JPanel();
        mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.Y_AXIS));
        add(mainMenu);
        mainMenu.setVisible(true);

        addHomeScreenLabel();

        createMenuButtons();
        addButtonsToMenu();
        setButtonAction();
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: adds welcome message and shows available balance on main menu using labels
    private void addHomeScreenLabel() {
        JLabel homeLabel = new JLabel("Hello, welcome to your stock portfolio");
        homeLabel.setFont(new Font("Georgia", Font.BOLD, 35));
        mainMenu.add(homeLabel);

        balance = new JLabel(("Your available balance is: $" + portfolio.getBalance()));
        balance.setFont(new Font("Georgia", Font.BOLD, 16));
        mainMenu.add(balance);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: creates all the menu buttons on the main menu
    private void createMenuButtons() {
        addMoneyButton = new JButton("Add Money to Portfolio Balance");
        addStockButton = new JButton("Add a New Stock");
        viewStocksButton = new JButton("View Current Stocks");
        sellStockButton = new JButton("Sell First Stock");
        investInStocksButton = new JButton("Invest in Your Stocks Now");
        savePortfolioButton = new JButton("Save Current Portfolio");
        loadPortfolioButton = new JButton("Load Previous Portfolio");
        endButton = new JButton("Exit application");
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: adds all the created buttons to the main menu
    private void addButtonsToMenu() {
        addButtonToMenu(addMoneyButton);
        addButtonToMenu(addStockButton);
        addButtonToMenu(viewStocksButton);
        addButtonToMenu(sellStockButton);
        addButtonToMenu(investInStocksButton);
        addButtonToMenu(savePortfolioButton);
        addButtonToMenu(loadPortfolioButton);
        addButtonToMenu(endButton);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: adds and formats a button to the mainMenu
    private void addButtonToMenu(JButton button) {
        button.setFont(new Font("Georgia", Font.BOLD, 12));
        button.setBackground((Color.BLACK));
        mainMenu.add(button);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);

    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: sets the action listener and action command for all buttons on the main page
    private void setButtonAction() {
        addMoneyButton.addActionListener(this);
        addMoneyButton.setActionCommand("Add Money to Portfolio Balance");

        addStockButton.addActionListener(this);
        addStockButton.setActionCommand("Add a New Stock");

        viewStocksButton.addActionListener(this);
        viewStocksButton.setActionCommand("View Current Stocks");

        sellStockButton.addActionListener(this);
        sellStockButton.setActionCommand("Sell First Stock");

        investInStocksButton.addActionListener(this);
        investInStocksButton.setActionCommand("Invest in Your Stocks Now");

        savePortfolioButton.addActionListener(this);
        savePortfolioButton.setActionCommand("Save Current Portfolio");

        loadPortfolioButton.addActionListener(this);
        loadPortfolioButton.setActionCommand("Load Previous Portfolio");

        endButton.addActionListener(this);
        endButton.setActionCommand("Exit application");
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: initializes all the sub menus
    private void initializeSubMenus() {
        initializeBalanceMenu();
        initializeViewStockMenu();
        initializeAddStockMenu();
        initializeInvestInStocks();
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: initializes the menu for adding balance to portfolio
    //- creates JPanel, labels and buttons
    private void initializeBalanceMenu() {
        balanceMenu = new JPanel();

        JLabel label = new JLabel("Enter amount of money to add to balance:");
        label.setFont((new Font("Georgia", Font.BOLD, 12)));
        balanceMenu.add(label);

        balanceField = new JTextField(5);
        balanceField.setMaximumSize(new Dimension(1200, 40));
        balanceMenu.add(balanceField);

        JButton addBalance = new JButton("Add Balance");
        addBalance.setFont(new Font("Georgia", Font.BOLD, 12));
        addBalance.setBackground((Color.BLACK));
        balanceMenu.add(addBalance);
        pack();
        addBalance.setActionCommand("Add Balance");
        addBalance.addActionListener(this);

        JButton mainMenuButton = new JButton("Back to Main Menu");
        mainMenuButton.setFont(new Font("Georgia", Font.BOLD, 12));
        mainMenuButton.setBackground((Color.BLACK));
        balanceMenu.add(mainMenuButton);
        pack();
        mainMenuButton.setActionCommand("Back to Main Menu");
        mainMenuButton.addActionListener(this);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: intializes menu for investing in the current stocks
    //              - creates jpanel, fields, and buttons
    public void initializeInvestInStocks() {
        investStocksMenu = new JPanel();

        investTextFields = createInvestFields();

        JButton investStocksButton = new JButton("Invest Stocks");
        investStocksButton.setFont(new Font("Georgia", Font.BOLD, 12));
        investStocksButton.setBackground((Color.BLACK));
        investStocksMenu.add(investStocksButton);
        pack();
        investStocksButton.setActionCommand("Invest Stocks");
        investStocksButton.addActionListener(this);

        JButton mainMenuButton = new JButton("Back to Main Menu");
        mainMenuButton.setFont(new Font("Georgia", Font.BOLD, 12));
        mainMenuButton.setBackground((Color.BLACK));
        investStocksMenu.add(mainMenuButton);
        pack();
        mainMenuButton.setActionCommand("Back to Main Menu");
        mainMenuButton.addActionListener(this);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: for each stock in portfolio it will create a label and text field asking for amount to invest in
    // - also creates label and field for how many days to invest in all the stocks
    private ArrayList<JTextField> createInvestFields() {
        ArrayList<JTextField> labelList = new ArrayList<>();

        for (Stock stock : portfolio.getPortfolioList()) {
            JLabel label = new JLabel("How much would you like to invest int stock " + stock.getSymbol() + "?");
            label.setFont(new Font("Georgia", Font.BOLD, 12));
            JTextField textField = new JTextField(5);
            textField.setMaximumSize(new Dimension(1200, 40));

            investStocksMenu.add(label);
            investStocksMenu.add(textField);

            labelList.add(textField);
        }
        JLabel label1 = new JLabel("How many days would you like to invest these stocks for?");
        label1.setFont(new Font("Georgia", Font.BOLD, 12));
        investDays = new JTextField(5);
        investDays.setMaximumSize(new Dimension(1200, 40));

        investStocksMenu.add(label1);
        investStocksMenu.add(investDays);
        return labelList;
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: for each stock in portfolio it will create a label and text field asking for amount to invest in
    //              - also creates label and field for how many days to invest in all the stocks
    private void initializeViewStockMenu() {
        viewStockMenu = new JPanel();

        JLabel title = new JLabel("Your Stocks:");
        title.setFont(new Font("Georgia", Font.BOLD, 20));
        viewStockMenu.add(title);

        stockList = new JLabel(convertToMultiline(portfolio.getStocksAsString()), SwingConstants.CENTER);
        stockList.setFont(new Font("Georgia", Font.BOLD, 12));

        JScrollPane pane = new JScrollPane(stockList);
        pane.setPreferredSize(new Dimension(1200, 300));
        viewStockMenu.add(pane);

        JButton toMenu = new JButton("Back to Main Menu");
        toMenu.setFont(new Font("Georgia", Font.BOLD, 12));
        toMenu.setBackground((Color.BLACK));
        viewStockMenu.add(toMenu);
        pack();
        toMenu.addActionListener(this);
        toMenu.setActionCommand("Back to Main Menu");
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS:  Initializes the menu for adding stocks
    //              - creates j panel, input fields, add stock button, back to main menu button
    private void initializeAddStockMenu() {
        addStockMenu = new JPanel();

        addInputFields();

        JButton addStockButton = new JButton("Add Stock");
        addStockButton.setFont(new Font("Georgia", Font.BOLD, 12));
        addStockButton.setBackground((Color.BLACK));
        addStockMenu.add(addStockButton);
        pack();
        addStockButton.setActionCommand("Add Stock");
        addStockButton.addActionListener(this);

        JButton mainMenuButton = new JButton("Back to Main Menu");
        mainMenuButton.setFont(new Font("Georgia", Font.BOLD, 12));
        mainMenuButton.setBackground((Color.BLACK));
        addStockMenu.add(mainMenuButton);
        pack();
        mainMenuButton.setActionCommand("Back to Main Menu");
        mainMenuButton.addActionListener(this);
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: adds input for each field for a stock
    //              - gives all same font and dimension
    private void addInputFields() {
        Font font = new Font("Georgia", Font.BOLD, 12);
        Dimension dimension = new Dimension(1200, 40);

        addInput1(font, dimension);

        addInput2(font, dimension);

        addInput3(font, dimension);

        addInput4(font, dimension);
    }


    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: creates JLabel and text field to get the riskiness of a stock
    private void addInput4(Font font, Dimension dimension) {
        JLabel label;
        label = new JLabel("How risky is the stock from 1 - 5? (eg. 5 is riskiest, could lose or gain the most)");
        label.setFont(font);

        risk = new JTextField(5);
        risk.setMaximumSize(dimension);
        addStockMenu.add(label);
        addStockMenu.add(risk);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: creates j label and text field to get the market cap of stock
    private void addInput3(Font font, Dimension dimension) {
        JLabel label = new JLabel("What is the market cap of this stock? (eg. AAPL: $2723000000000)");
        label.setFont(font);

        marketCap = new JTextField(5);
        marketCap.setMaximumSize(dimension);
        addStockMenu.add(label);
        addStockMenu.add(marketCap);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: creates j label and text field to get the current stock price of stock
    private void addInput2(Font font, Dimension dimension) {
        JLabel label = new JLabel("What is the current stock price of this stock? (E.g. $135.37)");
        label.setFont(font);

        stockPriceCurrent = new JTextField(5);
        stockPriceCurrent.setMaximumSize(dimension);
        addStockMenu.add(label);
        addStockMenu.add(stockPriceCurrent);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: creates j label and text field to get the symbol of the stock
    private void addInput1(Font font, Dimension dimension) {
        JLabel label = new JLabel("What is the ticker/symbol of the stock you would like to sell? (E.g. AAPL)");
        label.setFont(font);

        stockTicker = new JTextField(5);
        stockTicker.setMaximumSize(dimension);
        addStockMenu.add(label);
        addStockMenu.add(stockTicker);
    }

    // EFFECTS: calls methods when button pressed for each menu
    @Override
    public void actionPerformed(ActionEvent e) {
        mainMenuActions(e);
        viewStockMenuActions(e);
        addStockMenuActions(e);
        investStocksMenuActions(e);
        addBalanceMenuActions(e);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: calls method given button pressed in add balance menu
    //              - one button adds balance to portfolio
    //              - one button returns to main menu
    private void addBalanceMenuActions(ActionEvent e) {
        if (e.getActionCommand().equals("Add Balance")) {
            portfolio.setBalance(portfolio.getBalance() + Double.parseDouble(balanceField.getText()));
            balance.setText("Your available balance is: $" + portfolio.getBalance());
        } else if (e.getActionCommand().equals("Back to Main Menu")) {
            mainMenu.setVisible(true);
            viewStockMenu.setVisible(false);
            addStockMenu.setVisible(false);
            investStocksMenu.setVisible(false);
            balanceMenu.setVisible(false);
        }
    }

    // REQUIRES:
    // MODIFIES:
    // EFFECTS: calls methods for each button pressed in main Menu
    private void mainMenuActions(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Add a New Stock":
                addStockToPortfolioButtonPress();
                break;
            case "View Current Stocks":
                viewStocksActionButtonPress();
                break;
            case "Save Current Portfolio":
                savePortfolio();
                break;
            case "Load Previous Portfolio":
                loadPortfolio();
                break;
            case "Exit application":
                System.exit(0);
                break;
            case "Invest in Your Stocks Now":
                investInStocksButtonPress();
                break;
            case "Add Money to Portfolio Balance":
                addBalanceButtonPress();
                break;
            case "Sell First Stock":
                sellFirstStock();
                break;
        }
    }

    private void sellFirstStock() {
        if (portfolio.getPortfolioList().size() > 0) {
            Stock stock = portfolio.getPortfolioList().get(0);
            portfolio.sellStock(stock);

            balance.setText("Your available balance is: $" + portfolio.getBalance());

            stockList.setText(portfolio.getStocksAsString());
        }
    }


    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: switches to add balance menu
    private void addBalanceButtonPress() {
        add(balanceMenu);
        mainMenu.setVisible(false);
        viewStockMenu.setVisible(false);
        addStockMenu.setVisible(false);
        investStocksMenu.setVisible(false);
        balanceMenu.setVisible(true);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: methods for button clicks in invest stock menu
    //             - method for investing stocks
    //             - returns back to main menu
    private void investStocksMenuActions(ActionEvent e) {
        if (e.getActionCommand().equals("Invest Stocks")) {
            setInvestments();
        } else if (e.getActionCommand().equals("Back to Main Menu")) {
            mainMenu.setVisible(true);
            viewStockMenu.setVisible(false);
            addStockMenu.setVisible(false);
            investStocksMenu.setVisible(false);
            balanceMenu.setVisible(false);
        }
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: switches to invest in stocks menu
    private void investInStocksButtonPress() {
        initializeInvestInStocks();
        add(investStocksMenu);
        mainMenu.setVisible(false);
        viewStockMenu.setVisible(false);
        addStockMenu.setVisible(false);
        investStocksMenu.setVisible(true);
        balanceMenu.setVisible(false);
    }

    // REQUIRES: the text in TextField can be parsed to be the correct type of field in stock
    // MODIFIES: this
    // EFFECTS: invests in each stock depending on what is entered in textFields, changes balance label
    private void setInvestments() {
        for (int i = 0; i < portfolio.getPortfolioList().size(); i++) {
            Stock stock = portfolio.getPortfolioList().get(i);
            try {
                stock.setCurrentInvestmentWorth(Double.parseDouble(investTextFields.get(i).getText()));
                portfolio.subtractBalance(Double.parseDouble(investTextFields.get(i).getText()));
            } catch (NegativeDoubleException e) {
                System.out.println("cannot invest negative amount");
            }
            balance.setText("Your available balance is: $" + portfolio.getBalance());

        }

        portfolio.investStocksForDays(Integer.parseInt(investDays.getText()));

        stockList.setText(portfolio.getStocksAsString());
    }

    // MODIFIES: this
    // EFFECTS: loads portfolio from file
    private void loadPortfolio() {
        try {
            jsonReader = new JsonReader(JSON_STORE);
            portfolio = jsonReader.read();
            System.out.println("Loaded portfolio saved at " + JSON_STORE);
            stockList.setText(portfolio.getStocksAsString());
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the portfolio to file in data folder
    private void savePortfolio() {
        try {
            jsonWriter = new JsonWriter(JSON_STORE);
            jsonWriter.open();
            jsonWriter.write(portfolio);
            jsonWriter.close();
            System.out.println("Saved portfolio to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: switches to addstock to portfolio menu
    private void addStockToPortfolioButtonPress() {
        add(addStockMenu);
        mainMenu.setVisible(false);
        viewStockMenu.setVisible(false);
        addStockMenu.setVisible(true);
        investStocksMenu.setVisible(false);
        balanceMenu.setVisible(false);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: switches to view stocks menu
    private void viewStocksActionButtonPress() {
        add(viewStockMenu);
        mainMenu.setVisible(false);
        viewStockMenu.setVisible(true);
        addStockMenu.setVisible(false);
        investStocksMenu.setVisible(false);
        balanceMenu.setVisible(false);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: actions for button clicks in view stock menu
    //              - returns to main menu
    private void viewStockMenuActions(ActionEvent e) {
        if (e.getActionCommand().equals("Back to Main Menu")) {
            mainMenu.setVisible(true);
            viewStockMenu.setVisible(false);
            addStockMenu.setVisible(false);
            investStocksMenu.setVisible(false);
            balanceMenu.setVisible(false);
        }
    }

    // REQUIRES: Ensure text fields have proper formatting to be parsed into the correct field type in stock
    // MODIFIES: this
    // EFFECTS: actions for button clicks in add stock menu
    //              - if Add Stock then it will add a new stock with fields parsed from textfields to portfolio
    //              - return to main menu button returns to main menu
    private void addStockMenuActions(ActionEvent e) {
        if (e.getActionCommand().equals("Add Stock")) {
            playSound("./data/CashRegister.wav");
            Stock stock = new Stock();
            try {
                stock.setSymbol(stockTicker.getText());
                stock.setStockPriceCurrent(Double.parseDouble(stockPriceCurrent.getText()));
                stock.setMarketCap(Double.parseDouble(marketCap.getText()));
                stock.setRisk(Integer.parseInt(risk.getText()));
                portfolio.addStock(stock);
                stockList.setText(portfolio.getStocksAsString());
            } catch (NonCapLetterException exception) {
                System.out.println("Only capital letters allowed");
            } catch (TicketLengthException exception) {
                System.out.println("Invalid symbol: longer than 5 characters");
            } catch (NegativeDoubleException exception) {
                System.out.println("Negative values not allowed");
            } catch (RiskOutOfBoundaryException exception) {
                System.out.println("Risk outside of boundary 1-5");
            }
        } else if (e.getActionCommand().equals("Back to Main Menu")) {
            mainMenuVisibility();
        }
    }

    private void mainMenuVisibility() {
        mainMenu.setVisible(true);
        viewStockMenu.setVisible(false);
        addStockMenu.setVisible(false);
        investStocksMenu.setVisible(false);
        balanceMenu.setVisible(false);
    }

    // REQUIRES:
    // MODIFIES: this
    // EFFECTS: converts Label string to one that has multiple lanes for formatiing
    public static String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br/>") + "</html>";
    }

    // EFFECTS: Plays sound from given file string
    //          Credit to: https://www.programcreek.com/java-api-examples/?class=javax.sound.sampled.Clip&method=start
    public void playSound(String soundName) {
        try {
            Clip clip = AudioSystem.getClip();

            AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundName));

            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing the sound");
        }
    }
}

