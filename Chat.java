import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

/**
* Create a Chat system for Teacher-Student Chat
* Allows communication between the Teacher and the
* Students connected to the server.
* @author Kristen and DACK team
* @version 2.0
*/
public class Chat extends JPanel{
   /**
    * The text area that holds all of the messages.
    */
   private JTextArea jtaChat;
   /**
    * The button that sends messages to the server.
    */
   private JButton jbSend;
   /**
    * The text field that users enter their messages into.
    */
   private JTextField jtfMessage;
   /**
    * The panel that holds the main chat.
    */
   private JPanel jpLeft;
   /**
    * The panel that holds jtfMessage and jbSend.
    */
   private JPanel jpBottomLeft;
   /**
    * The panel that holds the calculator.
    */
   private JPanel jpCalc;
   /**
    * The string that is used for the IP for students
    */
   private String ip;

   /**
    * Chat constructor
    * Sets the IP to localhost.
    * Creates the JTextArea for chat appending.
    * Creates the JTextField and Send button for
    * sending messages.
    * Adds the send handler to the send button
    * @param ipAddress - the ipaddress of the user
    * @param username - the username of the user
    */
   public Chat(String ipAddress, String username){
      String actualIP = ipAddress;                               // IP address
      String actualUsername = username;                          // Username
      
      jpLeft = new JPanel(new GridLayout(0,1));
      jtaChat = new JTextArea(20,10);
         jtaChat.setLineWrap( true );
         jtaChat.setWrapStyleWord( true );
         jtaChat.setEditable( false );
      JScrollPane mainChat = new JScrollPane(jtaChat);
      jpLeft.add(mainChat);
      
      jpBottomLeft = new JPanel();
      jtfMessage = new JTextField("Send a Message");
         jtfMessage.requestFocus();
         jtfMessage.setHorizontalAlignment(JTextField.LEFT);
      jbSend = new JButton("Send");
         jbSend.setHorizontalAlignment(JButton.LEFT);
      
      jpBottomLeft.add(jtfMessage);
      jpBottomLeft.add(jbSend);
            
      jpLeft.add(jpBottomLeft, BorderLayout.SOUTH);
    
      ChatSendHandler sh = new ChatSendHandler(jtaChat, jtfMessage, ipAddress, actualUsername);   //add items to SendHandler
      new Thread(sh).start();
      jbSend.addActionListener(sh);
      this.add(jpLeft);
   } // end constructor
 
   /**
    * SendHandler that handles the chatroom messages.
    * This handler is what recognizes the button pushes
    * and actually connects to the server.
    */
class ChatSendHandler implements ActionListener, Runnable, StudentTeacherChatConstants{
   /**
    * Mimic of the chat's JTextArea.
    */
   private JTextArea jtaMain;
   /**
    * Mimic of the chat's JTextField.
    */
   private JTextField jtfMessage;
   /**
    * The socket that will connect the chat to the server.
    */
   private Socket chatCS;
   /**
    * The buffered reader that will read in messages from the server.
    */
   private BufferedReader chatBR;
   /**
    * The print writer that will push out messages to the server.
    */
   private PrintWriter chatPW;
   /**
    * The string that will be taken as the IP address.
    */
   private String actualIP;
   /**
    * The string that will be sent to the server as a username.
    */
   private String actualUsername;
   
   
   /** SendHandler Constructor
    *  Sets the jta and jtf to the same from the GUI
    *  sets up Socket, BufferedReader, PrintWriter
    *  Sends messages to the Server
    *  Appends what is sent back to the chatroom
    *  @param jtaMain - the main text area that holds the chat
    *@param jtfMessage - The text area that people type messages into
    *@param actualIP - The IP address of the teacher.
    *@param actualUsername - the username of the user.
    */
   public ChatSendHandler(JTextArea jtaMain, JTextField jtfMessage, String actualIP, String actualUsername){
      this.jtaMain = jtaMain;                                                          // get the text area
      this.jtfMessage = jtfMessage;                                                    // get the text field
      this.actualIP = actualIP;
      this.actualUsername = actualUsername;
      try{
         chatCS = new Socket(actualIP, PORT_CHAT);                                      // create a new socket specifically for the chat system
         chatBR = new BufferedReader( new InputStreamReader( chatCS.getInputStream())); // reader for the chat
         chatPW = new PrintWriter( new OutputStreamWriter( chatCS.getOutputStream()));  // writer for the chat
      }
      catch(UnknownHostException uhe){
         jtaMain.append("There is no server available");
         System.out.println("UnknownHostException | ChatSendHandler");
      }// end of the catch
      catch(IOException ie){
         System.out.println("IOException | ChatSendHandler");
      }//end of IO catch
   }//end of constructor
   
   /**
    * The jbSend is the only event that the chat handles.
    * So, on the push of jbSend, it will take in any message
    * that is current in the jtfMessage. It then sends it to the
    * server through println.
    */
   public void actionPerformed(ActionEvent ae){                                         // when you press the SEND BUTTON
      String message = jtfMessage.getText();                                            // get the text from the text field
      chatPW.println(actualUsername + ": " + message);                                  // print that to the server
      chatPW.flush();                                                                   // flush the message
   }//end of ActionPerformed
   
   /**
    * The run method is constantly accepting in new messages that
    * are sent in from the server. Once it accepts the message
    * through readLine(), it wil append that message into the
    * jtaMain.
    */
   public void run(){
      System.out.println("[Chat] The chat has began to run.");      
      try{
         while(true){
           String line = chatBR.readLine();                                             // constantly reading in new lines from the server to append to the chat
           jtaMain.append(line + "\n");
         }        
      }
      catch(IOException ie){
         jtaMain.append("The server has been disconnected - you can no longer chat.");
         System.out.println("IOException | ChatSendHandler");
      }   
   } // end run
 }//end of SendHandler
} // end Chatroom
