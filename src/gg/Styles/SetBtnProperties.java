package gg.Styles;

import javax.swing.*;
import java.awt.*;

public class SetBtnProperties extends JButton{

    public SetBtnProperties(String text) {
        this.setBackground(new Color(59, 89, 182));
        this.setForeground(Color.WHITE);
        this.setFocusPainted(false);
        this.setFont(new Font("Tahoma", Font.BOLD, 14));
        this.setText(text);
    }
    public void SetBattleGuiButtons(){
    }
}
