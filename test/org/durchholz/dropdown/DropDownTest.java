package org.durchholz.dropdown;


import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * A main program to "play" with DropDown.<p>
 * 
 * Copyright (C) 2012 Joachim Durchholz (toolforger@durchholz.org)
 */
public class DropDownTest {

  protected static DropDown dropDown;
  protected static JComboBox comboBox;

  public static void main (String [] args) {
    final SortedMap <String, String> lafs = new TreeMap <String, String> ();
    String xPlatformLaf = UIManager.getCrossPlatformLookAndFeelClassName ();
    String systemLaf = UIManager.getSystemLookAndFeelClassName ();
    for (LookAndFeelInfo laf: UIManager.getInstalledLookAndFeels ()) {
      String displayText = laf.getName ();
      String className = laf.getClassName ();
      if (className.equals (xPlatformLaf)) {
        displayText += " (Cross-Platform)";
        xPlatformLaf = null;
      }
      if (className.equals (systemLaf)) {
        displayText += " (System)";
        systemLaf = null;
      }
      lafs.put (displayText, className);
    }
    if (xPlatformLaf != null) {
      lafs.put ("(Cross-Platform)", xPlatformLaf);
    }
    if (systemLaf != null) {
      lafs.put ("(System)", systemLaf);
    }

    final JFrame frame = new JFrame ();
    frame.setSize (600, 200);
    frame.setLocation (100, 100);
    Container container = frame.getContentPane ();
    container.setLayout (null);

    JPanel popup = new JPanel ();
    popup.setLayout (null);
    popup.setPreferredSize (new Dimension (400, 200));
    JLabel label = new JLabel ("blah at (2,10)");
    popup.add (label);
    label.setLocation (2, 10);

    JTextField textField = new JTextField (20) {
      private static final long serialVersionUID = 1L;
      @Override
      public void setSize (Dimension d) {
        super.setSize (d);
      }
    };
    textField.setText ("Sample");
//    dropDown = new DropDown2 ();
    dropDown = new DropDown (textField, popup);
    container.add (dropDown);
    dropDown.setLocation (1, 1);

    comboBox = new JComboBox ();
    for (String displayText: lafs.keySet ()) {
      comboBox.addItem (displayText);
    }
    comboBox.setSelectedIndex (-1);
    container.add (comboBox);
    comboBox.setLocation (1, 31);
    comboBox.addActionListener (new ActionListener () {
      @Override
      public void actionPerformed (ActionEvent evt) {
        try {
          UIManager.setLookAndFeel (
              lafs.get (comboBox.getSelectedItem ()));
        } catch (UnsupportedLookAndFeelException e) {
          throw new RuntimeException (e);
        } catch (ClassNotFoundException e) {
          throw new RuntimeException (e);
        } catch (InstantiationException e) {
          throw new RuntimeException (e);
        } catch (IllegalAccessException e) {
          throw new RuntimeException (e);
        }
        SwingUtilities.updateComponentTreeUI (frame);
        System.out.println ("New Look&Feel installed");
        adjustAndReportLayout ();
    }});

    adjustAndReportLayout ();

    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    frame.setVisible (true);
  }

  protected static void adjustAndReportLayout () {
    comboBox.setSize (comboBox.getPreferredSize ());
    dropDown.setSize (dropDown.getPreferredSize ());
    System.out.println ("sizes (min/preferred/max/current):");
//    printSizes ("component", dropDown.getComponent ());
//    printSizes ("button", dropDown.getButton ());
    printSizes ("DropDown", dropDown);
  }

  protected static void printSizes (String description, JComponent c) {
    System.out.println (
        description + ": " +
        dim (c.getMinimumSize ()) + "/" +
        dim (c.getPreferredSize ()) + "/" +
        dim (c.getMaximumSize ()) + "/" +
        dim (c.getSize ()));
  }

  protected static String dim (Dimension d) {
    return "" + d.width + "x" + d.height;
  }

}
