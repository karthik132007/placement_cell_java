package Models;
import java.util.ArrayList;
import DAOs.AirlineDAO;

public class Airline {
    String Name;
    int Id;
    
    public Airline() {
        // Default constructor
    }
    
    public Airline(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public ArrayList<String>aboutAirline(){
        ArrayList<String> result = new ArrayList<>();
        result.add("Name: "+this.Name);
        result.add("Id: "+this.Id);

        return result;
    }
    public void addAircraft(Aircraft aircraft){
        AirlineDAO.addAircraft_toFleet(aircraft);
    }

    public String getName(){
        return this.Name;
    }

    public void setName(String name){
        this.Name=name;
    }

    public int getId(){
        return this.Id;
    }

    public void setId(int id){
        this.Id=id;
    }
}
 