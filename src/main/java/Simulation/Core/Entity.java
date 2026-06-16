package Simulation.Core;
import org.joml.Vector3f;
import org.joml.Vector3f.*;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    Vector3f scale;
    Vector3f Rotation;
    Mesh mesh;
    Vector3f Translation;
    float hasTexture;

    public Entity(Vector3f scale, Vector3f   translation, Vector3f rotation , Mesh mesh, float hasTexture){
        this.mesh = mesh;
        this.Translation = translation;
        this.Rotation = rotation;
        this.scale = scale;
        this.hasTexture = hasTexture;
    }
    public Entity(Entity other) {
        this.mesh = other.mesh; // share the 3D model/mesh data (saves memory)
        this.Translation = new Vector3f(other.Translation); // separate position!
        this.Rotation = new Vector3f(other.Rotation);
        this.scale = other.scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public float hasTexture() {
        return hasTexture;
    }

    public void setRotation(Vector3f rotation) {
        Rotation = rotation;
    }

    public Vector3f getRotation() {
        return Rotation;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3f getScale() {
        return scale;
    }

    public Vector3f getTranslation() {
        return Translation;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public void setTranslation(Vector3f translation) {
        Translation = translation;
    }
}
