package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.GsonBuilder;

import model.entity.Ingredient;
import model.entity.Recipe;
import model.entity.Step;
import model.entity.User;

public class DataInitializer {

    private static final String RECIPES_FILE_PATH = "recetas.json";
    private static final String USERS_FILE_PATH = "usuarios.json";

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
                                new Ingredient("Manzanas", 3, "manzana"),
                                new Ingredient("Azúcar", 50, "azucar"),
                                new Ingredient("Canela", 5, "canela")
                        }),
                        new Step(4, "Hornear durante 30 minutos.", "horno", new Ingredient[]{}, 30)
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
                        new Step(4, "Hornear durante 15 minutos.", "horno", new Ingredient[]{}, 15)
                }),
                new Recipe("Salchichas", "salchichas", new Step[]{
                        new Step(1, "Hervir agua en una olla.", "ollaAgua", new Ingredient[]{
                                new Ingredient("Agua", 5000, "agua")
                        }),
                        new Step(2, "Volcar dentro un paquete de salchichas.", "salchichasHirviendo", new Ingredient[]{
                                new Ingredient("Salchichas", 300, "salchichas")
                        }),
                        new Step(3, "Hervir por 15 minutos.", "ollaAgua", new Ingredient[]{}, 15)
                }),
                new Recipe("Carne con papas", "carneConPapas", new Step[]{
                        new Step(1, "Precalentar el horno a 220 grados Celsius.", "horno", new Ingredient[]{}),
                        new Step(2, "Cortar la carne y salarla a gusto.", "cortadoCarne", new Ingredient[]{
                                new Ingredient("Carne", 200, "carne"),
                                new Ingredient("Sal", 5, "sal")
                        }),
                        new Step(3, "Cortar las papas.", "cortadoPapasJuliana", new Ingredient[]{
                                new Ingredient("Papas", 3, "papas")
                        }),
                        new Step(4, "Hornear durante 45 minutos.", "horno", new Ingredient[]{}, 45)
                }),
                new Recipe("Filet de merluza", "filetDeMerluza", new Step[]{
                        new Step(1, "Precalentar el horno a 220 grados Celsius.", "horno", new Ingredient[]{}),
                        new Step(2, "Filetear el pescado y salarlo a gusto.", "cortadoPescado", new Ingredient[]{
                                new Ingredient("Merluza", 200, "merluza"),
                                new Ingredient("Sal", 5, "sal")
                        }),
                        new Step(3, "Hornear durante 10 minutos.", "horno", new Ingredient[]{}, 10)
                }),
                new Recipe("Hamburguesa", "hamburguesa", new Step[]{
                        new Step(1, "Precalentar el horno a 220 grados Celsius.", "horno", new Ingredient[]{}),
                        new Step(2, "Cortar a la mitad el pan.", "cortadoPan", new Ingredient[]{
                                new Ingredient("Pan", 100, "pan")
                        }),
                        new Step(3, "Picar la carne y salarla a gusto.", "picadoCarne", new Ingredient[]{
                                new Ingredient("Carne", 200, "carne"),
                                new Ingredient("Sal", 5, "sal")
                        }),
                        new Step(4, "Hornear la carne durante 20 minutos.", "horno", new Ingredient[]{}, 20),
                        new Step(5, "Colocar la carne cocinada dentro del pan.", "hamburguesa", new Ingredient[]{})
                }),
                new Recipe("Papas fritas", "papasFritas", new Step[]{
                        new Step(1, "Precalentar aceite en una sartén a 220 grados Celsius.", "sartenAceite", new Ingredient[]{}),
                        new Step(2, "Pelar las papas.", "peladoPapas", new Ingredient[]{
                                new Ingredient("Papas", 1000, "papas")
                        }),
                        new Step(3, "Cortar las papas en juliana.", "cortadoPapasJuliana", new Ingredient[]{
                                new Ingredient("Papas", 1000, "papas")
                        }),
                        new Step(4, "Freir las papas 5 minutos.", "sartenAceite", new Ingredient[]{}, 5)
                }),
                new Recipe("Fideos con manteca", "fideosConManteca", new Step[]{
                        new Step(1, "Hervir agua en una olla.", "ollaAgua", new Ingredient[]{
                                new Ingredient("Agua", 5000, "agua")
                        }),
                        new Step(2, "Volcar dentro un paquete de fideos.", "fideosHirviendo", new Ingredient[]{
                                new Ingredient("Fideos", 500, "paqueteFideos")
                        }),
                        new Step(3, "Hervir por 15 minutos.", "ollaAgua", new Ingredient[]{}, 15)
                }),
                new Recipe("Ensalada de tomate y lechuga", "ensaladaTyL", new Step[]{
                        new Step(1, "Lavar la lechuga.", "lavadoLechuga", new Ingredient[]{
                                new Ingredient("Lechuga", 500, "lechuga")
                        }),
                        new Step(2, "Lavar el tomate.", "lavadoTomate", new Ingredient[]{
                                new Ingredient("Tomate", 300, "tomate")
                        }),
                        new Step(3, "Cortar la lechuga.", "cortadoLechuga", new Ingredient[]{
                                new Ingredient("Lechuga", 500, "lechuga")
                        }),
                        new Step(4, "Cortar el tomate en rodajas.", "cortadoTomate", new Ingredient[]{
                                new Ingredient("Tomate", 300, "tomate")
                        }),
                        new Step(5, "Colocar la mezcla en una ensaladera.", "ensaladaTyL", new Ingredient[]{}, 5)
                }),
                new Recipe("Pollo frito", "polloFrito", new Step[]{
                        new Step(1, "Precalentar aceite en una sartén a 220 grados Celsius.", "sartenAceite", new Ingredient[]{}),
                        new Step(2, "Picar el pollo y salar a gusto.", "picadoPollo", new Ingredient[]{
                                new Ingredient("Pollo", 500, "pollo"),
                                new Ingredient("Sal", 5, "sal")
                        }),
                        new Step(3, "Freir el pollo durante 15 minutos.", "sartenAceite", new Ingredient[]{}, 15)
                }),
                new Recipe("Pastel de chocolate", "pastelChocolate", new Step[]{
                        new Step(1, "Precalentar el horno a 180 grados Celsius.", "horno", new Ingredient[]{}),
                        new Step(2, "Preparar la masa de chocolate.", "preparadoMasa", new Ingredient[]{
                                new Ingredient("Harina", 200, "harina"),
                                new Ingredient("Chocolate", 200, "chocolate"),
                                new Ingredient("Azúcar", 50, "azucar"),
                                new Ingredient("Mantequilla", 100, "mantequilla"),
                                new Ingredient("Agua", 50, "agua")
                        }),
                        new Step(3, "Hornear durante 30 minutos.", "horno", new Ingredient[]{}, 30)
                })
        };
        checkIfImagesExist(defaultRecipes);
        return new GsonBuilder().setPrettyPrinting().create().toJson(defaultRecipes);
    }

    private static String getDefaultUsersJson() {
        User[] defaultUsers = new User[]{
                new User("Juan Perez", "juan.perez@example.com", "123456", new String[]{
                        "Tarta de Manzana"
                }),
                new User("Maria Lopez", "maria.lopez@example.com", "abcdef", new String[]{
                        "Pizza Margarita"
                })
                // Agregar más usuarios si es necesario
        };
        return new GsonBuilder().setPrettyPrinting().create().toJson(defaultUsers);
    }

    private static void checkIfImagesExist(final Recipe[] recipes) {
        final List<String> imagesAvailable = new ArrayList<>();
        final File folder = new File("imagenes");
        for (final File file : folder.listFiles()) {
            imagesAvailable.add(removeExtensionFromFilename(file.getName()));
        }
        for (final Recipe recipe : recipes) {
            if (!imagesAvailable.contains(recipe.getImagen())) {
                System.out.println("Falta la imagen: " + recipe.getImagen());
            }
            for (final Step step : recipe.getPasos()) {
                if (!imagesAvailable.contains(step.getImagen())) {
                    System.out.println("Falta la imagen: " + step.getImagen());
                }
                for (final Ingredient ingredient : step.getIngredientes()) {
                    if (!imagesAvailable.contains(ingredient.getImagen())) {
                        System.out.println("Falta la imagen: " + ingredient.getImagen());
                    }
                }
            }
        }
    }

    private static String removeExtensionFromFilename(final String filename) {
        final int index = filename.lastIndexOf('.');
        if (index > -1) {
            return filename.substring(0, index);
        }
        return filename;
    }

}
