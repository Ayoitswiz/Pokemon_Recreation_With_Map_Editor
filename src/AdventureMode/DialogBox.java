package AdventureMode;

import MainMenu.BattleGUI;
import MainMenu.GUIManager;
import MainMenu.MainMenuGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
public class DialogBox extends JPanel {
    private Border raisedetched, loweredetched, raisedbevel, loweredbevel, redLine, thick, buffer;
    private Border compound;
    private Color menu = new Color(200, 55, 55);
    private JTextArea txtLeftDefaultText;
    private JTextArea dialogBoxTextArea;
    private DialogBoxButton dialogBoxButton;
    private AdventureModeUiPanel ui;
    private GUIManager gui;
    private String[] dialogBoxDialog;
    private MySprite sprite;

    DialogBox(MySprite sprite, GUIManager gui) {
        this.gui = gui;
        this.sprite = sprite;
        this.dialogBoxDialog = sprite.getNpcOpponent().preBattleDialog;
        Icon mainMenuBackground = new ImageIcon("src/imagesUI/MainMenuBackground.jpg");
        setLayout(new BorderLayout());
        raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        //Border a = BorderFactory.createMatteBorder(30, 30, 10, 30, mainMenuBackground);
        raisedbevel = BorderFactory.createRaisedBevelBorder();
        loweredbevel = BorderFactory.createLoweredBevelBorder();
        buffer = BorderFactory.createMatteBorder(2, 7, 3, 7, new Color(255, 0,0));
        //empty = BorderFactory.createEmptyBorder();
        redLine = BorderFactory.createLineBorder(menu, 16);
        thick = (BorderFactory.createStrokeBorder(new BasicStroke(10.0f)));
        compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
        compound = BorderFactory.createCompoundBorder(redLine, compound);
        compound = BorderFactory.createCompoundBorder(thick, compound);
        compound = BorderFactory.createCompoundBorder(loweredetched, compound);
        compound = BorderFactory.createCompoundBorder(raisedetched, compound);
        compound = BorderFactory.createCompoundBorder(buffer, compound);

        setBorder(compound);
        dialogBoxTextArea = new DialogBoxTextArea();

        dialogBoxButton = new DialogBoxButton();
        add(dialogBoxButton);
        setVisible(false);
        setBackground(new Color(255, 225, 255));

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * (float) ((getWidth() + getHeight()) / 500));
        g.setFont(newFont);
        g.setColor(Color.BLACK);
        g.drawString(dialogBoxTextArea.getText(), getInsets().left + 3, getInsets().top + g.getFontMetrics().getHeight());
        dialogBoxButton.setBounds(getInsets().left, (getHeight() - (getHeight() / 8)) - getInsets().bottom, getWidth() - (getInsets().right * 2), getHeight() / 8);
    }

    class DialogBoxTextArea extends JTextArea {
        DialogBoxTextArea() {
            Font f = new Font("Times New Roman", Font.PLAIN, 24);
            setBackground(new Color(255, 0, 0, 1));
            setRows(2);
            setColumns(20);
            setWrapStyleWord(true);
            setLineWrap(true);
            setEditable(false);
            setFocusable(false);
            setFont(f);
            setText(dialogBoxDialog[0]);
            setVisible(true);
            setBorder(UIManager.getBorder("Label.border"));
        }
    }

    class DialogBoxButton extends JButton {
        int i = 1;

        DialogBoxButton() {

            setBackground(new Color(59, 89, 182));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setVisible(true);
            setFont(new Font("Tahoma", Font.BOLD, 14));
            setText("Next");

            addActionListener(e -> {
                if (i == dialogBoxDialog.length) {
                    removeActionListener(getActionListeners()[0]);
                    getParent().setVisible(false);
                    //Create battle start class
                    BattleGUI battleGUI = new BattleGUI(sprite, gui);
                    gui.addNewUi(battleGUI);
                    gui.setUi(battleGUI.getClass().getSimpleName());
                    gui.addNewBackgroundImage(battleGUI.getClass().getSimpleName(), battleGUI.getBackgroundImage());
                    gui.setBackgroundImage(battleGUI.getClass().getSimpleName());
                } else {
                    dialogBoxTextArea.setText(dialogBoxDialog[i]);
                }
                i++;
            });
        }
    }
}