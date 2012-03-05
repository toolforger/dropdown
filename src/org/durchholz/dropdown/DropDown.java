package org.durchholz.dropdown;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;

import javax.accessibility.Accessible;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/**
 * A JComboBox that does not use the list of items, but allows an
 * arbitrary JPanel in the drop-down popup.<br>
 * Since there is no list, there is no list cell renderer, and instead
 * of an editor, this class will accept an arbitrary JComponent with no
 * specific contract or interface on it.<br>
 * Use standard Swing event handling mechanisms to establish consistency
 * between JPanel and JComponent. As always, this can be done by
 * creating a subclass, at the cost of being dependent on implementation
 * details.<p>
 * 
 * Tested with Java 1.6.0_23 (IcedTea6 1.11pre).
 */
public class DropDown extends JComboBox {

  private static final long serialVersionUID = 1L;

  JComponent component;
  JPanel popup;

  public DropDown (JComponent component, JPanel popup) {
    super ();
    ///////////////////////
    // Set up the component
    this.component = component;
    // Work around BasicComboBoxUI setting up a minimum width of 100 if
    // the JComboBox is editable. *facepalm*
    setPrototypeDisplayValue ("");
    // Work around BasicComboBoxUI picking up its preferred size from
    // the list of items, which stays empty.
    setEditable (true);
    // So we need a ComboBoxEditor:
    setEditor (new ComponentWrapper ());
    ///////////////////
    // Set up the popup
    this.popup = popup;
    addPopupMenuListener (new PopupListener ());
  }

  protected class PopupListener implements PopupMenuListener {

    @Override
    public void popupMenuWillBecomeVisible (PopupMenuEvent e) {
      Accessible accPopup =
          getUI ().getAccessibleChild (DropDown.this, 0);
      if (! (accPopup instanceof Container)) {
        throw new RuntimeException (
            "For DropDown to work, the UI provided by the Look&Feel " +
            "must use a Container for the drop-down panel.");
      }
      Container cPopup = (Container) accPopup;
      if (
          cPopup.getComponentCount () != 1
          || cPopup.getComponent (0) != popup)
      {
        cPopup.removeAll ();
        cPopup.add (popup);
      }
    }

    @Override
    public void popupMenuWillBecomeInvisible (PopupMenuEvent e) {
      // Nothing to do
    }

    @Override
    public void popupMenuCanceled (PopupMenuEvent e) {
      // Nothing to do
    }
    
  }

  protected class ComponentWrapper implements ComboBoxEditor {

    @Override
    public Component getEditorComponent () {
      return component;
    }

    @Override
    public void setItem (Object anObject) {
      // Not needed: the item list is left unused.
    }

    @Override
    public Object getItem () {
      // Not needed: the item list is left unused.
      return null;
    }

    @Override
    public void selectAll () {
      // A JComponent has no idea of "selection" (that's in subclasses.)
      // However, if a "select all" operation is desired, component can
      // always select itself fully whenever it gains focus.
      component.requestFocusInWindow ();
    }

    @Override
    public void addActionListener (ActionListener l) {
      // There is no concept of an "action" in DropDown, so we cannot
      // do anything useful with an ActionListener.
    }

    @Override
    public void removeActionListener (ActionListener l) {
      // There is no concept of an "action" in DropDown, so we cannot
      // do anything useful with an ActionListener.
    }

  }

}
