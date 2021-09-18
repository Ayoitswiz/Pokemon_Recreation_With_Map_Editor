package gg.Styles;

import javax.swing.*;
import java.net.URL;


public class GifLbl extends JLabel
{
    GifLbl(String name)
    {
        URL url = getClass().getResource(name + ".gif");
        Icon icon = new ImageIcon(url);
       this.setIcon(icon);
        //this.setBackground(new Color(50,50,50,0));
        setOpaque(false);
    }
}
