package VectorAnimation;// BodySegment.java
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class BodySegment {
    public String name;
    public List<Point> localVertices; // Points defining the shape of the segment in its local space
    public Point localPivot;          // The point in PARENT'S local space where this segment connects
    public double length;             // Length of the segment (for positioning children)
    public BodySegment parent;
    public List<BodySegment> children;

    public double currentAngleDegrees; // Current rotation angle in degrees
    public Matrix34 worldTransform;     // This segment's current world transformation matrix

    public BodySegment(String name, List<Point> localVertices, Point localPivot, double length) {
        this.name = name;
        this.localVertices = localVertices;
        this.localPivot = localPivot;
        this.length = length;
        this.children = new ArrayList<>();
        this.worldTransform = new Matrix34(); // Initialize to identity
    }

    public void setAngle(double angleDegrees) {
        this.currentAngleDegrees = angleDegrees;
    }

    /**
     * Calculates the world transformation matrix for this segment and recursively for its children.
     * The order of transformations is crucial for hierarchical animation.
     * It applies the parent's world transform, then translates to this segment's pivot point
     * (relative to the parent), then applies this segment's local rotation.
     */
    public void updateWorldTransform() {
        // Step 1: Create the local transformation matrix for this segment.
        // This matrix describes how this segment is positioned and oriented relative to its parent's attachment point.

        // Translate to the local pivot point (where this segment connects to its parent)
        Matrix34 translationToPivot = Matrix34.createTranslation(localPivot.getX(), localPivot.getY());

        // Rotate this segment around its own origin (which is implicitly at the pivot after translation)
        Matrix34 localRotation = Matrix34.createRotation(Math.toRadians(currentAngleDegrees));

        // Combine the local transformations: first translate to pivot, then rotate.
        // This order means the rotation happens around the pivot point.
        Matrix34 localTransform = translationToPivot.multiply(localRotation);

        if (parent == null) { // This is the root segment (e.g., Torso)
            // The root's world transform is its own local transform (plus any global character position,
            // which is handled by g2d.translate in AnimationPanel for this example).
            this.worldTransform = localTransform;
        } else {
            // A child segment's world transform is its parent's world transform
            // multiplied by its own local transformation.
            this.worldTransform = parent.worldTransform.multiply(localTransform);
        }

        // Recursively update children
        for (BodySegment child : children) {
            child.updateWorldTransform();
        }
    }

    /**
     * Gets the vertices of this segment transformed into world coordinates.
     * @return A list of Point objects representing the transformed vertices.
     */
    public List<Point> getTransformedVertices() {
        List<Point> transformed = new ArrayList<>();
        for (Point p : localVertices) {
            // Apply the segment's world matrix to its local vertices
            double[] worldCoords = worldTransform.transformPoint(p.getX(), p.getY());
            transformed.add(new Point((int) worldCoords[0], (int) worldCoords[1]));
        }
        return transformed;
    }
}