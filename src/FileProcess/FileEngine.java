/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FileProcess;

import DataStructers.MyLinkedList;
import java.util.List;

/**
 *
 * @author aliemre
 */
public class FileEngine {

    protected ExtractSourceFiles esf;
    protected MakeTxtFiles mtf;
    protected MemberInformaion mi;
    protected SourceListAndTargetListİmplementaion slatli;
    protected WriteResults wr;

    public FileEngine() {
        this(new ExtractSourceFiles(), new MakeTxtFiles(), new MemberInformaion(),
                new SourceListAndTargetListİmplementaion(), new WriteResults());
    }

    public FileEngine(ExtractSourceFiles esf, MakeTxtFiles mtf, MemberInformaion mi, SourceListAndTargetListİmplementaion sla, WriteResults wr) {
        this.esf = esf;
        this.mtf = mtf;
        this.mi = mi;
        this.slatli = sla;
        this.wr = wr;
    }
    public boolean readFileAgain(){
    return mi.readFile();
    }
    
    //MemberINformaion Metodalrı
    public boolean RegisterProcess(String username, String password, String password2) {
        if (username == null || !username.matches("[\\p{L}\\p{N}_]+")
                || password == null || password.isBlank() || !mi.readFile()) {
            return false;
        }
        if (mi.isUserAvaible(username)) {
            return false;

        }
        if (! password.equals( password2)) {
            return false;
        }
        
        
        return true;
    }
    
     public boolean SignInProcess(String username, String password) {
        
         return mi.checkUserInformation(username, password);
    }
      public String getterUsername() {
        
         return mi.getterUsername();
    }
       public String getterEmail() {
        
         return mi.getterEmail();
    }
       public boolean WriteNewMembers(String username, String password,String email) {
        
         return wr.writeNewMemberInformation(username, password, email) ;
    }
       
       //****************
       
    
       //ExtractSourceList
       
    public boolean addingSourceList(String sourceList, String username,String nameSourceList){
        
        return esf.addNewSourceList(sourceList, username,nameSourceList);

    }
    public String sendSourceListsToUser(String username){
        return esf.collectSourceList(username);
    
    } 
    
    public String choosenList(String username ,String sourceListname){
    
    
    
    return esf.chooseSourceList(username, sourceListname);
    }
    
    
    
    //implemntion
    public List<MyLinkedList> writeResultsOnTextArea(String sourceList, String targetList, String lessonName,String sourcelistname){
    
        slatli.resetTable();
        slatli.readOldInformation(esf.logPath(sourcelistname));
        return slatli.implement(sourceList, targetList, lessonName);

    }
    public List<MyLinkedList> returnTable(){
        List<MyLinkedList> list=slatli.returnTable();
        
        return list;
    }

    
    public boolean writeSouceListLogs(String sourceList, String targetList, String lessonName,String sourcelistname){
        
        List<MyLinkedList> list = writeResultsOnTextArea(sourceList, targetList, lessonName, sourcelistname);
        return wr.writeLessonNotes(list, esf.logPath(sourcelistname));

    }
    
   
    
    
    
    //maketxt
    public boolean makeTxtLesson(String lessonName){
    
    return mtf.makeTxtLesson(returnTable(), lessonName);
    }
    public boolean makeTxtStudentNotes(String number){
    
    return mtf.makeTxtStudentNumbers(returnTable(), number);
    }
    
     public boolean makeTxtAllResults(String fileName){
    
    return mtf.makeTxtAllOfThem(returnTable(),fileName);
    }
}
