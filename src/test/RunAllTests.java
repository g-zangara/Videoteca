package test;

import test.controller.GestoreVideotecaTest;
import test.model.FilmTest;
import test.model.StatoVisioneTest;
import test.command.CommandManagerTest;
import test.command.AggiungiFilmCommandTest;
import test.command.ModificaFilmCommandTest;
import test.command.EliminaFilmCommandTest;
import test.strategy.OrdinatoreFilmStrategyTest;
import test.dao.FilmDAOTest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe per eseguire tutti i test JUnit nella suite di test.
 * Implementazione semplificata che non richiede la dipendenza da junit-platform-launcher.
 */
public class RunAllTests {

    // Contatori per i risultati dei test
    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;
    private static final List<String> failedTestNames = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("Esecuzione di tutti i test...");
        long startTime = System.currentTimeMillis();

        runTestsForClass(FilmTest.class);
        runTestsForClass(StatoVisioneTest.class);
        runTestsForClass(GestoreVideotecaTest.class);
        runTestsForClass(CommandManagerTest.class);
        runTestsForClass(AggiungiFilmCommandTest.class);
        runTestsForClass(ModificaFilmCommandTest.class);
        runTestsForClass(EliminaFilmCommandTest.class);
        runTestsForClass(OrdinatoreFilmStrategyTest.class);
        runTestsForClass(FilmDAOTest.class);

        long endTime = System.currentTimeMillis();

        System.out.println("\n=== RIEPILOGO DEI TEST ===");
        System.out.println("Test eseguiti: " + totalTests);
        System.out.println("Test superati: " + passedTests);
        System.out.println("Test falliti: " + failedTests);
        System.out.println("Tempo totale di esecuzione: " + (endTime - startTime) + " ms");

        if (failedTests > 0) {
            System.out.println("\n=== DETTAGLI DEI TEST FALLITI ===");
            for (String failedTest : failedTestNames) {
                System.out.println("  - " + failedTest);
            }
        }

        if (failedTests > 0) {
            System.exit(1);
        }
    }

    /**
     * Esegue tutti i metodi di test nella classe specificata.
     *
     * @param testClass Classe di test da eseguire
     */
    private static void runTestsForClass(Class<?> testClass) {
        System.out.println("\nEsecuzione dei test per: " + testClass.getSimpleName());

        try {
            // Crea un'istanza della classe di test
            Object testInstance = testClass.getDeclaredConstructor().newInstance();

            // Ottieni tutti i metodi della classe
            Method[] methods = testClass.getDeclaredMethods();

            // Trova ed esegui i metodi di setup
            Method setupMethod = null;
            for (Method method : methods) {
                if (method.isAnnotationPresent(org.junit.jupiter.api.BeforeEach.class)) {
                    setupMethod = method;
                    break;
                }
            }

            // Trova ed esegui i metodi di teardown
            Method teardownMethod = null;
            for (Method method : methods) {
                if (method.isAnnotationPresent(org.junit.jupiter.api.AfterEach.class)) {
                    teardownMethod = method;
                    break;
                }
            }

            // Itera attraverso i metodi ed esegue quelli annotati con @Test
            for (Method method : methods) {
                if (method.isAnnotationPresent(org.junit.jupiter.api.Test.class)) {
                    String testName = testClass.getSimpleName() + "." + method.getName();
                    totalTests++;

                    try {
                        // Esegui il setup
                        if (setupMethod != null) {
                            setupMethod.invoke(testInstance);
                        }

                        // Esegui il test
                        method.invoke(testInstance);

                        // Esegui il teardown
                        if (teardownMethod != null) {
                            teardownMethod.invoke(testInstance);
                        }

                        passedTests++;
                        System.out.println("  ✓ " + method.getName());
                    } catch (Exception e) {
                        failedTests++;
                        failedTestNames.add(testName + ": " + e.getCause().getMessage());
                        System.out.println("  ✗ " + method.getName() + " - " + e.getCause().getMessage());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Errore durante l'esecuzione dei test per " + testClass.getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

}
