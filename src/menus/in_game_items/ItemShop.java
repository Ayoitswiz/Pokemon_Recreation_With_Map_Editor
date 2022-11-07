package menus.in_game_items;

import menus.Styles.LblProperties;
import menus.Styles.SetBtnProperties;
import menus.battle.trainers.Trainer;
import menus.gui;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.math.BigDecimal;
import java.util.Map;

public class ItemShop extends JPanel {

private JButton btnUpdateItems;
private Item resellItem;
private Image pnl4Image = new ImageIcon("src/img/UI/PokeballFlat.png").getImage();
private JPanel pnl4;

public ItemShop(Trainer ht) {

	JPanel itemPanel = new JPanel(new GridBagLayout());
	GridBagConstraints c = new GridBagConstraints();
	JPanel storePanel = new JPanel(new GridBagLayout());
	JPanel pnl1 = new JPanel();
	JPanel pnl2 = new JPanel();
	JPanel pnl3 = new JPanel();
	pnl4 = new JPanel(new GridBagLayout());
	JPanel coverPnl4 = new JPanel(new GridBagLayout());
	JPanel moneyPanel = new JPanel(new GridBagLayout());
	JLabel moneyLabel = new LblProperties("Your Money: $" + ht.getMoney());
	Font f = new Font("Times New Roman", Font.PLAIN, 32);
	Font f2 = new Font("Times New Roman", Font.PLAIN, 18);


	String[] columnNames = {"Item", "Price"};


	Item item = new Item();
	Map<String, Item> items;
	items = item.createItem();

	Object[][] data = new Object[items.size()][2];

	int i = 0;
	for (Item Item : items.values()) {
		data[i][0] = Item.getName();
		data[i][1] = "$" + Item.getCost();
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
	scrollPaneT.getViewport().setBackground(new Color(190, 210, 255));
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
	pnl1.setBackground(new Color(190, 210, 255));


	pnl2.setBackground(new Color(70, 70, 250));
	pnl3.setBackground(Color.WHITE);
	coverPnl4.setBackground(new Color(115, 191, 235, 30));
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
	c.gridy = 1;
	storePanel.add(scrollPaneT, c);
	c.gridy = 2;
	c.weighty = 0.50;
	storePanel.add(pnl2, c);

	c.anchor = GridBagConstraints.SOUTHEAST;
	c.gridy = 1;
	c.weighty = 1;
	c.weightx = .40;
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
	c.gridheight = 100;
	this.add(coverPnl4, c);

	c.fill = 1;
	c.gridy = 1;
	c.gridx = 2;
	c.gridheight = 100;
	pnl4.setOpaque(false);
	this.add(pnl4, c);

	btnUpdateItems.addActionListener(e -> {
		displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);
		moneyLabel.setText("Your Money: $" + ht.getMoney());
	});
	displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);

	btnPurchase.addActionListener(e -> {
		if (jTable.getSelectedColumnCount() != 0) {
			String a = (tableModel.getValueAt(jTable.getSelectedRow(), 1).toString().substring(1));
			String itemName = tableModel.getValueAt(jTable.getSelectedRow(), 0).toString();
			int cost = Integer.parseInt(a);

			if (ht.getMoney().compareTo(new BigDecimal(cost)) >= 0) {
				ht.setMoney(ht.getMoney().subtract(new BigDecimal(cost)));
				moneyLabel.setText("Your Money: $" + ht.getMoney());
				if (ht.getItems().get(itemName) == null)
					ht.getItems().put(items.get(itemName).getName(), items.get(itemName));
				ht.getItems().get(itemName).setQuantity(ht.getItems().get(itemName).getQuantity() + 1);
				displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);
			}
		}
	});

	btnSell.addActionListener(e -> {
		if (resellItem != null)
			if (ht.getItems().get(resellItem.getName()) != null) {
				ht.setMoney(ht.getMoney().add(new BigDecimal(ht.getItems().get(resellItem.getName()).getResellPrice())));
				ht.getItems().get(resellItem.getName()).setQuantity(ht.getItems().get(resellItem.getName()).getQuantity() - 1);
				moneyLabel.setText("Your Money: $" + ht.getMoney());
				displayHtItems(ht, pnl4, c, moneyPanel, moneyLabel);
			}
	});

	btnClose.addActionListener(e -> gui.setUI("MainMenuGUI"));

	this.setVisible(true);
	setOpaque(false);
	gui.addNewUI(this);
	gui.setUI(this.getClass().getSimpleName());

}

public void displayHtItems(Trainer ht, JPanel pnl4, GridBagConstraints c, JPanel moneyPanel, JLabel moneyLabel) {
	moneyPanel.removeAll();

	c.gridy = 0;
	moneyPanel.add(moneyLabel);
	for (Item item : ht.getItems().values()) {
		JButton row = new JButton();
		row.setLayout(new GridBagLayout());
		row.setBackground(Color.WHITE);
		row.addActionListener(e -> Resell(item));

		addItemRowInBag(c, moneyPanel, item, row);
	}
	pnl4.setVisible(false);
	pnl4.setVisible(true);
}

public static int rowNum = 1;
public static void addItemRowInBag(GridBagConstraints c, JPanel moneyPanel, Item item, JButton row) {
	c.fill = 2;
	c.gridwidth = 1;
	c.gridx = 0;
	c.weightx = .50;
	row.add(new LblProperties(item.getName()), c);
	c.gridx = 1;
	JLabel p = new LblProperties("x" + item.getQuantity());
	p.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	row.add(p, c);

	c.weightx = 1;
	c.gridx = 0;
	c.gridwidth = 1;
	c.gridy = rowNum++;
	c.gridheight = 1;
	moneyPanel.add(row, c);
	if (item.getQuantity() <= 0) {
		moneyPanel.remove(row);
	}
}

public void Resell(Item item) {
	resellItem = item;
}

@Override
protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.drawImage(pnl4Image, pnl4.getX(), pnl4.getY(), pnl4.getWidth(), pnl4.getHeight(), pnl4);
}
}
