package netUtiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by svetlana on 13.05.17.
 */
public class CommandLineMH implements MessageHandler {
    @Override
    public String handle(String message) {
        StringBuffer output = new StringBuffer();
        Process process;
        try {
            process  = Runtime.getRuntime().exec(new String[]{message});
            process.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"Cp866"));
            BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream(), "Cp866"));
            String line = "";
            if(error.ready()){
                while ((line = error.readLine()) != null){
                    output.append(line + "\n");
                }
            }
            else {
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return output.toString();
    }
}
