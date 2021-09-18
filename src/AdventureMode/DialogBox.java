package AdventureMode;

import utilities.Lambda;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class DialogBox extends JPanel {
  private Border raisedetched, loweredetched, raisedbevel, loweredbevel, redLine, thick, buffer;
  private Border compound;
  private Color menu = new Color(200, 55, 55);


  private JTextArea textarea;
  private DialogBoxButton next;
  private Lambda onDialogEnd = () -> {};
  private Queue<Lambda> onNext = new LinkedList<>();

  public DialogBox() {
    setLayout(new BorderLayout());
    raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
    loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    //Border a = BorderFactory.createMatteBorder(30, 30, 10, 30, mainMenuBackground);
    raisedbevel = BorderFactory.createRaisedBevelBorder();
    loweredbevel = BorderFactory.createLoweredBevelBorder();
    buffer = BorderFactory.createMatteBorder(2, 7, 3, 7, new Color(255, 0, 0));
    //empty = BorderFactory.createEmptyBorder();
    redLine = BorderFactory.createLineBorder(menu, 10);
    thick = (BorderFactory.createStrokeBorder(new BasicStroke(10.0f)));
    compound = BorderFactory.createCompoundBorder(raisedbevel, loweredbevel);
    compound = BorderFactory.createCompoundBorder(redLine, compound);
    compound = BorderFactory.createCompoundBorder(thick, compound);
    compound = BorderFactory.createCompoundBorder(loweredetched, compound);
    compound = BorderFactory.createCompoundBorder(raisedetched, compound);
    compound = BorderFactory.createCompoundBorder(buffer, compound);

    setBorder(compound);

    /** This text area is never added to the ui.
     * Instead, it's text is being drawn in the overrided paintcomponent.
     * This is done because the text returned from it's<code>getText()</code> method is still formatted.
     * Therefore, when using <code>Graphics.drawString()</code> the text will output with all the
     */
    textarea = new JTextArea() {{
      setWrapStyleWord(true);
      setLineWrap(true);
      setEditable(false);
      setFocusable(false);
      setBackground(new Color(255, 0, 0, 1));
      setFont(new Font("Times New Roman", Font.PLAIN, 24));
      setBorder(UIManager.getBorder("Label.border"));
      setRows(2);
      setColumns(20);
      setVisible(true);
    }};

    next = new DialogBoxButton();
    setBackground(new Color(255, 225, 255));
    add(next);
    setVisible(false);
  }


  public DialogBox open() {
    this.setVisible(true);
    return this;
  }

  /**
   * Used to set text without displaying the <attribute>dialogBoxButton</attribute>
   */
  public DialogBox setDialog(String dialog) {
    textarea.setText(dialog);
    return this;
  }

  public DialogBox onNext(Lambda onNext) {
    next.setVisible(true);
    this.onNext.add(onNext);
    return this;
  }



  public DialogBox setDialog(Lambda onNext) {
    next.setVisible(true);
    this.onNext.add(onNext);
    return this;
  }

  public DialogBox setNextNotVisible() {
    next.setVisible(false);
    return this;
  }

  public DialogBox cycleDialog(Queue<String> cycleDialog) {
    cycleDialog.forEach(dialog -> onNext.add(() -> setDialog(dialog)));
    return this;
  }

  public DialogBox cycleDialog(String[] cycleDialog) {
    setVisible(true);
    next.setVisible(true);
    Arrays.asList(cycleDialog).forEach(dialog -> onNext.add(() -> setDialog(dialog)));
    return this;
  }

  public DialogBox onDialogEnd(Lambda funcToExecuteOnDialogEnd) {
    next.setVisible(true);
    this.onDialogEnd = funcToExecuteOnDialogEnd;
    return this;
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setFont(g.getFont().deriveFont(g.getFont().getSize() * (float) ((getWidth() + getHeight()) / 500)));
    g.setColor(Color.BLACK);
    g.drawString(textarea.getText(), getInsets().left + 3, getInsets().top + g.getFontMetrics().getHeight());
    next.setBounds(
  getInsets().left,
  (getHeight() - (getHeight() / 8)) - getInsets().bottom,
  getWidth() - (getInsets().right * 2),
      getHeight() / 8);
    System.out.println(textarea.getText());

  }


  private final class DialogBoxButton extends JButton {
    DialogBoxButton() {
      setVisible(false);
      setText("Next");
      setBackground(new Color(59, 89, 182));
      setForeground(Color.WHITE);
      setFocusPainted(false);
      setFont(new Font("Tahoma", Font.BOLD, 14));

      addActionListener(e -> {
        if (onNext.size() > 0) {
          onNext.remove().foo();
          DialogBox.this.repaint();
          DialogBox.this.validate();
        } else {
          setNextNotVisible();
          onDialogEnd.foo();
          onDialogEnd = () -> {};
        }
      });
    }
  }
}
