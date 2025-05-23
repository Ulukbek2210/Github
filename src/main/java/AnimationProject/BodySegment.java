package AnimationProject;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class BodySegment {
    public String name;
    public List<Point> localVertices; // Points defining the shape of the segment in its local space
    public Point localPivot;          // The point in local space around which it rotates (e.g., shoulder, elbow)
    public double length;             // Length of the segment (for positioning children)
    public BodySegment parent;
    public List<BodySegment> children;

    public double currentAngleDegrees; // Current rotation angle in degrees
    public Matrix3 worldTransform;     // This segment's current world transformation matrix

    public BodySegment(String name, List<Point> localVertices, Point localPivot, double length) {
        this.name = name;
        this.localVertices = localVertices;
        this.localPivot = localPivot;
        this.length = length;
        this.children = new ArrayList<>();
        this.worldTransform = new Matrix3(); // Initialize to identity
    }

    public void setAngle(double angleDegrees) {
        this.currentAngleDegrees = angleDegrees;
    }

    // This method calculates the world transformation matrix for this segment
    // and recursively for its children.
    public void updateWorldTransform() {
        // Step 1: Create local transformation matrix for this segment
        // This involves translating to the local pivot, rotating, then translating back
        // For a bone, it's often more intuitive:
        // 1. Translate such that the pivot is at the origin (if not already)
        // 2. Rotate by currentAngleDegrees
        // 3. Translate back to the original pivot position
        // 4. (Implicitly) Translate by its length for its attachment point to children

        // A simpler way for a bone starting at (0,0) and extending along X:
        // Local transformation: Rotate around (0,0)
        Matrix3 localRotation = Matrix3.createRotation(Math.toRadians(currentAngleDegrees));

        // This is the relative position of the child's *pivot* point to the parent's *end* point.
        // Assuming parent bone extends along X-axis from (0,0) to (length,0)
        Matrix3 parentToChildTranslation = Matrix3.createTranslation(this.length, 0); // Connect at parent's end

        if (parent == null) { // This is the root segment (e.g., Torso)
            // The root's world transform is its own rotation and initial world position
            // For a character, this would be the character's global position and orientation
            Matrix3 rootInitialTranslation = Matrix3.createTranslation(200, 300); // Example world position
            this.worldTransform = rootInitialTranslation.multiply(localRotation);
        } else {
            // Apply parent's world transform, then the translation to this segment's pivot,
            // then this segment's local rotation.
            // Order: ParentWorldMatrix * (TranslationToChildPivot * ChildLocalRotation)
            this.worldTransform = parent.worldTransform
                    .multiply(parentToChildTranslation)
                    .multiply(localRotation);
        }

        // Recursively update children
        for (BodySegment child : children) {
            child.updateWorldTransform();
        }
    }

    // Get the vertices of this segment transformed into world coordinates
    public List<Point> getTransformedVertices() {
        List<Point> transformed = new ArrayList<>();
        for (Point p : localVertices) {
            double[] worldCoords = worldTransform.transformPoint(p.getX(), p.getY());
            transformed.add(new Point((int) worldCoords[0], (int) worldCoords[1]));
        }
        return transformed;
    }
}