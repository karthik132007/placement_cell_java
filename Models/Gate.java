package Models;
import java.time.LocalTime;
public class Gate {
    int gate_num;
    int capacity;
    LocalTime openTime;
    LocalTime closTime;

    public int getGate_num() {
        return gate_num;
    }

    public int getCapacity() {
        return capacity;
    }

    public LocalTime getOpenTime() {
        return openTime;
    }

    public LocalTime getClosTime() {
        return closTime;
    }

    public Gate(int number, int capacity, LocalTime openTime, LocalTime closTime) {
        this.gate_num = number;
        this.capacity = capacity;
        this.openTime = openTime;
        this.closTime = closTime;
    }
}
