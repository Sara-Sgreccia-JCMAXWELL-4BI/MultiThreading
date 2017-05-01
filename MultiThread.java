/*
 * Con questo programma voglio illustrare i seguenti concetti:
 * 1. MAIN e' un thread come gli altri e quindi puo' terminare prima che gli altri
 * 2. THREADs vengono eseguiti allo stesso tempo
 * 3. THREADs possono essere interrotti e hanno la possibilita' di interrompersi in modo pulito
 * 4. THREADs possono essere definiti mediante una CLASSE che implementa un INTERFACCIA Runnable
 * 5. THREADs possono essere avviati in modo indipendente da quando sono stati definiti
 * 6. posso passare parametri al THREADs tramite il costruttore della classe Runnable
 */
package multithread;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import static multithread.TicTacToe.conta;
/**
 *
 * @author Matteo Palitto
 */
public class MultiThread {

    /**
     * @param args the command line arguments
     */
    // "main" e' il THREAD principale da cui vengono creati e avviati tutti gli altri THREADs
    // i vari THREADs poi evolvono indipendentemente dal "main" che puo' eventualmente terminare prima degli altri
    public static void main(String[] args) {
        System.out.println("Main Thread iniziata...");
        Random random = new Random();
        int j = 100;
        int n = 300-j;
        int TTic= random.nextInt(n)+j;  //Valori compresi tra 100 e 300
        int TTac= random.nextInt(n)+j;  //Valori compresi tra 100 e 300
        int TToe= random.nextInt(n)+j;  //Valori compresi tra 100 e 300
        long start = System.currentTimeMillis();  
        
        // Posso creare un THREAD e avviarlo immediatamente
        Thread tic = new Thread (new TicTacToe("TIC"));
        
        // Posso creare un 2ndo THREAD e farlo iniziare qualche tempo dopo...
        Thread tac = new Thread(new TicTacToe("TAC"));
        
        // Posso creare un 3ndo THREAD e farlo iniziare qualche tempo dopo...
        Thread toe = new Thread (new TicTacToe("TOE"));
        
        tic.start();  // avvio del primo THREAD
        tac.start();  // avvio del secondo THREAD
        toe.start();  // avvio del terzo THREAD

        long end = System.currentTimeMillis();
        
        try{
            tic.join(); //mette in attesa finchè  il THREAD non finisce la sua esecuzione 
            tac.join(); //mette in attesa finchè  il THREAD non finisce la sua esecuzione
            toe.join(); //mette in attesa finchè  il THREAD non finisce la sua esecuzione
        }
        catch(InterruptedException e){
            
        }
        System.out.println("Main Thread completata! tempo di esecuzione: " + (end - start) + "ms");
        System.out.println("score: "+conta);
    }
    
}

// Ci sono vari (troppi) metodi per creare un THREAD in Java questo e' il mio preferito per i vantaggi che offre
// +1 si puo estendere da un altra classe
// +1 si possono passare parametri (usando il Costruttore)
// +1 si puo' controllare quando un THREAD inizia indipendentemente da quando e' stato creato
class TicTacToe implements Runnable {
    
    // non essesndo "static" c'e' una copia delle seguenti variabili per ogni THREAD 
    private String t;
    private String msg;
    public static boolean score = false;  // variabile booleana 
    public static int conta = 0;
                                 
    public TicTacToe (String s) {   // Costruttore, possiamo usare il costruttore per passare dei parametri al THREAD
        this.t = s;
    }
    
    @Override // Annotazione per il compilatore
    // se facessimo un overloading invece di un override il copilatore ci segnalerebbe l'errore
    // per approfondimenti http://lancill.blogspot.it/2012/11/annotations-override.html
    public void run() {        // esegue il THREAD quando è attivo
        for (int i = 10; i > 0; i--) {
            msg = "<" + t + "> ";    //System.out.print(msg);
            Random random = new Random();  //creazione numero casuale
            int j = 100;
            int n = 300-j;
            if("TAC".equals(t))
            {
                score = true;
            }
            
            int r = random.nextInt(n)+j;  //Valori compresi tra 100 e 300
            
            try {
                TimeUnit.MILLISECONDS.sleep(r); //interrompe momentaneamente il THREAD
            } catch (InterruptedException e) {}
            
            
           if("TOE".equals(t) && score == true)  //controllo se toe si trova subito dopo tac
           {
               conta++;     //incremento il punteggio 
           }
           else
           {
               score = false;
           }
                   
            msg += t + ": " + i;
            System.out.println(msg);
         
        }
    }
    
}
