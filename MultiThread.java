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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        Monitor monitor = new Monitor();
        // Posso creare un THREAD e avviarlo immediatamente
        Thread tic = new Thread (new TicTacToe("TIC", monitor));
        
        // Posso creare un 2ndo THREAD e farlo iniziare qualche tempo dopo...
        Thread tac = new Thread(new TicTacToe("TAC",monitor ));
        
        // Posso creare un 3ndo THREAD e farlo iniziare qualche tempo dopo...
        Thread toe = new Thread (new TicTacToe("TOE", monitor));
        
        tic.start();  // avvio del primo THREAD
        tac.start();  // avvio del secondo THREAD
        toe.start();  // avvio del terzo THREAD

        long end = System.currentTimeMillis();
        
        try{
            tic.join();
            tac.join();
            toe.join();
        }
        catch(InterruptedException e){
            
        }
        System.out.println("Main Thread completata! tempo di esecuzione: " + (end - start) + "ms");
        System.out.println("score: "+monitor.getScore());
    }
    
}
class Monitor {                   //classe del monitor
    String ultimoTHREAD = " ";    //ultimo thread che ha scritto sullo schermo 
    int score = 0;                //punteggio viene condiviso dai thread                 
    
   public int getScore(){         //fornisce il punteggio 
       return score;              
   }
   
  public synchronized void Score (String thread, String msg) throws InterruptedException{   //metodo condiviso dai thread 
      Random random = new Random();  //creazione numero casuale
      int j = 100;
      int n = 300-j;
      int r = random.nextInt(n)+j;  //Valori compresi tra 100 e 300
      msg = "<" + r + "> ";    //System.out.print(msg);
      if(thread == "TOE" && ultimoTHREAD == "TAC"){          //controlla quando toe viene prima di tac 
          score++;                                          //aggiornamento del punteggio
      }
      TimeUnit.MILLISECONDS.sleep(r);              //casuale ora diventa rappresentante
      ultimoTHREAD = thread;                      //l'ultimo thread viene visualizzato
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
    Monitor monitor; 
                                 
    public TicTacToe (String s, Monitor m) {   // Costruttore, possiamo usare il costruttore per passare dei parametri al THREAD
        this.t = s;
        this.monitor = m;
    }
    
    @Override // Annotazione per il compilatore
    // se facessimo un overloading invece di un override il copilatore ci segnalerebbe l'errore
    // per approfondimenti http://lancill.blogspot.it/2012/11/annotations-override.html
    public void run() {        // esegue il THREAD quando è attivo
        for (int i = 10; i > 0; i--) {
            try {    
                monitor.Score(t,msg);
            } catch (InterruptedException ex) {   //richiamo eccezione
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }
            msg = "<" + t + ">" + t + ":" + i;
            System.out.println(msg);
        }
    }
    
}
