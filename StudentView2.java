import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

/**
 * Create the StudentView for the Teacher-Student Chat.
 * The view contains a Student chat area, teacher instructional
 * area, a drawing board, and a calculator.
 * @author Dustin and DACK Team
 */

public class StudentView2 implements Runnable{

   /**
    * is a JPanel holds all the elements in the current JFrame
    */
   private JPanel jpContainer;
 
   /**
    * is a JPanel that holds the instructinonsT scrollpane
    */
   private JPanel jpLowerRight;
 
   /**
    *  is a JTextArea that allows the student to see all the comments sent by the teacher, the student cannot edit this area
    */
   private JTextArea jtaInstructionsT;
 
   /**
    * is a JScrollPane that holds the jtaInstructionsT
    */
   private JScrollPane instructionsT;
 
   /**
    * is a String that holds the IP address neccessary to connect to the teacher's server
    */
   private String actualIP;
 
   /**
    * is a Chat object that contains a JTextArea, JTextField, and JButton that allows the students to communicate
    * with the teacher and other students
    */
   private Chat chat;
 
   /**
    * is a Drawing object that allows the user to view an image (sent from the teacher)
    */
   private Drawing board;
 
   /**
    * is a calculator object that allows the student to perform basic math calculations
    */
   private Calculator calc;
   /**
    * is a String that holds the name of the student
   */   
   private String username;
 
   /**
    * is a JMenu that allows the student to find out more information about the application
    */
   private JMenu jmHelp;
 
   /**
    * is a Jmenu that allows the student to have an additional way to exit the application
    */
   private JMenu jmFile;
 
  /**
    * is a JMenuItem that allows the student to find directions on how to use the application
    */
   private JMenuItem jmiIntruc;
 
   /**
    * is a JMenuItem that allows the student to exit the application
    */
   private JMenuItem jmiExit;
 
 
   /**
   * StudentView2 creates a thread that will create the JFrame for the studentView
   * @param ipAddress creates an ipaddress for the student to connect to
   * @param username gives the student a username
   */
   public StudentView2(String ipAddress, String username){
      actualIP = ipAddress;
      new Thread(this).start();
      this.username = username;
   }
 
   /**
    * Run method creates the JFrame and adds all objects to the studentView
    */
   public void run(){
 
      JFrame jf = new JFrame("Student View");                      // Create a JFrame
      System.out.println("In the RUN for STUDENT");                // For Testing
   
      jpContainer = new JPanel(new GridBagLayout());               // Create a gridbag layout for the student view
      GridBagConstraints gbc = new GridBagConstraints();
   
      chat = new Chat(actualIP, username);                         // Create a  chat object with an IP address and username

      jtaInstructionsT = new JTextArea();                          // Create an JTextArea for the instructions to be seen by the students

     
      StudentInstructionsHandler handler = new StudentInstructionsHandler(jtaInstructionsT, actualIP);
      new Thread(handler).start();                                // Send the jtaInstructionsT and IP to the StudentInstructionHandler
                                                                  // Create a thread for the view
 
      board = new Drawing(actualIP, "Student");                    // Create object with an IP and String
   
      gbc.gridx = 0;                                               // UPPER LEFT
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 443;                                             // set height of upperleft to 443
      gbc.ipadx = 105;                                             // set width of upperleft to 105
      jpContainer.add(chat, gbc);
   
      JMenuBar jmbMenu = new JMenuBar();                           // Create a JMenu Bar
     
      jmHelp = new JMenu("Help");                                  // Create JMenu for Help
      jmiIntruc = new JMenuItem("Instructions");                   // Crete JMenuItem for instructions
     
      jmFile = new JMenu("File");                                  // Create a JMenu for File
      jmiExit = new JMenuItem("Exit");                             // Create a JMenuItem for
     
      jmHelp.add(jmiIntruc);                                       // Add JMenuItems to appropriate Menu
      jmFile.add( jmiExit );
     
      jmbMenu.add(jmFile);                                         // Add Menu to bar
      jmbMenu.add(jmHelp);
     
      jf.setJMenuBar(jmbMenu);                                     // Add bar to jframe
   
     jmiIntruc.addActionListener(                                 // Create Anonymous inner class for jmiInstruc
            new ActionListener(){                                  // Displays instructions for student
               public void actionPerformed(ActionEvent ae){
                  JOptionPane.showMessageDialog(null, "How to Chat: Type message underneath the chat, press Send!!" + "\n\n" +
                                                   "How to use the calculator: Press the number buttons that you wish to use, press" + "\n" +
                                                   " + , - , / , or *, hit enter. Clear when you want to try again!" + "\n\n" +
                                                   "How to use the drawing board: Press and hold on the drawing board to draw.");
               }
            });
           
      jmiExit.addActionListener(                                  // Add an action listener to jmiExit to close application
         new ActionListener(){
            public void actionPerformed(ActionEvent ae){
               System.exit(0);
            }
         });
 
      calc = new Calculator();                            // LOWER LEFT
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 197;                                    // set height of LowerLeft to 147
      gbc.ipadx = 105;                                    // set width of LowerLeft to 105
      jpContainer.add( calc , gbc);
   
      board.setBackground(Color.WHITE);                  // UPPER RIGHT
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 443;                                   // set height of Upperright to 443
      gbc.ipadx = 695;                                   // set the width UpperRight to 695
      jpContainer.add( board , gbc);
   
      jpLowerRight = new JPanel(new GridLayout(0,1));    //LOWER RIGHT
      jtaInstructionsT.setLineWrap( true );
      jtaInstructionsT.setWrapStyleWord( true );
      //jtaInstructionsT.append("Here is where the teacher will provide answers to your questions. \n");
      jtaInstructionsT.setEditable( false );             // So students can't type in the field
      instructionsT = new JScrollPane(jtaInstructionsT); // Add blank text area to student view
   
      jpLowerRight.add( instructionsT );
   
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 197;                                   // set height of LowerRight to 147
      gbc.ipadx = 695;                                    // set width of Lowerright to 695
      jpContainer.add( jpLowerRight, gbc);
 
      jf.add( jpContainer );
      jf.setSize(800, 650);
      jf.setResizable( false );
      jf.setVisible(true);
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      jf.setLocationRelativeTo( null );
   }
 
   /**
    * StudentInstructionsHandler Class is a class that creates the communication between the teacher and student
    * through the jtInstructionsT.
    */
 
   class StudentInstructionsHandler implements Runnable, StudentTeacherChatConstants{
      /**
       * Creates a local instance of the jtaInstructions view from the studentview
       */
      private JTextArea jtaInstructions;
      /**
       * Creates an instructions socket to communicate with the chatServer
       */
      private Socket instructionsCS;
     
      /**
       * Create a buffered reader for the instructions panel
       */
      private BufferedReader instructionsBR;
     
      /**
       * Create a printwriter for the instructions panel
       */
      private PrintWriter instructionsPW;
    
      /**
       * Create a local variable that has the IP address passed from the studentView
       */
      private String ipAddress;
 
   /** SendHandler Constructor
   *  Sets the jta and jtf to the same from the GUI
   *  sets up Socket, BufferedReader, PrintWriter
   *  Sends messages to the Server
   *  Appends what is sent back to the chatroom
   *  @param jtaInstructionsT is the jTextArea from the student view
   *  @param ipAddress is the  address
   */
      public StudentInstructionsHandler(JTextArea jtaInstructionsT, String ipAddress){          
         jtaInstructions = jtaInstructionsT;
         this.ipAddress = ipAddress;
         System.out.println("Pre instructions");
         try{
            instructionsCS = new Socket(ipAddress, PORT_INSTRUCTIONS);
            instructionsBR = new BufferedReader( new InputStreamReader( instructionsCS.getInputStream()));
            instructionsPW = new PrintWriter( new OutputStreamWriter( instructionsCS.getOutputStream()));
         }
         catch(UnknownHostException uhe){
            System.out.println("UnknownHostException | StudentInstructionsHandler");
         }// end of the catch
         catch(IOException ie){
            System.out.println("IOException | StudentInstructionsHandler");
         }//end of IO catch
      }//end of constructor
      
      /**
       * Takes in instructions messages from the server
       * If the message is "Clear", it clears the entire
       * instructions text area.
       */
      public void run(){
         System.out.println("Instructions has began to run.");
         instructionsPW.println("Updating Instructions!");
         instructionsPW.flush();
         try{
            while(true){
               String line = instructionsBR.readLine();
               if(line.equals("Clear")){
                  jtaInstructions.setText(null);
               }
               else{
                  jtaInstructions.append(line + "\n");
               }
            }     
         }
         catch(IOException ie){
            System.out.println("IOException | TeacherInstructionsHandler run()");
         }  
      } // end run
   }//end of SendHandler
} // end StudentView2

