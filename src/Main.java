import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("RayCaster with Minimap");
        RayCaster rayCaster = new RayCaster();
        frame.add(rayCaster);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
