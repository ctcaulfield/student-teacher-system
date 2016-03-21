import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

/**
* Teacher TeacherView2
* @author Dustin and DACK team
* @version 2.0
*/

public class TeacherView2 implements Runnable{
  /**
   * is a JPanel holds all the elements in the current JFrame
   */
   private JPanel jpContainer;
 
   /**
   * is a JPanel with a (0,1) grid layout that holds the instructinonsT scrollpane, jtfTeacher, jbSend and jbclear
   */
   private JPanel jpLowerRight;
 
   /**
    * is a JPanel with a flowlayout that holds the jtfTeacher, jbSend, and jbClear
    */
   private JPanel jpLowerRight2;
 
   /**
    * is a JPanel with a borderlayout that holds the entire teacher messaging area
    */
   private JPanel jpLowerRightContainer;
   /**
   *  is a JTextArea where the messages the teacher sends can be viewed
   */
   private JTextArea jtaInstructionsT;
   /**
   * is a JScrollPane that holds the jtaInstructionsT
   */
   private JScrollPane instructionsT;
   /**
   * JTextField that allows the teacher to send a
   * message in instruction area
   */
   private JTextField jtfTeacher;
   /**
   * JButton that allows the teacher to send the
   * message to all the clients (students) through the jtaInstructions text area
   */
   private JButton jbSend;
   /**
   * JButton that allows the user to clear the
   * instruction text area
   */
   private JButton jbClear;
   /**
   * is a String that holds the IP address
   */
   private String ipAddress;
   /**
   * is a String that holds the name of the user
   */
   private String username;
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

   /**
   * is a JMenu item that allows the user to find out more information about the application
   */
   private JMenu jmHelp;
   /**
    * is a Jmenu that allows the student to have an additional way to exit the application
   */
   private JMenu jmFile;
   /**
  * is a JMenuItem that allows the user to find directions on how to use the application
   */
   private JMenuItem jmiIntruc;
   /**
    * is a JMenuItem that allows the student to exit the application
    */
   private JMenuItem jmiExit;

 
   /**
   * TeacherView2 creates a thread that will create the JFrame for the teacherView
   * @param username is a string that represents who the teacher is
   */
   public TeacherView2(String username){  
      ChatServer serverStart = new ChatServer();              // start the server
      serverStart.start();
      new Thread(this).start();
      System.out.println("[Teacher] Server Started ");
      this.username = username;
   }
 
   /**
   * Run method creates a teacher view by creating all necessary objects
   */
   public void run(){
      JFrame jf = new JFrame("Teacher View");                  // Createa a JFrame
 
      jpContainer = new JPanel(new GridBagLayout());           // Create a gridbaglayout container
      GridBagConstraints gbc = new GridBagConstraints();
   
      chat = new Chat("localhost", username);             // Create a Chat Object
      jtfTeacher = new JTextField("Enter message here", 20);   // Create a JTextField for the teacher
      jtaInstructionsT = new JTextArea(10, 10);                // Create a JTextArea for the Teacher
 
      TeacherInstructionsHandler handler = new TeacherInstructionsHandler(jtaInstructionsT, jtfTeacher, "localhost");
      new Thread(handler).start();                             // Send jtaInstructions, jtfTeacher, and ip to
                                                               // TeacherInstructionsHandler and starta a thread
   
      Drawing board = new Drawing("localhost", "Teacher");     // Create a board object
     
      System.out.println("---- [Teacher] Made the drawing");   // for testing
     
      gbc.gridx = 0;                               // UPPER LEFT
      gbc.gridy = 0;
      gbc.ipady = 450;                             // set Upperleft height to 450
      gbc.ipadx = 200;                             // set UpperLeft width to 200
      gbc.fill = GridBagConstraints.BOTH;
      jpContainer.add(chat, gbc);                  // add chat to the gridbaglayout
 
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
 
      Calculator calc = new Calculator();     // LOWER LEFT
      gbc.gridx = 0;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 200;                        // Set lowerleft height to 200
      gbc.ipadx = 200;                        // set lowerleft width to 200
      jpContainer.add( calc , gbc);           // add calc to gridbaglayout
 
                                             // UPPER RIGHT
      board.setBackground(Color.WHITE);      // set the background of the whiteboard to white
      gbc.gridx = 1;
      gbc.gridy = 0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 450;                       // set upperright height to 450
      gbc.ipadx = 650;                       // set upperright width to 600
      jpContainer.add( board , gbc);
 
      jpLowerRightContainer = new JPanel(new BorderLayout());     //LOWER RIGHT
      jpLowerRight = new JPanel(new GridLayout(0,1));
      jtaInstructionsT.setLineWrap( true );
      jtaInstructionsT.setWrapStyleWord( true );
      jtaInstructionsT.append("Here is where your message will be sent and be viewable by you and your students. \n");
      jtaInstructionsT.setEditable( false );                   // So students can't type in the field
      instructionsT = new JScrollPane(jtaInstructionsT);       // Add blank text area to student view
 
      jpLowerRight.add( instructionsT );
      jpLowerRight2 = new JPanel(new FlowLayout());
 
      jbSend = new JButton("Send");                            // Create buttons for the teacher to use
      jbClear = new JButton("Clear");
 
      jpLowerRight2.add( jtfTeacher );
      jpLowerRight2.add( jbSend );
      jpLowerRight2.add( jbClear );
 
      jpLowerRight.add( jpLowerRight2 );
   
      jpLowerRightContainer.add(jpLowerRight, BorderLayout.CENTER);  // add all elements to a container
      
      gbc.gridx = 1;
      gbc.gridy = 1;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.ipady = 200;                                   // set lowerRight heigh to 200
      gbc.ipadx = 650;                                   // set lowerRight width to 600
      jpContainer.add( jpLowerRightContainer, gbc);
 
      jbSend.addActionListener(handler);                 // Create ActionListeners for the buttons
      jbClear.addActionListener(handler);
 
      jf.add( jpContainer );                             // Add the container to the frame
     
      jf.setSize(800, 650);
      jf.setResizable( false );
      jf.setVisible(true);
      jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
 
   /**
    * TeacherInstructionsHandler Class is a class that creates the communication between the teacher and student
    * through the jtInstructionsT.
    */
   class TeacherInstructionsHandler implements ActionListener, Runnable, StudentTeacherChatConstants{
       /**
       * Creates a local instance of the jtaInstructions view from the studentview
       */
      private JTextArea jtaInstructions;
      /**
       * Creates a local instance of jtfTeacher to pull text from the field and send it to the
       * text area
       */
      private JTextField jtfTeacher;
     
      /**
       * Creates a socket for the instructions
       */
      private Socket instructionsCS;
     
      /**
       * Creates a buffered reader for the instructions
       */
      private BufferedReader instructionsBR;
      /**
       * Create a print writer for the instructions
       */
      private PrintWriter instructionsPW;
      /**
       * Get the ipaddress from the teacherview
       */
      private String ipAddress;
 
   /** SendHandler Constructor
   *  Sets the jta and jtf to the same from the GUI
   *  sets up Socket, BufferedReader, PrintWriter
   *  Sends messages to the Server
   *  Appends what is sent back to the chatroom
   * @param jtaInstructionsT - The text area that holds the instructions
   * @param jtfTeacher - The text field that the teacher writes instructions into
   * @param ipAddress - The IP address
   */
      public TeacherInstructionsHandler(JTextArea jtaInstructionsT, JTextField jtfTeacher, String ipAddress){
   
         jtaInstructions = jtaInstructionsT;
         this.jtfTeacher = jtfTeacher;
         this.ipAddress = ipAddress;
     
         try{
            instructionsCS = new Socket(ipAddress, PORT_INSTRUCTIONS);
            instructionsBR = new BufferedReader( new InputStreamReader( instructionsCS.getInputStream()));
            instructionsPW = new PrintWriter( new OutputStreamWriter( instructionsCS.getOutputStream()));
         }
         catch(UnknownHostException uhe){
            System.out.println("UnknownHostException | TeacherInstructionsHandler");
         }
         catch(IOException ie){
            System.out.println("IOException | TeacherInstructionsHandler");
         }
      }//end of constructor
 
      public void actionPerformed(ActionEvent ae){
         if(ae.getSource() == jbSend){
            String message = jtfTeacher.getText(); //Sending jtaMessage to server
            instructionsPW.println(message);
            instructionsPW.flush();
         }
         else{
            jtaInstructions.setText(null);
            String message = "Clear";           //Sending jtaMessage to server
            instructionsPW.println(message);
            instructionsPW.flush();
         }
     
      }//end of ActionPerformed
 
      /**
       * run method that waits for instructiosn to be sent to the text area
       */
      public void run(){
         System.out.println("[Teacher] Instructions has began to run.");
         try{
            while(true){
               String line = instructionsBR.readLine();
               
               if(line.equals("Clear")){
                  jtaInstructions.setText(null);
               }
               else if(line.equals("Updating Instructions!")){
                  String update = jtaInstructions.getText();
                  instructionsPW.println(update);
                  instructionsPW.flush();
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
}//end of TeacherView2

