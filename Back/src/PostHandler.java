package application;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostHandler implements HttpHandler {

    private static final int _port = 9099;
    private static final int _maxConnections = 10;
    
    private final Map<String,String> _imagenes;
    private final Map<String,Recipe> _recipes;
    private final List<String> _allIngredients;
    private final Map<String,User> _users;
    
    public static void main(final String[] args) throws Exception{
        final HttpServer server = HttpServer.create(new InetSocketAddress(_port), _maxConnections);
        final PostHandler postHandler = new PostHandler();
        server.createContext("/", postHandler);
        server.start();
        System.out.println("Escuchando conexiones en puerto " + _port);
    }
    
    private PostHandler() throws IOException{
        _imagenes = readImagenesFile();
        _allIngredients = new ArrayList<>();
        _recipes = readRecipesFile();
        _users = readUsersFile();
    }
    
    private String removeExtensionFromFilename(final String filename){
        final int index = filename.lastIndexOf('.');
        if(index > -1){
            return filename.substring(0,index);
        }
        return filename;
    }
    
    private Map readImagenesFile() throws IOException{
        final Map map = new HashMap<String,String>();
        final File imagenesFolder = new File("imagenes");
        for(final File file : imagenesFolder.listFiles()){
            final String base64 = Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
            map.put(removeExtensionFromFilename(file.getName()), base64);
        }
//        final String fileContent = readFile("imagenes.json");
//        final JsonArray imagesFile = JsonParser.parseString(fileContent).getAsJsonArray();
//        for(final JsonElement je : imagesFile){
//            final JsonObject jo = je.getAsJsonObject();
//            final String id = jo.get("id").getAsString();
//            final String base64 = jo.get("data").getAsString();
//            map.put(id, base64);
//        }    
        return map;
    }
    
    private Map readRecipesFile(){
        final Map map = new HashMap<String,Recipe>();
        final String fileContent = readFile("recetas.json");
        final JsonArray recipesFile = JsonParser.parseString(fileContent).getAsJsonArray();
        for(final JsonElement je : recipesFile){
            final JsonObject jo = je.getAsJsonObject();
            final String nombreReceta = jo.get("nombre").getAsString();
            final String imagenReceta = jo.get("imagen").getAsString();
            final JsonArray pasos = jo.get("pasos").getAsJsonArray();
            final List<Step> steps = new ArrayList<>();
            for(int i=0; i<pasos.size(); i++){
                final JsonObject step = pasos.get(i).getAsJsonObject();
                final int numero = step.get("numero").getAsInt();
                final String descripcion = step.get("descripcion").getAsString();
                final String imagen = step.get("imagen").getAsString();
                final JsonArray ingredientes = step.get("ingredientes").getAsJsonArray();
                final List<Ingredient> ingredients = new ArrayList<>();
                for(int j=0; j<ingredientes.size(); j++){
                    final JsonObject ingrediente = ingredientes.get(j).getAsJsonObject();
                    final String nombreIngrediente = ingrediente.get("nombre").getAsString();
                    if(!_allIngredients.contains(nombreIngrediente)){
                        _allIngredients.add(nombreIngrediente);
//                        _allIngredients.add(nombreIngrediente);
                    }
                    final int cantidadIngrediente = ingrediente.get("cantidad").getAsInt();
                    final String imagenIngrediente = ingrediente.get("imagen").getAsString();
                    ingredients.add(new Ingredient(nombreIngrediente, cantidadIngrediente, imagenIngrediente));
                }
                steps.add(new Step(numero,descripcion,imagen,ingredients));
            }
            final Recipe receta = new Recipe(nombreReceta, imagenReceta, steps);
            map.put(nombreReceta, receta);
        }
        Collections.sort(_allIngredients);
        return map;
    }
    
    private Map readUsersFile(){
        final Map map = new HashMap<String,User>();
        final String fileContent = readFile("usuarios.json");
        final JsonArray usersFile = JsonParser.parseString(fileContent).getAsJsonArray();
        for(final JsonElement je : usersFile){
            final JsonObject jo = je.getAsJsonObject();
            final String nombre = jo.get("nombre").getAsString();
            final String email = jo.get("email").getAsString();
            final String clave = jo.get("contraseña").getAsString();
            final List<String> recetas = new ArrayList<>();
            final JsonArray recetasGuardadas = jo.get("recetas").getAsJsonArray();
            for(final JsonElement nombreRecetaJe : recetasGuardadas){
                recetas.add(nombreRecetaJe.getAsString());
            }
            final User user = new User(nombre, email, clave, recetas);
            map.put(email, user);
        }
        return map;
    }
    
    @Override
    public void handle(final HttpExchange t) throws IOException {
        final String responseAsString = processRequest(t, t.getRequestMethod());
        final byte[] bytesToSend = responseAsString.getBytes();
        t.sendResponseHeaders(200, bytesToSend.length);
        final OutputStream os = t.getResponseBody();
        os.write(bytesToSend);
        os.close();
        t.close();
    }
    
    protected String processRequest(final HttpExchange httpExchange, final String method) {
        final String responseAsString;
        if("POST".equals(method)){
            final String bodyAsString = readBody(httpExchange);
            final JsonObject body = JsonParser.parseString(bodyAsString).getAsJsonObject();
            final String action = body.get("action").getAsString();
            switch(action){
                case "downloadResources":
                    responseAsString = downloadResources(body);
                    break;
                case "login":
                    responseAsString = login(body);
                    break;
                case "addRecipeToUser":
                    responseAsString = addRecipeToUser(body);
                    break;
                case "removeRecipeFromUser":
                    responseAsString = removeRecipeFromUser(body);
                    break;
                case "searchRecipes":
                    responseAsString = searchRecipes(body);
                    break;
                case "getAllIngredients":
                    responseAsString = getAllIngredients();
                    break;
                default: 
                    responseAsString = "Bad action";
            }
        } else {
            responseAsString = "Metodo no soportado";
        }
        System.out.println(responseAsString);
        return responseAsString;
    }
    
    protected String readBody(final HttpExchange httpExchange){
        final Headers requestHeaders = httpExchange.getRequestHeaders();
        String charset = "UTF-8";
        if(requestHeaders != null){
            final String contentType = requestHeaders.get("Content-type").get(0);
            final String[] split = contentType.split(";");
            for(final String string : split){
                if(string.trim().contains("charset=")){
                    charset = string.trim().replace("charset=", "");
                    break;
                }
            }
        }
        final InputStream requestBody = httpExchange.getRequestBody();
        return readInputStream(requestBody,charset);
    }
    
    public static String readInputStream(final InputStream is, final String encoding) {
        int read;
        final StringBuilder sb = new StringBuilder();
        try {
            final InputStreamReader isr = new InputStreamReader(is, encoding);
            while ((read = isr.read()) != -1) {
                sb.append((char) read);
            }
            isr.close();
            is.close();
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }
    
    public static String readFile(final String filePath) {
        final StringBuilder sb = new StringBuilder();
        final List<String> lines = readFileLines(new File(filePath));
        for(final String line : lines){
            sb.append(line).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public static List<String> readFileLines(final File file) {
        final List list = new ArrayList<>();
        try {
            final FileInputStream fis = new FileInputStream(file);
            final InputStreamReader isr = new InputStreamReader(fis);
            final BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            isr.close();
            fis.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private String downloadResources(final JsonObject body) {
        final JsonArray ja = new JsonArray();
        for(final Map.Entry<String, String> elem : _imagenes.entrySet()){
            final JsonObject jo = new JsonObject();
            jo.addProperty("nombre", elem.getKey());
            jo.addProperty("base64", elem.getValue());
            ja.add(jo);
        }
        return ja.toString();
    }

    private String login(final JsonObject body) {
        final String email = body.get("email").getAsString();
        final String password = body.get("password").getAsString();
        final User user = _users.get(email);
        if(user != null){
            if(user.getContraseña().equals(password)){
                return jsonize(crearListaRecetasDesdeNombres(user.getRecetas()));
            }
        }
        return "LOGIN FAILED";
    }
    
    private String addRecipeToUser(final JsonObject body) {
        final String email = body.get("email").getAsString();
        final String nombreReceta = body.get("nombreReceta").getAsString();
        final User user = _users.get(email);
        if(user != null){
//            if(user.getContraseña().equals(password)){
                return jsonize(crearListaRecetasDesdeNombres(user.getRecetas()));
//            }
        }
        return "ERROR";
    }
    
    private String removeRecipeFromUser(final JsonObject body) {
        final String email = body.get("email").getAsString();
//        final String password = body.get("password").getAsString();
        final String nombreReceta = body.get("nombreReceta").getAsString();
        final User user = _users.get(email);
        if(user != null){
//            if(user.getContraseña().equals(password)){
                int index = 0;
                for(; index < user.getRecetas().size() ; index++){
                    if(nombreReceta.equals(user.getRecetas().get(index))){
                        user.getRecetas().remove(index);
                        break;
                    }
                }
                return jsonize(crearListaRecetasDesdeNombres(user.getRecetas()));
//            }
        }
        return "ERROR";
    }
    
    private String searchRecipes(final JsonObject body) {
        final JsonArray ingredientesElegidosJa = body.get("ingredientes").getAsJsonArray();
        final List<String> ingredientesElegidos = new ArrayList<>();
        for(final JsonElement je : ingredientesElegidosJa){
            ingredientesElegidos.add(je.getAsString().toLowerCase());
        }
        final List<Recipe> recetasValidas = new ArrayList<>();
        otraReceta:
        for(final Map.Entry<String, Recipe> recipe : _recipes.entrySet()){
            final Recipe receta = recipe.getValue();
            final List<Step> pasos = receta.getPasos();
            for(final Step paso : pasos){
                final List<Ingredient> ingredientes = paso.getIngredientes();
                for(final Ingredient ingredient : ingredientes){
                    if(!ingredientesElegidos.contains(ingredient.getNombre().toLowerCase())){
                        continue otraReceta;
                    }
                }
            }
            recetasValidas.add(receta);
        }
        return jsonize(recetasValidas);
    }
    
    private String getAllIngredients(){
        final StringBuilder sb = new StringBuilder();
        for(final String ingredient : _allIngredients){
            sb.append(",\"").append(ingredient).append("\"");
        }
        return "[" + sb.substring(1) + "]";
    }

    private List<Recipe> crearListaRecetasDesdeNombres(final List<String> nombresRecetas){
        final List<Recipe> list = new ArrayList<>();
        for(final String nombreReceta : nombresRecetas){
            final Recipe receta = _recipes.get(nombreReceta);
            list.add(receta);
        }
        return list;
    }
    
    private String jsonize(final List<Recipe> recetas) {
        final JsonArray ja = new JsonArray();
        for(final Recipe receta : recetas){
            final JsonObject jo = new JsonObject();
            jo.addProperty("nombre", receta.getNombre());
            jo.addProperty("imagen",receta.imagen);
            final JsonArray pasos = new JsonArray();
            for(final Step paso : receta.getPasos()){
                final JsonObject joPaso = new JsonObject();
                joPaso.addProperty("numero",paso.numero);
                joPaso.addProperty("descripcion",paso.descripcion);
                joPaso.addProperty("imagen",paso.imagen);
                final JsonArray ingredientes = new JsonArray();
                for(final Ingredient ingrediente : paso.ingredientes){
                    final JsonObject joIngrediente = new JsonObject();
                    joIngrediente.addProperty("nombre", ingrediente.nombre);
                    joIngrediente.addProperty("cantidad", ingrediente.cantidad);
                    joIngrediente.addProperty("imagen",ingrediente.imagen);
                    ingredientes.add(joIngrediente);
                }
                joPaso.add("ingredientes",ingredientes);
                pasos.add(joPaso);
            }
            jo.add("pasos", pasos);
            ja.add(jo);
        }
        return ja.toString();
    }

    //---------------------
    private class User {
        String nombre;
        String email;
        String contraseña;
        List<String> recetas;

        private User(final String nombre, final String email, final String clave, final List<String> recetas) {
            this.nombre = nombre;
            this.email = email;
            this.contraseña = clave;
            this.recetas = recetas;
        }
        String getContraseña(){return contraseña;}
        List<String> getRecetas(){return recetas;}
    }
    private class Step{
        int numero;
        String descripcion;
        String imagen;
        List<Ingredient> ingredientes;

        private Step(final int numero, final String descripcion, final String imagen, final List<Ingredient> ingredients) {
            this.numero = numero;
            this.descripcion = descripcion;
            this.imagen = imagen;
            this.ingredientes = ingredients;
        }
        private List<Ingredient> getIngredientes() {return ingredientes;}
    }
    private class Recipe{
        String nombre;
        String imagen;
        List<Step> pasos;

        private Recipe(final String nombre, final String imagen, final List<Step> steps) {
            this.nombre = nombre;
            this.imagen = imagen;
            this.pasos = steps;
        }
        String getNombre(){return nombre;}
        private List<Step> getPasos() {return pasos;}
    }
    private class Ingredient implements Comparable{
        String nombre;
        int cantidad;
        String imagen;
        private Ingredient(final String nombreIngrediente, final int cantidadIngrediente, final String imagenIngrediente) {
            nombre = nombreIngrediente;
            cantidad = cantidadIngrediente;
            imagen = imagenIngrediente;
        }
        private String getNombre(){return nombre;}

        @Override
        public int compareTo(final Object o) {
            final Ingredient ing = (Ingredient) o;
            return ing.nombre.compareTo(this.nombre);
        }
    }

}

