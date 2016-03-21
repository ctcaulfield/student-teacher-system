import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The TCSplash is a "splash screen" that
 * appears upon opening the system. This
 * screen asks if you are a student or a
 * teacher. If you are a teacher, upon
 * connection it will start a TeacherView2
 * object. If you are a student, you must
 * also enter in an IP address. Upon connection,
 * it will create a StudentView2 object.
 * @author Dustin and DACK team
 */
public class TCSplash extends JFrame implements ActionListener{
   /**
    * The JPanel that holds the Messages panel.
    */
   private JPanel jpContainer;
   
    /**
    * The JPanel that holds all of the panels other than jpContainer.
    */
   private JPanel jpMessages;
   
    /**
    * Holds the "Welcome" label on the splash screen.
    */
   private JPanel jpWelcome;
   
    /**
    * Used as a space in the splash screen.
    */
   private JPanel jpSpace;
   
    /**
    * JPanel that holds the JTextField that asks for a username.
    */
   private JPanel jpWho;
   
   /**
    * A label that holds a welcome message to users.
    */
   private JLabel jlWelcome;
   
   /**
    * A label that asks for a username.
    */
   private JLabel jlWho;
   
   /**
    * A text field that users enter their username into.
    */
   private JTextField jtfUser;
   
   /**
    * A text field that students enter an IP into.
    */
   private JTextField jtfIP;
   
   /**
    * The radio button that indicates to create a Student object.
    */
   private JRadioButton jrbStudent;
   
    /**
    * The radio button that indicates to create a Teacher object.
    */
   private JRadioButton jrbTeacher;
   
   /**
    * The button that creates the indicated object, closes the splash screen.
    */
   private JButton jbGo;
   
   /**
    * The button group that holds the radio buttons jrbTeacher, jrbStudent.
    */
   private ButtonGroup bgUsers;
   
   /**
    * The Student View client object that is created. See the Student View class for more info.
    */
   private StudentView2 student;
   
   /**
    * The Teacher View client object that is created. See the Teacher View class for more info.
    */
   private TeacherView2 teacher;
   
   /**
    * Arguments are not actually used in this class.
    */
   private String[] theArgs;
   
   /**
    * The font that is used throughout the splash screen.
    */
   Font font = new Font("Helvetica", Font.BOLD, 12);
   
   /**
    * On start, calls the TCSplash constructor. Creates
    * a TCSPlash JFrame. It then sets it to visible.
    * @param args Unused
    */
   public static void main(String [] args){
      TCSplash jfSplash = new TCSplash();
         jfSplash.setVisible( true );
         jfSplash.setSize(300, 300);
         jfSplash.setLocationRelativeTo( null );
         jfSplash.setDefaultCloseOperation( EXIT_ON_CLOSE );
         jfSplash.pack();    
   }// end of main

   /**
    * Creates the required JPanels for the TCSplash.
    * It then sets up the splash screen, and adds
    * action listeners onto the buttons.
    */
   public TCSplash(){    
      jpContainer = new JPanel();                              //Create new JPanel to hold many JPanels that will wait to collect information
      add( jpContainer );
     
      jpMessages = new JPanel(new GridLayout(0, 1, 0, 10));    //Create a JPanel to hold many rows of information
      jpContainer.add( jpMessages, BorderLayout.CENTER );
    
      jpSpace = new JPanel();                                  //Adds a space in the grid
      jpMessages.add( jpSpace );
     
      jpWelcome = new JPanel();
             
      jlWelcome = new JLabel("Welcome!");                      //Add JLabels
      jlWelcome.setFont( font );
      jpWelcome.add( jlWelcome );
        
      jpMessages.add( jpWelcome );
     
      jpWho = new JPanel();
       
      jlWho = new JLabel("Who are you?");                      // Add Label "Who are you"
      jlWho.setFont( font );
      jpWho.add( jlWho );
        
      jpMessages.add( jpWho );
        
      jtfUser = new JTextField("enter your name");             // Add the TextField
      jtfIP = new JTextField("Student: Enter IP address");
     
      jpMessages.add( jtfUser );
      jpMessages.add( jtfIP );
      
      jrbTeacher = new JRadioButton("Teacher");                // Create Radio Buttons for Student and Teacher
      jrbStudent = new JRadioButton("Student");
     
      bgUsers = new ButtonGroup();
      bgUsers.add( jrbTeacher );
      bgUsers.add( jrbStudent );
     
      jrbTeacher.addActionListener(this);
      jrbStudent.addActionListener(this);
     
      jpMessages.add( jrbTeacher );
      jpMessages.add( jrbStudent );
     
      jbGo = new JButton("Go!");
     
      jpMessages.add( jbGo );
     
      jbGo.addActionListener(this);

   }// end of TCSplash Constructor
   
   
   /**
    * The actionPerformed method first checks to
    * see what invoked the event. If it was the
    * "Go" button, it checks to see which radio
    * button is selected. If the Teacher radio
    * button is selected, it creates a Teacher
    * client. If the student radio button is
    * selected, it creates a Student client.
    * After the object is created, the TCSplash
    * is then closed.
    */
   public void actionPerformed(ActionEvent ae){      
      if(ae.getActionCommand() == "Go!"){                    // Wait for someone to push Go!
         System.out.println("You pushed the go button");
             
         if(jrbTeacher.isSelected()){                        // Check to see which button was pressed
            System.out.println("Open Teacher View");
            teacher = new TeacherView2(jtfUser.getText());
            this.dispose();
         }
         if(jrbStudent.isSelected()){
            System.out.println("Open Student View");            

            student = new StudentView2(jtfIP.getText(), jtfUser.getText());
            this.dispose();     
         }        
      }
   }// end of actionPerformed
}// end of TCSplash
