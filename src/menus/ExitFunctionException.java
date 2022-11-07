package menus;

import menus.Styles.ErrorLabel;
import lombok.NoArgsConstructor;

/**
 * This class serves as a cleaner, shorter, & more informative way of
 * creating and using "guard clauses"
 */
@NoArgsConstructor
public class ExitFunctionException extends IllegalArgumentException {
  private static final ExitFunctionException ex = new ExitFunctionException();
  public ExitFunctionException(String message) {
    super(message);
  }

  public ExitFunctionException(String message, ErrorLabel errorLabel) {
    super(message);
      errorLabel.setText(message);
      errorLabel.setVisible(true);
  }

  public ExitFunctionException(String message, String logLvl) {
    super(message);
  }

  public ExitFunctionException(String message, ErrorLabel errorLabel, String logLvl) {
    super(message);
      errorLabel.setText(message);
      errorLabel.setVisible(true);
  }

  public static ExitFunctionException If(boolean bool) {
    if (bool) throw new ExitFunctionException();
    return ex;
  }

  public static ExitFunctionException If(boolean bool, String msg) {
    if (bool) throw new ExitFunctionException(msg);
    return ex;
  }
}
