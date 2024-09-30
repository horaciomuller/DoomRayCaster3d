import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Minimap extends JPanel implements KeyListener {

    // Tamanho do mapa
    private final int mapWidth = 15, mapHeight = 10; // Ajuste a altura se necessário

    // O mapa: 1 = parede, 0 = espaço vazio
    private final int[][] map = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    // Posição do jogador
    private double playerX = 2, playerY = 2;
    private double playerAngle = 0;  // Ângulo de visão do jogador
    private final double moveSpeed = 0.1; // Velocidade de movimento
    private final int minimapScale = 30; // Aumente este valor para aumentar o tamanho do minimap

    /**
     * Construtor da classe Minimap.
     * Define o tamanho do painel e registra o KeyListener.
     */
    public Minimap() {
        setPreferredSize(new Dimension(600, 600)); // Tamanho da janela
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Desenhar o minimap
        drawMinimap(g2d);
    }

    /**
     * Desenha o minimap baseado na posição do jogador e no mapa.
     *
     * @param g Objeto Graphics2D para renderização.
     */
    private void drawMinimap(Graphics2D g) {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                // Verifica se x e y estão dentro dos limites do mapa
                if (map[y][x] == 1) {
                    g.setColor(Color.GRAY);  // Desenha paredes
                } else {
                    g.setColor(Color.DARK_GRAY);  // Desenha o chão
                }
                g.fillRect(x * minimapScale, y * minimapScale, minimapScale, minimapScale);
                g.setColor(Color.BLACK);
                g.drawRect(x * minimapScale, y * minimapScale, minimapScale, minimapScale);
            }
        }

        // Desenhar o jogador no minimap
        g.setColor(Color.RED);
        g.fillOval((int)(playerX * minimapScale) - 3, (int)(playerY * minimapScale) - 3, 6, 6);

        // Desenhar a direção de visão do jogador (setas)
        drawVisionArrows(g);
    }

    // Função para desenhar setas de visão
    private void drawVisionArrows(Graphics2D g) {
        g.setColor(Color.YELLOW);

        // Quantidade de setas a serem desenhadas
        int arrowCount = 6; // Defina a quantidade de setas

        for (int i = 0; i < arrowCount; i++) {
            double rayAngle = playerAngle - Math.PI / 6 + (i * (Math.PI / 3) / (arrowCount - 1));
            int rayEndX = (int) (playerX + Math.cos(rayAngle) * 2); // Comprimento da seta
            int rayEndY = (int) (playerY + Math.sin(rayAngle) * 2); // Comprimento da seta

            g.drawLine((int)(playerX * minimapScale), (int)(playerY * minimapScale),
                (int)(rayEndX * minimapScale), (int)(rayEndY * minimapScale));
        }
    }

    /**
     * Movimenta o jogador no minimap, verificando colisões.
     *
     * @param deltaX Deslocamento em X.
     * @param deltaY Deslocamento em Y.
     */
    private void movePlayer(double deltaX, double deltaY) {
        int testX = (int) (playerX + deltaX);
        int testY = (int) (playerY + deltaY);

        // Verifica se o movimento não colide com paredes
        if (testX >= 0 && testX < mapWidth && testY >= 0 && testY < mapHeight && map[testY][testX] == 0) {
            playerX += deltaX;
            playerY += deltaY;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        double deltaX = 0, deltaY = 0;

        // Calcula o deslocamento baseado na direção
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            deltaX = Math.cos(playerAngle) * moveSpeed;  // Movimento para cima
            deltaY = Math.sin(playerAngle) * moveSpeed;  // Movimento para cima
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            deltaX = -Math.cos(playerAngle) * moveSpeed;  // Movimento para baixo
            deltaY = -Math.sin(playerAngle) * moveSpeed;  // Movimento para baixo
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerAngle -= 0.1;  // Rotação para a esquerda
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerAngle += 0.1;  // Rotação para a direita
        }

        // Move o jogador
        movePlayer(deltaX, deltaY);
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("RayCaster with Minimap and Vision Arrows");
        Minimap minimap = new Minimap();
        frame.add(minimap);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
