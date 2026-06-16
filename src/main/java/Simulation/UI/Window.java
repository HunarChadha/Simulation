package Simulation.UI;
import Simulation.Core.Camera;
import Simulation.UI.Mechanics.ProjectileMotion;
import Simulation.UI.RenderManger;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window {
    int HEIGHT;
    int Width;
    float x_pixels;
    float y_pixels;
    TextHandling handling;
    RenderManger renderMangerUI;
    Simulation.Core.RenderManger RenderSimulation;
    double lastTime = glfwGetTime(); // Get time in seconds
    int frameCount = 0;
    int currentFPS = 0;
    private long window;
    Simulation.Mechanics.ProjectileMotion projectileMotionMec;
    Camera camera;

    public Window() throws Exception{
        init();
        setPixels();
        System.out.println(x_pixels);
        System.out.println(y_pixels);
        setUpSimulation();
    }
    public void setUpSimulation() throws Exception{
        camera = new Camera();
        handling = new TextHandling();
        ProjectileMotion projectileMotionUI = new ProjectileMotion(handling);
        projectileMotionMec = new Simulation.Mechanics.ProjectileMotion(projectileMotionUI);
        renderMangerUI = new RenderManger(x_pixels, y_pixels, Width, HEIGHT, camera, projectileMotionUI, handling);
        RenderSimulation = new Simulation.Core.RenderManger(HEIGHT, Width, projectileMotionUI, projectileMotionMec);
        camera.init(window);
    }
    public void setPixels() {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);
            glfwGetFramebufferSize(window, pWidth, pHeight);
            x_pixels = (float) pWidth.get(0);
            y_pixels = (float) pHeight.get(0);
        }
    }

    public void init(){
        glfwInit();
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        HEIGHT = vidMode.height();
        Width = vidMode.width();
        window = glfwCreateWindow(Width, HEIGHT, "Simulation", 0, 0);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        GL.createCapabilities();
        glfwShowWindow(window);
        GLFWErrorCallback.createPrint(System.err).set();
    }
    public void Loop(){
        glDisable(GL_CULL_FACE);
        glClearColor(0.1211875f, 0.12109375f, 0.1328125f, 1);
        while(!glfwWindowShouldClose(window)){
            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            camera.update(window);
            renderMangerUI.Render(window, projectileMotionMec);
            RenderSimulation.Render(camera, handling, 144);
            frameCount++;
            double currentTime = glfwGetTime();
            if (currentTime - lastTime >= 1.0) {
                currentFPS = frameCount;
                glfwSetWindowTitle(window, "My Game | FPS: " + currentFPS);
                frameCount = 0;
                lastTime = currentTime;
            }

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
        glfwTerminate();
    }
}
