package engineTester;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import models.RawModel;
import models.TexturedModel;
import networking.copy.NetworkingManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import terrains.Terrain;
import textures.ModelTexture;
import toolbox.Maths;
import toolbox.MousePicker;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;

import java.io.IOException;
import java.lang.Thread;
import java.net.UnknownHostException;
public class MainGameLoop {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		NetworkingManager nn = new NetworkingManager();
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		RawModel modelc = OBJLoader.loadObjModel("stall", loader); 
		TexturedModel staticModelc = new TexturedModel(modelc,new ModelTexture(loader.loadTexture("texture")));
		
	//	RawModel tree = OBJLoader.loadObjModel("tree", loader); 
	//	TexturedModel trees = new TexturedModel(tree,new ModelTexture(loader.loadTexture("tree")));
	//	Entity treee =  new Entity(trees, new Vector3f(200,0,150),0,0,0,10);
		
		RawModel modelb = OBJLoader.loadObjModel("bunny", loader);
		TexturedModel staticModelb = new TexturedModel(modelb,new ModelTexture(loader.loadTexture("gray")));
		TexturedModel staticModelb2 = new TexturedModel(modelb,new ModelTexture(loader.loadTexture("red")));
		
		RawModel modeld = OBJLoader.loadObjModel("dra", loader);
		TexturedModel staticModeld = new TexturedModel(modeld,new ModelTexture(loader.loadTexture("orange")));

		
		RawModel modelt = OBJLoader.loadObjModel("cherry", loader);
		TexturedModel staticModelt = new TexturedModel(modelt,new ModelTexture(loader.loadTexture("cherry")));
		
		RawModel modelp = OBJLoader.loadObjModel("pine", loader);
		TexturedModel staticModelp = new TexturedModel(modelp,new ModelTexture(loader.loadTexture("pine")));
		
		RawModel modelf = OBJLoader.loadObjModel("fern", loader);
		TexturedModel staticModelf = new TexturedModel(modelf,new ModelTexture(loader.loadTexture("fern")));
		
		staticModelt.getTexture().setHasTransparency(true);
		staticModelt.getTexture().setUseFakeLighting(true);
		staticModelp.getTexture().setHasTransparency(true);
		staticModelp.getTexture().setUseFakeLighting(true);
		staticModelf.getTexture().setHasTransparency(true);
		staticModelf.getTexture().setUseFakeLighting(true);
		
		
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(new Entity(staticModelt, new Vector3f(50,0,200),0,0,0,8));
		entities.add(new Entity(staticModelf, new Vector3f(55,0,160),0,0,0,5));
		entities.add(new Entity(staticModelt, new Vector3f(50,0,117),0,0,0,8));
		entities.add(new Entity(staticModelf, new Vector3f(60,0,80),0,0,0,4));
		entities.add(new Entity(staticModelp, new Vector3f(60,0,50),0,0,0,5));
		entities.add(new Entity(staticModelp, new Vector3f(50,0,230),0,0,0,4));//
		entities.add(new Entity(staticModelf, new Vector3f(80,0,230),0,0,0,6));//
		entities.add(new Entity(staticModelt, new Vector3f(120,0,250),0,0,0,10));
		entities.add(new Entity(staticModelf, new Vector3f(160,0,230),0,0,0,4));
		entities.add(new Entity(staticModelp, new Vector3f(185,0,230),0,0,0,6));
		entities.add(new Entity(staticModelf, new Vector3f(220,0,230),0,0,0,4));
		entities.add(new Entity(staticModelt, new Vector3f(235,0,200),0,0,0,8));
		entities.add(new Entity(staticModelf, new Vector3f(235,0,160),0,0,0,5));
		entities.add(new Entity(staticModelp, new Vector3f(240,0,110),0,0,0,6));
		entities.add(new Entity(staticModelf, new Vector3f(235,0,70),0,0,0,3));
		entities.add(new Entity(staticModelb, new Vector3f(30,0,30),0,0,0,2));
		entities.add(new Entity(staticModelb2, new Vector3f(250,0,30),0,0,0,2));
		entities.add(new Entity(staticModeld, new Vector3f(30,0,250),0,0,0,3));
		entities.add(new Entity(staticModelc, new Vector3f(150,0,150),0,0,0,13));
	

		
		RawModel model = OBJLoader.loadObjModel("person", loader); 
		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("playerTexture")));
		TexturedModel staticModel2 = new TexturedModel(model,new ModelTexture(loader.loadTexture("playerTexture2")));
		Player player =  new Player(staticModel, new Vector3f(10,-60,270),0,0,0,2); //2 represents the size of entity 80,0,70),0,0,0,5
		Entity rival =  new Entity(staticModel2, new Vector3f(30,-60,310),0,0,0,2); //2 represents the size of entity 80,0,70),0,0,0,5
		
		
		
		
		
		Light light = new Light(new Vector3f(3000,2000,2000),new Vector3f(1,1,1));
		
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		GuiTexture guii = new GuiTexture(loader.loadTexture("intructions"), new Vector2f(0.7f,-1f), new Vector2f(0.3f, 0.5f));
		GuiTexture gui = new GuiTexture(loader.loadTexture("name"), new Vector2f(0.95f,0.55f), new Vector2f(0.3f, 0.5f));
		guis.add(gui);
		guis.add(guii);
		
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		
		Terrain terrain = new Terrain(0,0,loader,new ModelTexture(loader.loadTexture("finalmon")));
		
		
		boolean showCard = false; //
		boolean showWelcome = false;
		
		Camera camera = new Camera(player);	
		MasterRenderer renderer = new MasterRenderer(loader);
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
		
		List<GuiTexture> welcome = new ArrayList<GuiTexture>();
		GuiTexture welcomes = new GuiTexture(loader.loadTexture("welcome"), new Vector2f(0f,0f), new Vector2f(1.0f, 1.0f));
		welcome.add(welcomes);
		
		RawModel tree = OBJLoader.loadObjModel("tree", loader); 
		TexturedModel trees = new TexturedModel(tree,new ModelTexture(loader.loadTexture("tree")));
		List<Entity> TreeEntities = new ArrayList<Entity>();
		
		while(!Keyboard.isKeyDown(Keyboard.KEY_M)){
			
			guiRenderer.render(welcome);
			DisplayManager.updateDisplay();
		}
			guis.clear();
			guiRenderer.render(guis);
			
			while(!Display.isCloseRequested()) {
			camera.move();
			player.move();
			//player2.move();
		
			Vector3f[] arr= nn.LocationExchange(player.getPosition(), player.getRotation());
			rival.setPosition(arr[0]);
			rival.setRotation(arr[1]);
			
			
		
			if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
					TreeEntities.add( new Entity(trees, player.getPosition(),0,0,0,19));/////
			}
				for (Entity entity:TreeEntities) {  
					renderer.processEntity(entity);
				}
			
			
			
			
		
			for (Entity entity:entities) {
				renderer.processEntity(entity);
			}
			renderer.processEntity(player);
			renderer.processEntity(rival);
			renderer.processTerrain(terrain);
			renderer.render(light, camera);
			guiRenderer.render(guis);
			
			if (Keyboard.isKeyDown(Keyboard.KEY_I)) { 
					List<GuiTexture> info = new ArrayList<GuiTexture>();
					GuiTexture infos = new GuiTexture(loader.loadTexture("info"), new Vector2f(0f,0f), new Vector2f(1.0f, 1.0f));
					info.add(infos);
					guiRenderer.render(info);
					}
		
			
			int i= Maths.getRandomNumberInRange(1, 11);
			while (Keyboard.isKeyDown(Keyboard.KEY_C)) { // 
				List<GuiTexture> guisChance = new ArrayList<GuiTexture>();
				GuiTexture chance = new GuiTexture(loader.loadTexture(Integer.toString(i)), new Vector2f(0.0f,0.0f), new Vector2f(0.7f, 0.7f));
				guisChance.add(chance);
				GuiRenderer guiRendererChance = new GuiRenderer(loader);
				guiRendererChance.render(guisChance);	
				DisplayManager.updateDisplay();
			}guis.clear();
			guiRenderer.render(guis);

	
			int j=Maths.getRandomNumberInRange(12, 21);
			while (Keyboard.isKeyDown(Keyboard.KEY_T)) { // 
				List<GuiTexture> guisChance = new ArrayList<GuiTexture>();
				GuiTexture chance = new GuiTexture(loader.loadTexture(Integer.toString(j)), new Vector2f(0.0f,0.0f), new Vector2f(0.7f, 0.7f));
				guisChance.add(chance);
				GuiRenderer guiRendererChance = new GuiRenderer(loader);
				guiRendererChance.render(guisChance);	
				DisplayManager.updateDisplay();
			}guis.clear();
			guiRenderer.render(guis);
			
			int r=Maths.getRandomNumberInRange(22, 27);
			while (Keyboard.isKeyDown(Keyboard.KEY_R)) { // 
				List<GuiTexture> guisChance = new ArrayList<GuiTexture>();
				GuiTexture chance = new GuiTexture(loader.loadTexture(Integer.toString(r)), new Vector2f(0.0f,0.3f), new Vector2f(0.3f, 0.5f));
				guisChance.add(chance);
				GuiRenderer guiRendererChance = new GuiRenderer(loader);
				guiRendererChance.render(guisChance);	
				DisplayManager.updateDisplay();
			}guis.clear();
			guiRenderer.render(guis);
					
			DisplayManager.updateDisplay();
		}

		guiRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();

	}

}