package MainMenu;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


class gifLbl extends JLabel
{
    gifLbl(String name)
    {
        URL url = getClass().getResource("/images"+ name + ".gif");
        Icon icon = new ImageIcon(url);
       this.setIcon(icon);
        //this.setBackground(new Color(50,50,50,0));
        setOpaque(false);
    }
}
