import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
* Creates a Calculator for Teacher-Student Chat
*
* @author Anna and DACK team
* @version 2.0
*/
public class Calculator extends JPanel{
   /**
    * JPanel that holds the entire calculator
    */
   private JPanel jpCalc;
   /**
    * JPanel that holds the text describing what
    * calculation is being made.
    */
   private JPanel jpText;
   /**
    * JButton that allows the user to add (+)
    */
   private JButton jbAdd;
   /**
    * JButton that allows the user to subtract (-)
    */
   private JButton jbSub;
   /**
    * JButton that allows the user to multiply (*)
    */
   private JButton jbMult;
   /**
    * JButton that allows the user to divide (/)
    */
   private JButton jbDiv;
   /**
    * JButton that allows the user to clear the
    * text area
    */
   private JButton jbClear;
   /**
    * JButton that allows the user to display zero
    */
   private JButton jbZero;
   /**
    * JButton that allows the user to display one
    */
   private JButton jbOne;
   /**
    * JButton that allows the user to display two
    */
   private JButton jbTwo;
   /**
    * JButton that allows the user to display three
    */
   private JButton jbThree;
   /**
    * JButton that allows the user to display four
    */
   private JButton jbFour;
   /**
    * JButton that allows the user to display five
    */
   private JButton jbFive;
   /**
    * JButton that allows the user to display six
    */
   private JButton jbSix;
   /**
    * JButton that allows the user to display seven
    */
   private JButton jbSeven;
   /**
    * JButton that allows the user to display eight
    */
   private JButton jbEight;
   /**
    * JButton that allows the user to display nine
    */
   private JButton jbNine;
   /**
    * JButton that allows the user to display equal (=)
    */
   private JButton jbEqual;
   /**
    * JButton that allows the user to display decimal (.)
    */
   private JButton jbDecimal;
   /**
    * JButton that allows the user to backspace the number
    */
   private JButton jbBackspace;
   /**
    * JTextArea that displays the desired numbers
    */
   private JTextArea jtaProblem;
   /**
    * Double type for the result
    */
   private double result;
   /**
    * Double type for num1, which is initialized to 0
    */
   private double num1 = 0;
   /**
    * Double type for num2, which is initialized to 0
    */
   private double num2 = 0;
   /**
    * String type for choose, which sets to null value
    */
   private String choose = null;
   /**
    * Border that creates a line around JPanel
    */
   private Border line;

  /**
   * Constructor for Calculator
   * Creates an instance of a calculator in a JPanel.
   * Adds ActionListener onto all of the buttons.
   */
   public Calculator(){
      JPanel jpContainer = new JPanel(new BorderLayout());              // Holds everything inside of the calculator
      jpCalc = new JPanel(new GridLayout(6, 4));                        // Holds all of the buttons
      jpText = new JPanel(new GridLayout(1, 1));                        // Holds the text field
      jtaProblem = new JTextArea();                                     // The text area that visualizes the calculations     
      jtaProblem.setBorder(line);
      jtaProblem.setPreferredSize(new Dimension(20, 25));
      jtaProblem.setLineWrap(true);
      jpText.add(jtaProblem);
      jtaProblem.setEditable(false);    
      jtaProblem.setFont(new Font("ARIAL", Font.PLAIN, 18));
      line = BorderFactory.createLineBorder(Color.black);               // Basic line used throughout to border it

      jbAdd = new JButton("+");                                         // Adding buttons
      jpCalc.add(jbAdd);
   
      jbSub = new JButton("-");
      jpCalc.add(jbSub);
   
      jbMult = new JButton("*");
      jpCalc.add(jbMult);
   
      jbDiv = new JButton("/");
      jpCalc.add(jbDiv);
   
      jbClear = new JButton("Clear");
      jpCalc.add(jbClear);
   
      jbBackspace = new JButton("DEL");
      jpCalc.add(jbBackspace);
   
      jbZero = new JButton("0");
      jpCalc.add(jbZero);
   
      jbOne = new JButton("1");
      jpCalc.add(jbOne);
   
      jbTwo = new JButton("2");
      jpCalc.add(jbTwo);
   
      jbThree = new JButton("3");
      jpCalc.add(jbThree);
   
      jbFour = new JButton("4");
      jpCalc.add(jbFour);
   
      jbFive = new JButton("5");
      jpCalc.add(jbFive);
   
      jbSix = new JButton("6");
      jpCalc.add(jbSix);
   
      jbSeven = new JButton("7");
      jpCalc.add(jbSeven);
   
      jbEight = new JButton("8");
      jpCalc.add(jbEight);
   
      jbNine = new JButton("9");
      jpCalc.add(jbNine);
   
      jbEqual = new JButton("=");
      jpCalc.add(jbEqual);
   
      jbDecimal = new JButton(".");
      jpCalc.add(jbDecimal);

      ButtonsListener bl = new ButtonsListener();                  
      jbAdd.addActionListener(bl);                                      // Adding button listeners to each button.
      jbSub.addActionListener(bl);
      jbMult.addActionListener(bl);
      jbDiv.addActionListener(bl);
      jbClear.addActionListener(bl);
      jbBackspace.addActionListener(bl);
      jbDecimal.addActionListener(bl);
      jbEqual.addActionListener(bl);
      jbZero.addActionListener(bl);
      jbOne.addActionListener(bl);
      jbTwo.addActionListener(bl);
      jbThree.addActionListener(bl);
      jbFour.addActionListener(bl);
      jbFive.addActionListener(bl);
      jbSix.addActionListener(bl);
      jbSeven.addActionListener(bl);
      jbEight.addActionListener(bl);
      jbNine.addActionListener(bl);
   
      jpContainer.add(jpText, BorderLayout.NORTH);                      // Adding things to the container  
      jpContainer.add(jpCalc, BorderLayout.SOUTH);
      add(jpContainer);                                                 // Adding the container to the JFrame
   }  
 
  /**
   * Finds which button has been pressed
   * Makes an action based on that button.
   * Everything before a sign (+, -, /, *) = first number
   * Everything after a sign (+, -, /, *) = second number  
   */
   class ButtonsListener implements ActionListener{
      public ButtonsListener(){
   }
      
      /**
       * Takes in first number and second number
       * Does calculation based on sign selected
       * Returns that number to jtaProblem
       */  
      public void actionPerformed(ActionEvent ae){
         String value;
         int startHere = 0;
      
         if(ae.getActionCommand().equals("+")){                         // Adds an addition sign, signals the "=" to add num1 and num2
            num1 = Double.parseDouble(jtaProblem.getText());
            jtaProblem.append("+");
            choose = "+";
         }
         else if(ae.getActionCommand().equals("-")){                    // Adds a subtraction sign, signals the "=" to subtract num1 and num2
            num1 = Double.parseDouble(jtaProblem.getText());
            jtaProblem.append("-");
            choose = "-";
         }
         else if(ae.getActionCommand().equals("*")){                    // Adds a multiplication sign, signals the "=" to multiply num1 and num2
            num1 = Double.parseDouble(jtaProblem.getText());
            jtaProblem.append("*");
            choose = "*";
         }
         else if(ae.getActionCommand().equals("/")){                    // Adds a division sign, signals the "=" to divide num1 and num2
            num1 = Double.parseDouble(jtaProblem.getText());
            jtaProblem.append("/");
            choose = "/";
         }
         else if(ae.getActionCommand().equals("Clear")){                // Clears the entire jtaProblem
            jtaProblem.setText("");
         }
         else if(ae.getActionCommand().equals("DEL")){                  // Deletes the last piece of text from jtaProblem
            jtaProblem.setText(jtaProblem.getText().substring(0, jtaProblem.getText().length() - 1));    
         }
         else if(ae.getActionCommand().equals("0")){                   // Adds a 0 to jtaProblem
            jtaProblem.append("0");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("1")){                   // Adds a 1 to jtaProblem
            jtaProblem.append("1");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("2")){                   // Adds a 2 to jtaProblem
            jtaProblem.append("2");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("3")){                   // Adds a 3 to jtaProblem
            jtaProblem.append("3");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("4")){                   // Adds a 4 to jtaProblem
            jtaProblem.append("4");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("5")){                   // Adds a 5 to jtaProblem
            jtaProblem.append("5");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("6")){                   // Adds a 6 to jtaProblem
            jtaProblem.append("6");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("7")){                   // Adds a 7 to jtaProblem
            jtaProblem.append("7");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("8")){                   // Adds a 8 to jtaProblem
            jtaProblem.append("8");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals("9")){                   // Adds a 9 to jtaProblem
            jtaProblem.append("9");
            value = jtaProblem.getText();
         }
         else if(ae.getActionCommand().equals(".")){                   // Adds a . to jtaProblem, signals the number is a decimal number
            jtaProblem.append(".");
            value = jtaProblem.getText();
         }

         else if(ae.getActionCommand().equals("=")){                   // Does the calculations based on the sign selected
            if(choose.equals("+")){                             
               startHere = jtaProblem.getText().indexOf("+");          // Adds num1 and num2, appends result to jtaProblem
               num2 = Double.parseDouble(jtaProblem.getText().substring(startHere+1, jtaProblem.getText().length()));
               result = (double)(num1 + num2);
               jtaProblem.setText(Double.toString(result));
            }
            else if(choose.equals("-")){
               startHere = jtaProblem.getText().indexOf("-");          // Subtracts num1 and num2, appends result to jtaProblem
               num2 = Double.parseDouble(jtaProblem.getText().substring(startHere+1, jtaProblem.getText().length()));
               result = (double)(num1 - num2);
               jtaProblem.setText(Double.toString(result));
            }
            else if(choose.equals("*")){
               startHere = jtaProblem.getText().indexOf("*");          // Multiplies num1 and num2, appends result to jtaProblem
               num2 = Double.parseDouble(jtaProblem.getText().substring(startHere+1, jtaProblem.getText().length()));
               result = (double)(num1 * num2);
               jtaProblem.setText(Double.toString(result));
            }
            else if(choose.equals("/")){
               startHere = jtaProblem.getText().indexOf("/");          // Divides num1 and num2, appends result to jtaProblem
               num2 = Double.parseDouble(jtaProblem.getText().substring(startHere+1, jtaProblem.getText().length()));
               result = (double)(num1 / num2);
               jtaProblem.setText(Double.toString(result));
            }
            choose = "=";
         }
      }//end actionPerformed
   }//end ButtonsListener
}//end Calculator class

