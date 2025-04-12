import Classes.Devices.Controller;
import Classes.Devices.DeviceStorage;
import Classes.Graphics.Graphics;

public class Main {
    public static void main(String[] args) {
        DeviceStorage storage = new DeviceStorage();
        Graphics gui = new Graphics();
        Controller controller = new Controller(storage, gui);
        gui.linkController(controller);
        storage.linkController(controller);
        storage.populateStationLayers();
    }
}

