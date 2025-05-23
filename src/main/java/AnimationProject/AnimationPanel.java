package AnimationProject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationPanel extends JPanel implements Runnable {
    private BodySegment torso; // The root of our character hierarchy
    private long lastTime;
    private int frameCount = 0;
    private final double FPS = 30.0;
    private final long FRAME_TIME_MILLIS = (long) (1000 / FPS);

    public AnimationPanel() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.LIGHT_GRAY);
        setupCharacter();
        new Thread(this).start(); // Start the animation thread
    }

    private void setupCharacter() {
        // Define segments for a simple stick figure arm
        // Vertices for a "bone" starting at (0,0) and extending to (length, 0)
        List<Point> boneShape = Arrays.asList(new Point(0, 0), new Point(1, 0)); // Line from 0,0 to 1,0 (scaled by length)

        // Torso (root, just a placeholder line here)
        torso = new BodySegment("Torso", boneShape, new Point(0,0), 0); // Length 0, just for its world transform
        // Torso's actual "shape" for drawing can be custom.
        // For simplicity, let's make it a small vertical line:
        torso.localVertices = Arrays.asList(new Point(0, -25), new Point(0, 25)); // A short vertical line

        // Upper Arm (starts at torso's shoulder)
        List<Point> upperArmShape = Arrays.asList(new Point(0, 0), new Point(60, 0));
        BodySegment upperArm = new BodySegment("UpperArm", upperArmShape, new Point(0, 0), 60);
        upperArm.parent = torso;
        torso.children.add(upperArm);

        // Forearm (starts at upper arm's elbow)
        List<Point> forearmShape = Arrays.asList(new Point(0, 0), new Point(50, 0));
        BodySegment forearm = new BodySegment("Forearm", forearmShape, new Point(0, 0), 50);
        forearm.parent = upperArm;
        upperArm.children.add(forearm);

        // Hand (starts at forearm's wrist)
        List<Point> handShape = Arrays.asList(new Point(0, 0), new Point(40, 0));
        BodySegment hand = new BodySegment("Hand", handShape, new Point(0, 0), 40);
        hand.parent = forearm;
        forearm.children.add(hand);

        // Initialize positions
        torso.updateWorldTransform(); // Update all segments' initial world transforms
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3)); // Make lines thicker

        drawSegment(g2d, torso); // Draw the entire hierarchy starting from the root
    }

    private void drawSegment(Graphics2D g2d, BodySegment segment) {
        List<Point> transformed = segment.getTransformedVertices();
        if (transformed.size() >= 2) {
            // Draw the "bone" line
            Point start = transformed.get(0);
            Point end = transformed.get(1);
            g2d.setColor(Color.BLUE);
            g2d.drawLine(start.x, start.y, end.x, end.y);

            // Draw a red circle at the joint (pivot point)
            double[] pivotInWorld = segment.parent != null ?
                    segment.parent.worldTransform.transformPoint(segment.parent.length, 0) : // End of parent bone
                    segment.worldTransform.transformPoint(segment.localPivot.getX(), segment.localPivot.getY()); // Root's own pivot

            // Adjust pivotInWorld for actual joint drawing, based on parent's end point
            if (segment.parent != null) {
                // Get the transformed end point of the parent segment. This is where this segment connects.
                Point parentEnd = segment.parent.getTransformedVertices().get(1);
                g2d.setColor(Color.RED);
                g2d.fillOval(parentEnd.x - 5, parentEnd.y - 5, 10, 10); // Draw joint as circle
            } else {
                // For the root (torso), draw its base pivot
                Point torsoBase = segment.getTransformedVertices().get(0);
                g2d.setColor(Color.RED);
                g2d.fillOval(torsoBase.x - 5, torsoBase.y - 5, 10, 10);
            }
        }

        // Recursively draw children
        for (BodySegment child : segment.children) {
            drawSegment(g2d, child);
        }
    }


    @Override
    public void run() {
        lastTime = System.currentTimeMillis();
        while (true) {
            long now = System.currentTimeMillis();
            long elapsedTime = now - lastTime;

            if (elapsedTime >= FRAME_TIME_MILLIS) {
                lastTime = now;
                frameCount++;

                // --- Animation Logic for one frame ---
                double time = frameCount * (1.0 / FPS); // Time in seconds
                double angleWave = 30 * Math.sin(time * 2 * Math.PI); // +/- 30 degrees wave
                double handWave = 45 * Math.sin(time * 2 * Math.PI + Math.PI / 4); // Hand slightly offset

                // Apply angles to segments
                // Note: These angles are relative to their parent's transformed orientation
                // For a simple wave, make the upper arm swing, forearm follow, hand follow
                torso.setAngle(0); // Torso doesn't rotate much

                // Example of simple arm wave animation
                torso.children.get(0).setAngle(angleWave); // UpperArm
                torso.children.get(0).children.get(0).setAngle(-angleWave * 0.8); // Forearm
                torso.children.get(0).children.get(0).children.get(0).setAngle(handWave); // Hand

                // Update all transformation matrices for the new pose
                torso.updateWorldTransform(); // This call recursively updates children

                // Request a repaint
                repaint();
            }

            try {
                // Sleep for the remainder of the frame time or a small fixed duration
                Thread.sleep(Math.max(0, FRAME_TIME_MILLIS - (System.currentTimeMillis() - now)));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                System.err.println("Animation thread interrupted: " + e.getMessage());
                break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Matrix-Based Human Animation (Java 2D)");
            AnimationPanel panel = new AnimationPanel();
            frame.add(panel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}