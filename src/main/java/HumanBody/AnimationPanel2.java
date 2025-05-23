package HumanBody;// AnimationPanel.java
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class AnimationPanel2 extends JPanel implements Runnable {
    private bodySegment torso; // The root of our character hierarchy
    private long lastTime;
    private int frameCount = 0;
    private final double FPS = 30.0;
    private final long FRAME_TIME_MILLIS = (long) (1000 / FPS);

    // --- Walking Cycle Keyframes (Example data for a simple 20-frame cycle) ---
    // Angles are in degrees. Each inner array is a pose for one frame.
    // Order: [hip_L, knee_L, ankle_L, hip_R, knee_R, ankle_R, shoulder_L, elbow_L, shoulder_R, elbow_R, head_nod]
    // These angles are relative rotations for each joint.
    private static final double[][] WALK_CYCLE_ANGLES = {
            // Frame 0 (Contact Phase - Right foot forward, Left foot back)
            {-10, 0, 0, 20, 0, 0, 10, 0, -10, 0, 0},
            // Frame 1
            {-5, 5, 0, 15, -5, 0, 5, 5, -5, -5, 0},
            // Frame 2
            {0, 10, 0, 10, -10, 0, 0, 10, 0, -10, 0},
            // Frame 3
            {5, 15, 0, 5, -15, 0, -5, 15, 5, -15, 0},
            // Frame 4 (Mid-swing - Left leg forward, Right leg back)
            {10, 20, 0, 0, -20, 0, -10, 20, 10, -20, 0},
            // Frame 5
            {15, 15, 0, -5, -15, 0, -15, 15, 15, -15, 0},
            // Frame 6
            {20, 10, 0, -10, -10, 0, -20, 10, 20, -10, 0},
            // Frame 7 (Contact Phase - Left foot forward, Right foot back)
            {20, 5, 0, -15, -5, 0, -20, 5, 20, -5, 0},
            // Frame 8
            {15, 0, 0, -20, 0, 0, -15, 0, 15, 0, 0},
            // Frame 9
            {10, -5, 0, -15, 5, 0, -10, -5, 10, 5, 0},
            // Frame 10 (Mid-stance - Right leg forward, Left leg back)
            {5, -10, 0, -10, 10, 0, -5, -10, 5, 10, 0},
            // Frame 11
            {0, -15, 0, -5, 15, 0, 0, -15, 0, 15, 0},
            // Frame 12
            {-5, -20, 0, 0, 20, 0, 5, -20, -5, 20, 0},
            // Frame 13
            {-10, -15, 0, 5, 15, 0, 10, -15, -10, 15, 0},
            // Frame 14
            {-15, -10, 0, 10, 10, 0, 15, -10, -15, 10, 0},
            // Frame 15 (Contact Phase - Right foot forward, Left foot back - repeating)
            {-20, -5, 0, 15, 5, 0, 20, -5, -20, 5, 0},
            // Frame 16
            {-15, 0, 0, 10, 0, 0, 15, 0, -15, 0, 0},
            // Frame 17
            {-10, 5, 0, 5, -5, 0, 10, 5, -10, -5, 0},
            // Frame 18
            {-5, 10, 0, 0, -10, 0, 5, 10, -5, -10, 0},
            // Frame 19
            {0, 15, 0, -5, -15, 0, 0, 15, 0, -15, 0}
    };
    private static final int WALK_CYCLE_LENGTH = WALK_CYCLE_ANGLES.length;


    // --- Character parts (references for easy access) ---
    private bodySegment head;
    private bodySegment upperArmL, lowerArmL, handL;
    private bodySegment upperArmR, lowerArmR, handR;
    private bodySegment upperLegL, lowerLegL, footL;
    private bodySegment upperLegR, lowerLegR, footR;

    private int characterX = 100; // X position of the character's root
    private final int characterY = 250; // Y position of the character's root (standing on ground)
    private final int WALK_SPEED = 2; // Pixels per frame

    public AnimationPanel2() {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
        setupCharacter();
        new Thread(this).start(); // Start the animation thread
    }

    /**
     * Sets up the hierarchical structure of the human stick figure.
     * Each segment is defined by its local vertices, pivot point (relative to parent),
     * and length.
     */
    private void setupCharacter() {
        // Define common bone shape (a line from 0,0 to length,0 in local space)
        // We'll scale this by the segment's length when drawing.
        List<Point> boneLine = Arrays.asList(new Point(0, 0), new Point(1, 0));

        // --- Torso (Root) ---
        // Torso is the base. Its local pivot is (0,0). Its shape is a vertical line.
        List<Point> torsoShape = Arrays.asList(new Point(0, -40), new Point(0, 40)); // A body "spine"
        torso = new bodySegment("Torso", torsoShape, new Point(0,0), 80); // Length for conceptual size

        // --- Head ---
        // Head connects to the top of the torso. Torso's top is at (0, -40) in its local space.
        List<Point> headShape = Arrays.asList(new Point(0,0), new Point(0, -30)); // Head as a vertical line
        head = new bodySegment("Head", headShape, new Point(0,0), 30); // Local pivot (0,0) for head itself
        head.parent = torso;
        torso.children.add(head);
        head.localPivot = new Point(0, -40); // Head connects at torso's top point

        // --- Arms (Left and Right) ---
        // Upper Arm: Connects to shoulders (relative to torso's local space)
        List<Point> upperArmShape = Arrays.asList(new Point(0, 0), new Point(40, 0));
        upperArmL = new bodySegment("UpperArmL", upperArmShape, new Point(-20, -20), 40); // Left shoulder
        upperArmR = new bodySegment("UpperArmR", upperArmShape, new Point(20, -20), 40);  // Right shoulder

        upperArmL.parent = torso;
        upperArmR.parent = torso;
        torso.children.add(upperArmL);
        torso.children.add(upperArmR);

        // Lower Arm: Connects at the end of the upper arm (elbow)
        List<Point> lowerArmShape = Arrays.asList(new Point(0, 0), new Point(35, 0));
        lowerArmL = new bodySegment("LowerArmL", lowerArmShape, new Point((int) upperArmL.length, 0), 35);
        lowerArmR = new bodySegment("LowerArmR", lowerArmShape, new Point((int) upperArmR.length, 0), 35);

        lowerArmL.parent = upperArmL;
        lowerArmR.parent = upperArmR;
        upperArmL.children.add(lowerArmL);
        upperArmR.children.add(lowerArmR);

        // Hand: Connects at the end of the lower arm (wrist)
        List<Point> handShape = Arrays.asList(new Point(0, 0), new Point(20, 0));
        handL = new bodySegment("HandL", handShape, new Point((int) lowerArmL.length, 0), 20);
        handR = new bodySegment("HandR", handShape, new Point((int) lowerArmR.length, 0), 20);

        handL.parent = lowerArmL;
        handR.parent = lowerArmR;
        lowerArmL.children.add(handL);
        lowerArmR.children.add(handR);

        // --- Legs (Left and Right) ---
        // Upper Leg: Connects to hips (relative to torso's local space)
        List<Point> upperLegShape = Arrays.asList(new Point(0, 0), new Point(50, 0));
        upperLegL = new bodySegment("UpperLegL", upperLegShape, new Point(-15, 40), 50); // Left hip
        upperLegR = new bodySegment("UpperLegR", upperLegShape, new Point(15, 40), 50); // Right hip

        upperLegL.parent = torso;
        upperLegR.parent = torso;
        torso.children.add(upperLegL);
        torso.children.add(upperLegR);

        // Lower Leg: Connects at the end of the upper leg (knee)
        List<Point> lowerLegShape = Arrays.asList(new Point(0, 0), new Point(45, 0));
        lowerLegL = new bodySegment("LowerLegL", lowerLegShape, new Point((int) upperLegL.length, 0), 45);
        lowerLegR = new bodySegment("LowerLegR", lowerLegShape, new Point((int) upperLegR.length, 0), 45);

        lowerLegL.parent = upperLegL;
        lowerLegR.parent = upperLegR;
        upperLegL.children.add(lowerLegL);
        upperLegR.children.add(lowerLegR);

        // Foot: Connects at the end of the lower leg (ankle)
        List<Point> footShape = Arrays.asList(new Point(0, 0), new Point(30, 0));
        footL = new bodySegment("FootL", footShape, new Point((int) lowerLegL.length, 0), 30);
        footR = new bodySegment("FootR", footShape, new Point((int) lowerLegR.length, 0), 30);

        footL.parent = lowerLegL;
        footR.parent = lowerLegR;
        lowerLegL.children.add(footL);
        lowerLegR.children.add(footR);

        // Initialize world transforms for all segments
        torso.updateWorldTransform();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(3)); // Make lines thicker

        // Translate the entire character based on its global walking position
        // This moves the character across the screen.
        g2d.translate(characterX, characterY);

        // Draw the entire hierarchy starting from the root (torso)
        drawSegment(g2d, torso);

        // Translate back after drawing (important if you draw other things not part of the character)
        g2d.translate(-characterX, -characterY);
    }

    /**
     * Helper method to draw a single segment (bone) and its joint.
     * @param g2d The Graphics2D object for drawing.
     * @param segment The BodySegment to draw.
     */
    private void drawSegment(Graphics2D g2d, bodySegment segment) {
        // Calculate the actual transformed start and end points of the "bone"
        // The segment's local vertices are (0,0) and (length,0) - so we take those
        Point start = new Point((int) segment.worldTransform.transformPoint(0, 0)[0],
                (int) segment.worldTransform.transformPoint(0, 0)[1]);
        Point end = new Point((int) segment.worldTransform.transformPoint(segment.length, 0)[0],
                (int) segment.worldTransform.transformPoint(segment.length, 0)[1]);

        g2d.setColor(Color.BLUE); // Draw bones in blue
        g2d.drawLine(start.x, start.y, end.x, end.y);

        // Draw a red circle at the joint (where this segment connects to its parent)
        // The joint for a child segment is the transformed localPivot of the child, relative to its parent.
        if (segment.parent != null) {
            // Get the world position of this segment's pivot point
            double[] jointWorldPosArray = segment.parent.worldTransform.transformPoint(
                    segment.localPivot.getX(), segment.localPivot.getY());
            Point jointWorldPos = new Point((int) jointWorldPosArray[0], (int) jointWorldPosArray[1]);

            g2d.setColor(Color.RED); // Draw joints in red
            g2d.fillOval(jointWorldPos.x - 5, jointWorldPos.y - 5, 10, 10); // Draw joint as circle
        } else {
            // For the torso (root), draw its base pivot (or center)
            Point torsoOrigin = new Point((int) segment.worldTransform.transformPoint(0,0)[0],
                    (int) segment.worldTransform.transformPoint(0,0)[1]);
            g2d.setColor(Color.RED);
            g2d.fillOval(torsoOrigin.x - 5, torsoOrigin.y - 5, 10, 10);
        }

        // Recursively draw children
        for (bodySegment child : segment.children) {
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

                // Determine current frame in the walk cycle
                int currentWalkFrame = frameCount % WALK_CYCLE_LENGTH;
                double[] angles = WALK_CYCLE_ANGLES[currentWalkFrame];

                // Apply angles from the keyframe data to the respective segments
                // Order: [hip_L, knee_L, ankle_L, hip_R, knee_R, ankle_R, shoulder_L, elbow_L, shoulder_R, elbow_R, head_nod]
                upperLegL.setAngle(angles[0]);
                lowerLegL.setAngle(angles[1]);
                footL.setAngle(angles[2]);

                upperLegR.setAngle(angles[3]);
                lowerLegR.setAngle(angles[4]);
                footR.setAngle(angles[5]);

                upperArmL.setAngle(angles[6]);
                lowerArmL.setAngle(angles[7]);
                upperArmR.setAngle(angles[8]);
                lowerArmR.setAngle(angles[9]);

                // Head nod for a bit of realism
                head.setAngle(angles[10]);

                // Simple forward movement for the entire character
                characterX += WALK_SPEED;
                // Optional: If character goes off screen, reset its position
                if (characterX > getWidth() + 200) {
                    characterX = -200;
                }

                // Add a slight vertical bobbing to the torso for a more natural walk
                double bobbing = 5 * Math.sin(frameCount * (2 * Math.PI / ((double) WALK_CYCLE_LENGTH / 2))); // Bob up/down twice per cycle
                // Apply a small lean to the torso as well
                torso.setAngle(bobbing * 0.1);

                // Update all transformation matrices for the new pose
                torso.updateWorldTransform(); // This call recursively updates all child segments

                // Request a repaint of the panel
                repaint();
                frameCount++; // Increment frame counter for the next loop iteration
            }

            try {
                // Sleep for the remainder of the frame time to maintain desired FPS
                Thread.sleep(Math.max(0, FRAME_TIME_MILLIS - (System.currentTimeMillis() - now)));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status if interrupted
                System.err.println("Animation thread interrupted: " + e.getMessage());
                break; // Exit the animation loop
            }
        }
    }

    public static void main(String[] args) {
        // Ensure Swing UI updates are done on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("2D Walking Animation (Java Matrices)");
            AnimationPanel2 panel = new AnimationPanel2();
            frame.add(panel);
            frame.pack(); // Sizes the frame to fit the preferred size of the panel
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation
            frame.setLocationRelativeTo(null); // Center the frame on the screen
            frame.setVisible(true); // Make the frame visible
        });
    }
}