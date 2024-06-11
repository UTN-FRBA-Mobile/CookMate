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
                new Recipe("Tarta de Manzana", new Step[]{
                        new Step(1, "Precalentar el horno a 180 grados Celsius.", new Ingredient[]{}),
                        new Step(2, "Preparar la masa.", new Ingredient[]{
                                new Ingredient("Harina", 200),
                                new Ingredient("Mantequilla", 100),
                                new Ingredient("Agua", 50)
                        }),
                        new Step(3, "Cortar las manzanas y colocarlas sobre la masa.", new Ingredient[]{
                                new Ingredient("Manzanas", 3),
                                new Ingredient("Azúcar", 50),
                                new Ingredient("Canela", 5)
                        }),
                        new Step(4, "Hornear durante 30 minutos.", new Ingredient[]{})
                }),
                new Recipe("Pizza Margarita", new Step[]{
                        new Step(1, "Precalentar el horno a 220 grados Celsius.", new Ingredient[]{}),
                        new Step(2, "Preparar la masa de pizza.", new Ingredient[]{
                                new Ingredient("Harina", 250),
                                new Ingredient("Levadura", 5),
                                new Ingredient("Agua", 150),
                                new Ingredient("Sal", 5)
                        }),
                        new Step(3, "Añadir salsa de tomate y mozzarella.", new Ingredient[]{
                                new Ingredient("Salsa de tomate", 100),
                                new Ingredient("Mozzarella", 200)
                        }),
                        new Step(4, "Hornear durante 15 minutos.", new Ingredient[]{})
                })
        };
        return new GsonBuilder().setPrettyPrinting().create().toJson(defaultRecipes);
    }

    private static String getDefaultUsersJson() {
        User[] defaultUsers = new User[]{
                new User("Juan Perez", "juan.perez@example.com", "123456", new Recipe[]{
                        new Recipe("Tarta de Manzana", new Step[]{
                                new Step(1, "Precalentar el horno a 180 grados Celsius.", new Ingredient[]{}),
                                new Step(2, "Preparar la masa.", new Ingredient[]{
                                        new Ingredient("Harina", 200),
                                        new Ingredient("Mantequilla", 100),
                                        new Ingredient("Agua", 50)
                                }),
                                new Step(3, "Cortar las manzanas y colocarlas sobre la masa.", new Ingredient[]{
                                        new Ingredient("Manzanas", 3),
                                        new Ingredient("Azúcar", 50),
                                        new Ingredient("Canela", 5)
                                }),
                                new Step(4, "Hornear durante 30 minutos.", new Ingredient[]{})
                        })
                }),
                new User("Maria Lopez", "maria.lopez@example.com", "abcdef", new Recipe[]{
                        new Recipe("Pizza Margarita", new Step[]{
                                new Step(1, "Precalentar el horno a 220 grados Celsius.", new Ingredient[]{}),
                                new Step(2, "Preparar la masa de pizza.", new Ingredient[]{
                                        new Ingredient("Harina", 250),
                                        new Ingredient("Levadura", 5),
                                        new Ingredient("Agua", 150),
                                        new Ingredient("Sal", 5)
                                }),
                                new Step(3, "Añadir salsa de tomate y mozzarella.", new Ingredient[]{
                                        new Ingredient("Salsa de tomate", 100),
                                        new Ingredient("Mozzarella", 200)
                                }),
                                new Step(4, "Hornear durante 15 minutos.", new Ingredient[]{})
                        })
                })
        };
        return new GsonBuilder().setPrettyPrinting().create().toJson(defaultUsers);
    }

}
