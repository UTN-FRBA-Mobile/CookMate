package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.GsonBuilder;
import model.entity.Ingredient;
import model.entity.Recipe;
import model.entity.Step;
import model.entity.User;

public class DataInitializer {

    private static final String RECIPES_FILE_PATH = "recetas.json";
    private static final String USERS_FILE_PATH = "usuarios.json";

    public static void main(String[] args) {
        initializeDataFiles();
    }

    public static void initializeDataFiles() {
        createFileIfNotExists(RECIPES_FILE_PATH, getDefaultRecipesJson());
        createFileIfNotExists(USERS_FILE_PATH, getDefaultUsersJson());
    }

    private static void createFileIfNotExists(String filePath, String defaultContent) {
        File file = new File(filePath);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(defaultContent);
                System.out.println(filePath + " created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(filePath + " already exists.");
        }
    }

    private static String getDefaultRecipesJson() {
        Recipe[] defaultRecipes = new Recipe[]{
                new Recipe("Tarta de Manzana", "tartaManzana", new Step[]{
                        new Step(1, "Precalentar el horno a 180 grados Celsius.", "horno", new Ingredient[]{}),
                        new Step(2, "Preparar la masa.", "preparadoMasa", new Ingredient[]{
                                new Ingredient("Harina", 200, "harina"),
                                new Ingredient("Mantequilla", 100, "mantequilla"),
                                new Ingredient("Agua", 50, "agua")
                        }),
                        new Step(3, "Cortar las manzanas y colocarlas sobre la masa.", "cortadoManzanas", new Ingredient[]{
                                new Ingredient("Manzanas", 3, "manzanas"),
                                new Ingredient("Azúcar", 50, "azucar"),
                                new Ingredient("Canela", 5, "canela")
                        }),
                        new Step(4, "Hornear durante 30 minutos.", "horno", new Ingredient[]{})
                }),
                new Recipe("Pizza Margarita", "pizzaMargarita", new Step[]{
                        new Step(1, "Precalentar el horno a 220 grados Celsius.", "horno", new Ingredient[]{}),
                        new Step(2, "Preparar la masa de pizza.", "preparadoMasa", new Ingredient[]{
                                new Ingredient("Harina", 250, "harina"),
                                new Ingredient("Levadura", 5, "levadura"),
                                new Ingredient("Agua", 150, "agua"),
                                new Ingredient("Sal", 5, "sal")
                        }),
                        new Step(3, "Añadir salsa de tomate y mozzarella.", "agregadoSalsa", new Ingredient[]{
                                new Ingredient("Salsa de tomate", 100, "salsaTomate"),
                                new Ingredient("Mozzarella", 200, "mozzarella")
                        }),
                        new Step(4, "Hornear durante 15 minutos.", "horno", new Ingredient[]{})
                })
        };
        return new GsonBuilder().setPrettyPrinting().create().toJson(new RecipesWrapper(defaultRecipes));
    }

    private static String getDefaultUsersJson() {
        User[] defaultUsers = new User[]{
                new User("Juan Perez", "juan.perez@example.com", "123456", new String[]{
                        "Tarta de Manzana"
                }),
                new User("Maria Lopez", "maria.lopez@example.com", "abcdef", new String[]{
                        "Pizza Margarita"
                })
        };
        return new GsonBuilder().setPrettyPrinting().create().toJson(new UsersWrapper(defaultUsers));
    }

    static class RecipesWrapper {
        private Recipe[] recetas;

        RecipesWrapper(Recipe[] recetas) {
            this.recetas = recetas;
        }

        public Recipe[] getRecetas() {
            return recetas;
        }
    }

    static class UsersWrapper {
        private User[] usuarios;

        UsersWrapper(User[] usuarios) {
            this.usuarios = usuarios;
        }

        public User[] getUsuarios() {
            return usuarios;
        }
    }

}
