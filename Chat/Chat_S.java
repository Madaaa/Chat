
import java.util.*;
import java.io.*;
import java.net.*;

public class Chat_S {
    
private static final int port = 1111;
private static Vector<String> nicknames = new Vector();
private static Vector<DataOutputStream> conversation = new Vector();
private static Vector<Handler> v = new Vector();

private static class Handler extends Thread{
    private String nickname;
    private String nick;
    private String nick1;
    private Socket ss;
    private DataOutputStream dos;
    private DataInputStream dis;
   
   
    
    public Handler (Socket ss) {
        this.ss = ss;
        start();
    }
    
    public void run() {
        try{
        dis = new DataInputStream(ss.getInputStream());
        dos = new DataOutputStream(ss.getOutputStream());
        
         
        while(true) {
            dos.writeUTF("TypeANickname");
            nickname = dis.readUTF();
            if(nickname == null)
            return;
        
            synchronized(nicknames) {
                if(!nicknames.contains(nickname)&&nickname.equals("")==false) {
                    nicknames.addElement(nickname);
                    break;
                }
            }
        }
       for ( int i = 0; i < nicknames.size(); i ++ )
        dos.writeUTF("NameAccepted" + nicknames.get(i));
        conversation.addElement(dos);
        
        while(true) {
            String input = dis.readUTF();
            if(input == null)
            return;
            if (input.startsWith("Name"))
                 for ( int i = 0; i < nicknames.size(); i ++ )
                     dos.writeUTF("List"+nicknames.get(i));
             else if (input.startsWith("Change")) {
                  dos.writeUTF("Change1");
                }
                 else if(input.startsWith("New")){
                     nick1 = "";
                     nick1 = input.substring(3);
                     if(nick1.equals("null") || nick1.equals(""))
                             dos.writeUTF("Message"+ " You have not changed your nickname");
                     else
                     if(nicknames.contains(nick1)) 
                         dos.writeUTF("Change1");
                    
                    else { 

                        nicknames.set(nicknames.indexOf(nickname),nick1);
                        dos.writeUTF("Message"+" You've changed your nickname in " + nick1);
                        nickname = nick1;
                      
                    }
                }
                  else if (input.startsWith("@")){
                      if(!input.contains(":")) dos.writeUTF("Er1Sintaxa corecta pentru mesaj privat este: @nume:mesaj");
                      else{
                      StringTokenizer st = new StringTokenizer(input.substring(1,input.length()), ":");
                      String pm = st.nextToken();
                      String msg = "";
                      if(input.length() >(pm.length() +2))
                           msg = st.nextToken();
                   
                      int ok = 0;
                     if(nicknames.contains(pm)) 
                      for ( int i = 0; i < v.size(); i ++ ) {
                         Handler h = (Handler)v.get(i);
                          if ( h.nickname.equals(pm)) {
                              if(msg.length() > 0)
                                h.dos.writeUTF("PM"+"Private message from " + nickname + ":" + msg);
                              else 
                                h.dos.writeUTF("PM"+"Private mssage from " + nickname + ":");
                              
                              break;
                        }
                    }  
                
                    else dos.writeUTF("ERUtilizatorul nu este online");
                    }
                }
                       else
                           for (DataOutputStream output: conversation) 
                                output.writeUTF("Message " + nickname + ":" + input);
        }
    }catch (IOException e) {
            System.out.println("Nu s-au putut deschide fisierele de IO");
        }finally {
            if(nickname != null) {
                nicknames.remove(nickname);
            }
            if(dos != null ) {
                conversation.remove(dos);
            }
            try{
                ss.close();
            }catch(IOException e) {
            }
        }
    }
}

    

public static void main() throws Exception {
    System.out.println("The chat server starts working");
    ServerSocket ss = new ServerSocket(port);
    
    try {
        while(true) {
           v.add(new Handler(ss.accept()));            
        } 
    }finally {
            ss.close();
        }

  }
}
