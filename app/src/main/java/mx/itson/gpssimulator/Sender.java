package mx.itson.gpssimulator;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends AsyncTask<Void, Void, Void> {

    private Socket socket;
    private String IP = "192.168.0.17";
    private PrintWriter printWriter;
    private String message;

    public Sender(String message){
        this.message = message;
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param voids The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Void doInBackground(Void... voids) {
        try{
            socket = new Socket(IP, 5000);
            printWriter = new PrintWriter(socket.getOutputStream());
            System.out.println("Sending message");
            printWriter.write(message);
            printWriter.flush();
            printWriter.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
