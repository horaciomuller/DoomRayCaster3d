import javax.swing.JFrame;

public class Main {
    /**
     * Método principal para executar a aplicação.
     *
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("RayCaster with Minimap and Vision Arrows");
        Minimap minimap = new Minimap();
        frame.add(minimap);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
