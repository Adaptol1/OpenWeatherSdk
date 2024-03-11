import Controller.Controller;
import com.google.gson.JsonObject;
import java.io.FileWriter;

public class Main
{
    public static void main(String[] args) {
        Controller controller = new Controller();
        JsonObject json = controller.requestWeather("Ufa", "7030eef86a2371f7ec6fb10a6d911587");
        String path = "C:\\Users\\adapt\\OneDrive\\Документы\\GitHub\\test.json";
        try
        {
            FileWriter fw = new FileWriter(path);
            fw.write(json.toString());
            fw.flush();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }


        System.out.println(json);
        System.out.println(json.toString());

    }
}
