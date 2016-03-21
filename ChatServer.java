import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.geom.Line2D;

/**
* Creates a Server for Teacher-Student Chat.
* Handles clients connecting to it; there is
* no limit to how many clients may connect.
* Inner classes handle different types of clients
* such as: Chat, Instructions, and Drawing.
* @author Kristen and DACK team
* @version 2.0
*/

public class ChatServer extends Thread implements StudentTeacherChatConstants {
   /**
    * The vector that holds all of the PrintWriters for the chat clients
    */
   private Vector<PrintWriter> chatPWList;
   /**
    * The vector that holds all of the ObjectOutputStreams for the drawing clients
    */
   private Vector<ObjectOutputStream> drawOOList;
   /**
    * The vector that holds all of the ObjectInputStreams for the drawig clients
    */
   private Vector<ObjectInputStream> drawOIList;
   /**
    * The vector that holds all of the PrintWriters for the instructions clients
    */
   private Vector<PrintWriter> instrucPWList;
   /**
    * The serversocket that connects to drawing clients
    */
   private ServerSocket ss;
   /**
    * The serversocket that connects to chat clients
    */
   private ServerSocket ss2;
   /**
    * The serversocket that connects to instructions clients
    */
   private ServerSocket ss3;
   
   
   /** ChatServer Constructor
    * Creates vectors to hold data such as:
    * Chat printers, Drawing obeject writers,
    * Drawing object inputs, Instructions print
    * writers, and Username lists.
    * This constructor also starts the thread that
    * begins to accept clients.
    */
   public ChatServer(){
      chatPWList = new Vector<PrintWriter>();                            // Vector holding all of the CHAT printers
      drawOOList = new Vector<ObjectOutputStream>();                     // Vector holding all of the DRAWING BOARD OBJECT OUTPUT STREAMS
      drawOIList = new Vector<ObjectInputStream>();                      // Vector holding all of the DRAWING BOARD OBJECT INPUT STREAMS
      instrucPWList = new Vector<PrintWriter>();                         // Vector holding all of the INSTRUCTIONS printers
   }// end MyServer constructor
 
   /**
    * The run() method creates sockets for all
    * clients that connect. After they connect,
    * the sever will start a thread that handles
    * that specific client (chat, instructions,
    * or drawing board).
    */
   public void run(){
      try{
         ss = new ServerSocket(PORT_DRAWING);                            // Socket for DRAWING BOARD
         ss2 = new ServerSocket(PORT_CHAT);                              // Socket for CHAT
         ss3 = new ServerSocket(PORT_INSTRUCTIONS);                      // Socket for INSTRUCTIONS
         while(true){
            System.out.println("Waiting for Client...");
            Socket cs = ss.accept();                                     // Accepts the clients
            System.out.println("Found Drawing");
            Socket cs2 = ss2.accept();
            System.out.println("Found Chat");
            Socket cs3 = ss3.accept();
            System.out.println("Found Instructions");
         
            ChatThreadedServer cts = new ChatThreadedServer(cs2);        // Starts the threads after the clients connect
            cts.start();
            System.out.println("Started the CHAT Thread -- Server");
         
            DrawingThreadedServer dts = new DrawingThreadedServer(cs);
            dts.start();
            System.out.println("Started the DRAWING Thread -- Server");
               
            InstructionsThreadedServer its = new InstructionsThreadedServer(cs, cs2, cs3);
            its.start();
            System.out.println("Started the INSTRUCTIONS Thread -- Server");
         }           
      } // end loop
      catch(IOException ie){
         ie.printStackTrace();
      }
   }
   
   /**
    * ChatThreadedServer handles
    * the CHAT clients. It takes in
    * Messages from the chat, and sends
    * them back out to all of the currently
    * connected users.
    */
   public class ChatThreadedServer extends Thread{
      /**
       * The same socket that contains the Chat Client connection
       */
      private Socket chatCS;
      /**
       * The PrinterWriter that prints only to the Chat clients
       */
      private PrintWriter chatPW;
      /**
       * The BufferedReader that reads only from the Chat client's messages
       */
      private BufferedReader chatBR;
      
      /** ChatThreadedServer constructor
       * Sets the chatCS socket.
       * @param chatCS - the socket for the chat connection.
       */
      public ChatThreadedServer(Socket chatCS){
         this.chatCS = chatCS;
      }// end constructor
      
      
      /** Creates print readers and writers
       *  for the chat connection.
       *  Sends the welcome message upon connection
       *  to the chats.
       *  Takes in the client msgs as they are sent
       *  to the server, and sends back msgs to everyone.
       */
      public void run(){    
         try{
            chatPW = new PrintWriter ( new OutputStreamWriter( chatCS.getOutputStream()));     // Creates the print writer for this chat client
            chatPWList.add(chatPW);                                                            // add into the chat PW list
            chatPW.println("Welcome \n");                                                      // Sends welcome message to the chat client
            chatPW.flush();            
            chatBR = new BufferedReader( new InputStreamReader( chatCS.getInputStream()));     // Creates the buffered reader for this chat client
         
            while(true){
               System.out.println("(Chat) Waiting for msg...");                                // Constantly waiting for a new message
               String clientMsg = chatBR.readLine();                                           // Reads in the new message
               for(int i=0; i<chatPWList.size(); i++){                                         // Gets all of the printers for ALL chat clients, sends that message to them.
                  chatPWList.get(i).println(clientMsg);
                  chatPWList.get(i).flush();
               }
            }   
         }
         catch(IOException ie){
            for(int i=0; i<chatPWList.size(); i++){
               chatPWList.get(i).println("A client has disconnected");
               chatPWList.get(i).flush();
            }
            System.out.println("IOException | run");     
         }          
      }// end of run method
   }//end of ThreadedServer
   
   
 /**
    * InstructionsThreadedServer handles
    * the INSTRUCTIONS clients. It takes in
    * Messages from the instructions, written by
    * ONLY the teacher, and sends them back out to
    * all of the currently connected users.
    */
   class InstructionsThreadedServer extends Thread{
      /**
       * The same socket that connects the Instructions client to the server.
       */
      private Socket instrucCS;
      /**
       * The same socket that connects the Chat to the server
       */
      private Socket chatCS;
      /**
       * The same socket that connects the Drawing to the server.
       */
      private Socket drawingCS;
      /**
       * The PrintWriter that prints only to the instructions clients.
       */
      private PrintWriter instrucPW;
      /**
       * The BufferedReader that reads only the messages from the Teacher instruction's client.
       */
      private BufferedReader instrucBR;
   
   /** ThreadedServer constructor
    * Sets the instrucCS socket.
    * @param instrucCS - The socket for the instructions.
    * @param drawingCS - the socket for the drawing, used by instructions.
    * @param chatCS - the socket for the chat, used by the instructions.
    */
      public InstructionsThreadedServer(Socket drawingCS, Socket chatCS, Socket instrucCS){
         this.instrucCS = instrucCS;
         this.chatCS = chatCS;
         this.drawingCS = drawingCS;
      }// end constructor
   
   
   /** Creates print readers and writers
       *  for the instructions connection.
       *  Takes in the teacher's instructions msgs
       *  as they are sent over to the server, and
       *  sends back the instructions to everyone.
       */
      public void run(){    
         try{  
            instrucPW = new PrintWriter( new OutputStreamWriter( instrucCS.getOutputStream()));            // Printer Writer for this instructions client
            instrucPWList.add(instrucPW);                                                                  // Adds the print writer to the instructions PW list
            instrucBR = new BufferedReader( new InputStreamReader( instrucCS.getInputStream()));           // Buffered Reader for this instructions client
         
            while(true){
               System.out.println("Waiting for instructions...");                                          // Constantly waiting for new instructions from Teacher
               String teacherInstructions = instrucBR.readLine();
            
               if(teacherInstructions.equals("!CloseServer")){
                  instrucCS.close();
                  chatCS.close();
                  drawingCS.close();
               }   
                       
               if(teacherInstructions.equals("Updating Instructions!")){
                  instrucPWList.get(0).println("Updating Instructions!");
                  instrucPWList.get(0).flush();
               }
                                                          
               for(int i=0; i<instrucPWList.size(); i++){                                                  // Sends these messages back out to all clients connected.
                  instrucPWList.get(i).println(teacherInstructions);
                  instrucPWList.get(i).flush();
               }
            }
         }
         catch(IOException ie){
            System.out.println("IOException | Instructions run()");     
         }          
      }// end of run method
   }//end of ThreadedServer

   /**
    * DrawingThreadedServer handles
    * the DRAWING clients. It takes in
    * the points from the drawing array,
    * and sends them back out to
    * all of the currently connected users.
    */
   class DrawingThreadedServer extends Thread{
      /**
       * The same socket that connects the Drawing clients to the server.
       */
      private Socket drawCS;
      /**
       * The ObjectInputStream that reads in the objects from the Drawing client.
       */
      private ObjectInputStream drawOI;
      /**
       * The ObjectOutputStream that sends out objects to the Drawing client.
       */
      private ObjectOutputStream drawOO;
       
   
       /** ThreadedServer constructor
        *  Creates a copy of the drawCS socket to use
        *  for sending and receiving objects.
        *  @param drawCS - the socket for the drawing connection.
        */
      public DrawingThreadedServer(Socket drawCS){
         this.drawCS = drawCS;
      }// end constructor
       
       
       /** Creates object streams that send objects back
        *  and forth from the Drawing client.
        *  Takes in new list of points, and sends them back
        *  out to the clients connected to update their drawing.
        */
      public void run(){    
         try{  
            drawOO = new ObjectOutputStream( drawCS.getOutputStream());
            drawOOList.add(drawOO);
            drawOI = new ObjectInputStream(drawCS.getInputStream() );
         
            while(true){
               try{
                  System.out.println("Waiting for drawing....");
                  ArrayList<Line2D> lineCollection = (ArrayList<Line2D>)drawOI.readObject();
                  for(Line2D line:lineCollection){
                     System.out.println("server! -line");
                     System.out.println(line.getP1());
                     System.out.println(line.getP2());
                  }
                  for(int i=0; i< drawOOList.size(); i++){
                     drawOOList.get(i).writeObject(lineCollection);    
                     drawOOList.get(i).flush();
                  }
               }
               catch(ClassNotFoundException cnf){
                  System.out.println("Class not found in Drawing SendHandler");
               }
            }
         }
         catch(IOException ie){
            System.out.println("IOException | Drawing run()");     
         }          
      }// end of run method
   }//end of ThreadedServer
}// end of MyServer




