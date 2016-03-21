import java.util.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.Line2D;
import java.io.*;
import java.net.*;

/**
* Blackboard Class for Student Client
* This class creates the Blackboard and monitors/stores the points
* as the user draws on the board using their mouse.
* @author Christopher and DACK team
* @version 2.0
*/

public class Drawing extends JPanel implements ActionListener{
    
   /**
   * stores each Line2D that is created
   */
   public ArrayList<Line2D> lineCollection = new ArrayList<Line2D>();
    /**
     * stores each of the line points 0,1,2,4 where each represent
     * integer line points that are used to create the 2D line
     */
   public ArrayList<Integer> linePoints = new ArrayList<Integer>();
    /**
     * x1 cordinate - the x point of the beginning of a line
     */
   private int x1 = 0;
    /**
     * y1 cordinate - the y point of the beginning of a line
     */
   private int y1 = 0;
    /**
     * y2 cordinate - the y point of the end of a line
     */
   private int y2 = 0;
    /**
     * x2 cordinate - the x point of the end of a line
     */
   private int x2 = 0;
    /**
     * if there is no previous line while the current line is being drawn,
     * the beginning points are stored as x1,y1,x1,y1 instead of x1,y1,x2,y2
     */
   private boolean noLine = false;
    /**
     * button that is used to erase the white board. When it is pressed, the 
     * lineCollection arrayList is cleared.
     */
   private JButton jbEraser;
    /**
     * The jPanel of the drawing board.
     */
   private JPanel jpDrawing;
    /**
     * The string that determines if the user is a "Student" or a "Teacher"
     * based on the results, the user will have a eraser if its a "Teacher"
     */
   private String studentTeacher;
    /**
     * The ObjectOutputStream is global because it is needed to be used to send
     * the lineCollection to all clients after the user releaes his mouse or 
     * presses the erase button.
     */
   private ObjectOutputStream drawOO;  
   
     /**
     *pastReleaseMillisTime is designed to store the "old" current release time.
     */
   private long pastReleaseMillisTime = 0;
       /**
     * currentReleaseMillisTime is designed to store the current time after user relases the mouse
     */
   private long currentReleaseMillisTime = 0;
   
    
/**
     * Creates instance of the board
     * Sets the board visible
    * Determines if there should be a Eraser button or not for the drawing board
    * @param ipAddress - takes in the ipAddres
    * @param studentTeacher - takes in "Teacher" or "Student" to determine if the eraser functionality should be allowed.
*/
   public Drawing(String ipAddress, String studentTeacher){
   
      String actualIP = ipAddress;
      MouseListeners listeners = new MouseListeners();   
      addMouseListener(listeners);
      addMouseMotionListener(listeners);
             
      jpDrawing = new JPanel();
      add(jpDrawing);
      if(studentTeacher.equals("Teacher")){
         jbEraser = new JButton("Eraser");
         jpDrawing.add(jbEraser);
         jbEraser.addActionListener(this);
      }     
      jpDrawing.setBackground(Color.WHITE);
      
      
      SendHandler sh = new SendHandler(lineCollection, actualIP);
   new Thread(sh).start();
   


   }
    /**
     * This is what displays the drawing points. Everytime "repaint" is called
     * the points are redrawn.
     * @param g - paintComponent, Graphics parameter
     */
   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2 = (Graphics2D) g;
         for( Line2D aLine: lineCollection){
            g2.setColor(Color.BLACK);
            g2.draw(aLine);
         }
   }
    
    /**
     * When the user pushes the "eraser" button this actionPerformed method
     * is called. The lineCollection that contains all the points is
     * erased/cleared. The Object Ouput (drawOO) is reseted and sends the empty
     * lineCollection to all the clients.
     * @param ae - this is connected to the "erase" button.
     *
     */
    
   public void actionPerformed(ActionEvent ae){
         lineCollection.clear();
      try{   
         drawOO.reset();
         drawOO.writeObject(lineCollection);
         drawOO.flush(); 
         }
      catch(IOException ie){
         System.out.println(ie);
      }
         repaint();    
   }
    
    /**
     * draws
     * Gets the points x1 and y1 - the starting points and stores them.
     * then adds them to an arrayList of linePoints at the indexes two and three
     * Checks if no line has drawn: if no line has been drawn, a 2D line is created and stored
     * in the ArrayList lineCollection. The x1, y1 is used for the starting and end point
     *  because there is no previous line end point in storage since this is the first drawn line point.
     *
     * If there has been a line, the x2, y2 points from the previously drawn points is used as the starting point of the new line. and the
     * x1, y1 from the mouseDragged is used as the ending points.
     *
     * Once completed, it calls repaint.
     *
     * After repaint is called, linePoints indexes 0,1 are set to x1,y1 respectively as those are the considered the new
     * "starting" points for the next new line.
     *
     *
     */
   
    private class MouseListeners extends MouseAdapter {
      public void mouseDragged(MouseEvent e) {
         x1 = e.getX();
         y1 = e.getY();
         linePoints.add(2,x1);
         linePoints.add(3,y1);
         if(noLine == true )
         {
            lineCollection.add(new Line2D.Double(linePoints.get(2), linePoints.get(3),linePoints.get(2), linePoints.get(3)));
            noLine = false;
         }
         else{
            lineCollection.add(new Line2D.Double(linePoints.get(0), linePoints.get(1),linePoints.get(2), linePoints.get(3)));
         }
         repaint();

         linePoints.add(0,x1);
         linePoints.add(1,y1);           
      }
      
       /**
        * When the user released the mouse after doing a drawing - the x,y
        * points are recieved and set to x2, y2 and added to the linePoints at
        * index 0,1.
        * Similar to the "Eraser" button - The Object Ouput (drawOO) is reseted
        * and sends the lineCollection to all the clients.
        *
        * The line collection is only sent if the difference between release times
        * is not a short period milliseconds. If it is a short period of milliseconds,
        * the computer gets mixed up. 
        * @param e - monitors when the mouse is released on the drawing board.
        *
        */

       public void mouseReleased(MouseEvent e) {
            x2 = e.getX();
            y2 = e.getY();
            linePoints.add(0,x2);
            linePoints.add(1,y2);
            noLine = true;
         pastReleaseMillisTime = currentReleaseMillisTime;
         currentReleaseMillisTime = System.currentTimeMillis();
         long timeElapsedSince =  currentReleaseMillisTime - pastReleaseMillisTime;
         System.out.println(timeElapsedSince);
         if(timeElapsedSince>600){
            try{   
               drawOO.reset();
               drawOO.writeObject(lineCollection);
               drawOO.flush(); 
               }
            catch(IOException ie){
               System.out.println(ie);
            }
         }
  
       }
    
       /**
        * Gets the x and y when the mouse is pressed on the white board. and adds them to the array at 0,2
        *
        * @param e - mointors when the mouse is pressed on the white board
        *
        */
      
    public void mousePressed(MouseEvent e) {
      x1 = e.getX();
         y1 = e.getY();
         linePoints.add(0,x1);
         linePoints.add(1,y2);
         noLine = true;
      }
   }
    
    /**
     * Creates instance of the board
     * Sets the board visible
     */
   
    class SendHandler extends MouseAdapter implements Runnable, StudentTeacherChatConstants{
    /**
    *a local variable version of the lineCollection that is recieved from the client
    */
    public ArrayList<Line2D> localCollection = new ArrayList<Line2D>();
    /**
    * drawCS is the Client Socket for the drawingboard
    */
    private Socket drawCS;
    /**
    * ObjectInputStream takes in the collection of 2D lines.
    */
    private ObjectInputStream drawOI;
    /**
    * The IP address that is passed in as a tring.
    */
    private String actualIP;
   
    /** SendHandler Constructor
    *  Sets the jta and jtf to the same from the GUI
    *  sets up Socket, BufferedReader, PrintWriter
    *  Sends messages to the Server
    *  Appends what is sent back to the chatroom
    *  @param lineCollection - the collection of all the 2D lines
    *  @param actualIP - the ipAddress of the user
    */
      
    public SendHandler(ArrayList<Line2D> lineCollection, String actualIP){
         this.actualIP = actualIP;
      
         try{
            drawCS = new Socket(actualIP, PORT_DRAWING);
            drawOI = new ObjectInputStream( drawCS.getInputStream() );
           drawOO = new ObjectOutputStream( drawCS.getOutputStream());
         }
         catch(UnknownHostException uhe){
            System.out.println("UnknownHostException | Draw SendHandler");
         }// end of the catch
         catch(IOException ie){
            System.out.println("IOException | Draw SendHandler");
         }//end of IO catch
      }//end of constructor
       
       /**
        * Takes the localCollection and clears it, then sets the localCollection to the recieved collection of lines.
        * if the size of localCollection 0 or empty - clear the lineCollection because the localCollection
        * has been erased (The teacher erased the board) - then repainted.
        * if there are lines within the localCollection - the lines in the lineCollection
        * is replaced with the lines from the localCollection - then repainted
        */
       
      public void run(){
      double timer = 0;
         System.out.println("Would be reading the drawing");    
        try{
            while(true){
               localCollection.clear();//clears the current arraylist for a new update
            try {
                        Thread.sleep(2000);
                    
           } catch (InterruptedException e) {
                    e.printStackTrace();
           }
               localCollection = (ArrayList<Line2D>)drawOI.readObject();
            if(localCollection.size() == 0){
               lineCollection.clear();
               System.out.println("clear");
               repaint();
            }
            else{
               lineCollection.addAll(localCollection);
                  repaint();
            }
               repaint();
            }        
         }
         catch(IOException ie){
          System.out.println("IOException | Draw Run");
         }   
        catch(ClassNotFoundException cnf){
        System.out.println("Class Not FOund Exception | Draw Run");
         }
      } // end run
   }//end of SendHandler
}//end of Drawing