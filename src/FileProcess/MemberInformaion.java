/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileProcess;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 *
 * @author aliemre
 */
public class MemberInformaion {

    private String username;
    private String email;
    private String password;

    private List<String> lines = new java.util.ArrayList<>();

   
    
    
   
    
    public MemberInformaion() {
        readFile();
        
    }
    
    
    
    public boolean readFile(){
    try {
        
         Path path = Paths.get("src", "TxtFiles", "MembersInformation.txt");
        if (!Files.exists(path)) {
            lines = new java.util.ArrayList<>();
            return true;
        }
        lines = Files.readAllLines(
            Paths.get("src","TxtFiles", "MembersInformation.txt")
        );

        return true;

    } catch (IOException e) {
        System.out.println(e.getMessage() +"sıkıntı");
        return false;
    }
    
    }

   public boolean isUserAvaible(String username) {
        for (String line : lines) {
           String []part=line.split("\\s+");
           if(part.length==3 && username.equals(part[0])){
               return true;
           }
  
       }
        this.username=username;
       return false;
       
       
        }

    public boolean checkUserInformation(String username, String password) {
         for (String line : lines) {
             
           String []part=line.split("\\s+");
           
           
           if(part.length==3 ){
               
               if(username.equals(part[0]) && password.equals(part[1])){
               
                   this.username=username;
                   this.email=part[2];
                   
               return true;
               }
               
               
              
           }
  
       }
       return false;
        
        
       
    }

    public String getterUsername() {
        return username;
    }

    public String getterEmail() {
        return email;
    }
}
