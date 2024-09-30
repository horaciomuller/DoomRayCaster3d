import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RayCaster extends JPanel implements KeyListener {
    private final int mapWidth = 10, mapHeight = 10;

    private final int[][] map = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 0, 0, 1, 0, 0, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
        {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    private double playerX = 2, playerY = 2;
    private double playerAngle = 0;
    private final double moveSpeed = 0.1, rotationSpeed = 0.05;
    private final int minimapScale = 20;

    // Posição do totem
    private double totemX = 5.5, totemY = 5.5;

    public RayCaster() {
        setPreferredSize(new Dimension(800, 600));
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Cor de fundo
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Ray casting
        for (int x = 0; x < getWidth(); x++) {
            double rayAngle = (playerAngle - Math.PI / 6) + (x * (Math.PI / 3) / getWidth());

            double distanceToWall = 0;
            boolean hitWall = false;

            double eyeX = Math.cos(rayAngle);
            double eyeY = Math.sin(rayAngle);

            // Lançar o raio e verificar colisão com parede
            while (!hitWall && distanceToWall < 30) {
                distanceToWall += 0.1;
                int testX = (int) (playerX + eyeX * distanceToWall);
                int testY = (int) (playerY + eyeY * distanceToWall);

                if (testX < 0 || testX >= mapWidth || testY < 0 || testY >= mapHeight) {
                    hitWall = true;
                    distanceToWall = 30;
                } else if (map[testY][testX] == 1) { // Corrigido: testY e testX
                    hitWall = true;
                }
            }

            // Calcular altura da parede
            int lineHeight = (int) (getHeight() / distanceToWall);
            int color = 255 / (1 + (int) distanceToWall);
            g2d.setColor(new Color(color, color, color));
            g2d.drawLine(x, (getHeight() - lineHeight) / 2, x, (getHeight() + lineHeight) / 2);
        }


        // Desenhar o totem
        drawTotem(g2d);

        // Desenhar o minimap
        drawMinimap(g2d);
    }

    // Método para desenhar o totem
    private void drawTotem(Graphics2D g) {
        // Calcular a distância entre o jogador e o totem
        double deltaX = totemX - playerX;
        double deltaY = totemY - playerY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        // Calcular o ângulo entre o jogador e o totem
        double angleToTotem = Math.atan2(deltaY, deltaX) - playerAngle;

        // Verificar se o totem está dentro do campo de visão
        if (Math.abs(angleToTotem) < Math.PI / 6) {
            double totemHeight = getHeight() / distance; // Altura do totem com base na distância
            int totemScreenX = (int) ((0.5 + angleToTotem / (Math.PI / 3)) * getWidth());

            // Desenhar o totem
            g.setColor(Color.ORANGE);
            g.fillRect(totemScreenX - 10, (int) (getHeight() / 2 - totemHeight / 2), 20, (int) totemHeight);
        }
    }


    private void drawMinimap(Graphics2D g) {
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                g.setColor(map[y][x] == 1 ? Color.GRAY : Color.DARK_GRAY);
                g.fillRect(x * minimapScale, y * minimapScale, minimapScale, minimapScale);
                g.setColor(Color.BLACK);
                g.drawRect(x * minimapScale, y * minimapScale, minimapScale, minimapScale);
            }
        }

        // Desenhar o jogador no minimap
        g.setColor(Color.RED);
        g.fillOval((int)(playerX * minimapScale) - 3, (int)(playerY * minimapScale) - 3, 6, 6);

        // Desenhar o totem no minimap
        g.setColor(Color.ORANGE);
        g.fillRect((int)(totemX * minimapScale) - 5, (int)(totemY * minimapScale) - 5, 10, 10);

        // Desenhar o raio no minimap
        g.setColor(Color.YELLOW);
        for (int i = 0; i < getWidth(); i += 10) {
            double rayAngle = (playerAngle - Math.PI / 6) + (i * (Math.PI / 3) / getWidth());

            double eyeX = Math.cos(rayAngle);
            double eyeY = Math.sin(rayAngle);

            double distanceToWall = 0;
            boolean hitWall = false;

            while (!hitWall && distanceToWall < 30) {
                distanceToWall += 0.1;
                int testX = (int) (playerX + eyeX * distanceToWall);
                int testY = (int) (playerY + eyeY * distanceToWall);

                if (testX < 0 || testX >= mapWidth || testY < 0 || testY >= mapHeight) {
                    hitWall = true;
                } else if (map[testY][testX] == 1) { // Corrigido: testY e testX
                    hitWall = true;
                }
            }

            int rayEndX = (int) (playerX + eyeX * distanceToWall);
            int rayEndY = (int) (playerY + eyeY * distanceToWall);

            g.drawLine((int)(playerX * minimapScale), (int)(playerY * minimapScale),
                rayEndX * minimapScale, rayEndY * minimapScale);
        }
    }


    private boolean isWall(double x, double y) {
        // Verifica os limites do mapa
        if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight) {
            return true; // Fora dos limites do mapa é considerado uma parede
        }
        // Retorna true se a posição contém uma parede
        return map[(int) y][(int) x] == 1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        double newPlayerX = playerX;
        double newPlayerY = playerY;

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            newPlayerX += Math.cos(playerAngle) * moveSpeed;
            newPlayerY += Math.sin(playerAngle) * moveSpeed;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            newPlayerX -= Math.cos(playerAngle) * moveSpeed;
            newPlayerY -= Math.sin(playerAngle) * moveSpeed;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            playerAngle -= rotationSpeed;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerAngle += rotationSpeed;
        }

        // Verifica se o novo local é uma parede
        if (!isWall(newPlayerX, newPlayerY)) {
            playerX = newPlayerX;
            playerY = newPlayerY;
        }

        repaint();
    }


    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
