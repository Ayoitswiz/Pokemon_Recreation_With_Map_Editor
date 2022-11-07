package menus.backpack;

import menus.ExitFunctionException;
import menus.gui;
import menus.in_game_items.Item;
import menus.in_game_items.ItemShop;
import menus.Styles.LblProperties;
import menus.Styles.SetBtnProperties;
import menus.battle.trainers.HumanTrainer;
import menus.ViewPokeSlots.PokemonInPartyPanel;
import lombok.NoArgsConstructor;
import utilities.Lambda;

import javax.swing.*;
import java.awt.*;

@NoArgsConstructor
public class Backpack extends JPanel {

  private Item selectedItem;
  private Lambda onBackpackClose;
  private Lambda onUsedItem;


public Backpack open(HumanTrainer ht) {

    JPanel itemPanel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    JPanel storePanel = new JPanel(new GridBagLayout());
    JPanel pnl3 = new JPanel();
    JPanel itemsInBagPanel = new JPanel(new GridBagLayout());
    JPanel itemsInBagTitlePanel = new JPanel(new GridBagLayout());
    JLabel yourItemslbl = new LblProperties("Your Items");
    Font f = new Font("Times New Roman", Font.PLAIN, 32);
    Font f2 = new Font("SansSerif", Font.PLAIN, 24);
    Image bg = new ImageIcon("src/img/UI/Backpack.png").getImage();
    JPanel backpackPic = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
      }
    };

    JLabel title = new JLabel("Backpack");
    JPanel titlePanel = new JPanel();
    JButton btnUse = new SetBtnProperties("Use");
    JButton btnExit = new SetBtnProperties("Exit");
    Lambda l = () -> {
      title.setFont(f);
      btnUse.setFont(f2);
      btnExit.setFont(f2);

      this.setLayout(new GridBagLayout());

      storePanel.setBackground(Color.RED);
      itemPanel.setBackground(Color.MAGENTA);

      title.setForeground(Color.WHITE);
      titlePanel.add(title);
      titlePanel.setBackground(new Color(160, 69, 0, 255));

      pnl3.setBackground(Color.WHITE);
      itemsInBagPanel.setBackground(new Color(196, 182, 132));
      itemsInBagTitlePanel.setBackground(new Color(160, 69, 0, 255));
      yourItemslbl.setForeground(Color.WHITE);

      itemsInBagTitlePanel.add(yourItemslbl, c);

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
      c.gridy = 1;
      storePanel.add(backpackPic, c);
      c.gridy = 2;
      c.weighty = 0.50;


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
      itemsInBagPanel.add(btnExit, c);

      c.gridy = 0;
      c.anchor = GridBagConstraints.NORTHEAST;
      itemsInBagPanel.add(itemsInBagTitlePanel, c);

      c.fill = 1;
      c.gridy = 1;
      c.gridx = 2;
      c.gridheight = 100;
      this.add(itemsInBagPanel, c);
      itemsInBagPanel.setVisible(true);
    };
    l.foo();

    ht.getItems().values().forEach(i ->
      new JButton() {{
        setLayout(new GridBagLayout());
        setOpaque(false);
        setBackground(new Color(196, 182, 132));
        addActionListener(e -> selectedItem = i);
        ItemShop.addItemRowInBag(c, itemsInBagTitlePanel, i, this);
      }});


    btnUse.addActionListener(e -> {
      ExitFunctionException.If(selectedItem == null);
      PokemonInPartyPanel pokemonInPartyPanel = new PokemonInPartyPanel();
      pokemonInPartyPanel.open(
       ht.getPokeSlots(),
			poke -> {
         if (ht.useItem(poke, selectedItem)) {
           onUsedItem.foo();
         }
       });
      pokemonInPartyPanel.onPartyClose(onBackpackClose::foo);
    });


    btnExit.addActionListener(e -> onBackpackClose.foo());

    this.setVisible(true);
    this.setBackground(new Color(227, 207, 134));
    gui.addNewBackgroundImage(getClass().getSimpleName(), new ImageIcon("src/imagesUI/Pokeball.png").getImage());
    gui.addNewUI(getClass().getSimpleName(), this);
    gui.setBackgroundImage(getClass().getSimpleName());
    gui.setUI(getClass().getSimpleName());
    return this;
  }

  public Backpack onBackpackClose(Lambda l) {
    this.onBackpackClose = l;
    return this;
  }

  public Backpack onUsedItem(Lambda l) {
    onUsedItem = l;
    return this;
  }
}
