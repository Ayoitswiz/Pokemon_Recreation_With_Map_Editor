package MainMenu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.beans.BeanProperty;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ItemShop extends JPanel {

    private JButton btnUpdateItems;
    private Item resellItem;
    private Image pnl4Image = new ImageIcon("src/imagesUi/PokeballFlat.png").getImage();
    private JPanel pnl4;

    ItemShop(Trainer ht, GUIManager gui){
        gui.addNewUi(this);

        JPanel itemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel storePanel = new JPanel(new GridBagLayout());
        JPanel pnl1 = new JPanel();
        JPanel pnl2 = new JPanel();
        JPanel pnl3 = new JPanel();
        pnl4 = new JPanel(new GridBagLayout());
        JPanel coverPnl4 = new JPanel(new GridBagLayout());
        JPanel moneyPanel = new JPanel(new GridBagLayout());
        JLabel moneyLabel = new LblProperties("Your Money: $" + ht.money);
        Font f = new Font("Times New Roman", Font.PLAIN, 32);
        Font f2 = new Font("Times New Roman", Font.PLAIN, 18);


        String[] columnNames = {"Item", "Price"};


        Item item = new Item();
        HashMap<String, Item> items;
        items = item.createItem();

        Object[][] data = new Object[items.size()][2];

        int i = 0;
        for (Item Item : items.values())
        {
            data[i][0] = Item.name;
            data[i][1] = "$" + Item.cost;
            i++;
        }


        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        JTable jTable = new JTable(tableModel);
        //jTable.setFillsViewportHeight(true);

        JScrollPane scrollPaneT = new JScrollPane(jTable);
        jTable.setGridColor(Color.RED);
        jTable.setFont(f2);
        jTable.setRowHeight(22);
        jTable.setDefaultEditor(Object.class, null);

        //jTable.setOpaque(false);
        scrollPaneT.getViewport().setBackground(new Color(190,210,255));
        jTable.getTableHeader().setFont(new Font("SansSerif", Font.ITALIC, 24));
        jTable.getTableHeader().setBackground(new Color(50, 50, 255));
        jTable.getTableHeader().setForeground(Color.WHITE);
        //jTable.setBackground(Color.RED);


        JLabel title = new JLabel("Item Shop");
        JPanel titlePanel = new JPanel();
        JButton btnPurchase = new SetBtnProperties("Purchase");
        JButton btnSell = new SetBtnProperties("Sell Back");
        JButton btnClose = new SetBtnProperties("Return to Main Menu");
        btnUpdateItems = new JButton();
        /*Add quantity of item you own to list indices */

        title.setFont(f);

        this.setLayout(new GridBagLayout());

        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        titlePanel.setBackground(new Color(70, 70, 250));
        pnl1.setBackground(new Color(190,210,255));


        pnl2.setBackground(new Color(70, 70, 250));
        pnl3.setBackground(Color.WHITE);
        coverPnl4.setBackground(new Color(115,191,235, 30));
        moneyPanel.setBackground(new Color(70, 70, 250, 255));
        moneyLabel.setForeground(Color.WHITE);

        moneyPanel.add(moneyLabel, c);

        c.weightx = 1;
        c.fill = 1;
        c.anchor = 11;
        c.gridwidth = 2;
        this.add(titlePanel, c);

        itemPanel.add(btnPurchase, c);
        itemPanel.add(btnSell, c);
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.gridwidth = 2;
        c.gridwidth = 1;
        this.add(itemPanel, c);

        c.weighty = .20;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        storePanel.add(pnl1, c);
        itemPanel.add(pnl3, c);

        c.weighty = .30;
        c.gridy= 1;
        storePanel.add(scrollPaneT, c);
        c.gridy = 2;
        c.weighty = 0.50;
        storePanel.add(pnl2, c);

        c.anchor = GridBagConstraints.SOUTHEAST;
        c.gridy = 1;
        c.weighty = 1;
        c.weightx =.40;
        c.gridwidth = 2;
        this.add(storePanel, c);

        c.weightx = .60;
        c.gridy = 100;
        c.gridwidth = 1;
        c.gridx = 0;
        c.fill = 2;
        coverPnl4.add(btnClose, c);

        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        coverPnl4.add(moneyPanel, c);


        c.fill = 1;
        c.gridy = 1;
        c.gridx = 2;
        c.gridheight =100;
        this.add(coverPnl4, c);

        c.fill = 1;
        c.gridy = 1;
        c.gridx = 2;
        c.gridheight =100;
        pnl4.setOpaque(false);
        this.add(pnl4, c);

        btnUpdateItems.addActionListener(e -> {
            displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);
            moneyLabel.setText("Your Money: $" + ht.money);
    });
        displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);

        btnPurchase.addActionListener(e -> {
            if(jTable.getSelectedColumnCount() != 0) {
                String a = (tableModel.getValueAt(jTable.getSelectedRow(), 1).toString().substring(1));
                String itemName = tableModel.getValueAt(jTable.getSelectedRow(), 0).toString();
                int cost = Integer.parseInt(a);

                if (ht.money.compareTo(new BigDecimal(cost)) >= 0) {
                    ht.money = ht.money.subtract(new BigDecimal(cost));
                    moneyLabel.setText("Your Money: $" + ht.money);
                    if (ht.items.get(itemName) == null)
                        ht.items.put(items.get(itemName).name, items.get(itemName));
                    ht.items.get(itemName).quantity++;
                    displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);
                }
            }
        });

        btnSell.addActionListener(e -> {
            if(resellItem != null)
            if(ht.items.get(resellItem.name) != null) {
                ht.money = ht.money.add(new BigDecimal(ht.items.get(resellItem.name).resellPrice));
                ht.items.get(resellItem.name).quantity -= 1;
                moneyLabel.setText("Your Money: $" + ht.money);
                displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);
            }

        });

        btnClose.addActionListener(e -> {
            this.setVisible(false);
            gui.setUi("MainMenuGUI");
        });

        this.setVisible(true);
        setOpaque(false);
    }
    public void displayHtItems(Trainer ht, JPanel pnl4, GridBagConstraints c, JPanel moneyPanel, JLabel moneyLabel){
        moneyPanel.removeAll();
        ArrayList<Item> ItemArrayList = new ArrayList<>();

        c.gridy = 0;
        moneyPanel.add(moneyLabel);
        int i = 1;
        for (Item item: ht.items.values()) {
            JButton row = new JButton();
            row.setLayout(new GridBagLayout());
            row.setBackground(Color.WHITE);
            row.addActionListener(e -> Resell(item));

            i = getI(c, moneyPanel, ItemArrayList, i, item, row);

        }
        for (Item item: ItemArrayList) {
            ht.items.remove(item.name);
        }
        pnl4.setVisible(false);
        pnl4.setVisible(true);
    }

    static int getI(GridBagConstraints c, JPanel moneyPanel, ArrayList<Item> itemArrayList, int i, Item item, JButton row) {
        c.fill = 2;
        c.gridwidth = 1;
        c.gridx = 0;
        c.weightx = .50;
        row.add(new LblProperties(item.name), c);
        c.gridx = 1;
        JLabel p = new LblProperties("x" + item.quantity);
        p.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        row.add(p, c);

        c.weightx = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        c.gridy = i;
        c.gridheight = 1;
        moneyPanel.add(row, c);
        if(item.quantity<=0) {
            moneyPanel.remove(row);
            itemArrayList.add(item);
        }
        i++;
        return i;
    }

    public void Resell(Item item){
        resellItem = item;
    }

    public void getBtnUpdateItems() {
        btnUpdateItems.doClick();
    }

    public Image getBackgroundImage() {
        return pnl4Image;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(pnl4Image, pnl4.getX(), pnl4.getY(), pnl4.getWidth(), pnl4.getHeight(), pnl4);
    }
}
