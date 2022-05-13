import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Server {

    public static String getNumber(String k)throws Exception{
        Scanner sc = new Scanner(new File("DatiServer.csv"));
        while(sc.hasNext()){
            String line = sc.nextLine();
            String elm[] = line.split(";");
            if(elm[0].equals(k)){
                return elm[1];
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        int port  = 10000;
        ServerSocket server = new ServerSocket(port); //crea server
        Socket s = server.accept(); //il server aspetta che un client si connetta
                                    //questo metodo è bloccante

        OutputStreamWriter o = new OutputStreamWriter(s.getOutputStream()); //creo un outputWriter per scrivere sul canale di trasmissione
        InputStreamReader i = new InputStreamReader(s.getInputStream()); //creo un inputReader per leggere dal canale di trasmissione
                                                                            //devo passargli la connessione, il canale

        //il canale va bufferizzato sennò può leggere e scrivere solo un carattere alla volta :/
        BufferedWriter out = new BufferedWriter(o);
        BufferedReader in = new BufferedReader(i);
                                                    //così abbiamop bufferizzato i canali

        //--------------------------------Fine implementazione cose importanti------------------------------------------

        String a = "Welcome"; //stringa da trasmettere

        //--------------------------------Fine output-------------------------------------------------------------------
        while(true) {
            a = in.readLine(); //per leggere un input, e' bloccante
            System.out.println(a);
            if(a.equals("end")){
                break;
            }
            if(a.substring(0, a.indexOf(":")).equals("give")){
                try {
                    out.write("find:"+getNumber(a.substring(a.indexOf(":")+1, a.length())));
                } catch (Exception e) {
                    out.write("No so bon a scrivere codice");
                }
            }
            out.newLine();
            out.flush();
        }

        o.close();
        i.close();
        //---------------------------------Chiudo la connessione--------------------------------------------------------
        s.close(); //chiudo la connessione
        server.close(); //chiude il server
    }
}
