package MainMenu;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Backpack extends JPanel {

    private Item selectedItem;
    private JButton btnUpdateItems;
    public Backpack(HumanTrainer humanTrainer, GUIManager gui){

        gui.addNewUi(this);
        JPanel itemPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        JPanel storePanel = new JPanel(new GridBagLayout());
        JPanel pnl1 = new JPanel();
        JPanel pnl2 = new JPanel();
        JPanel pnl3 = new JPanel();
        JPanel pnl4 = new JPanel(new GridBagLayout());
        JPanel yourItemsPanel = new JPanel(new GridBagLayout());
        JLabel yourItemslbl = new LblProperties("Your Items");
        Font f = new Font("Times New Roman", Font.PLAIN, 32);
        Font f2 = new Font("SansSerif", Font.PLAIN, 24);
        btnUpdateItems = new JButton();
        Image bg = new ImageIcon("src/imagesUI/Backpack.png").getImage();
        JPanel backpackPic = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };

        JLabel title = new JLabel("Backpack");
        JPanel titlePanel = new JPanel();
        JButton btnUse = new SetBtnProperties("Use");
        JButton btnExit = new SetBtnProperties("Exit");
        /*Add quantity of item you own to list indices */

        title.setFont(f);
        btnUse.setFont(f2);
        btnExit.setFont(f2);

        this.setLayout(new GridBagLayout());

        storePanel.setBackground(Color.RED);
        itemPanel.setBackground(Color.MAGENTA);

        title.setForeground(Color.WHITE);
        titlePanel.add(title);
        titlePanel.setBackground(new Color(160, 69,0, 255));

        pnl1.setBackground(Color.cyan);
        pnl2.setBackground(Color.ORANGE);
        pnl3.setBackground(Color.WHITE);
        pnl4.setBackground(new Color(196,182,132));
        yourItemsPanel.setBackground(new Color(160, 69,0, 255));
        yourItemslbl.setForeground(Color.WHITE);

        yourItemsPanel.add(yourItemslbl, c);

        c.weightx = 1;
        c.fill = 1;
        c.anchor = 11;
        c.gridwidth = 2;
        this.add(titlePanel, c);

        itemPanel.add(btnUse, c);
        c.anchor = GridBagConstraints.SOUTHEAST;
        c.gridwidth = 2;
        c.gridwidth = 1;
        this.add(itemPanel, c);


        c.weighty = .20;
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridheight = 1;


        itemPanel.add(pnl3, c);

        c.weighty = .30;
        c.gridy= 1;
        storePanel.add(backpackPic, c);
        c.gridy = 2;
        c.weighty = 0.50;


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
        pnl4.add(btnExit, c);

        c.gridy = 0;
        c.anchor = GridBagConstraints.NORTHEAST;
        pnl4.add(yourItemsPanel, c);

        c.fill = 1;
        c.gridy = 1;
        c.gridx = 2;
        c.gridheight =100;
        this.add(pnl4, c);
        displayHtItems(humanTrainer, pnl4, c, yourItemsPanel, yourItemslbl);


        btnUpdateItems.addActionListener(e -> displayHtItems(humanTrainer, pnl4, c, yourItemsPanel, yourItemslbl));

        btnUse.addActionListener(e -> {
            if(selectedItem != null)
                if(humanTrainer.getItems().get(selectedItem.name) != null) {
                    gui.setUi("PokemonInPartyPanel");
                    gui.setBackgroundImage("PokemonInPartyPanel");
                    humanTrainer.setUseItem(selectedItem);
                    //Updates items upon leaving so when backpack is opened again the ui displays the correct information
                    displayHtItems(humanTrainer, pnl4, c, yourItemsPanel, yourItemslbl);
                }
        });

        btnExit.addActionListener(e -> {
            if (humanTrainer.getNpcOpponent() != null) //user will always have an opponent if in battle
                gui.setUi("BattleGUI");
            else
                gui.setUi("AdventureModeMain");
        });

        this.setVisible(true);
        this.setBackground(new Color(227,207,134));
    }

    public void displayHtItems(Trainer ht, JPanel pnl4, GridBagConstraints c, JPanel yourItemsPanel, JLabel yourItemslbl){
        yourItemsPanel.removeAll();
        ArrayList<Item> ItemArrayList = new ArrayList<>();

        c.gridy = 0;
        yourItemsPanel.add(yourItemslbl);
        int i = 1;
        for (Item item: ht.getItems().values()) {
            JButton row = new JButton();
            row.setLayout(new GridBagLayout());
            //row.setOpaque(false);
            row.setBackground(new Color(196,182,132));
            row.addActionListener(e -> SelectItem(item));

            i = ItemShop.getI(c, yourItemsPanel, ItemArrayList, i, item, row);
        }
        for (Item item: ItemArrayList) {
            ht.getItems().remove(item.name);
        }
        pnl4.setVisible(false);
        pnl4.setVisible(true);
    }
    public void SelectItem(Item item){
        selectedItem = item;
    }

    public void getBtnUpdateItems() {
        btnUpdateItems.doClick();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        btnUpdateItems.doClick();
    }

}
